package com.dfa.vinatrip.MainFunction.Province.EachItemProvinceDetail.Rating;

import android.os.Parcel;
import android.os.Parcelable;

public class UserRating implements Parcelable {
    private String uid, nickname, avatar, email, content, numStars, date;

    public UserRating() {
    }

    public UserRating(String uid, String nickname, String avatar, String email, String content, String numStars, String date) {
        this.uid = uid;
        this.nickname = nickname;
        this.avatar = avatar;
        this.email = email;
        this.content = content;
        this.numStars = numStars;
        this.date = date;
    }

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

