package com.dfa.vinatrip.MainFunction.Plan;

import com.dfa.vinatrip.MainFunction.Me.UserProfile;

import java.util.ArrayList;
import java.util.List;

public class Plan {
    String name, destination, schedule, dateGo, dateBack;
    List<String> friendInvitedList;
    UserProfile userMakePlan;

    public Plan() {
    }

    public Plan(String name, String destination, String schedule, String dateGo,
                String dateBack, List<String> friendInvitedList, UserProfile userMakePlan) {
        this.name = name;
        this.destination = destination;
        this.schedule = schedule;
        this.dateGo = dateGo;
        this.dateBack = dateBack;
        this.friendInvitedList = friendInvitedList;
        this.userMakePlan = userMakePlan;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public void setDateGo(String dateGo) {
        this.dateGo = dateGo;
    }

    public void setDateBack(String dateBack) {
        this.dateBack = dateBack;
    }

    public void setFriendInvitedList(List<String> friendInvitedList) {
        this.friendInvitedList = new ArrayList<>();
        this.friendInvitedList.addAll(friendInvitedList);
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

    public String getSchedule() {
        return schedule;
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

    public UserProfile getUserMakePlan() {
        return userMakePlan;
    }
}
