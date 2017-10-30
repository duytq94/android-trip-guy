package com.dfa.vinatrip.domains.auth.sign_up;

import com.dfa.vinatrip.base.BaseMvpView;
import com.dfa.vinatrip.models.response.User;

/**
 * Created by duytq on 10/30/2017.
 */

public interface SignUpView extends BaseMvpView {
    void signUpSuccess(User user);

    void signUpFail(Throwable throwable);
}
