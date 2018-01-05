package com.dfa.vinatrip.domains.other_user_profile;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseActivity;
import com.dfa.vinatrip.infrastructures.ActivityModule;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;

import javax.inject.Inject;

/**
 * Created by duonghd on 1/5/2018.
 * duonghd1307@gmail.com
 */

@SuppressLint("Registered")
@EActivity(R.layout.activity_other_user_profile)
public class OtherUserProfileActivity extends BaseActivity<OtherUserProfileView, OtherUserProfilePresenter>
        implements OtherUserProfileView {
    @App
    protected MainApplication mainApplication;
    @Inject
    protected OtherUserProfilePresenter presenter;

    @AfterInject
    void initInject() {
        DaggerOtherUserProfileComponent.builder()
                .applicationComponent(mainApplication.getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build().inject(this);
    }

    @AfterViews
    void init() {

    }

    @NonNull
    @Override
    public OtherUserProfilePresenter createPresenter() {
        return presenter;
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
