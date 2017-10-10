package com.dfa.vinatrip.domains.main.fragment.me.detail_me.make_friend;

import java.io.Serializable;

public class UserFriend implements Serializable {
    private String friendId, nickname, avatar, email, state;
    private boolean isOnline;

    public UserFriend() {
    }

    public UserFriend(String friendId, String nickname, String avatar, String email, String state) {
        this.friendId = friendId;
        this.nickname = nickname;
        this.avatar = avatar;
        this.email = email;
        this.state = state;
        this.isOnline = false;
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

    public boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(boolean online) {
        isOnline = online;
    }
}
