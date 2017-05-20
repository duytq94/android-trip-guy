package com.dfa.vinatrip.domains.main.location_my_friend;

import com.google.android.gms.maps.model.Marker;

public class UserFriendMarker {
    private Marker marker;
    private String friendId;

    public UserFriendMarker(Marker marker, String friendId) {
        this.marker = marker;
        this.friendId = friendId;
    }

    public Marker getMarker() {
        return marker;
    }

    public String getFriendId() {
        return friendId;
    }
}
