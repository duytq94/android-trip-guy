package com.dfa.vinatrip.domains.main.province.each_item_detail_province.rating;

import android.os.Parcel;
import android.os.Parcelable;

public class UserRating implements Parcelable {
    private String uid, nickname, avatar, email, content, numStars, date, locationName, locationProvince,
            locationPhoto, type;

    protected UserRating(Parcel in) {
        uid = in.readString();
        nickname = in.readString();
        avatar = in.readString();
        email = in.readString();
        content = in.readString();
        numStars = in.readString();
        date = in.readString();
        locationName = in.readString();
        locationProvince = in.readString();
        locationPhoto = in.readString();
        type = in.readString();
    }

    public static final Creator<UserRating> CREATOR = new Creator<UserRating>() {
        @Override
        public UserRating createFromParcel(Parcel in) {
            return new UserRating(in);
        }

        @Override
        public UserRating[] newArray(int size) {
            return new UserRating[size];
        }
    };

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

    public UserRating() {

    }

    public UserRating(String uid, String nickname, String avatar, String email, String content, String numStars,
                      String date, String locationName, String locationProvince, String locationPhoto,
                      String type) {

        this.uid = uid;
        this.nickname = nickname;
        this.avatar = avatar;
        this.email = email;
        this.content = content;
        this.numStars = numStars;
        this.date = date;
        this.locationName = locationName;
        this.locationProvince = locationProvince;
        this.locationPhoto = locationPhoto;
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uid);
        parcel.writeString(nickname);
        parcel.writeString(avatar);
        parcel.writeString(email);
        parcel.writeString(content);
        parcel.writeString(numStars);
        parcel.writeString(date);
        parcel.writeString(locationName);
        parcel.writeString(locationProvince);
        parcel.writeString(locationPhoto);
        parcel.writeString(type);
    }
}

