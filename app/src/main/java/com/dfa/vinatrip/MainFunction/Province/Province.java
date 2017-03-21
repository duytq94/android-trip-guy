package com.dfa.vinatrip.MainFunction.Province;

import java.io.Serializable;

public class Province implements Serializable {

    private String name;
    private String title;
    private String avatar;
    private String description;

    public Province() {
    }

    public Province(String name, String title, String avatar, String description) {
        this.name = name;
        this.title = title;
        this.avatar = avatar;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getDescription() {
        return description;
    }
}
