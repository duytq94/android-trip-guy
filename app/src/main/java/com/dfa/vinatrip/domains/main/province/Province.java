package com.dfa.vinatrip.domains.main.province;

import android.os.Parcel;
import android.os.Parcelable;

public class Province implements Parcelable {

    private String name;
    private String title;
    private String avatar;
    private String description;

    public Province() {
    }

    public Province(String name, String title, String avatar, String description) {
        this.name = name;
        this.title = title;
        this.avatar = avatar;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.title);
        dest.writeString(this.avatar);
        dest.writeString(this.description);
    }

    protected Province(Parcel in) {
        this.name = in.readString();
        this.title = in.readString();
        this.avatar = in.readString();
        this.description = in.readString();
    }

    public static final Parcelable.Creator<Province> CREATOR = new Parcelable.Creator<Province>() {
        @Override
        public Province createFromParcel(Parcel source) {
            return new Province(source);
        }

        @Override
        public Province[] newArray(int size) {
            return new Province[size];
        }
    };
}
