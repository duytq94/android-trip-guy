package com.dfa.vinatrip.MainFunction.Plan;

import com.dfa.vinatrip.MainFunction.Me.UserProfile;
import com.dfa.vinatrip.MainFunction.Plan.MakePlan.PlanSchedule;

import java.io.Serializable;
import java.util.List;

public class Plan implements Serializable {
    String name, destination, dateGo, dateBack;
    List<String> friendInvitedList;
    List<PlanSchedule> planScheduleList;
    UserProfile userMakePlan;

    public Plan() {
    }

    public Plan(String name, String destination, String dateGo, String dateBack,
                List<String> friendInvitedList, List<PlanSchedule> planScheduleList, UserProfile userMakePlan) {
        this.name = name;
        this.destination = destination;
        this.dateGo = dateGo;
        this.dateBack = dateBack;
        this.friendInvitedList = friendInvitedList;
        this.planScheduleList = planScheduleList;
        this.userMakePlan = userMakePlan;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDateGo(String dateGo) {
        this.dateGo = dateGo;
    }

    public void setDateBack(String dateBack) {
        this.dateBack = dateBack;
    }

    public void setFriendInvitedList(List<String> friendInvitedList) {
        this.friendInvitedList = friendInvitedList;
    }

    public void setPlanScheduleList(List<PlanSchedule> planScheduleList) {
        this.planScheduleList = planScheduleList;
    }

    public void setUserMakePlan(UserProfile userMakePlan) {
        this.userMakePlan = userMakePlan;
    }

    public String getName() {

        return name;
    }

    public String getDestination() {
        return destination;
    }

    public String getDateGo() {
        return dateGo;
    }

    public String getDateBack() {
        return dateBack;
    }

    public List<String> getFriendInvitedList() {
        return friendInvitedList;
    }

    public List<PlanSchedule> getPlanScheduleList() {
        return planScheduleList;
    }

    public UserProfile getUserMakePlan() {
        return userMakePlan;
    }
}
