package com.dfa.vinatrip.domains.auth.sign_in;

import com.dfa.vinatrip.base.BaseMvpView;
import com.dfa.vinatrip.models.response.User;

/**
 * Created by duonghd on 9/27/2017.
 */

public interface SignInView extends BaseMvpView {
    void signInSuccess(User user);

    void signInFail(Throwable throwable);
}
