package com.dfa.vinatrip.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dfa.vinatrip.MainFunction.Me.UserProfile;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.SplashScreen.SplashScreenActivity_;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.activity_sign_up)
public class SignUpActivity extends AppCompatActivity implements Validator.ValidationListener {

    private Validator validator;

    @NotEmpty
    @Email
    @ViewById(R.id.activity_sign_up_et_email)
    EditText etEmail;

    @Password
    @ViewById(R.id.activity_sign_up_et_password)
    EditText etPassword;

    @ViewById(R.id.activity_sign_up_progressBar)
    ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;

    @AfterViews
    void onCreate() {
        validator = new Validator(this);
        validator.setValidationListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        changeColorStatusBar();
    }

    public void changeColorStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorMain));
        }
    }

    @Click(R.id.activity_sign_up_btn_sign_up)
    void btnSignUpClicked() {
        validator.validate();
    }

    @Click(R.id.activity_sign_up_btn_sign_in)
    void btnSignInClicked() {
        startActivity(new Intent(SignUpActivity.this, SignInActivity_.class));
    }

    @Click(R.id.activity_sign_up_btn_reset_password)
    void btnResetPasswordClicked() {
        startActivity(new Intent(SignUpActivity.this, ResetPasswordActivity_.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    @Override
    public void onValidationSucceeded() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(SignUpActivity.this,
                                        "Email không hợp lệ!",
                                        Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthUserCollisionException e) {
                                Toast.makeText(SignUpActivity.this,
                                        "Email đã được đăng ký!",
                                        Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                            }
                        } else {
                            // Create first database for user with empty data
                            FirebaseUser firebaseUser =
                                    FirebaseAuth.getInstance().getCurrentUser();
                            if (firebaseUser != null) {
                                // Add empty profile for user
                                UserProfile userProfile
                                        = new UserProfile("", "", "", "", "", "", "",
                                        firebaseUser.getEmail());
                                DatabaseReference databaseReference =
                                        FirebaseDatabase.getInstance().getReference();
                                databaseReference.child("UserProfile")
                                        .child(firebaseUser.getUid())
                                        .setValue(userProfile);
                            }
                            startActivity(new Intent(SignUpActivity.this, SplashScreenActivity_.class));
                            finish();
                        }
                    }
                });
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages
            switch (message) {
                case "Invalid email\nThis field is required":
                    message = "Email trống\nBạn phải nhập email";
                    break;
                case "Invalid email":
                    message = "Email không hợp lệ";
                    break;
                case "Invalid password":
                    message = "Mật khẩu phải từ 6 ký tự trở lên";
                    break;
            }
            ((EditText) view).setError(message);
        }
    }
}
