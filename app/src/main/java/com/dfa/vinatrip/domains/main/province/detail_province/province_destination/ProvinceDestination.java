package com.dfa.vinatrip.domains.main.province.detail_province.province_destination;

import android.os.Parcel;
import android.os.Parcelable;

public class ProvinceDestination implements Parcelable {
    private String name, avatar, typeOfTourism, timeSpend, province;

    protected ProvinceDestination(Parcel in) {
        name = in.readString();
        avatar = in.readString();
        typeOfTourism = in.readString();
        timeSpend = in.readString();
        province = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(avatar);
        dest.writeString(typeOfTourism);
        dest.writeString(timeSpend);
        dest.writeString(province);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProvinceDestination> CREATOR = new Creator<ProvinceDestination>() {
        @Override
        public ProvinceDestination createFromParcel(Parcel in) {
            return new ProvinceDestination(in);
        }

        @Override
        public ProvinceDestination[] newArray(int size) {
            return new ProvinceDestination[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getTypeOfTourism() {
        return typeOfTourism;
    }

    public String getTimeSpend() {
        return timeSpend;
    }

    public String getProvince() {
        return province;
    }

    public ProvinceDestination() {

    }

    public ProvinceDestination(String name, String avatar, String typeOfTourism, String timeSpend,
                               String province) {

        this.name = name;
        this.avatar = avatar;
        this.typeOfTourism = typeOfTourism;
        this.timeSpend = timeSpend;
        this.province = province;
    }
}
