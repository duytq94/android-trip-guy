package com.dfa.vinatrip.domains.auth.sign_in;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseActivity;
import com.dfa.vinatrip.domains.auth.reset_password.ResetPasswordActivity_;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.models.request.AuthRequest;
import com.google.firebase.auth.FirebaseAuth;
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
        implements SignInView, Validator.ValidationListener {
    @App
    protected MainApplication mainApplication;
    @Inject
    protected SignInPresenter presenter;
    
    @NotEmpty
    @Email
    @ViewById(R.id.activity_sign_in_et_email)
    protected EditText etEmail;
    @Password
    @ViewById(R.id.activity_sign_in_et_password)
    protected EditText etPassword;
    @ViewById(R.id.activity_sign_in_ll_root)
    protected LinearLayout llRoot;
    @ViewById(R.id.activity_sign_in_iv_symbol)
    protected ImageView ivSymbol;
    
    private Validator validator;
    private FirebaseAuth firebaseAuth;
    private Animation animSlideUp;
    private Animation animSlideDown;
    
    // To keep icon not run anim when click done
    private boolean isBtnSignInClick = false;
    
    @AfterInject
    void initInject() {
        DaggerSignInComponent.builder()
                .applicationComponent(mainApplication.getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build().inject(this);
    }
    
    @AfterViews
    void onCreate() {
        animSlideUp = AnimationUtils.loadAnimation(this, R.anim.anim_slide_up);
        animSlideDown = AnimationUtils.loadAnimation(this, R.anim.anim_slide_down);
        
        llRoot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                llRoot.getWindowVisibleDisplayFrame(r);
                int screenHeight = llRoot.getRootView().getHeight();
                
                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;
                
                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    // keyboard is opened
                    ivSymbol.startAnimation(animSlideUp);
                    isBtnSignInClick = false;
                } else {
                    // keyboard is closed
                    if (!isBtnSignInClick) {
                        ivSymbol.startAnimation(animSlideDown);
                    }
                }
            }
        });
        
        validator = new Validator(this);
        validator.setValidationListener(this);
        
        firebaseAuth = FirebaseAuth.getInstance();
    }
    
    @Override
    public void onValidationSucceeded() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        
        presenter.loginWithEmail(new AuthRequest(email, password));
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
    void btnSignUpClicked() {
        SignInActivity_.intent(this).start();
        finish();
    }
    
    @Click(R.id.activity_sign_in_btn_sign_in)
    void btnSignInClicked() {
        isBtnSignInClick = true;
        validator.validate();
    }
    
    @Click(R.id.activity_sign_in_btn_reset_password)
    void btnResetPassword() {
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
    public void showLoading() {
        
    }
    
    @Override
    public void hideLoading() {
        
    }
    
    @NonNull
    @Override
    public SignInPresenter createPresenter() {
        return presenter;
    }
}
