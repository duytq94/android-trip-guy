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
}
