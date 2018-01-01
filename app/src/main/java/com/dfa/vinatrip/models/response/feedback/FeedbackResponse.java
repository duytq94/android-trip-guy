package com.dfa.vinatrip.models.response.feedback;

import com.dfa.vinatrip.models.response.user.User;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by duonghd on 12/7/2017.
 * duonghd1307@gmail.com
 */

public class FeedbackResponse implements Serializable{
    @SerializedName("id")
    private int id;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("hotel_id")
    private int hotelId;
    @SerializedName("food_id")
    private int foodId;
    @SerializedName("place_id")
    private int placeId;
    @SerializedName("type")
    private String type;
    @SerializedName("content")
    private String content;
    @SerializedName("rate")
    private float rate;
    @SerializedName("status")
    private int status;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("profile")
    private User profile;
    
    public int getId() {
        return id;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public int getHotelId() {
        return hotelId;
    }
    
    public int getFoodId() {
        return foodId;
    }
    
    public int getPlaceId() {
        return placeId;
    }
    
    public String getType() {
        return type;
    }
    
    public String getContent() {
        return content;
    }
    
    public float getRate() {
        return rate;
    }
    
    public int getStatus() {
        return status;
    }
    
    public String getCreatedAt() {
        return createdAt;
    }
    
    public String getUpdatedAt() {
        return updatedAt;
    }
    
    public User getProfile() {
        return profile;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }
    
    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }
    
    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public void setRate(float rate) {
        this.rate = rate;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public void setProfile(User profile) {
        this.profile = profile;
    }
}
