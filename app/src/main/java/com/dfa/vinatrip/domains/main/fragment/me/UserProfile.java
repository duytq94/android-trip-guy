package com.dfa.vinatrip.domains.main.fragment.me;

import java.io.Serializable;

public class UserProfile implements Serializable {
    String nickname, avatar, introduceYourSelf, city, birthday, uid, sex, email;

    public UserProfile() {
    }

    public UserProfile(String nickname, String avatar, String introduceYourSelf, String city,
                       String birthday, String uid, String sex, String email) {
        this.nickname = nickname;
        this.avatar = avatar;
        this.introduceYourSelf = introduceYourSelf;
        this.city = city;
        this.birthday = birthday;
        this.uid = uid;
        this.sex = sex;
        this.email = email;

    }

    public String getSex() {
        return sex;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getIntroduceYourSelf() {
        return introduceYourSelf;
    }

    public String getCity() {
        return city;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getUid() {
        return uid;
    }
}
