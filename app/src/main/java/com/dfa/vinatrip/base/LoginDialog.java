package com.dfa.vinatrip.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.utils.AppUtil;

/**
 * Created by duonghd on 12/30/2017.
 * duonghd1307@gmail.com
 */

public class LoginDialog extends Dialog {
    private CallbackActivity callbackActivity;
    private EditText etEmail;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_login);

        LinearLayout llRoot = (LinearLayout) findViewById(R.id.dialog_login_ll_root);
        ImageView ivClose = (ImageView) findViewById(R.id.dialog_login_iv_close);
        LinearLayout llSend = (LinearLayout) findViewById(R.id.dialog_login_ll_send);
        etEmail = (EditText) findViewById(R.id.dialog_login_et_email);
        etPassword = (EditText) findViewById(R.id.dialog_login_et_password);

        AppUtil.setupUI(llRoot);

        Window window = getWindow();
        assert window != null;
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        params.width = (AppUtil.getWidth(getContext()) - AppUtil.dpToPx(getContext(), 40));
        window.setAttributes(params);

        ivClose.setOnClickListener(v -> {
            dismiss();
        });
        llSend.setOnClickListener(v -> {
            if (validateInput(etEmail.getText().toString().trim(), etPassword.getText().toString().trim())) {
                callbackActivity.loginInfo(etEmail.getText().toString().trim(), etPassword.getText().toString().trim());
            }
        });
    }

    public void clearData(){
        if (etEmail != null) {
            etEmail.setText("");
        }
        if (etPassword != null) {
            etPassword.setText("");
        }
    }

    private boolean validateInput(String email, String password) {
        return true;
    }

    public LoginDialog(Context context) {
        super(context, R.style.dialog_style);
        callbackActivity = (CallbackActivity) context;
    }

    public interface CallbackActivity {
        void loginInfo(String email, String password);
    }
}
