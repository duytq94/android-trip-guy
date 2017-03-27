package com.dfa.vinatrip.MainFunction.Plan;

import com.dfa.vinatrip.MainFunction.Me.UserProfile;
import com.dfa.vinatrip.MainFunction.Plan.MakePlan.PlanSchedule;

import java.io.Serializable;
import java.util.List;

public class Plan implements Serializable {
    String id, name, destination, dateGo, dateBack;
    List<String> friendInvitedList;
    List<PlanSchedule> planScheduleList;
    UserProfile userMakePlan;

    public Plan() {
    }

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
}
