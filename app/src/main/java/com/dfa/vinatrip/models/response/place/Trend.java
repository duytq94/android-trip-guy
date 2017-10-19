package com.dfa.vinatrip.models.response.place;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by duytq on 10/17/2017.
 */

public class Trend implements Serializable {
    @SerializedName("title")
    private String title;
    @SerializedName("intro")
    private String intro;
    @SerializedName("content")
    private String content;
    @SerializedName("url")
    private String url;

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
