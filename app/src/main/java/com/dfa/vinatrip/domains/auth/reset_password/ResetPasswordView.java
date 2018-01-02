package com.dfa.vinatrip.domains.auth.reset_password;

import com.dfa.vinatrip.base.BaseMvpView;

/**
 * Created by duonghd on 1/2/2018.
 * duonghd1307@gmail.com
 */

public interface ResetPasswordView extends BaseMvpView {
    void sendResetSuccess(String s);
}
