package com.dfa.vinatrip.models.response.festival;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by duonghd on 10/13/2017.
 * duonghd1307@gmail.com
 */

public class FestivalResponse implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("province_id")
    private int provinceId;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("star")
    private float star;
    @SerializedName("review")
    private int review;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("address")
    private String address;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("time_start")
    private String timeStart;
    @SerializedName("time_type")
    private int timeType;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    
    public FestivalResponse() {
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
    
    public float getStar() {
        return star;
    }
    
    public String getAvatar() {
        return avatar;
    }
    
    public String getAddress() {
        return address;
    }
    
    public double getLatitude() {
        return latitude;
    }
    
    public double getLongitude() {
        return longitude;
    }
    
    public String getTimeStart() {
        return timeStart;
    }
    
    public int getTimeType() {
        return timeType;
    }
    
    public String getCreatedAt() {
        return createdAt;
    }
    
    public String getUpdatedAt() {
        return updatedAt;
    }
    
    public String getTime() {
        return String.format("%s (%s)", this.timeStart, this.timeType != 1 ? "Âm lịch" : "Dương lịch");
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
    
    public void setStar(float star) {
        this.star = star;
    }
    
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }
    
    public void setTimeType(int timeType) {
        this.timeType = timeType;
    }
    
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }
}
