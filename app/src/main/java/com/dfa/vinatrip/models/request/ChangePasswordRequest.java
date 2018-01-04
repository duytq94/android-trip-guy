package com.dfa.vinatrip.models.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by duonghd on 1/4/2018.
 * duonghd1307@gmail.com
 */

public class ChangePasswordRequest implements Serializable {
    @SerializedName("currentPassword")
    private String currentPassword;
    @SerializedName("newPassword")
    private String newPassword;

    public ChangePasswordRequest(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
