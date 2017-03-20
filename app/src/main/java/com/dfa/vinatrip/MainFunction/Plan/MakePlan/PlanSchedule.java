package com.dfa.vinatrip.MainFunction.Plan.MakePlan;

import java.io.Serializable;

public class PlanSchedule implements Serializable {
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
}
