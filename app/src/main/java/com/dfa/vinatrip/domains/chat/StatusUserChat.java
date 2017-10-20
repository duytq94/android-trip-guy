package com.dfa.vinatrip.domains.chat;

import com.google.gson.annotations.SerializedName;

/**
 * Created by duytq on 10/06/2017.
 */

public class StatusUserChat {

    @SerializedName("email")
    private String email;
    @SerializedName("is_online")
    private int isOnline;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getIsOnline() {
        if (isOnline == 0) return false;
        else return true;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }
}
