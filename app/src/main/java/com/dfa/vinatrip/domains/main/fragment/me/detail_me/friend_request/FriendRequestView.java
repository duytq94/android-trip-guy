package com.dfa.vinatrip.domains.main.fragment.me.detail_me.friend_request;

import com.dfa.vinatrip.base.BaseMvpView;
import com.dfa.vinatrip.models.response.User;

import java.util.List;

/**
 * Created by duytq on 10/31/2017.
 */

public interface FriendRequestView extends BaseMvpView {
    void getListUserSuccess(List<User> userList);

    void getListUserFail(Throwable throwable);
}
