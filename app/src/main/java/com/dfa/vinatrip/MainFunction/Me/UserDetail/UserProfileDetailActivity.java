package com.dfa.vinatrip.MainFunction.Me.UserDetail;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.dfa.vinatrip.MainFunction.Me.UserProfile;
import com.dfa.vinatrip.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserProfileDetailActivity extends AppCompatActivity {

    private UserProfile userProfile;
    private List<UserProfile> listUserProfiles;
    private String fromView;
    private Toolbar toolbar;
    private android.support.v7.app.ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_detail);

        changeColorStatusBar();

        // Get ListUserProfiles form MeFragment
        listUserProfiles = new ArrayList<>();
        listUserProfiles = (List<UserProfile>) getIntent().getSerializableExtra("ListUserProfiles");

        // Get UserProfile from MeFragment
        userProfile = (UserProfile) getIntent().getSerializableExtra("UserProfile");

        // Get FromView from MeFragment to know which view user clicked
        fromView = getIntent().getStringExtra("FromView");

        UserProfileDetailFragment userProfileDetailFragment = new UserProfileDetailFragment();

        Bundle bdUserProfile = new Bundle();
        bdUserProfile.putSerializable("UserProfile", userProfile);

        Bundle bdFromView = new Bundle();
        bdFromView.putString("FromView", fromView);

        Bundle bdListUserProfiles = new Bundle();
        bdListUserProfiles.putSerializable("ListUserProfiles", (Serializable) listUserProfiles);

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
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(userProfile.getNickname());
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#228B22")));

            // Set button back
            actionBar.setHomeButtonEnabled(true);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();

        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setQueryHint("Tìm kiếm...");

        return true;
    }
}
