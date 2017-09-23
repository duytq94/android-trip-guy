package com.dfa.vinatrip.models.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by duytq on 9/19/2017.
 */

public class UserResponse {
    @SerializedName("id")
    private long id;
    @SerializedName("username")
    private String username;
    @SerializedName("email")
    private String email;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("birthday")
    private String birthday;
    @SerializedName("intro")
    private String intro;
    @SerializedName("sex")
    private boolean sex;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }
}
