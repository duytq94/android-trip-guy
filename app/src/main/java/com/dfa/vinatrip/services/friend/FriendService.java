package com.dfa.vinatrip.services.friend;

import com.dfa.vinatrip.models.response.User;
import com.dfa.vinatrip.models.response.user.FriendResponse;

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

    Observable<FriendResponse> addFriendRequest(long peerId);
}
