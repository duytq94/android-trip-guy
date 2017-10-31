package com.dfa.vinatrip.models.response;

import com.beesightsoft.caf.models.BaseEntity;
import com.beesightsoft.caf.services.authentication.model.LoginResponse;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by duonghd on 9/27/2017.
 */

public class User extends BaseEntity implements LoginResponse, Serializable {
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
    private int sex;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("accessToken")
    private String accessToken;
    @SerializedName("background")
    private String background;
    @SerializedName("city")
    private String city;
    @SerializedName("isOnline")
    private boolean isOnline;

    public User(String username, String avatar, String birthday, String intro, int sex,
                String background, String city) {
        this.username = username;
        this.avatar = avatar;
        this.birthday = birthday;
        this.intro = intro;
        this.sex = sex;
        this.background = background;
        this.city = city;
    }

    public boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(boolean online) {
        isOnline = online;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getIntro() {
        return intro;
    }

    public int getSex() {
        return sex;
    }

    public String getStringSex() {
        if (sex == 0) {
            return "Nam";
        } else {
            return "Nữ";
        }
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
