package com.dfa.vinatrip.base;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.fragment.me.MeFragment;
import com.dfa.vinatrip.utils.AppUtil;

/**
 * Created by duonghd on 1/4/2018.
 * duonghd1307@gmail.com
 */

public class ChangePasswordDialog extends Dialog {
    private View.OnClickListener closeListener;
    private CallbackMeFragment callbackMeFragment;

    private EditText edtCurrentPass;
    private EditText edtNewPass;
    private EditText edtNewPassConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_change_password);

        LinearLayout llRoot = (LinearLayout) findViewById(R.id.dialog_change_password_ll_root);
        ImageView ivClose = (ImageView) findViewById(R.id.dialog_change_password_iv_close);
        LinearLayout llSend = (LinearLayout) findViewById(R.id.dialog_change_password_ll_send);
        edtCurrentPass = (EditText) findViewById(R.id.dialog_change_password_edt_current_pass);
        edtNewPass = (EditText) findViewById(R.id.dialog_change_password_edt_new_pass);
        edtNewPassConfirm = (EditText) findViewById(R.id.dialog_change_password_edt_new_pass_comfirm);

        AppUtil.setupUI(llRoot);

        Window window = getWindow();
        assert window != null;
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        params.width = (AppUtil.getWidth(getContext()) - AppUtil.dpToPx(getContext(), 40));
        window.setAttributes(params);

        ivClose.setOnClickListener(closeListener);
        llSend.setOnClickListener(v -> {
            String currentText = edtCurrentPass.getText().toString();
            String newText = edtNewPass.getText().toString();
            String newTextComfirm = edtNewPassConfirm.getText().toString();

            if (validateInput(newText, newTextComfirm)) {
                callbackMeFragment.sendClickListener(currentText, newText);
            }
        });
    }

    private boolean validateInput(String newText, String newTextConfirm) {
        if (newText.length() < 6) {
            Toast.makeText(getContext(), "Mật khẩu phải có ít nhất 6 ký tự.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!newText.equals(newTextConfirm)) {
            Toast.makeText(getContext(), "Mật khẩu mới không khớp.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public void clearData() {
        if (edtCurrentPass != null) {
            edtCurrentPass.setText("");
        }
        if (edtNewPass != null) {
            edtNewPass.setText("");
        }
        if (edtNewPassConfirm != null) {
            edtNewPassConfirm.setText("");
        }
    }

    public ChangePasswordDialog(MeFragment meFragment, View.OnClickListener closeListener) {
        super(meFragment.getContext(), R.style.dialog_style);
        this.closeListener = closeListener;
        this.callbackMeFragment = meFragment;
    }

    public interface CallbackMeFragment {
        void sendClickListener(String currentPass, String newPass);
    }
}
