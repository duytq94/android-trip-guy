package com.dfa.vinatrip.domains.auth.sign_in;

import com.hannesdorfmann.mosby3.mvp.MvpView;

/**
 * Created by duytq on 9/17/2017.
 */

public interface SignInView extends MvpView {
    void showLoading();

    void hideLoading();
}
