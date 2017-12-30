package com.dfa.vinatrip.domains.auth.reset_password;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.auth.sign_in.SignInActivity_;
import com.dfa.vinatrip.utils.AppUtil;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import es.dmoral.toasty.Toasty;

@SuppressLint("Registered")
@EActivity(R.layout.activity_reset_password)
public class ResetPasswordActivity extends AppCompatActivity implements Validator.ValidationListener {

    @NotEmpty
    @Email
    @ViewById(R.id.activity_reset_password_et_email)
    protected EditText etEmail;
    @ViewById(R.id.activity_reset_password_btn_reset_password)
    protected Button btnResetPassword;
    @ViewById(R.id.activity_reset_password_btn_back)
    protected Button btnBack;
    @ViewById(R.id.activity_reset_password_progressBar)
    protected ProgressBar progressBar;
    @ViewById(R.id.activity_reset_password_ll_root)
    protected LinearLayout llRoot;
    @ViewById(R.id.activity_reset_password_iv_symbol)
    protected ImageView ivSymbol;

    private Validator validator;
    private Animation animSlideUp;
    private Animation animSlideDown;

    // To keep icon not run anim when click done
    private boolean isBtnSignInClick = false;

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

    }

    @Click(R.id.activity_reset_password_btn_back)
    void btnBackClicked() {
        SignInActivity_.intent(this).start();
        finish();
    }

    @Click(R.id.activity_reset_password_btn_reset_password)
    void btnResetPasswordClicked() {
        isBtnSignInClick = true;
        validator.validate();
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
    public void onValidationSucceeded() {
        String email = etEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toasty.warning(ResetPasswordActivity.this, "Nhập email của bạn!", Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        // TODO for reset password
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
            }
            ((EditText) view).setError(message);
        }
    }
}
