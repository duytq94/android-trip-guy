package com.dfa.vinatrip.domains.main.province.each_item_detail_province.each_province_destination;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DFA on 5/26/2017.
 */

public class ProvinceDestinationDetail implements Parcelable {
    private String address, description, name, province, scheduleAndFee;
    long latitude, longitude;

    protected ProvinceDestinationDetail(Parcel in) {
        address = in.readString();
        description = in.readString();
        name = in.readString();
        province = in.readString();
        scheduleAndFee = in.readString();
        latitude = in.readLong();
        longitude = in.readLong();
    }

    public static final Creator<ProvinceDestinationDetail> CREATOR = new Creator<ProvinceDestinationDetail>() {
        @Override
        public ProvinceDestinationDetail createFromParcel(Parcel in) {
            return new ProvinceDestinationDetail(in);
        }

        @Override
        public ProvinceDestinationDetail[] newArray(int size) {
            return new ProvinceDestinationDetail[size];
        }
    };

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getProvince() {
        return province;
    }

    public String getScheduleAndFee() {
        return scheduleAndFee;
    }

    public long getLatitude() {
        return latitude;
    }

    public long getLongitude() {
        return longitude;
    }

    public ProvinceDestinationDetail() {

    }

    public ProvinceDestinationDetail(String address, String description, String name, String province,
                                     String scheduleAndFee, long latitude, long longitude) {

        this.address = address;
        this.description = description;
        this.name = name;
        this.province = province;
        this.scheduleAndFee = scheduleAndFee;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(address);
        parcel.writeString(description);
        parcel.writeString(name);
        parcel.writeString(province);
        parcel.writeString(scheduleAndFee);
        parcel.writeLong(latitude);
        parcel.writeLong(longitude);
    }
}
