package com.dfa.vinatrip.domains.main.fragment.plan.make_plan;

import java.io.Serializable;

public class PlanSchedule implements Serializable {
    private String dayOrder;
    private String content;
    private String title;
    private long timestamp;

    public PlanSchedule() {
    }

    public PlanSchedule(String dayOrder, String content, String title, long timestamp) {
        this.dayOrder = dayOrder;
        this.content = content;
        this.title = title;
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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
