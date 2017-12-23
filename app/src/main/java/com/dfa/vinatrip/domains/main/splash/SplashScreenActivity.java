package com.dfa.vinatrip.domains.main.splash;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.MainActivity_;
import com.dfa.vinatrip.models.response.user.User;
import com.dfa.vinatrip.utils.Constants;
import com.dfa.vinatrip.video_call.BaseVideoCallActivity;
import com.dfa.vinatrip.video_call.SinchService;
import com.orhanobut.hawk.Hawk;
import com.sinch.android.rtc.SinchError;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_splash_screen)
public class SplashScreenActivity extends BaseVideoCallActivity implements SinchService.StartFailedListener {

    @ViewById(R.id.activity_splash_screen_iv_logo)
    protected ImageView ivLogo;

    protected User currentUser;

    @AfterViews
    public void init() {
        Animation zoomOut = AnimationUtils.loadAnimation(this, R.anim.anim_zoom_out);
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

        if (Hawk.get(Constants.KEY_USER_AUTH) != null) {
            currentUser = Hawk.get(Constants.KEY_USER_AUTH);
        }

        //asking for permissions here
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_PHONE_STATE}, 100);
        }
    }

    //this method is invoked when the connection is established with the SinchService
    @Override
    protected void onServiceConnected() {
        loginSinch();
        getSinchServiceInterface().setStartListener(this);
    }

    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
    }

    //Invoked when just after the service is connected with Sinch
    @Override
    public void onStarted() {
        MainActivity_.intent(SplashScreenActivity.this)
                .flags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
                .start();
        overridePendingTransition(R.anim.anim_right_to_center, R.anim.anim_center_to_left);
    }

    //Login is Clicked to manually to connect to the Sinch Service
    private void loginSinch() {
        if (!currentUser.getEmail().isEmpty()) {
            if (!getSinchServiceInterface().isStarted()) {
                getSinchServiceInterface().startClient(currentUser.getEmail());
            } else {
                MainActivity_.intent(SplashScreenActivity.this)
                        .flags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
                        .start();
                overridePendingTransition(R.anim.anim_right_to_center, R.anim.anim_center_to_left);
            }
        }
    }

}
