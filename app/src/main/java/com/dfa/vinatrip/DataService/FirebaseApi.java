package com.dfa.vinatrip.DataService;

import com.dfa.vinatrip.MainFunction.Me.UserDetail.MakeFriend.UserFriend;
import com.dfa.vinatrip.MainFunction.Me.UserProfile;
import com.dfa.vinatrip.MainFunction.Province.Province;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FirebaseApi {
    @GET("/Province.json")
    Call<HashMap<String, Province>> loadProvince();

    @GET("/UserProfile.json")
    Call<HashMap<String, UserProfile>> loadUserProfile();

    @GET("/UserFriend/{userId}.json")
    Call<HashMap<String, UserFriend>> loadUserFriend(@Path("userId") String userId);
}
