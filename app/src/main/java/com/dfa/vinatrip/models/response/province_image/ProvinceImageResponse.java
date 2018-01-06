package com.dfa.vinatrip.models.response.province_image;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by duonghd on 11/3/2017.
 * duonghd1307@gmail.com
 */

public class ProvinceImageResponse implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("province_id")
    private int provinceId;
    @SerializedName("url")
    private String url;
    
    public int getId() {
        return id;
    }
    
    public int getProvinceId() {
        return provinceId;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
}
