package com.dfa.vinatrip.models.response.food;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by duonghd on 10/13/2017.
 */

public class FoodImage implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("food_id")
    private int foodId;
    @SerializedName("url")
    private String url;
    
    public int getId() {
        return id;
    }
    
    public int getFoodId() {
        return foodId;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
}