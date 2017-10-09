package com.dfa.vinatrip.domains.main.fragment.share;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DFA on 6/10/2017.
 */

public class Share implements Parcelable {
    private String id;
    private String uid;
    private String nickname;
    private String avatar;
    private String email;
    private String name;
    private String type;
    private String destination;
    private String content;
    private String photo1;
    private String photo2;
    private String photo3;
    private String photo4;
    private String address;
    private String province;

    protected Share(Parcel in) {
        id = in.readString();
        uid = in.readString();
        nickname = in.readString();
        avatar = in.readString();
        email = in.readString();
        name = in.readString();
        type = in.readString();
        destination = in.readString();
        content = in.readString();
        photo1 = in.readString();
        photo2 = in.readString();
        photo3 = in.readString();
        photo4 = in.readString();
        address = in.readString();
        province = in.readString();
    }

    public static final Creator<Share> CREATOR = new Creator<Share>() {
        @Override
        public Share createFromParcel(Parcel in) {
            return new Share(in);
        }

        @Override
        public Share[] newArray(int size) {
            return new Share[size];
        }
    };

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhoto1() {
        return photo1;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }

    public String getPhoto2() {
        return photo2;
    }

    public void setPhoto2(String photo2) {
        this.photo2 = photo2;
    }

    public String getPhoto3() {
        return photo3;
    }

    public void setPhoto3(String photo3) {
        this.photo3 = photo3;
    }

    public String getPhoto4() {
        return photo4;
    }

    public void setPhoto4(String photo4) {
        this.photo4 = photo4;
    }

    public Share() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(uid);
        parcel.writeString(nickname);
        parcel.writeString(avatar);
        parcel.writeString(email);
        parcel.writeString(name);
        parcel.writeString(type);
        parcel.writeString(destination);
        parcel.writeString(content);
        parcel.writeString(photo1);
        parcel.writeString(photo2);
        parcel.writeString(photo3);
        parcel.writeString(photo4);
        parcel.writeString(address);
        parcel.writeString(province);
    }
}
