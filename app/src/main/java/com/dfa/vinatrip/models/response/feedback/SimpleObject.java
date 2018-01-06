package com.dfa.vinatrip.models.response.feedback;

import com.sinch.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by duonghd on 1/6/2018.
 * duonghd1307@gmail.com
 */

public class SimpleObject implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("avatar")
    private String avatar;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
