package com.dfa.vinatrip.domains.main.plan;

import android.os.Parcel;
import android.os.Parcelable;

import com.dfa.vinatrip.domains.main.me.UserProfile;
import com.dfa.vinatrip.domains.main.plan.make_plan.PlanSchedule;

import java.util.ArrayList;
import java.util.List;

public class Plan implements Parcelable {
    String id, name, destination, dateGo, dateBack;
    List<String> friendInvitedList;
    List<PlanSchedule> planScheduleList;
    UserProfile userMakePlan;

    public Plan(String id, String name, String destination, String dateGo, String dateBack,
                List<String> friendInvitedList, List<PlanSchedule> planScheduleList, UserProfile userMakePlan) {
        this.id = id;
        this.name = name;
        this.destination = destination;
        this.dateGo = dateGo;
        this.dateBack = dateBack;
        this.friendInvitedList = friendInvitedList;
        this.planScheduleList = planScheduleList;
        this.userMakePlan = userMakePlan;
    }

    public Plan() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDateGo() {
        return dateGo;
    }

    public void setDateGo(String dateGo) {
        this.dateGo = dateGo;
    }

    public String getDateBack() {
        return dateBack;
    }

    public void setDateBack(String dateBack) {
        this.dateBack = dateBack;
    }

    public List<String> getFriendInvitedList() {
        return friendInvitedList;
    }

    public void setFriendInvitedList(List<String> friendInvitedList) {
        this.friendInvitedList = friendInvitedList;
    }

    public List<PlanSchedule> getPlanScheduleList() {
        return planScheduleList;
    }

    public void setPlanScheduleList(List<PlanSchedule> planScheduleList) {
        this.planScheduleList = planScheduleList;
    }

    public UserProfile getUserMakePlan() {
        return userMakePlan;
    }

    public void setUserMakePlan(UserProfile userMakePlan) {
        this.userMakePlan = userMakePlan;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.destination);
        dest.writeString(this.dateGo);
        dest.writeString(this.dateBack);
        dest.writeStringList(this.friendInvitedList);
        dest.writeList(this.planScheduleList);
        dest.writeParcelable(this.userMakePlan, flags);
    }

    protected Plan(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.destination = in.readString();
        this.dateGo = in.readString();
        this.dateBack = in.readString();
        this.friendInvitedList = in.createStringArrayList();
        this.planScheduleList = new ArrayList<PlanSchedule>();
        in.readList(this.planScheduleList, PlanSchedule.class.getClassLoader());
        this.userMakePlan = in.readParcelable(UserProfile.class.getClassLoader());
    }

    public static final Parcelable.Creator<Plan> CREATOR = new Parcelable.Creator<Plan>() {
        @Override
        public Plan createFromParcel(Parcel source) {
            return new Plan(source);
        }

        @Override
        public Plan[] newArray(int size) {
            return new Plan[size];
        }
    };
}