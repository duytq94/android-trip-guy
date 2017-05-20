package com.dfa.vinatrip.domains.main.province.detail_province.province_food;

import android.os.Parcel;
import android.os.Parcelable;

public class ProvinceFood implements Parcelable {
    private String name, avatar, price, address, description, timeOpen, phone, province;
    private float latitude, longitude;

    public String getName() {
        return name;
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

    public String getTimeOpen() {
        return timeOpen;
    }

    public String getPhone() {
        return phone;
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

    public ProvinceFood(String name, String avatar, String price, String address,
                        String description, String timeOpen, String phone, String province,
                        float latitude, float longitude) {
        this.name = name;
        this.avatar = avatar;
        this.price = price;
        this.address = address;
        this.description = description;
        this.timeOpen = timeOpen;
        this.phone = phone;
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
        dest.writeString(this.avatar);
        dest.writeString(this.price);
        dest.writeString(this.address);
        dest.writeString(this.description);
        dest.writeString(this.timeOpen);
        dest.writeString(this.phone);
        dest.writeString(this.province);
        dest.writeFloat(this.latitude);
        dest.writeFloat(this.longitude);
    }

    protected ProvinceFood(Parcel in) {
        this.name = in.readString();
        this.avatar = in.readString();
        this.price = in.readString();
        this.address = in.readString();
        this.description = in.readString();
        this.timeOpen = in.readString();
        this.phone = in.readString();
        this.province = in.readString();
        this.latitude = in.readFloat();
        this.longitude = in.readFloat();
    }

    public static final Parcelable.Creator<ProvinceFood> CREATOR = new Parcelable.Creator<ProvinceFood>() {
        @Override
        public ProvinceFood createFromParcel(Parcel source) {
            return new ProvinceFood(source);
        }

        @Override
        public ProvinceFood[] newArray(int size) {
            return new ProvinceFood[size];
        }
    };
}
