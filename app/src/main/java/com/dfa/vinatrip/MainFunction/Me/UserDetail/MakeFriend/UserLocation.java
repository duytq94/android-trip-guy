package com.dfa.vinatrip.MainFunction.Me.UserDetail.MakeFriend;

public class UserLocation {
    String uid, avatar;
    Double latitude, longitude;

    public UserLocation() {
    }

    public UserLocation(String uid, String avatar, Double latitude, Double longitude) {
        this.uid = uid;
        this.avatar = avatar;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getUid() {
        return uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
