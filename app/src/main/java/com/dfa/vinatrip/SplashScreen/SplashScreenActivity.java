package com.dfa.vinatrip.SplashScreen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.dfa.vinatrip.DataService.DataService;
import com.dfa.vinatrip.DataService.FirebaseApi;
import com.dfa.vinatrip.MainFunction.MainActivity_;
import com.dfa.vinatrip.MainFunction.Me.UserDetail.MakeFriend.UserFriend;
import com.dfa.vinatrip.MainFunction.Me.UserProfile;
import com.dfa.vinatrip.MainFunction.Province.Province;
import com.dfa.vinatrip.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@EActivity(R.layout.activity_splash_screen)
public class SplashScreenActivity extends AppCompatActivity {

    @Bean
    DataService dataService;

    private List<Province> provinceList;
    private List<UserProfile> userProfileList;
    private List<UserFriend> userFriendList;

    private FirebaseUser firebaseUser;

    private Retrofit retrofit;
    private FirebaseApi firebaseApi;

    // Listen when all data load done
    private int count = 0;

    @AfterViews
    void onCreate() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        retrofit = new Retrofit.Builder().baseUrl("https://tripguy-10864.firebaseio.com")
                .addConverterFactory(GsonConverterFactory.create()).build();
        firebaseApi = retrofit.create(FirebaseApi.class);

        if (firebaseUser != null) {
            loadProvinceAndMore();
            loadUserProfile();
            loadUserFriend();
        } else {
            loadProvince();
        }
    }

    public void loadProvince() {
        firebaseApi.loadProvince().enqueue(new Callback<HashMap<String, Province>>() {
            @Override
            public void onResponse(Call<HashMap<String, Province>> call, Response<HashMap<String, Province>> response) {
                provinceList = new ArrayList<>();
                provinceList.addAll(response.body().values());
                dataService.setProvinceList(provinceList);
                startActivity(new Intent(SplashScreenActivity.this, MainActivity_.class));
            }

            @Override
            public void onFailure(Call<HashMap<String, Province>> call, Throwable t) {

            }
        });
    }

    public void loadProvinceAndMore() {
        firebaseApi.loadProvince().enqueue(new Callback<HashMap<String, Province>>() {
            @Override
            public void onResponse(Call<HashMap<String, Province>> call, Response<HashMap<String, Province>> response) {
                provinceList = new ArrayList<>();
                provinceList.addAll(response.body().values());
                dataService.setProvinceList(provinceList);
                count++;
                if (count == 3) {
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity_.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<HashMap<String, Province>> call, Throwable t) {

            }
        });
    }

    public void loadUserProfile() {
        firebaseApi.loadUserProfile().enqueue(new Callback<HashMap<String, UserProfile>>() {
            @Override
            public void onResponse(Call<HashMap<String, UserProfile>> call, Response<HashMap<String, UserProfile>> response) {
                userProfileList = new ArrayList<>();
                userProfileList.addAll(response.body().values());
                dataService.setUserProfileList(userProfileList);
                for (UserProfile userProfile : userProfileList) {
                    if (userProfile.getUid().equals(firebaseUser.getUid())) {
                        dataService.setCurrentUser(userProfile);
                        break;
                    }
                }
                count++;
                if (count == 3) {
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity_.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<HashMap<String, UserProfile>> call, Throwable t) {

            }
        });
    }

    public void loadUserFriend() {
        firebaseApi.loadUserFriend(firebaseUser.getUid()).enqueue(new Callback<HashMap<String, UserFriend>>() {
            @Override
            public void onResponse(Call<HashMap<String, UserFriend>> call, Response<HashMap<String, UserFriend>> response) {
                userFriendList = new ArrayList<>();
                if (response.body() != null) {
                    userFriendList.addAll(response.body().values());
                    dataService.setUserFriendList(userFriendList);
                }
                count++;
                if (count == 3) {
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity_.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<HashMap<String, UserFriend>> call, Throwable t) {

            }
        });
    }
}
