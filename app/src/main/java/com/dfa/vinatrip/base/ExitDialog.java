package com.dfa.vinatrip.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.utils.AppUtil;

/**
 * Created by duonghd on 12/28/2017.
 * duonghd1307@gmail.com
 */

public class ExitDialog extends Dialog {
    private View.OnClickListener closeListener;
    private View.OnClickListener sendListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_exit);

        ImageView ivClose = (ImageView) findViewById(R.id.dialog_exit_iv_close);
        LinearLayout llSend = (LinearLayout) findViewById(R.id.dialog_exit_ll_send);

        Window window = getWindow();
        assert window != null;
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        params.width = (AppUtil.getWidth(getContext()) - AppUtil.dpToPx(getContext(), 40));
        window.setAttributes(params);

        ivClose.setOnClickListener(closeListener);
        llSend.setOnClickListener(sendListener);
    }

    public ExitDialog(Context context, View.OnClickListener closeListener, View.OnClickListener sendListener) {
        super(context, R.style.dialog_style);
        this.closeListener = closeListener;
        this.sendListener = sendListener;
    }
}
