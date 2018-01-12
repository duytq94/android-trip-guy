package com.dfa.vinatrip.domains.intro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.auth.sign_in.SignInActivity_;
import com.dfa.vinatrip.domains.main.splash.SplashScreenActivity_;
import com.dfa.vinatrip.models.IntroObj;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressLint("Registered")
@EActivity(R.layout.activity_welcome)
public class WelcomeActivity extends AppCompatActivity {
    @ViewById(R.id.activity_welcome_vp_slide_intro)
    protected ViewPager vpSlideIntro;
    @ViewById(R.id.activity_welcome_ll_dots)
    protected LinearLayout llDots;
    @ViewById(R.id.activity_welcome_btn_next)
    protected Button btnNext;
    @ViewById(R.id.activity_welcome_btn_back)
    protected Button btnBack;
    
    private ArrayList<IntroObj> introObjs;
    private CheckFirstTimeLaunch prefManager;
    
    @AfterViews
    void onCreate() {
        // make status bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        // Checking for first time launch - before calling setContentView()
        prefManager = new CheckFirstTimeLaunch(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        } else {
            handleBackgroundPager();
        }
    }
    
    private void handleBackgroundPager() {
        introObjs = new ArrayList<>(Arrays.asList(
                new IntroObj(getResources().getDrawable(R.drawable.bg_location), R.drawable.ic_location, getResources().getString(R.string.welcome_screen_title1), getResources().getString(R.string.welcome_screen_desc1)),
                new IntroObj(getResources().getDrawable(R.drawable.bg_plan), R.drawable.ic_plan, getResources().getString(R.string.welcome_screen_title2), getResources().getString(R.string.welcome_screen_desc2)),
                new IntroObj(getResources().getDrawable(R.drawable.bg_share), R.drawable.ic_share, getResources().getString(R.string.welcome_screen_title3), getResources().getString(R.string.welcome_screen_desc3)),
                new IntroObj(getResources().getDrawable(R.drawable.bg_memory), R.drawable.ic_my_friend, getResources().getString(R.string.welcome_screen_title4), getResources().getString(R.string.welcome_screen_desc4))
        ));
        
        BackgroundPagerAdapter backgroundPagerAdapter = new BackgroundPagerAdapter(this, introObjs);
        vpSlideIntro.setAdapter(backgroundPagerAdapter);
        vpSlideIntro.setOffscreenPageLimit(introObjs.size());
        vpSlideIntro.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
                
                // changing the next button text 'NEXT' / 'GOT IT'
                if (position == introObjs.size() - 1) {
                    // last page. make button text to GOT IT
                    btnNext.setVisibility(View.GONE);
                } else if (position == 0) {
                    btnBack.setVisibility(View.GONE);
                } else {
                    btnBack.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.VISIBLE);
                }
            }
            
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
            
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        
        // Check color first dot
        addBottomDots(0);
    }
    
    public void addBottomDots(int currentPage) {
        TextView[] tvDots = new TextView[introObjs.size()];
        
        llDots.removeAllViews();
        for (int i = 0; i < tvDots.length; i++) {
            tvDots[i] = new TextView(this);
            // symbol bullet
            tvDots[i].setText("\u2022");
            tvDots[i].setTextSize(35);
            tvDots[i].setTextColor(ContextCompat.getColor(this, R.color.colorDotInactive));
            llDots.addView(tvDots[i]);
        }
        
        if (tvDots.length > 0)
            tvDots[currentPage].setTextColor(ContextCompat.getColor(this, R.color.colorDotActive));
    }
    
    public void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(WelcomeActivity.this, SplashScreenActivity_.class));
        finish();
    }
    
    @Click(R.id.activity_welcome_btn_back)
    void onBtnBackClick() {
        int current = vpSlideIntro.getCurrentItem() - 1;
        if (current >= 0) {
            vpSlideIntro.setCurrentItem(current);
        }
    }
    
    @Click(R.id.activity_welcome_btn_next)
    void onBtnNextClick() {
        int current = vpSlideIntro.getCurrentItem() + 1;
        if (current < introObjs.size()) {
            vpSlideIntro.setCurrentItem(current);
        }
    }
    
    @Click(R.id.activity_welcome_btn_launch_now)
    void onBtnLaunchNowClick() {
        launchHomeScreen();
    }
    
    @Click(R.id.activity_welcome_btn_sign_up)
    void onBtnSignUpClick() {
        prefManager.setFirstTimeLaunch(false);
        SignInActivity_.intent(this).start();
    }
}