package com.dfa.vinatrip.MainFunction.Location.ProvinceDetail.ProvinceDestination;

import java.io.Serializable;

public class ProvinceDestination implements Serializable {
    private String name, avatar, address, description,
            typeOfTourism, scheduleAndFee, timeSpend, province;
    private float latitude, longitude;

    public ProvinceDestination(String name, String avatar, String address, String description,
                               String typeOfTourism, String scheduleAndFee, String timeSpend,
                               String province, float latitude, float longitude) {
        this.name = name;
        this.avatar = avatar;
        this.address = address;
        this.description = description;
        this.typeOfTourism = typeOfTourism;
        this.scheduleAndFee = scheduleAndFee;
        this.timeSpend = timeSpend;
        this.province = province;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public String getTypeOfTourism() {
        return typeOfTourism;
    }

    public String getScheduleAndFee() {
        return scheduleAndFee;
    }

    public String getTimeSpend() {
        return timeSpend;
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
}
