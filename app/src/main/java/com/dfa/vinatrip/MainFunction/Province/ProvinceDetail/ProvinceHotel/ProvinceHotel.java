package com.dfa.vinatrip.MainFunction.Province.ProvinceDetail.ProvinceHotel;

import java.io.Serializable;

public class ProvinceHotel implements Serializable {
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
}
