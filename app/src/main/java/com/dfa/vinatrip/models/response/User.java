package com.dfa.vinatrip.models.response;

import com.beesightsoft.caf.models.BaseEntity;
import com.beesightsoft.caf.services.authentication.model.LoginResponse;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by duonghd on 9/27/2017.
 */

public class User extends BaseEntity implements LoginResponse, Serializable {
    @SerializedName("username")
    private String username;
    @SerializedName("email")
    private String email;
    @SerializedName("avatar")
    private Object avatar;
    @SerializedName("birthday")
    private String birthday;
    @SerializedName("intro")
    private String intro;
    @SerializedName("sex")
    private Integer sex;
    @SerializedName("latitude")
    private Double latitude;
    @SerializedName("longitude")
    private Double longitude;
    @SerializedName("accessToken")
    private String accessToken;
    
    public String getUsername() {
        return username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public Object getAvatar() {
        return avatar;
    }
    
    public String getBirthday() {
        return birthday;
    }
    
    public String getIntro() {
        return intro;
    }
    
    public Integer getSex() {
        return sex;
    }
    
    public Double getLatitude() {
        return latitude;
    }
    
    public Double getLongitude() {
        return longitude;
    }
    
    public String getAccessToken() {
        return accessToken;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setAvatar(Object avatar) {
        this.avatar = avatar;
    }
    
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    
    public void setIntro(String intro) {
        this.intro = intro;
    }
    
    public void setSex(Integer sex) {
        this.sex = sex;
    }
    
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
