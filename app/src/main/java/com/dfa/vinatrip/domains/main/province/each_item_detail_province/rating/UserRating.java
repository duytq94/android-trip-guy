package com.dfa.vinatrip.domains.main.province.each_item_detail_province.rating;

import android.os.Parcel;
import android.os.Parcelable;

public class UserRating implements Parcelable {
    private String uid, nickname, avatar, email, content, numStars, date, locationName, locationPhoto, type;

    public UserRating() {
    }

    public UserRating(String uid, String nickname, String avatar, String email, String content, String numStars,
                      String date, String locationName, String locationPhoto, String type) {
        this.uid = uid;
        this.nickname = nickname;
        this.avatar = avatar;
        this.email = email;
        this.content = content;
        this.numStars = numStars;
        this.date = date;
        this.locationName = locationName;
        this.locationPhoto = locationPhoto;
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNumStars() {
        return numStars;
    }

    public void setNumStars(String numStars) {
        this.numStars = numStars;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationPhoto() {
        return locationPhoto;
    }

    public void setLocationPhoto(String locationPhoto) {
        this.locationPhoto = locationPhoto;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeString(this.nickname);
        dest.writeString(this.avatar);
        dest.writeString(this.email);
        dest.writeString(this.content);
        dest.writeString(this.numStars);
        dest.writeString(this.date);
    }

    protected UserRating(Parcel in) {
        this.uid = in.readString();
        this.nickname = in.readString();
        this.avatar = in.readString();
        this.email = in.readString();
        this.content = in.readString();
        this.numStars = in.readString();
        this.date = in.readString();
    }

    public static final Parcelable.Creator<UserRating> CREATOR = new Parcelable.Creator<UserRating>() {
        @Override
        public UserRating createFromParcel(Parcel source) {
            return new UserRating(source);
        }

        @Override
        public UserRating[] newArray(int size) {
            return new UserRating[size];
        }
    };
}

