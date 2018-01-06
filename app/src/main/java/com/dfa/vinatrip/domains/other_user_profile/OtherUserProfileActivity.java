package com.dfa.vinatrip.domains.other_user_profile;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beesightsoft.caf.exceptions.ApiThrowable;
import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseActivity;
import com.dfa.vinatrip.custom_view.NToolbar;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.models.response.user.User;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.blurry.Blurry;

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

    @ViewById(R.id.activity_other_user_profile_tv_ntoolbar)
    protected NToolbar nToolbar;
    @ViewById(R.id.activity_other_user_profile_iv_blur_avatar)
    protected ImageView ivBlurAvatar;
    @ViewById(R.id.activity_other_user_profile_iv_avatar)
    protected CircleImageView civAvatar;
    @ViewById(R.id.activity_other_user_profile_tv_user_name)
    protected TextView tvUserName;
    @ViewById(R.id.activity_other_user_profile_tv_user_mail)
    protected TextView tvUserMail;
    @ViewById(R.id.activity_other_user_profile_tv_user_location)
    protected TextView tvUserLocation;
    @ViewById(R.id.activity_other_user_profile_tv_description)
    protected TextView tvDescription;
    @ViewById(R.id.activity_other_user_profile_tv_birthday)
    protected TextView tvBirthday;
    @ViewById(R.id.activity_other_user_profile_tv_sex)
    protected TextView tvSex;

    @Extra
    protected long userId;

    @AfterInject
    void initInject() {
        DaggerOtherUserProfileComponent.builder()
                .applicationComponent(mainApplication.getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build().inject(this);
    }

    @AfterViews
    void init() {
        nToolbar.setup(this, "TripGuy");
        nToolbar.showLeftIcon();
        nToolbar.showToolbarColor();
        nToolbar.setOnLeftClickListener(v -> onBackPressed());

        presenter.getUserInfo(userId);
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
        ApiThrowable apiThrowable = (ApiThrowable) throwable;
        Toast.makeText(this, apiThrowable.firstErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void getUserInfoSuccess(User user) {
        if (user.getAvatar() != null) {
            Picasso.with(OtherUserProfileActivity.this)
                    .load(user.getAvatar())
                    .into(target);
        }
        tvUserName.setText(user.getUsername());
        tvUserMail.setText(user.getEmail());
        tvUserLocation.setText(user.getCity());

        tvDescription.setText(user.getIntro());
        tvBirthday.setText(user.getBirthday());
        tvSex.setText(user.getStringSex());
    }

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Blurry.with(OtherUserProfileActivity.this).color(Color.argb(70, 80, 80, 80)).radius(10)
                    .from(bitmap).into(ivBlurAvatar);
            civAvatar.setImageBitmap(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };
}
