package com.dfa.vinatrip.domains.main.fragment.me.detail_me.make_friend;

import com.dfa.vinatrip.base.BaseMvpView;
import com.dfa.vinatrip.models.response.user.User;
import com.dfa.vinatrip.models.response.user.FriendStatus;

import java.util.List;

/**
 * Created by duytq on 10/31/2017.
 */

public interface MakeFriendView extends BaseMvpView {
    void getListUserSuccess(List<User> userList, int page);

    void addFriendRequestSuccess(int position, FriendStatus friendStatus);

    void cancelFriendRequestSuccess(int position, FriendStatus friendStatus);
}
