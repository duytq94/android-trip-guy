package com.dfa.vinatrip.domains.main.fragment.plan;

import com.google.gson.annotations.SerializedName;

/**
 * Created by duytq on 11/06/2017.
 */

public class UserInPlan {
    @SerializedName("id")
    private long id;
    @SerializedName("email")
    private String email;
    @SerializedName("username")
    private String username;
    @SerializedName("avatar")
    private String avatar;

    public UserInPlan(long id, String email, String username, String avatar) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.avatar = avatar;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
