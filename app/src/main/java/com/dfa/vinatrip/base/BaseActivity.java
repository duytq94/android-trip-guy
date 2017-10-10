package com.dfa.vinatrip.base;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.dfa.vinatrip.R;
import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

/**
 * Created by duonghd on 9/19/2017.
 */

@EActivity
public abstract class BaseActivity<V extends MvpView, P extends MvpPresenter<V>> extends MvpActivity<V, P> {

    private Dialog progressDialogLoading;

    public void hideKeyBoard() {
        View currentView = this.getCurrentFocus();
        if (currentView != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(currentView.getWindowToken(), 0);
        }
    }
    
    @UiThread
    public void showHUD() {
        if (progressDialogLoading != null && progressDialogLoading.isShowing()) {
        
        } else {
            View view = getLayoutInflater().inflate(R.layout.layout_progress_loading_ball_spin, null);
            progressDialogLoading = new Dialog(this);
            progressDialogLoading.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialogLoading.setContentView(view);
            progressDialogLoading.setCancelable(false);
            progressDialogLoading.setCanceledOnTouchOutside(false);
        
            Window window = progressDialogLoading.getWindow();
            if (window != null) {
                window.setBackgroundDrawableResource(R.drawable.bg_layout_loading);
            }
            progressDialogLoading.show();
        }
    }
    
    @UiThread
    public void hideHUD() {
        if (progressDialogLoading != null && progressDialogLoading.isShowing())
            progressDialogLoading.dismiss();
    }
}
