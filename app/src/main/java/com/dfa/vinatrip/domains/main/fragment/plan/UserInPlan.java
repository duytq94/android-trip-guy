package com.dfa.vinatrip.domains.main.fragment.plan;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by duytq on 11/06/2017.
 */

public class UserInPlan implements Serializable {
    @SerializedName("id_user")
    private long id;
    @SerializedName("email")
    private String email;
    @SerializedName("username")
    private String username;
    @SerializedName("avatar")
    private String avatar;
    // 0 = off, 1 = on
    @SerializedName("is_online")
    private int isOnline;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;

    public UserInPlan(long id, String email, String username, String avatar) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.avatar = avatar;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
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
