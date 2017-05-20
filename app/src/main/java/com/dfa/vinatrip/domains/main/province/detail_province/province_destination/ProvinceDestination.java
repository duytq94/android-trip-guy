package com.dfa.vinatrip.domains.main.province.detail_province.province_destination;

import android.os.Parcel;
import android.os.Parcelable;

public class ProvinceDestination implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.avatar);
        dest.writeString(this.address);
        dest.writeString(this.description);
        dest.writeString(this.typeOfTourism);
        dest.writeString(this.scheduleAndFee);
        dest.writeString(this.timeSpend);
        dest.writeString(this.province);
        dest.writeFloat(this.latitude);
        dest.writeFloat(this.longitude);
    }

    protected ProvinceDestination(Parcel in) {
        this.name = in.readString();
        this.avatar = in.readString();
        this.address = in.readString();
        this.description = in.readString();
        this.typeOfTourism = in.readString();
        this.scheduleAndFee = in.readString();
        this.timeSpend = in.readString();
        this.province = in.readString();
        this.latitude = in.readFloat();
        this.longitude = in.readFloat();
    }

    public static final Parcelable.Creator<ProvinceDestination> CREATOR = new Parcelable.Creator<ProvinceDestination>() {
        @Override
        public ProvinceDestination createFromParcel(Parcel source) {
            return new ProvinceDestination(source);
        }

        @Override
        public ProvinceDestination[] newArray(int size) {
            return new ProvinceDestination[size];
        }
    };
}
