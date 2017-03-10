package com.dfa.vinatrip.SplashScreen;

import com.dfa.vinatrip.MainFunction.Location.Province;
import com.dfa.vinatrip.MainFunction.Me.UserDetail.MakeFriend.UserFriend;
import com.dfa.vinatrip.MainFunction.Me.UserDetail.MakeFriend.UserLocation;
import com.dfa.vinatrip.MainFunction.Me.UserProfile;

import org.androidannotations.annotations.EBean;

import java.util.List;

import static org.androidannotations.annotations.EBean.Scope.Singleton;

@EBean(scope = Singleton)
public class DataService {

    private List<UserProfile> userProfileList;
    private List<UserFriend> userFriendList;
    private List<Province> provinceList;
    private List<UserLocation> userLocationList;
    private UserProfile currentUser;

    public void setCurrentUser(UserProfile currentUser) {
        this.currentUser = currentUser;
    }

    public UserProfile getCurrentUser() {
        return currentUser;
    }

    public void setUserLocationList(List<UserLocation> userLocationList) {
        this.userLocationList = userLocationList;
    }

    public List<UserLocation> getUserLocationList() {

        return userLocationList;
    }

    public void setUserProfileList(List<UserProfile> userProfileList) {
        this.userProfileList = userProfileList;
    }

    public void setUserFriendList(List<UserFriend> userFriendList) {
        this.userFriendList = userFriendList;
    }

    public void setProvinceList(List<Province> provinceList) {
        this.provinceList = provinceList;
    }

    public List<UserProfile> getUserProfileList() {
        return userProfileList;
    }

    public List<UserFriend> getUserFriendList() {
        return userFriendList;
    }

    public List<Province> getProvinceList() {
        return provinceList;
    }
}
