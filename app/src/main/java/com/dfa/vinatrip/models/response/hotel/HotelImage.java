package com.dfa.vinatrip.models.response.hotel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by duonghd on 10/7/2017.
 */

public class HotelImage implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("hotel_id")
    private int hotel_id;
    @SerializedName("url")
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHotel_id() {
        return hotel_id;
    }

    public void setHotel_id(int hotel_id) {
        this.hotel_id = hotel_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
