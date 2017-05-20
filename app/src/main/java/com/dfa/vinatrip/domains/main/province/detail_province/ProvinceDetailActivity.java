package com.dfa.vinatrip.domains.main.province.detail_province;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.province.Province;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

// Control when user click item about hotel, description...
@EActivity(R.layout.activity_province_detail)
public class ProvinceDetailActivity extends AppCompatActivity {

    @ViewById(R.id.activity_province_detail_collapse_toolbar)
    CollapsingToolbarLayout collapseToolbar;

    @ViewById(R.id.activity_province_detail_toolbar)
    Toolbar toolbar;

    @ViewById(R.id.activity_province_detail_iv_header)
    ImageView ivHeader;

    @Extra
    Province province;

    private android.support.v7.app.ActionBar actionBar;
    private IntentFilter intentFilter;
    private BroadcastReceiver broadcastReceiver;

    @AfterViews
    void onCreate() {
        ProvinceDetailFragment provinceDetailFragment = new ProvinceDetailFragment_();
        Bundle bundle = new Bundle();
        bundle.putParcelable("Province", province);
        provinceDetailFragment.setArguments(bundle);

        changeColorStatusBar();
        setupAppBar();
        startFragment(provinceDetailFragment);
        initBroadcastReceiver();
    }

    public void setupAppBar() {
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(province.getName());
            collapseToolbar.setTitleEnabled(false);

            // Set button back
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    public void startFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        String fragmentTag = backStateName;

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null) {
            //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.activity_province_detail_fl_container, fragment, fragmentTag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        }
    }

    public void changeColorStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorStatusBar));
        }
    }

    public void initBroadcastReceiver() {
        intentFilter = new IntentFilter("com.dfa.vinatrip.action.IMAGE_HEADER");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int position = intent.getIntExtra("POSITION", 0);
                switch (position) {
                    case 0:
                        ImageViewAnimatedChange(getBaseContext(), ivHeader, R.drawable.img_header_description);
                        break;
                    case 1:
                        ImageViewAnimatedChange(getBaseContext(), ivHeader, R.drawable.img_header_hotel);
                        break;
                    case 2:
                        ImageViewAnimatedChange(getBaseContext(), ivHeader, R.drawable.img_header_food);
                        break;
                    case 3:
                        ImageViewAnimatedChange(getBaseContext(), ivHeader, R.drawable.img_header_destination);
                        break;
                    case 4:
                        ImageViewAnimatedChange(getBaseContext(), ivHeader, R.drawable.img_header_photo);
                        break;
                }
            }
        };
    }

    public void ImageViewAnimatedChange(Context c, final ImageView v, final int new_image) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, android.R.anim.fade_out);
        final Animation anim_in = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
        anim_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setImageResource(new_image);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (broadcastReceiver == null) {
            initBroadcastReceiver();
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }
}
