package com.dfa.vinatrip.domains.main.fragment.me.detail_me.friend_receive;

import com.dfa.vinatrip.base.BaseMvpView;
import com.dfa.vinatrip.models.response.User;

import java.util.List;

/**
 * Created by duytq on 10/31/2017.
 */

public interface FriendReceiveView extends BaseMvpView {
    void getListFriendReceiveSuccess(List<User> userList, int page);
}
