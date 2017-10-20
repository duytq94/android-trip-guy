package com.dfa.vinatrip.domains.location;

import com.google.gson.annotations.SerializedName;

public class UserLocation {
    @SerializedName("id")
    private long id;
    @SerializedName("from_user")
    private String fromUser;
    @SerializedName("to_group")
    private String toGroup;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("is_online")
    private int isOnline;

    private String avatar = "";

    public UserLocation(String fromUser, String toGroup, double latitude, double longitude, int isOnline, String avatar) {
        this.fromUser = fromUser;
        this.toGroup = toGroup;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isOnline = isOnline;
        this.avatar = avatar;
    }

    public boolean getIsOnline() {
        if (isOnline == 0) return false;
        else return true;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToGroup() {
        return toGroup;
    }

    public void setToGroup(String toGroup) {
        this.toGroup = toGroup;
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
}
