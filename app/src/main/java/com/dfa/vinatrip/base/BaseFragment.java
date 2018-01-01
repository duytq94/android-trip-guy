package com.dfa.vinatrip.base;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.dfa.vinatrip.R;
import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import org.androidannotations.annotations.EFragment;

/**
 * Created by duonghd on 10/6/2017.
 * duonghd1307@gmail.com
 */

@EFragment
public abstract class BaseFragment<V extends MvpView, P extends MvpPresenter<V>> extends MvpFragment<V, P> {
    private Dialog progressDialogLoading;
    
    public void hideKeyBoard() {
        View currentView = getActivity().getCurrentFocus();
        if (currentView != null) {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.hideSoftInputFromWindow(currentView.getWindowToken(), 0);
        }
    }
    
    public void showHUD() {
        /*if (progressDialogLoading != null && progressDialogLoading.isShowing()) {
        
        } else {
            View view = getActivity().getLayoutInflater().inflate(R.layout.layout_progress_loading_ball_spin, null);
            progressDialogLoading = new Dialog(getContext());
            progressDialogLoading.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialogLoading.setContentView(view);
            progressDialogLoading.setCancelable(false);
            progressDialogLoading.setCanceledOnTouchOutside(false);
        
            Window window = progressDialogLoading.getWindow();
            if (window != null) {
                window.setBackgroundDrawableResource(R.drawable.bg_layout_loading);
            }
            progressDialogLoading.show();
        }*/
    }
    
    public void hideHUD() {
        if (progressDialogLoading != null && progressDialogLoading.isShowing())
            progressDialogLoading.dismiss();
    }
}
