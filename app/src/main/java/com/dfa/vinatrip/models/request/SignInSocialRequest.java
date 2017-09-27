package com.dfa.vinatrip.models.request;

import com.beesightsoft.caf.services.authentication.model.LoginSocialRequest;

import java.io.Serializable;

/**
 * Created by duonghd on 9/27/2017.
 */

public class SignInSocialRequest implements LoginSocialRequest, Serializable {
    
    @Override
    public String getAccessToken() {
        return null;
    }
    
    @Override
    public void setAccessToken(String accessToken) {
        
    }
    
    @Override
    public String getPlatform() {
        return null;
    }
    
    @Override
    public void setPlatform(String platform) {
        
    }
}
