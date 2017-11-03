package com.dfa.vinatrip.domains.auth.sign_in;

import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseActivity;
import com.dfa.vinatrip.domains.auth.reset_password.ResetPasswordActivity_;
import com.dfa.vinatrip.domains.auth.sign_up.SignUpActivity_;
import com.dfa.vinatrip.domains.main.splash.SplashScreenActivity_;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.models.request.AuthRequest;
import com.dfa.vinatrip.models.response.User;
import com.dfa.vinatrip.utils.AppUtil;
import com.dfa.vinatrip.utils.KeyboardListener;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import javax.inject.Inject;

@EActivity(R.layout.activity_sign_in)
public class SignInActivity extends BaseActivity<SignInView, SignInPresenter>
        implements SignInView, Validator.ValidationListener, KeyboardListener.KeyboardVisibilityListener {
    @App
    protected MainApplication mainApplication;
    @Inject
    protected SignInPresenter presenter;

    @NotEmpty
    @Email
    @ViewById(R.id.activity_sign_in_et_email)
    protected EditText etEmail;
    @NotEmpty
    @Password
    @ViewById(R.id.activity_sign_in_et_password)
    protected EditText etPassword;
    @ViewById(R.id.activity_sign_in_ll_root)
    protected LinearLayout llRoot;
    @ViewById(R.id.activity_sign_in_iv_symbol)
    protected ImageView ivSymbol;

    private Validator validator;
    private Animation animSlideUp;
    private Animation animSlideDown;

    // To keep icon not run anim when click done
    private boolean isBtnSignInClick = false;

    @AfterInject
    protected void initInject() {
        DaggerSignInComponent.builder()
                .applicationComponent(mainApplication.getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build().inject(this);
    }

    @AfterViews
    public void init() {
        KeyboardListener.setEventListener(this, this);

        animSlideUp = AnimationUtils.loadAnimation(this, R.anim.anim_slide_up);
        animSlideDown = AnimationUtils.loadAnimation(this, R.anim.anim_slide_down);

        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public void onValidationSucceeded() {
        presenter.loginWithEmail(new AuthRequest(etEmail.getText().toString(), etPassword.getText().toString()));
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

    @Click(R.id.activity_sign_in_btn_sign_up)
    public void btnSignUpClicked() {
        SignUpActivity_.intent(this).start();
        finish();
    }

    @Click(R.id.activity_sign_in_btn_sign_in)
    public void btnSignInClicked() {
        isBtnSignInClick = true;
        validator.validate();
    }

    @Click(R.id.activity_sign_in_btn_reset_password)
    public void btnResetPassword() {
        ResetPasswordActivity_.intent(this).start();
        finish();
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
                AppUtil.hideKeyBoard(this);
        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public void showLoading() {
        showHUD();
    }

    @Override
    public void hideLoading() {
        hideHUD();
    }

    @Override
    public void apiError(Throwable throwable) {
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    public SignInPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void onKeyboardVisibilityChanged(boolean isOpen) {
        if (isOpen) {
            ivSymbol.startAnimation(animSlideUp);
            isBtnSignInClick = false;
        } else {
            if (!isBtnSignInClick) {
                ivSymbol.startAnimation(animSlideDown);
            }
        }
    }

    @Override
    public void signInSuccess(User user) {
        SplashScreenActivity_.intent(this).start();
        finish();
    }
}
