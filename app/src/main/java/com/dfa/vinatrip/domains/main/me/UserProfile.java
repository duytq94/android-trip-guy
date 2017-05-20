package com.dfa.vinatrip.MainFunction.Me;

import android.os.Parcel;
import android.os.Parcelable;

public class UserProfile implements Parcelable {
    String nickname, avatar, introduceYourSelf, city, birthday, uid, sex, email;

    public UserProfile() {
    }

    public UserProfile(String nickname, String avatar, String introduceYourSelf, String city,
                       String birthday, String uid, String sex, String email) {
        this.nickname = nickname;
        this.avatar = avatar;
        this.introduceYourSelf = introduceYourSelf;
        this.city = city;
        this.birthday = birthday;
        this.uid = uid;
        this.sex = sex;
        this.email = email;

    }

    public String getSex() {
        return sex;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getIntroduceYourSelf() {
        return introduceYourSelf;
    }

    public String getCity() {
        return city;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getUid() {
        return uid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nickname);
        dest.writeString(this.avatar);
        dest.writeString(this.introduceYourSelf);
        dest.writeString(this.city);
        dest.writeString(this.birthday);
        dest.writeString(this.uid);
        dest.writeString(this.sex);
        dest.writeString(this.email);
    }

    protected UserProfile(Parcel in) {
        this.nickname = in.readString();
        this.avatar = in.readString();
        this.introduceYourSelf = in.readString();
        this.city = in.readString();
        this.birthday = in.readString();
        this.uid = in.readString();
        this.sex = in.readString();
        this.email = in.readString();
    }

    public static final Parcelable.Creator<UserProfile> CREATOR = new Parcelable.Creator<UserProfile>() {
        @Override
        public UserProfile createFromParcel(Parcel source) {
            return new UserProfile(source);
        }

        @Override
        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };
}
