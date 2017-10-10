package com.dfa.vinatrip.domains.main.fragment.plan;

import android.os.Parcel;
import android.os.Parcelable;

import com.dfa.vinatrip.domains.main.fragment.me.UserProfile;
import com.dfa.vinatrip.domains.main.plan.make_plan.PlanSchedule;

import java.util.List;

public class Plan implements Parcelable {
    private String id, name, destination, dateGo, dateBack;
    private int idBackground;
    private List<String> friendInvitedList;
    private List<PlanSchedule> planScheduleList;
    private UserProfile userMakePlan;

    public Plan(String id, String name, String destination, String dateGo, String dateBack, int idBackground,
                List<String> friendInvitedList,
                List<PlanSchedule> planScheduleList, UserProfile userMakePlan) {
        this.id = id;
        this.name = name;
        this.destination = destination;
        this.dateGo = dateGo;
        this.dateBack = dateBack;
        this.idBackground = idBackground;
        this.friendInvitedList = friendInvitedList;
        this.planScheduleList = planScheduleList;
        this.userMakePlan = userMakePlan;
    }

    protected Plan(Parcel in) {
        id = in.readString();
        name = in.readString();
        destination = in.readString();
        dateGo = in.readString();
        dateBack = in.readString();
        idBackground = in.readInt();
        friendInvitedList = in.createStringArrayList();
        planScheduleList = in.createTypedArrayList(PlanSchedule.CREATOR);
        userMakePlan = in.readParcelable(UserProfile.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(destination);
        dest.writeString(dateGo);
        dest.writeString(dateBack);
        dest.writeInt(idBackground);
        dest.writeStringList(friendInvitedList);
        dest.writeTypedList(planScheduleList);
        dest.writeParcelable(userMakePlan, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Plan> CREATOR = new Creator<Plan>() {
        @Override
        public Plan createFromParcel(Parcel in) {
            return new Plan(in);
        }

        @Override
        public Plan[] newArray(int size) {
            return new Plan[size];
        }
    };

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

    public int getIdBackground() {
        return idBackground;
    }

    public void setIdBackground(int idBackground) {
        this.idBackground = idBackground;
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

    public Plan() {

    }
}