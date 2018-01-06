package com.dfa.vinatrip.domains.other_user_profile;

import com.dfa.vinatrip.base.BaseMvpView;
import com.dfa.vinatrip.models.response.user.User;

/**
 * Created by duonghd on 1/5/2018.
 * duonghd1307@gmail.com
 */

public interface OtherUserProfileView extends BaseMvpView {
    void getUserInfoSuccess(User user);
}
