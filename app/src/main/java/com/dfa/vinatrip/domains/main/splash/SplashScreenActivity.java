package com.dfa.vinatrip.domains.main.splash;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.MainActivity_;
import com.dfa.vinatrip.domains.main.me.UserProfile;
import com.dfa.vinatrip.domains.main.me.detail_me.make_friend.UserFriend;
import com.dfa.vinatrip.domains.main.province.Province;
import com.dfa.vinatrip.domains.main.province.each_item_detail_province.rating.UserRating;
import com.dfa.vinatrip.services.DataService;
import com.dfa.vinatrip.services.FirebaseApi;
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
    private List<UserRating> myRatingList;
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
            loadProvince();
            loadUserProfile();
            loadUserFriend();
            loadUserRating();
        } else {
            loadOnlyProvince();
        }
    }

    public void changeColorStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorStatusBar));
        }
    }

    public void loadOnlyProvince() {
        firebaseApi.loadProvince().enqueue(new Callback<HashMap<String, Province>>() {
            @Override
            public void onResponse(Call<HashMap<String, Province>> call, Response<HashMap<String, Province>> response) {
                provinceList = new ArrayList<>();
                provinceList.addAll(response.body().values());
                dataService.setProvinceList(provinceList);
                MainActivity_.intent(SplashScreenActivity.this).start();
                overridePendingTransition(R.anim.anim_right_to_center, R.anim.anim_center_to_left);
                finish();
            }

            @Override
            public void onFailure(Call<HashMap<String, Province>> call, Throwable t) {

            }
        });
    }

    public void loadProvince() {
        firebaseApi.loadProvince().enqueue(new Callback<HashMap<String, Province>>() {
            @Override
            public void onResponse(Call<HashMap<String, Province>> call, Response<HashMap<String, Province>> response) {
                provinceList = new ArrayList<>();
                provinceList.addAll(response.body().values());
                dataService.setProvinceList(provinceList);
                count++;
                if (count == 4) {
                    MainActivity_.intent(SplashScreenActivity.this).start();
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
            public void onResponse(Call<HashMap<String, UserProfile>> call,
                                   Response<HashMap<String, UserProfile>> response) {
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
                if (count == 4) {
                    MainActivity_.intent(SplashScreenActivity.this).start();
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
            public void onResponse(Call<HashMap<String, UserFriend>> call,
                                   Response<HashMap<String, UserFriend>> response) {
                userFriendList = new ArrayList<>();
                if (response.body() != null) {
                    userFriendList.addAll(response.body().values());
                    dataService.setUserFriendList(userFriendList);
                }
                count++;
                if (count == 4) {
                    MainActivity_.intent(SplashScreenActivity.this).start();
                    overridePendingTransition(R.anim.anim_right_to_center, R.anim.anim_center_to_left);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<HashMap<String, UserFriend>> call, Throwable t) {

            }
        });
    }

    public void loadUserRating() {
        firebaseApi.loadMyRating(firebaseUser.getUid()).enqueue(new Callback<HashMap<String, UserRating>>() {
            @Override
            public void onResponse(Call<HashMap<String, UserRating>> call,
                                   Response<HashMap<String, UserRating>> response) {
                myRatingList = new ArrayList<UserRating>();
                if (response.body() != null) {
                    myRatingList.addAll(response.body().values());
                    dataService.setMyRatingList(myRatingList);
                }
                count++;
                if (count == 4) {
                    MainActivity_.intent(SplashScreenActivity.this).start();
                    overridePendingTransition(R.anim.anim_right_to_center, R.anim.anim_center_to_left);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<HashMap<String, UserRating>> call, Throwable t) {

            }
        });
    }
}
