package com.dfa.vinatrip.models.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by duonghd on 1/2/2018.
 * duonghd1307@gmail.com
 */

public class ResetPasswordRequest implements Serializable {
    @SerializedName("email")
    private String email;

    public ResetPasswordRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
