package com.dfa.vinatrip.models.response.place;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by duonghd on 10/8/2017.
 * duonghd1307@gmail.com
 */

public class PlaceResponse implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("province_id")
    private int provinceId;
    @SerializedName("star")
    private float star;
    @SerializedName("review")
    private int review;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("address")
    private String address;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("images")
    private List<PlaceImage> placeImages;
    
    public PlaceResponse() {
    }

    public int getId() {
        return id;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public String getAvatar() {
        return avatar;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public List<PlaceImage> getPlaceImages() {
        return placeImages;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setPlaceImages(List<PlaceImage> placeImages) {
        this.placeImages = placeImages;
    }

    public float getStar() {
        return star;
    }

    public void setStar(float star) {
        this.star = star;
    }

    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }
}
