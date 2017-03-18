package com.dfa.vinatrip.MainFunction.Province.ProvinceDetail.ProvinceFood;

import java.io.Serializable;

public class ProvinceFood implements Serializable {
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
}
