package com.dfa.vinatrip.domains.main.location_my_friend;

import com.google.android.gms.maps.model.Marker;

public class UserFriendMarker {
    private Marker marker;
    private String email;

    public UserFriendMarker(Marker marker, String email) {
        this.marker = marker;
        this.email = email;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
