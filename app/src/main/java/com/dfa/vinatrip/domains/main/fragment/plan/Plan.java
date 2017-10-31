package com.dfa.vinatrip.domains.main.fragment.plan;

import com.dfa.vinatrip.domains.main.fragment.plan.make_plan.PlanSchedule;
import com.dfa.vinatrip.models.response.User;

import java.io.Serializable;
import java.util.List;

public class Plan implements Serializable {
    private String id, name, destination, dateGo, dateBack;
    private int idBackground;
    private List<Long> friendInvitedList;
    private List<PlanSchedule> planScheduleList;
    private User userMakePlan;

    public Plan(String id, String name, String destination, String dateGo, String dateBack, int idBackground,
                List<Long> friendInvitedList,
                List<PlanSchedule> planScheduleList, User userMakePlan) {
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

    public Plan() {

    }
}