package com.dfa.vinatrip.MainFunction.Plan.MakePlan;

import android.os.Parcel;
import android.os.Parcelable;

public class PlanSchedule implements Parcelable {
    private String dayOrder, content;

    public PlanSchedule() {
    }

    public PlanSchedule(String dayOrder, String content) {
        this.dayOrder = dayOrder;
        this.content = content;
    }

    public String getDayOrder() {
        return dayOrder;
    }

    public String getContent() {
        return content;
    }

    public void setDayOrder(String dayOrder) {
        this.dayOrder = dayOrder;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.dayOrder);
        dest.writeString(this.content);
    }

    protected PlanSchedule(Parcel in) {
        this.dayOrder = in.readString();
        this.content = in.readString();
    }

    public static final Parcelable.Creator<PlanSchedule> CREATOR = new Parcelable.Creator<PlanSchedule>() {
        @Override
        public PlanSchedule createFromParcel(Parcel source) {
            return new PlanSchedule(source);
        }

        @Override
        public PlanSchedule[] newArray(int size) {
            return new PlanSchedule[size];
        }
    };
}
