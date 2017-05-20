package com.dfa.vinatrip.domains.main.location_my_friend;

public class UserLocation {
    String uid, avatar, nickname;
    Double latitude, longitude;

    public UserLocation() {
    }

    public UserLocation(String uid, String avatar, String nickname, Double latitude, Double longitude) {
        this.uid = uid;
        this.avatar = avatar;
        this.nickname = nickname;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getUid() {
        return uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
