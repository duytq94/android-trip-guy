package com.dfa.vinatrip.base;

import com.hannesdorfmann.mosby3.mvp.MvpView;

/**
 * Created by duonghd on 9/19/2017.
 */

public interface BaseMvpView extends MvpView {
    void showLoading();
    
    void hideLoading();
}
