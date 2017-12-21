package com.dfa.vinatrip.domains.main.fragment.trend;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by duytq on 10/17/2017.
 */

public class Trend implements Serializable {
    @SerializedName("id")
    private long id;
    @SerializedName("title")
    private String title;
    @SerializedName("intro")
    private String intro;
    @SerializedName("content")
    private String content;
    @SerializedName("url")
    private String url;
    @SerializedName("count_view")
    private int countView;
    @SerializedName("from_website")
    private String fromWebsite;
    // -1 is all, 0 is spring, 1 is summer, 2 is autumn, 3 is winter
    @SerializedName("season")
    private int season;
    // -1 is all, 0 is highland, 1 is beach, 2 is city, 3 is river
    @SerializedName("type")
    private int type;

    public String getFromWebsite() {
        return fromWebsite;
    }

    public void setFromWebsite(String fromWebsite) {
        this.fromWebsite = fromWebsite;
    }

    public int getCountView() {
        return countView;
    }

    public void setCountView(int countView) {
        this.countView = countView;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
