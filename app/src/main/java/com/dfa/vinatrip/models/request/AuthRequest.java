package com.dfa.vinatrip.models.request;

import com.beesightsoft.caf.models.BaseEntity;
import com.beesightsoft.caf.services.authentication.model.LoginRequest;
import com.google.gson.annotations.SerializedName;

/**
 * Created by duytq on 9/19/2017.
 */

public class AuthRequest extends BaseEntity implements LoginRequest {
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    
    public AuthRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    @Override
    public String getUsername() {
        return null;
    }
    
    @Override
    public void setUsername(String username) {
        
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}