package com.dfa.vinatrip.services.friend;

import com.dfa.vinatrip.models.response.user.User;
import com.dfa.vinatrip.models.response.user.FriendStatus;

import java.util.List;

import rx.Observable;

/**
 * Created by duytq on 10/31/2017.
 */

public interface FriendService {
    Observable<List<User>> getListFriend(int page, int pageSize);

    Observable<List<User>> getListFriendReceive(int page, int pageSize);

    Observable<List<User>> getListFriendRequest(int page, int pageSize);

    Observable<List<User>> getListUser(int page, int pageSize);

    Observable<FriendStatus> addFriendRequest(long peerId);

    Observable<FriendStatus> cancelFriendRequest(long friendId);

    Observable<FriendStatus> unFriend(long peerId);

    Observable<FriendStatus> acceptFriendRequest(long friendId);
}
