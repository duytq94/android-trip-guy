package com.dfa.vinatrip.services;

import com.dfa.vinatrip.domains.main.me.UserProfile;
import com.dfa.vinatrip.domains.main.me.detail_me.make_friend.UserFriend;
import com.dfa.vinatrip.domains.main.province.Province;
import com.dfa.vinatrip.domains.main.province.each_item_detail_province.rating.UserRating;

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

    @GET("/UserRating/{userId}.json")
    Call<HashMap<String, UserRating>> loadMyRating(@Path("userId") String userId);
}
