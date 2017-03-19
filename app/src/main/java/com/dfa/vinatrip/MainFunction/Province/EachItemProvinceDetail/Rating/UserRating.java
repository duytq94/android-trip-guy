package com.dfa.vinatrip.MainFunction.Province.EachItemProvinceDetail.Rating;

import java.io.Serializable;

public class UserRating implements Serializable {
    private String uid, nickname, avatar, email, content, numStars, date;

    public UserRating() {
    }

    public UserRating(String uid, String nickname, String avatar, String email, String content, String numStars, String date) {
        this.uid = uid;
        this.nickname = nickname;
        this.avatar = avatar;
        this.email = email;
        this.content = content;
        this.numStars = numStars;
        this.date = date;
    }

    public String getUid() {
        return uid;
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

    public String getContent() {
        return content;
    }

    public String getNumStars() {
        return numStars;
    }

    public String getDate() {
        return date;
    }
}

