package com.dfa.vinatrip.models.response.place;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by duonghd on 10/8/2017.
 */

public class PlaceImage implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("place_id")
    private int placeId;
    @SerializedName("url")
    private String url;

    public int getId() {
        return id;
    }

    public int getPlaceId() {
        return placeId;
    }

    public String getUrl() {
        return url;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
