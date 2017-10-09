package com.dfa.vinatrip.domains.location;

import com.google.android.gms.maps.model.Marker;

public class UserFriendMarker {
    private Marker marker;
    private String from_user;

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public String getFrom_user() {
        return from_user;
    }

    public void setFrom_user(String from_user) {
        this.from_user = from_user;
    }

    public UserFriendMarker(Marker marker, String from_user) {

        this.marker = marker;
        this.from_user = from_user;
    }
}
