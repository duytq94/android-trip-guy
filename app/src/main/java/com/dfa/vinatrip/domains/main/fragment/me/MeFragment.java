package com.dfa.vinatrip.domains.main.fragment.me;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dfa.vinatrip.BuildConfig;
import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseFragment;
import com.dfa.vinatrip.domains.auth.sign_in.SignInActivity_;
import com.dfa.vinatrip.domains.main.fragment.me.detail_me.UserProfileDetailActivity_;
import com.dfa.vinatrip.domains.main.fragment.me.detail_me.make_friend.UserFriend;
import com.dfa.vinatrip.domains.main.fragment.province.each_item_detail_province.rating.UserRating;
import com.dfa.vinatrip.domains.main.splash.SplashScreenActivity_;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.blurry.Blurry;

import static com.dfa.vinatrip.utils.AppUtil.REQUEST_UPDATE_INFO;


@EFragment(R.layout.fragment_me)
public class MeFragment extends BaseFragment<MeView, MePresenter>
        implements MeView {

    @ViewById(R.id.fragment_me_tv_nickname)
    protected TextView tvNickname;
    @ViewById(R.id.fragment_me_tv_city)
    protected TextView tvCity;
    @ViewById(R.id.fragment_me_tv_app_info)
    protected TextView tvAppInfo;
    @ViewById(R.id.fragment_me_tv_birthday)
    protected TextView tvBirthday;
    @ViewById(R.id.fragment_me_tv_introduce_your_self)
    protected TextView tvIntroduceYourSelf;
    @ViewById(R.id.fragment_me_tv_sex)
    protected TextView tvSex;
    @ViewById(R.id.fragment_me_tv_friend_not_available)
    protected TextView tvFriendNotAvailable;
    @ViewById(R.id.fragment_me_tv_make_friend)
    protected TextView tvMakeFriend;
    @ViewById(R.id.fragment_me_tv_email)
    protected TextView tvEmail;
    @ViewById(R.id.fragment_me_iv_avatar)
    protected ImageView ivAvatar;
    @ViewById(R.id.fragment_me_iv_blur_avatar)
    protected ImageView ivBlurAvatar;
    @ViewById(R.id.fragment_me_ll_sign_out)
    protected LinearLayout llSignOut;
    @ViewById(R.id.fragment_me_ll_info)
    protected LinearLayout llInfo;
    @ViewById(R.id.fragment_me_ll_my_friends)
    protected LinearLayout llMyFriends;
    @ViewById(R.id.fragment_me_ll_settings)
    protected LinearLayout llSettings;
    @ViewById(R.id.fragment_me_ll_update_profile)
    protected LinearLayout llUpdateProfile;
    @ViewById(R.id.fragment_me_srlReload)
    protected SwipeRefreshLayout srlReload;
    @ViewById(R.id.fragment_me_rl_login)
    protected RelativeLayout rlLogin;
    @ViewById(R.id.fragment_me_rl_not_login)
    protected RelativeLayout rlNotLogin;
    @ViewById(R.id.fragment_me_tv_rating_not_available)
    protected TextView tvRatingNotAvailable;

    @ViewById(R.id.fragment_me_ll_my_rating1)
    protected LinearLayout llMyRating1;
    @ViewById(R.id.fragment_me_civ_location1)
    protected CircleImageView civLocation1;
    @ViewById(R.id.fragment_me_tv_name_location1)
    protected TextView tvNameLocation1;
    @ViewById(R.id.fragment_me_tv_content1)
    protected TextView tvContent1;

    @ViewById(R.id.fragment_me_ll_my_rating2)
    protected LinearLayout llMyRating2;
    @ViewById(R.id.fragment_me_civ_location2)
    protected CircleImageView civLocation2;
    @ViewById(R.id.fragment_me_tv_name_location2)
    protected TextView tvNameLocation2;
    @ViewById(R.id.fragment_me_tv_content2)
    protected TextView tvContent2;

    @ViewById(R.id.fragment_me_ll_my_friend1)
    protected LinearLayout llMyFriend1;
    @ViewById(R.id.fragment_me_civ_friend_avatar1)
    protected CircleImageView civAvatar1;
    @ViewById(R.id.fragment_me_tv_friend_nickname1)
    protected TextView tvNickname1;
    @ViewById(R.id.fragment_me_tv_friend_email1)
    protected TextView tvEmail1;

    @ViewById(R.id.fragment_me_ll_my_friend2)
    protected LinearLayout llMyFriend2;
    @ViewById(R.id.fragment_me_civ_friend_avatar2)
    protected CircleImageView civAvatar2;
    @ViewById(R.id.fragment_me_tv_friend_nickname2)
    protected TextView tvNickname2;
    @ViewById(R.id.fragment_me_tv_friend_email2)
    protected TextView tvEmail2;

    private List<UserFriend> listUserFriends;
    private List<UserRating> myRatingList;

    @App
    protected MainApplication application;
    @Inject
    protected MePresenter presenter;

    @AfterInject
    protected void initInject() {
        DaggerMeComponent.builder()
                .applicationComponent(application.getApplicationComponent())
                .activityModule(new ActivityModule(getActivity()))
                .build().inject(this);
    }

    @Override
    public MePresenter createPresenter() {
        return presenter;
    }

    @AfterViews
    public void init() {

        showAppInfo();
        srlReload.setColorSchemeResources(R.color.colorMain);

        if (presenter.getCurrentUser() != null) {
            initView();
        } else {
            rlLogin.setVisibility(View.GONE);
            rlNotLogin.setVisibility(View.VISIBLE);
        }

        srlReload.setOnRefreshListener(() -> {
            if (presenter.getCurrentUser() != null) {
                initView();
            }
            srlReload.setRefreshing(false);
        });
    }

    public void showAppInfo() {
        String versionName = BuildConfig.VERSION_NAME;
        int versionCode = BuildConfig.VERSION_CODE;

        tvAppInfo.setText("TripGuy - giúp bạn có chuyến đi trọn vẹn nhất!\n");
        tvAppInfo.append("Version name: " + versionName + "\n");
        tvAppInfo.append("Version code: " + versionCode + "\n");
    }

    public void initView() {
        rlLogin.setVisibility(View.VISIBLE);
        rlNotLogin.setVisibility(View.GONE);

        String versionName = BuildConfig.VERSION_NAME;
        int versionCode = BuildConfig.VERSION_CODE;

        tvAppInfo.setText("TripGuy - giúp bạn có chuyến đi trọng vẹn nhất!\n");
        tvAppInfo.append("Version name: " + versionName + "\n");
        tvAppInfo.append("Version code: " + versionCode + "\n");

        listUserFriends = new ArrayList<>();
        myRatingList = new ArrayList<>();

        initLlMyFriend();

        initLlMyRating();

        initFlInfor();

        tvIntroduceYourSelf.setText(presenter.getCurrentUser().getIntro());
        tvBirthday.setText(presenter.getCurrentUser().getBirthday());
        tvEmail.setText(presenter.getCurrentUser().getEmail());
        tvSex.setText(presenter.getCurrentUser().getStringSex());
    }

    public void initLlMyFriend() {
        listUserFriends.clear();
        UserFriend userFriend;
        switch (listUserFriends.size()) {
            case 0:
                tvFriendNotAvailable.setVisibility(View.VISIBLE);
                llMyFriend1.setVisibility(View.GONE);
                llMyFriend2.setVisibility(View.GONE);
                break;
            case 1:
                tvFriendNotAvailable.setVisibility(View.GONE);
                userFriend = listUserFriends.get(0);
                llMyFriend1.setVisibility(View.VISIBLE);
                Picasso.with(getActivity()).load(userFriend.getAvatar()).into(civAvatar1);
                tvNickname1.setText(userFriend.getNickname());
                tvEmail1.setText(userFriend.getEmail());
                llMyFriend2.setVisibility(View.GONE);
                break;
            default:
                tvFriendNotAvailable.setVisibility(View.GONE);

                userFriend = listUserFriends.get(0);
                llMyFriend1.setVisibility(View.VISIBLE);
                Picasso.with(getActivity()).load(userFriend.getAvatar()).into(civAvatar1);
                tvNickname1.setText(userFriend.getNickname());
                tvEmail1.setText(userFriend.getEmail());

                userFriend = listUserFriends.get(1);
                llMyFriend2.setVisibility(View.VISIBLE);
                Picasso.with(getActivity()).load(userFriend.getAvatar()).into(civAvatar2);
                tvNickname2.setText(userFriend.getNickname());
                tvEmail2.setText(userFriend.getEmail());

                break;
        }
    }

    public void initLlMyRating() {
        myRatingList.clear();
        UserRating myRating;
        switch (myRatingList.size()) {
            case 0:
                tvRatingNotAvailable.setVisibility(View.VISIBLE);
                llMyRating1.setVisibility(View.GONE);
                llMyRating2.setVisibility(View.GONE);
                break;
            case 1:
                tvRatingNotAvailable.setVisibility(View.GONE);
                myRating = myRatingList.get(0);
                llMyRating1.setVisibility(View.VISIBLE);
                Picasso.with(getActivity()).load(myRating.getLocationPhoto()).into(civLocation1);
                tvNameLocation1.setText(myRating.getLocationName());
                tvContent1.setText(myRating.getContent());
                llMyRating2.setVisibility(View.GONE);
                break;
            default:
                tvFriendNotAvailable.setVisibility(View.GONE);

                tvRatingNotAvailable.setVisibility(View.GONE);
                myRating = myRatingList.get(0);
                llMyRating1.setVisibility(View.VISIBLE);
                Picasso.with(getActivity()).load(myRating.getLocationPhoto()).into(civLocation1);
                tvNameLocation1.setText(myRating.getLocationName());
                tvContent1.setText(myRating.getContent());

                tvRatingNotAvailable.setVisibility(View.GONE);
                myRating = myRatingList.get(1);
                llMyRating2.setVisibility(View.VISIBLE);
                Picasso.with(getActivity()).load(myRating.getLocationPhoto()).into(civLocation2);
                tvNameLocation2.setText(myRating.getLocationName());
                tvContent2.setText(myRating.getContent());

                break;
        }
    }

    public void initFlInfor() {
        if (presenter.getCurrentUser().getUsername() != null) {
            tvNickname.setText(presenter.getCurrentUser().getUsername());
        }
        if (presenter.getCurrentUser().getCity() != null) {
            tvCity.setText(presenter.getCurrentUser().getCity());
        }
        if (presenter.getCurrentUser().getAvatar() != null) {
            Picasso.with(getActivity())
                    .load(presenter.getCurrentUser().getAvatar())
                    .into(target);
        }
    }

    @Click(R.id.fragment_me_ll_sign_out)
    public void onLlSignOutClick() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Đăng xuất");
        alertDialog.setMessage("Bạn có chắc chắn muốn đăng xuất tài khoản?");
        alertDialog.setIcon(R.drawable.ic_notification);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "ĐỒNG Ý", (dialogInterface, i) -> {
            presenter.signOut();
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "KHÔNG", (dialogInterface, i) -> {

        });
        alertDialog.show();
    }

    @Click(R.id.fragment_me_ll_update_profile)
    public void onLlUpdateProfileClick() {
        // Make UserProfileDetailActivity notify when it finish
        UserProfileDetailActivity_.intent(getActivity()).fromView("llUpdateProfile").startForResult(REQUEST_UPDATE_INFO);
    }

    @Click(R.id.fragment_me_tv_make_friend)
    public void onTvMakeFriendClick() {
        // Make UserProfileDetailActivity notify when it finish
        UserProfileDetailActivity_.intent(getActivity()).fromView("tvMakeFriend").startForResult(REQUEST_UPDATE_INFO);

    }

    @Click(R.id.fragment_me_tv_view_more_my_friend)
    public void onTvViewMoreMyFriendClick() {
        // Send notify to inform that tvViewMore be clicked
        UserProfileDetailActivity_.intent(getActivity()).fromView("tvViewMoreMyFriend").startForResult(REQUEST_UPDATE_INFO);

    }

    @Click(R.id.fragment_me_tv_view_more_my_rating)
    public void onTvViewMoreMyRatingClick() {
        // Send notify to inform that tvViewMore be clicked
        UserProfileDetailActivity_.intent(getActivity()).fromView("tvViewMoreMyRating").startForResult(REQUEST_UPDATE_INFO);
    }

    @Click(R.id.fragment_me_btn_sign_in)
    public void onBtnSignInClick() {
        SignInActivity_.intent(this).start();
    }

    @Override
    public void onActivityResult(int requestCode, int currentUserCode, Intent data) {
        super.onActivityResult(requestCode, currentUserCode, data);
        // Reload view
        if (requestCode == REQUEST_UPDATE_INFO) {
            initView();
        }
    }

    // Note that to use bitmap, have to create variable target out of .into()
    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            if (isAdded()) {
                Blurry.with(getActivity()).color(Color.argb(70, 80, 80, 80)).radius(10)
                        .from(bitmap).into(ivBlurAvatar);
                ivAvatar.setImageBitmap(bitmap);
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    @Override
    public void showLoading() {
//        showHUD();
    }

    @Override
    public void hideLoading() {
//        hideHUD();
    }

    @Override
    public void apiError(Throwable throwable) {

    }

    @Override
    public void signOutSuccess() {
        SplashScreenActivity_.intent(getActivity()).start();
        getActivity().finish();
    }

    @Override
    public void signOutFail(Throwable throwable) {
        Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
