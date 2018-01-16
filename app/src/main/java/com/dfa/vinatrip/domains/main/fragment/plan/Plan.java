package com.dfa.vinatrip.domains.main.fragment.plan;

import com.dfa.vinatrip.domains.main.fragment.plan.make_plan.PlanSchedule;
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
    @SerializedName("timestamp_date_go")
    private long timestampDateGo;
    @SerializedName("date_back")
    private String dateBack;
    @SerializedName("timestamp_date_back")
    private long timestampDateBack;
    @SerializedName("id_background")
    private int idBackground;
    @SerializedName("plan_schedule_list")
    private List<PlanSchedule> planScheduleList;
    @SerializedName("id_user_make_plan")
    private long idUserMakePlan;
    @SerializedName("email_user_make_plan")
    private String emailUserMakePlan;
    @SerializedName("avatar_user_make_plan")
    private String avatarUserMakePlan;
    @SerializedName("username_user_make_plan")
    private String usernameUserMakePlan;
    @SerializedName("invited_friend_list")
    private List<UserInPlan> invitedFriendList;
    private boolean isExpired = false;

    public Plan(long id, String name, String destination, String dateGo, long timestampDateGo, String dateBack, long timestampDateBack,
                int idBackground, List<PlanSchedule> planScheduleList, long idUserMakePlan, String emailUserMakePlan,
                String avatarUserMakePlan, String usernameUserMakePlan, List<UserInPlan> invitedFriendList) {
        this.id = id;
        this.name = name;
        this.destination = destination;
        this.dateGo = dateGo;
        this.timestampDateGo = timestampDateGo;
        this.dateBack = dateBack;
        this.timestampDateBack = timestampDateBack;
        this.idBackground = idBackground;
        this.planScheduleList = planScheduleList;
        this.idUserMakePlan = idUserMakePlan;
        this.emailUserMakePlan = emailUserMakePlan;
        this.avatarUserMakePlan = avatarUserMakePlan;
        this.usernameUserMakePlan = usernameUserMakePlan;
        this.invitedFriendList = invitedFriendList;
    }


    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public String getEmailUserMakePlan() {
        return emailUserMakePlan;
    }

    public void setEmailUserMakePlan(String emailUserMakePlan) {
        this.emailUserMakePlan = emailUserMakePlan;
    }

    public List<UserInPlan> getInvitedFriendList() {
        return invitedFriendList;
    }

    public void setInvitedFriendList(List<UserInPlan> invitedFriendList) {
        this.invitedFriendList = invitedFriendList;
    }

    public long getIdUserMakePlan() {
        return idUserMakePlan;
    }

    public void setIdUserMakePlan(long idUserMakePlan) {
        this.idUserMakePlan = idUserMakePlan;
    }

    public String getAvatarUserMakePlan() {
        return avatarUserMakePlan;
    }

    public void setAvatarUserMakePlan(String avatarUserMakePlan) {
        this.avatarUserMakePlan = avatarUserMakePlan;
    }

    public String getUsernameUserMakePlan() {
        return usernameUserMakePlan;
    }

    public void setUsernameUserMakePlan(String usernameUserMakePlan) {
        this.usernameUserMakePlan = usernameUserMakePlan;
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

    public List<PlanSchedule> getPlanScheduleList() {
        return planScheduleList;
    }

    public void setPlanScheduleList(List<PlanSchedule> planScheduleList) {
        this.planScheduleList = planScheduleList;
    }

}