package com.dfa.vinatrip.models.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by duonghd on 12/5/2017.
 */

public class FeedbackRequest implements Serializable {
    @SerializedName("content")
    private String content;
    @SerializedName("rate")
    private float rate;
    
    public FeedbackRequest(String content, float rate) {
        this.content = content;
        this.rate = rate;
    }
    
    public String getContent() {
        return content;
    }
    
    public float getRate() {
        return rate;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public void setRate(float rate) {
        this.rate = rate;
    }
}
