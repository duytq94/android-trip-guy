package com.dfa.vinatrip.services;

import com.dfa.vinatrip.domains.main.me.UserProfile;
import com.dfa.vinatrip.domains.main.me.detail_me.make_friend.UserFriend;
import com.dfa.vinatrip.domains.main.plan.Plan;
import com.dfa.vinatrip.domains.main.province.Province;
import com.dfa.vinatrip.domains.main.province.each_item_detail_province.rating.UserRating;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

import static org.androidannotations.annotations.EBean.Scope.Singleton;

@EBean(scope = Singleton)
public class DataService {

    private List<UserProfile> userProfileList = new ArrayList<>();
    private List<UserFriend> userFriendList = new ArrayList<>();
    private List<Province> provinceList = new ArrayList<>();
    private List<UserRating> myRatingList = new ArrayList<>();
    private List<Plan> planList = new ArrayList<>();
    private UserProfile currentUser;

    private OnChangeUserFriendList onChangeUserFriendList;
    private OnChangeMyRatingList onChangeMyRatingList;
    private OnChangeCurrentUser onChangeCurrentUser;

    public void clearData() {
        userProfileList.clear();
        userFriendList.clear();
        myRatingList.clear();
        planList.clear();
        currentUser = null;
    }

    public void setCurrentUser(UserProfile currentUser) {
        this.currentUser = currentUser;
    }

    public void updateInforCurrentUser(UserProfile currentUser) {
        this.currentUser = currentUser;
        onChangeCurrentUser.onUpdateInfor();
    }

    public UserProfile getCurrentUser() {
        return currentUser;
    }

    public void addToUserFriendList(UserFriend newUserFriend) {
        for (UserFriend userFriend : userFriendList) {
            if (userFriend.getFriendId().equals(newUserFriend.getFriendId())) {
                onChangeUserFriendList.onAddFriend();
                return;
            }
        }
        userFriendList.add(newUserFriend);
        onChangeUserFriendList.onAddFriend();
    }

    public void removeFromUserFriendList(String uid) {
        for (int i = 0; i < userFriendList.size(); i++) {
            if (userFriendList.get(i).getFriendId().equals(uid)) {
                userFriendList.remove(i);
                onChangeUserFriendList.onRemoveFriend();
                break;
            }
        }
    }

    public void addToMyRatingList(UserRating myRating) {
        myRatingList.add(myRating);
        onChangeMyRatingList.onAddRating();
        onChangeMyRatingList.onAddRating();
    }

    public void updateToMyRatingList(UserRating myRating) {
        for (int i = 0; i < myRatingList.size(); i++) {
            if (myRatingList.get(i).getLocationName().equals(myRating.getLocationName())) {
                myRatingList.remove(i);
                break;
            }
        }
        myRatingList.add(myRating);
        onChangeMyRatingList.onUpdateRating();
    }

    public void setUserProfileList(List<UserProfile> userProfileList) {
        this.userProfileList.clear();
        this.userProfileList.addAll(userProfileList);
    }

    public void setUserFriendList(List<UserFriend> userFriendList) {
        this.userFriendList.clear();
        this.userFriendList.addAll(userFriendList);
    }

    public void setProvinceList(List<Province> provinceList) {
        this.provinceList.clear();
        this.provinceList.addAll(provinceList);
    }

    public void setMyRatingList(List<UserRating> myRatingList) {
        this.myRatingList.clear();
        this.myRatingList.addAll(myRatingList);
    }

    public void setPlanList(List<Plan> planList) {
        this.planList.clear();
        this.planList.addAll(planList);
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

    public List<UserRating> getMyRatingList() {
        return myRatingList;
    }

    public List<Plan> getPlanList() {
        return planList;
    }

    public void setOnChangeUserFriendList(OnChangeUserFriendList onChangeUserFriendList) {
        this.onChangeUserFriendList = onChangeUserFriendList;
    }

    public void setOnChangeMyRatingList(OnChangeMyRatingList onChangeMyRatingList) {
        this.onChangeMyRatingList = onChangeMyRatingList;
    }

    public void setOnChangeCurrentUser(OnChangeCurrentUser onChangeCurrentUser) {
        this.onChangeCurrentUser = onChangeCurrentUser;
    }

    public interface OnChangeUserFriendList {
        void onAddFriend();

        void onRemoveFriend();
    }

    public interface OnChangeMyRatingList {
        void onUpdateRating();

        void onAddRating();
    }

    public interface OnChangeCurrentUser {
        void onUpdateInfor();
    }
}
