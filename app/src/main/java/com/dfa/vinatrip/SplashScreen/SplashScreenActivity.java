package com.dfa.vinatrip.SplashScreen;

import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

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
import org.androidannotations.annotations.ViewById;

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

    @ViewById(R.id.activity_splash_screen_iv_logo)
    ImageView ivLogo;

    private List<Province> provinceList;
    private List<UserProfile> userProfileList;
    private List<UserFriend> userFriendList;
    private FirebaseUser firebaseUser;
    private Retrofit retrofit;
    private FirebaseApi firebaseApi;
    private Animation zoomOut;

    // Listen when all data load done
    private int count = 0;

    @AfterViews
    void onCreate() {
        changeColorStatusBar();

        zoomOut = AnimationUtils.loadAnimation(this, R.anim.anim_zoom_out);
        ivLogo.startAnimation(zoomOut);
        zoomOut.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                ivLogo.startAnimation(zoomOut);

            }
        });

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

    public void changeColorStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorStatusBar));
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
                overridePendingTransition(R.anim.anim_right_to_center, R.anim.anim_center_to_left);
                finish();
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
                    overridePendingTransition(R.anim.anim_right_to_center, R.anim.anim_center_to_left);
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
                    overridePendingTransition(R.anim.anim_right_to_center, R.anim.anim_center_to_left);
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
                    overridePendingTransition(R.anim.anim_right_to_center, R.anim.anim_center_to_left);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<HashMap<String, UserFriend>> call, Throwable t) {

            }
        });
    }
}
