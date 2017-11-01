package com.dfa.vinatrip.domains.main.fragment.plan.make_plan;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PlanSchedule implements Serializable {
    @SerializedName("content")
    private String content;
    @SerializedName("title")
    private String title;
    @SerializedName("timestamp")
    private long timestamp;

    public PlanSchedule(String content, String title, long timestamp) {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
