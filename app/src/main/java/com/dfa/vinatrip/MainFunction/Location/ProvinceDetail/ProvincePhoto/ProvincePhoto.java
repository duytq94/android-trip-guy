package com.dfa.vinatrip.MainFunction.Location.ProvinceDetail.ProvincePhoto;

import java.io.Serializable;

public class

// Some beautiful photo about Province
ProvincePhoto implements Serializable {
    private String url;

    public ProvincePhoto(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

}
