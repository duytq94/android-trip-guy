package com.dfa.vinatrip.models;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by duonghd on 1/12/2018.
 * duonghd1307@gmail.com
 */

public class IntroObj implements Serializable {
    private Drawable bgDrawable;
    private int icDrawable;
    private String ioTitle;
    private String ioContent;
    
    public IntroObj(Drawable bgDrawable, int icDrawable, String ioTitle, String ioContent) {
        this.bgDrawable = bgDrawable;
        this.icDrawable = icDrawable;
        this.ioTitle = ioTitle;
        this.ioContent = ioContent;
    }
    
    public Drawable getBgDrawable() {
        return bgDrawable;
    }
    
    public int getIcDrawable() {
        return icDrawable;
    }
    
    public String getIoTitle() {
        return ioTitle;
    }
    
    public String getIoContent() {
        return ioContent;
    }
    
    public void setBgDrawable(Drawable bgDrawable) {
        this.bgDrawable = bgDrawable;
    }
    
    public void setIcDrawable(int icDrawable) {
        this.icDrawable = icDrawable;
    }
    
    public void setIoTitle(String ioTitle) {
        this.ioTitle = ioTitle;
    }
    
    public void setIoContent(String ioContent) {
        this.ioContent = ioContent;
    }
}