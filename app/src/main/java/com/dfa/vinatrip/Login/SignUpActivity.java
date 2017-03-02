package com.dfa.vinatrip.Login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dfa.vinatrip.MainFunction.MainActivity;
import com.dfa.vinatrip.MainFunction.Me.UserDetail.MakeFriend.UserFriend;
import com.dfa.vinatrip.MainFunction.Me.UserProfile;
import com.dfa.vinatrip.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        findViewByIds();
        changeColorStatusBar();
        onClickListener();

    }

    public void onClickListener() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(SignUpActivity.this, "Bạn hãy nhập email!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(SignUpActivity.this, "Bạn hãy nhập mật khẩu!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthWeakPasswordException e) {
                                        Toast.makeText(SignUpActivity.this,
                                                "Mật khẩu yếu, bạn hãy đặt mật khẩu từ 6 ký tự trở lên!",
                                                Toast.LENGTH_SHORT).show();
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

                                        // Add empty list friend for user
                                        UserFriend userFriend =
                                                new UserFriend(firebaseUser.getUid(), "", "", "", "");
                                        databaseReference.child("UserFriend")
                                                .child(firebaseUser.getUid())
                                                .child(firebaseUser.getUid())
                                                .setValue(userFriend);
                                    }
                                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            }
        });

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, ResetPasswordActivity.class));
            }
        });
    }

    public void changeColorStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.white));
        }
    }

    public void findViewByIds() {
        etEmail = (EditText) findViewById(R.id.activity_sign_up_et_email);
        etPassword = (EditText) findViewById(R.id.activity_sign_up_et_password);
        btnSignIn = (Button) findViewById(R.id.activity_sign_up_btn_sign_in);
        btnSignUp = (Button) findViewById(R.id.activity_sign_up_btn_sign_up);
        btnResetPassword = (Button) findViewById(R.id.activity_sign_up_btn_reset_password);
        progressBar = (ProgressBar) findViewById(R.id.activity_sign_up_progressBar);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}
