package com.dfa.vinatrip.MainFunction.Me.UserDetail.MakeFriend;

public class UserFriend {
    String friendId, state;

    public UserFriend(String friendId, String state) {
        this.friendId = friendId;
        this.state = state;
    }

    public String getFriendId() {
        return friendId;
    }

    public String getState() {
        return state;
    }
}
