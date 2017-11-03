package com.dfa.vinatrip.domains.main.fragment.me.detail_me.update_profile;

import com.dfa.vinatrip.base.BaseMvpView;
import com.dfa.vinatrip.models.response.User;

/**
 * Created by duytq on 10/31/2017.
 */

public interface UpdateUserProfileView extends BaseMvpView {
    void editProfileSuccess(User newUser);
}
