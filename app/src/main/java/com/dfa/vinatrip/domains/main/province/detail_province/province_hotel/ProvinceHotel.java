package com.dfa.vinatrip.domains.main.province.detail_province.province_hotel;

import android.os.Parcel;
import android.os.Parcelable;

public class ProvinceHotel implements Parcelable {
    private String name, rate, avatar, price, address, description, phone, mail, website, province;
    private float latitude, longitude;

    public String getName() {
        return name;
    }

    public String getRate() {
        return rate;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getPrice() {
        return price;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public String getPhone() {
        return phone;
    }

    public String getMail() {
        return mail;
    }

    public String getWebsite() {
        return website;
    }

    public String getProvince() {
        return province;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public ProvinceHotel(String name, String rate, String avatar, String price, String address,
                         String description, String phone, String mail, String website,
                         String province, float latitude, float longitude) {
        this.name = name;
        this.rate = rate;
        this.avatar = avatar;
        this.price = price;
        this.address = address;
        this.description = description;
        this.phone = phone;
        this.mail = mail;
        this.website = website;
        this.province = province;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.rate);
        dest.writeString(this.avatar);
        dest.writeString(this.price);
        dest.writeString(this.address);
        dest.writeString(this.description);
        dest.writeString(this.phone);
        dest.writeString(this.mail);
        dest.writeString(this.website);
        dest.writeString(this.province);
        dest.writeFloat(this.latitude);
        dest.writeFloat(this.longitude);
    }

    protected ProvinceHotel(Parcel in) {
        this.name = in.readString();
        this.rate = in.readString();
        this.avatar = in.readString();
        this.price = in.readString();
        this.address = in.readString();
        this.description = in.readString();
        this.phone = in.readString();
        this.mail = in.readString();
        this.website = in.readString();
        this.province = in.readString();
        this.latitude = in.readFloat();
        this.longitude = in.readFloat();
    }

    public static final Parcelable.Creator<ProvinceHotel> CREATOR = new Parcelable.Creator<ProvinceHotel>() {
        @Override
        public ProvinceHotel createFromParcel(Parcel source) {
            return new ProvinceHotel(source);
        }

        @Override
        public ProvinceHotel[] newArray(int size) {
            return new ProvinceHotel[size];
        }
    };
}
