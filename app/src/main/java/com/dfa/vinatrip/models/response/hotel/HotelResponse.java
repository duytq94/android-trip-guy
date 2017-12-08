package com.dfa.vinatrip.models.response.hotel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by duonghd on 10/7/2017.
 */

public class HotelResponse implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("province_id")
    private int province_id;
    @SerializedName("star")
    private float star;
    @SerializedName("name")
    private String name;
    @SerializedName("phone_number")
    private String phone_number;
    @SerializedName("web_address")
    private String web_address;
    @SerializedName("address")
    private String address;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("price")
    private int price;
    @SerializedName("per_price")
    private String per_price;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("images")
    private List<HotelImage> images;
    
    public HotelResponse() {
    }
    
    public int getId() {
        return id;
    }
    
    public int getProvince_id() {
        return province_id;
    }
    
    public float getStar() {
        return star;
    }
    
    public String getName() {
        return name;
    }
    
    public String getPhone_number() {
        return phone_number;
    }
    
    public String getWeb_address() {
        return web_address;
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
    
    public int getPrice() {
        return price;
    }
    
    public String getPer_price() {
        return per_price;
    }
    
    public String getAvatar() {
        return avatar;
    }
    
    public List<HotelImage> getImages() {
        return images;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setProvince_id(int province_id) {
        this.province_id = province_id;
    }
    
    public void setStar(float star) {
        this.star = star;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
    
    public void setWeb_address(String web_address) {
        this.web_address = web_address;
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
    
    public void setPrice(int price) {
        this.price = price;
    }
    
    public void setPer_price(String per_price) {
        this.per_price = per_price;
    }
    
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    
    public void setImages(List<HotelImage> images) {
        this.images = images;
    }
}
