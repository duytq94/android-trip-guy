package com.dfa.vinatrip.domains.main.fragment.deal;

import com.google.gson.annotations.SerializedName;

/**
 * Created by duytq on 10/14/2017.
 */

public class Deal {
    @SerializedName("id")
    private long id;
    @SerializedName("img")
    private String img;
    @SerializedName("title")
    private String title;
    @SerializedName("content")
    private String content;
    @SerializedName("price")
    private double price;
    @SerializedName("linkDetail")
    private String linkDetail;
    @SerializedName("route")
    private String route;
    @SerializedName("fromWebsite")
    private String fromWebsite;
    @SerializedName("dayStart")
    private String dayStart;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDayStart() {
        return dayStart;
    }

    public void setDayStart(String dayStart) {
        this.dayStart = dayStart;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getLinkDetail() {
        return linkDetail;
    }

    public void setLinkDetail(String linkDetail) {
        this.linkDetail = linkDetail;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getFromWebsite() {
        return fromWebsite;
    }

    public void setFromWebsite(String fromWebsite) {
        this.fromWebsite = fromWebsite;
    }
}
