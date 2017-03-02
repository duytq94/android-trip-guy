package com.dfa.vinatrip.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dfa.vinatrip.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import static com.dfa.vinatrip.R.id.activity_reset_password_btn_back;
import static com.dfa.vinatrip.R.id.activity_reset_password_btn_reset_password;
import static com.dfa.vinatrip.R.id.activity_reset_password_et_email;
import static com.dfa.vinatrip.R.id.activity_reset_password_progressBar;

@EActivity(R.layout.activity_reset_password)
public class ResetPasswordActivity extends AppCompatActivity {

    @ViewById(activity_reset_password_et_email)
    EditText etEmail;

    @ViewById(activity_reset_password_btn_reset_password)
    Button btnResetPassword;

    @ViewById(activity_reset_password_btn_back)
    Button btnBack;

    @ViewById(activity_reset_password_progressBar)
    ProgressBar progressBar;

    @Click
    void activity_reset_password_btn_back() {
        startActivity(new Intent(ResetPasswordActivity.this, SignInActivity_.class));
    }

    @Click
    void activity_reset_password_btn_reset_password() {
        String email = etEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(ResetPasswordActivity.this, "Nhập email của bạn!", Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPasswordActivity.this,
                                    "Chúng tôi đã gửi mật khẩu đến email của bạn!",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ResetPasswordActivity.this,
                                    "Reset mật khẩu không thành công!",
                                    Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private FirebaseAuth firebaseAuth;

    @AfterViews
    void onCreate() {
        firebaseAuth = FirebaseAuth.getInstance();
        changeColorStatusBar();
    }

    public void changeColorStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.white));
        }
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
}
