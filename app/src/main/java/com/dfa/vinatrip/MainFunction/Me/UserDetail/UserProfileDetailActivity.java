package com.dfa.vinatrip.MainFunction.Me.UserDetail;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.dfa.vinatrip.MainFunction.Me.UserProfile;
import com.dfa.vinatrip.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_user_profile_detail)
public class UserProfileDetailActivity extends AppCompatActivity {

    private UserProfile userProfile;
    private List<UserProfile> listUserProfiles;
    private String fromView;
    private android.support.v7.app.ActionBar actionBar;

    @ViewById(R.id.my_toolbar)
    Toolbar toolbar;

    @AfterViews
    void onCreate() {

        changeColorStatusBar();

        // Get ListUserProfiles form MeFragment
        listUserProfiles = new ArrayList<>();
        listUserProfiles = getIntent().getParcelableExtra("ListUserProfiles");

        // Get UserProfile from MeFragment
        userProfile = getIntent().getParcelableExtra("UserProfile");

        // Get FromView from MeFragment to know which view user clicked
        fromView = getIntent().getStringExtra("FromView");

        UserProfileDetailFragment userProfileDetailFragment = new UserProfileDetailFragment_();

        Bundle bdUserProfile = new Bundle();
        bdUserProfile.putParcelable("UserProfile", userProfile);

        Bundle bdFromView = new Bundle();
        bdFromView.putString("FromView", fromView);

        Bundle bdListUserProfiles = new Bundle();
        bdListUserProfiles.putParcelable("ListUserProfiles", (Parcelable) listUserProfiles);

        Bundle bd = new Bundle();
        bd.putBundle("bdUserProfile", bdUserProfile);
        bd.putBundle("bdFromView", bdFromView);
        bd.putBundle("bdListUserProfiles", bdListUserProfiles);

        userProfileDetailFragment.setArguments(bd);
        startFragment(userProfileDetailFragment);

        setupActionBar();
    }

    public void changeColorStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorStatusBar));
        }
    }

    public void startFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        String fragmentTag = backStateName;

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null) {
            //fragment not in back stack, create it.
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.replace(R.id.activity_user_profile_detail_fl_container, fragment, fragmentTag);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();
        }
    }

    public void setupActionBar() {
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(userProfile.getNickname());
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#228B22")));

            // Set button back
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }
}
