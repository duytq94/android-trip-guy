package com.dfa.vinatrip.SplashScreen;

import com.dfa.vinatrip.MainFunction.Location.Province;
import com.dfa.vinatrip.MainFunction.Me.UserDetail.MakeFriend.UserFriend;
import com.dfa.vinatrip.MainFunction.Me.UserDetail.MakeFriend.UserProfileAdapter;
import com.dfa.vinatrip.MainFunction.Me.UserProfile;

import org.androidannotations.annotations.EBean;

import java.util.List;

import static org.androidannotations.annotations.EBean.Scope.Singleton;

@EBean(scope = Singleton)
public class DataService {

    private List<UserProfile> userProfileList;
    private List<UserFriend> userFriendList;
    private List<Province> provinceList;
    private UserProfile currentUser;

    public void setCurrentUser(UserProfile currentUser) {
        this.currentUser = currentUser;
    }

    public UserProfile getCurrentUser() {
        return currentUser;
    }

    public void removeCurrentUser() {
        currentUser = null;
    }

    public void addToUserFriendList(UserFriend newUserFriend) {
        userFriendList.add(newUserFriend);
    }

    public void removeFromUserFriendList(String uid) {
        for (int i = 0; i < userFriendList.size(); i++) {
            if (userFriendList.get(i).getFriendId().equals(uid)) {
                userFriendList.remove(i);
                break;
            }
        }
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

    private UserProfileAdapter.OnChangeUserFriendList onChangeUserFriendList;

    public void setOnChangeUserFriendList(UserProfileAdapter.OnChangeUserFriendList onChangeUserFriendList) {
        this.onChangeUserFriendList = onChangeUserFriendList;
    }

    public interface OnChangeUserFriendList {
        void onAddItem();

        void onRemoveItem();
    }
}
