package com.dfa.vinatrip.domains.main.fragment.me;

import com.dfa.vinatrip.base.BaseMvpView;

/**
 * Created by duytq on 10/30/2017.
 */

public interface MeView extends BaseMvpView {
    void signOutSuccess();

    void loginOtherActivity();

    void changePasswordSuccess();
}
