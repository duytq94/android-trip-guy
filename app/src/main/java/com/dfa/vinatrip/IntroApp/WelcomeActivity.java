package com.dfa.vinatrip.IntroApp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfa.vinatrip.MainFunction.MainActivity;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.Login.SignUpActivity;

public class WelcomeActivity extends AppCompatActivity {

    private ViewPager vpSlideIntro;
    private ViewPagerAdapter viewPagerAdapter;
    private LinearLayout llDots;
    private TextView[] tvDots;
    private int[] layouts;
    private Button btnBack, btnNext, btnLaunchNow, btnSignUp;
    private CheckFirstTimeLaunch prefManager;

    // Catch event when page change, dots color will change
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
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
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Checking for first time launch - before calling setContentView()
        prefManager = new CheckFirstTimeLaunch(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }

        setContentView(R.layout.activity_welcome);
        findViewByIds();

        // layouts of all welcome sliders
        layouts = new int[]{
                R.layout.welcome_screen_location,
                R.layout.welcome_screen_plan,
                R.layout.welcome_screen_share,
                R.layout.welcome_screen_memory};

        // Check color first dot
        addBottomDots(0);

        // make status bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        viewPagerAdapter = new ViewPagerAdapter();
        vpSlideIntro.setAdapter(viewPagerAdapter);
        vpSlideIntro.addOnPageChangeListener(viewPagerPageChangeListener);

        onClickListener();

    }

    public void onClickListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = vpSlideIntro.getCurrentItem() - 1;
                if (current >= 0) {
                    vpSlideIntro.setCurrentItem(current);
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = vpSlideIntro.getCurrentItem() + 1;
                if (current < layouts.length) {
                    vpSlideIntro.setCurrentItem(current);
                }
            }
        });

        btnLaunchNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchHomeScreen();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefManager.setFirstTimeLaunch(false);
                startActivity(new Intent(WelcomeActivity.this, SignUpActivity.class));
            }
        });
    }

    public void findViewByIds() {
        vpSlideIntro = (ViewPager) findViewById(R.id.activity_welcome_vp_slide_intro);
        llDots = (LinearLayout) findViewById(R.id.activity_welcome_ll_dots);
        btnBack = (Button) findViewById(R.id.activity_welcome_btn_back);
        btnNext = (Button) findViewById(R.id.activity_welcome_btn_next);
        btnLaunchNow = (Button) findViewById(R.id.activity_welcome_btn_launch_now);
        btnSignUp = (Button) findViewById(R.id.activity_welcome_btn_sign_up);
    }

    public void addBottomDots(int currentPage) {
        tvDots = new TextView[layouts.length];

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
        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        finish();
    }

    public class ViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public ViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            // container là nơi chứa các welcome_screen
            container.addView(view);

            return view;
        }

        @Override
        // count views available
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}