package com.dfa.vinatrip.domains.main.fragment.me.detail_me;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseActivity;
import com.dfa.vinatrip.domains.chat.ChatGroupPresenter;
import com.dfa.vinatrip.domains.chat.DaggerChatGroupComponent;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.services.default_data.DataService;
import com.dfa.vinatrip.utils.AppUtil;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

@EActivity(R.layout.activity_user_profile_detail)
public class UserProfileDetailActivity extends BaseActivity<UserProfileDetailView, UserProfileDetailPresenter>
        implements UserProfileDetailView {

    private String fromView;
    private ActionBar actionBar;

    @ViewById(R.id.my_toolbar)
    protected Toolbar toolbar;

    @App
    protected MainApplication application;
    @Inject
    protected UserProfileDetailPresenter presenter;

    @AfterInject
    protected void initInject() {
        DaggerUserProfileDetailComponent.builder()
                .applicationComponent(application.getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build().inject(this);
    }

    @NonNull
    @Override
    public UserProfileDetailPresenter createPresenter() {
        return presenter;
    }

    @AfterViews
    public void init() {
        // Get FromView from MeFragment to know which view user clicked
        fromView = getIntent().getStringExtra("FromView");

        UserProfileDetailFragment userProfileDetailFragment = new UserProfileDetailFragment_();

        Bundle bdFromView = new Bundle();
        bdFromView.putString("FromView", fromView);

        Bundle bd = new Bundle();
        bd.putBundle("bdFromView", bdFromView);

        userProfileDetailFragment.setArguments(bd);
        startFragment(userProfileDetailFragment);

        setupActionBar();
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
            actionBar.setTitle(presenter.getCurrentUser().getUsername());
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
                AppUtil.hideKeyBoard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void showLoading() {
        showHUD();
    }

    @Override
    public void hideLoading() {
        hideHUD();
    }

    @Override
    public void apiError(Throwable throwable) {

    }
}
