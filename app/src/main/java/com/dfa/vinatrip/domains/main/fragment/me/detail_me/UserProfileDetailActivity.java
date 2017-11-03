package com.dfa.vinatrip.domains.main.fragment.me.detail_me;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseActivity;
import com.dfa.vinatrip.domains.main.fragment.me.detail_me.friend_receive.FriendReceiveFragment;
import com.dfa.vinatrip.domains.main.fragment.me.detail_me.friend_receive.FriendReceiveFragment_;
import com.dfa.vinatrip.domains.main.fragment.me.detail_me.make_friend.MakeFriendFragment;
import com.dfa.vinatrip.domains.main.fragment.me.detail_me.make_friend.MakeFriendFragment_;
import com.dfa.vinatrip.domains.main.fragment.me.detail_me.my_friend.MyFriendFragment;
import com.dfa.vinatrip.domains.main.fragment.me.detail_me.my_friend.MyFriendFragment_;
import com.dfa.vinatrip.domains.main.fragment.me.detail_me.my_rating.MyRatingFragment;
import com.dfa.vinatrip.domains.main.fragment.me.detail_me.my_rating.MyRatingFragment_;
import com.dfa.vinatrip.domains.main.fragment.me.detail_me.update_profile.UpdateUserProfileFragment;
import com.dfa.vinatrip.domains.main.fragment.me.detail_me.update_profile.UpdateUserProfileFragment_;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.utils.AppUtil;
import com.dfa.vinatrip.widgets.ViewPagerSwipeFragmentAdapter;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

@EActivity(R.layout.activity_user_profile_detail)
public class UserProfileDetailActivity extends BaseActivity<UserProfileDetailView, UserProfileDetailPresenter>
        implements UserProfileDetailView {

    @Extra
    protected String fromView;

    @ViewById(R.id.my_toolbar)
    protected Toolbar toolbar;
    @ViewById(R.id.activity_user_profile_detail_vp)
    protected ViewPager viewpager;
    @ViewById(R.id.activity_user_profile_detail_tl_menu)
    protected TabLayout tlMenu;

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
        setupActionBar();

        ViewPagerSwipeFragmentAdapter adapter = new ViewPagerSwipeFragmentAdapter(getSupportFragmentManager());

        UpdateUserProfileFragment updateUserProfileFragment = new UpdateUserProfileFragment_();
        MakeFriendFragment makeFriendFragment = new MakeFriendFragment_();
        FriendReceiveFragment friendReceiveFragment = new FriendReceiveFragment_();
        MyFriendFragment myFriendFragment = new MyFriendFragment_();
        MyRatingFragment myRatingFragment = new MyRatingFragment_();

        adapter.addFragment(updateUserProfileFragment, "CHỈNH SỬA");
        adapter.addFragment(makeFriendFragment, "THÊM BẠN");
        adapter.addFragment(friendReceiveFragment, "LỜI MỜI");
        adapter.addFragment(myFriendFragment, "BẠN TÔI");
        adapter.addFragment(myRatingFragment, "ĐÁNH GIÁ");

        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(adapter.getCount());

        tlMenu.setupWithViewPager(viewpager);

        switch (fromView) {
            case "llUpdateProfile":
                viewpager.setCurrentItem(0);
                break;
            case "tvMakeFriend":
                viewpager.setCurrentItem(1);
                break;
            case "tvViewMoreMyFriend":
                viewpager.setCurrentItem(2);
                break;
            case "tvViewMoreMyRating":
                viewpager.setCurrentItem(3);
                break;
        }
    }

    public void setupActionBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
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
