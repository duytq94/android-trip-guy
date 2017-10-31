package com.dfa.vinatrip.domains.main.splash;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.MainActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_splash_screen)
public class SplashScreenActivity extends AppCompatActivity {

    @ViewById(R.id.activity_splash_screen_iv_logo)
    protected ImageView ivLogo;

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

        new Handler().postDelayed(() -> {
            MainActivity_.intent(SplashScreenActivity.this)
                    .flags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK).start();
            overridePendingTransition(R.anim.anim_right_to_center, R.anim.anim_center_to_left);
        }, 2000);
    }
}
