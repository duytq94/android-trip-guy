package com.dfa.vinatrip.domains.main.fragment.me;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dfa.vinatrip.BuildConfig;
import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseFragment;
import com.dfa.vinatrip.domains.auth.sign_in.SignInActivity_;
import com.dfa.vinatrip.domains.main.fragment.me.detail_me.UserProfileDetailActivity_;
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

import javax.inject.Inject;

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

    @NonNull
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

        initFlInfo();

        tvIntroduceYourSelf.setText(presenter.getCurrentUser().getIntro());
        tvBirthday.setText(presenter.getCurrentUser().getBirthday());
        tvEmail.setText(presenter.getCurrentUser().getEmail());
        tvSex.setText(presenter.getCurrentUser().getStringSex());
    }

    public void initFlInfo() {
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
        alertDialog.setButton(
                DialogInterface.BUTTON_POSITIVE, "ĐỒNG Ý", (dialogInterface, i) -> presenter.signOut());
        alertDialog.setButton(
                DialogInterface.BUTTON_NEGATIVE, "KHÔNG", (dialogInterface, i) -> alertDialog.dismiss());
        alertDialog.show();
    }

    @Click(R.id.fragment_me_ll_update_profile)
    public void onLlUpdateProfileClick() {
        // Make UserProfileDetailActivity notify when it finish
        UserProfileDetailActivity_.intent(this).fromView("llUpdateProfile").startForResult(REQUEST_UPDATE_INFO);
    }

    @Click(R.id.fragment_me_btn_sign_in)
    public void onBtnSignInClick() {
        SignInActivity_.intent(this).start();
    }

    @Override
    public void onActivityResult(int requestCode, int currentUserCode, Intent data) {
        super.onActivityResult(requestCode, currentUserCode, data);
        // Reload view when update info success
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
        showHUD();
    }

    @Override
    public void hideLoading() {
        hideHUD();
    }

    @Override
    public void apiError(Throwable throwable) {

    }

    @Override
    public void signOutSuccess() {
        SplashScreenActivity_.intent(getActivity()).start();
        getActivity().finish();
    }
}
