package com.dfa.vinatrip.domains.main.fragment.plan;

import com.dfa.vinatrip.domains.main.fragment.plan.make_plan.PlanSchedule;
import com.dfa.vinatrip.models.response.User;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Plan implements Serializable {
    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name;
    @SerializedName("destination")
    private String destination;
    @SerializedName("date_go")
    private String dateGo;
    @SerializedName("date_back")
    private String dateBack;
    @SerializedName("id_background")
    private int idBackground;
    @SerializedName("plan_schedule_list")
    private List<PlanSchedule> planScheduleList;
    @SerializedName("user_make_plan")
    private User userMakePlan;

    private List<Long> friendInvitedList;

    public Plan(String name, String destination, String dateGo, String dateBack, int idBackground,
                List<Long> friendInvitedList,
                List<PlanSchedule> planScheduleList, User userMakePlan) {
        this.name = name;
        this.destination = destination;
        this.dateGo = dateGo;
        this.dateBack = dateBack;
        this.idBackground = idBackground;
        this.friendInvitedList = friendInvitedList;
        this.planScheduleList = planScheduleList;
        this.userMakePlan = userMakePlan;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public List<Long> getFriendInvitedList() {
        return friendInvitedList;
    }

    public void setFriendInvitedList(List<Long> friendInvitedList) {
        this.friendInvitedList = friendInvitedList;
    }

    public List<PlanSchedule> getPlanScheduleList() {
        return planScheduleList;
    }

    public void setPlanScheduleList(List<PlanSchedule> planScheduleList) {
        this.planScheduleList = planScheduleList;
    }

    public User getUserMakePlan() {
        return userMakePlan;
    }

    public void setUserMakePlan(User userMakePlan) {
        this.userMakePlan = userMakePlan;
    }

}