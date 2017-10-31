package com.dfa.vinatrip.services.friend;

import com.dfa.vinatrip.models.response.User;

import java.util.List;

import rx.Observable;

/**
 * Created by duytq on 10/31/2017.
 */

public interface FriendService {
    Observable<List<User>> getListFriend();

    Observable<List<User>> getListFriendReceive();
}
