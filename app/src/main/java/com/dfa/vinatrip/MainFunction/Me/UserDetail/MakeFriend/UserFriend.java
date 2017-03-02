package com.dfa.vinatrip.MainFunction.Me.UserDetail.MakeFriend;

public class UserFriend {
    String friendId, nickname, avatar, email, state;

    public UserFriend(String friendId, String nickname, String avatar, String email, String state) {
        this.friendId = friendId;
        this.nickname = nickname;
        this.avatar = avatar;
        this.email = email;
        this.state = state;
    }

    public String getFriendId() {
        return friendId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getEmail() {
        return email;
    }

    public String getState() {
        return state;
    }
}
