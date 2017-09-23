package com.dfa.vinatrip.models.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by duytq on 9/19/2017.
 */

public class SignUpRequest {
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}