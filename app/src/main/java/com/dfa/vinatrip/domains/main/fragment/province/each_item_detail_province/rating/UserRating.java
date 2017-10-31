package com.dfa.vinatrip.domains.main.fragment.province.each_item_detail_province.rating;

import java.io.Serializable;

public class UserRating implements Serializable {
    private String uid, nickname, avatar, email, content, numStars, date, locationName, locationProvince,
            locationPhoto, type;

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

    public String getLocationName() {
        return locationName;
    }

    public String getLocationProvince() {
        return locationProvince;
    }

    public String getLocationPhoto() {
        return locationPhoto;
    }

    public String getType() {
        return type;
    }
}

