package com.dfa.vinatrip.domains.province_detail.view_all.place.place_detail;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseActivity;
import com.dfa.vinatrip.custom_view.NToolbar;
import com.dfa.vinatrip.custom_view.SimpleRatingBar;
import com.dfa.vinatrip.domains.province_detail.view_all.place.place_detail.adapter.RecyclerImageAdapter;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.models.response.feedback.FeedbackResponse;
import com.dfa.vinatrip.models.response.place.PlaceResponse;
import com.dfa.vinatrip.utils.AppUtil;
import com.dfa.vinatrip.utils.KeyboardVisibility;
import com.dfa.vinatrip.utils.MapActivity_;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.dfa.vinatrip.utils.Constants.TYPE_HOTEL;
import static com.dfa.vinatrip.utils.Constants.TYPE_PLACE;

/**
 * Created by duonghd on 12/28/2017.
 */

@SuppressLint("Registered")
@EActivity(R.layout.activity_place_detail)
public class PlaceDetailActivity extends BaseActivity<PlaceDetailView, PlaceDetailPresenter>
        implements PlaceDetailView {
    @App
    protected MainApplication mainApplication;
    @Inject
    protected PlaceDetailPresenter presenter;

    @ViewById(R.id.activity_place_detail_ll_root)
    protected LinearLayout llRoot;
    @ViewById(R.id.activity_province_place_detail_tb_toolbar)
    protected NToolbar nToolbar;
    @ViewById(R.id.activity_place_detail_tv_place_name)
    protected TextView tvplaceName;
    @ViewById(R.id.activity_place_detail_tv_number_of_feedback)
    protected TextView tvNumberOfFeedback;
    @ViewById(R.id.activity_place_detail_iv_banner)
    protected ImageView ivBanner;
    @ViewById(R.id.activity_place_detail_tv_intro)
    protected TextView tvIntro;
    @ViewById(R.id.activity_place_detail_tv_address)
    protected TextView tvAddress;
    @ViewById(R.id.activity_place_detail_iv_map)
    protected ImageView ivMap;
    @ViewById(R.id.activity_place_detail_rcv_feedback)
    protected RecyclerView rcvFeedback;
    @ViewById(R.id.activity_place_detail_tv_none_feedback)
    protected TextView tvNoneFeedback;
    @ViewById(R.id.activity_place_detail_rcv_photo)
    protected RecyclerView rcvPhoto;

    @ViewById(R.id.activity_place_detail_ll_is_login)
    protected LinearLayout llIsLogin;
    @ViewById(R.id.activity_place_detail_ll_not_login)
    protected LinearLayout llNotLogin;
    @ViewById(R.id.activity_place_detail_civ_user_avatar)
    protected CircleImageView civUserAvatar;
    @ViewById(R.id.activity_place_detail_tv_user_name)
    protected TextView tvUserName;
    @ViewById(R.id.activity_place_detail_edt_feedback_content)
    protected TextView edtFeedbackContent;
    @ViewById(R.id.activity_place_detail_srb_feedback_rating)
    protected SimpleRatingBar srbFeedbackRating;
    @ViewById(R.id.activity_place_detail_tv_send_feedback)
    protected TextView tvSendFeedback;

    @Extra
    protected PlaceResponse placeResponse;

    @AfterInject
    void initInject() {
        DaggerPlaceDetailComponent.builder()
                .applicationComponent(mainApplication.getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build().inject(this);
    }

    @AfterViews
    void init() {
        AppUtil.setupUI(llRoot);
        showKeyboard();

        nToolbar.setup(this, "TripGuy");
        nToolbar.showLeftIcon();
        nToolbar.showToolbarColor();
        nToolbar.setOnLeftClickListener(v -> onBackPressed());

        setupViewWithData();
        //presenter.getPlaceFeedback(placeResponse.getId(), 0, 0);
    }

    private void showKeyboard() {
        KeyboardVisibility.setEventListener(this, isOpen -> {
            if (KeyboardVisibility.isKeyboardVisible(PlaceDetailActivity.this)) {
                Log.e("keyboard", "show");
            } else {
                if (getCurrentFocus() != null) {
                    getCurrentFocus().clearFocus();
                }
                Log.e("keyboard", "hide");
            }
        });
    }

    private void setupViewWithData() {
        Picasso.with(this).load(placeResponse.getAvatar())
                .error(R.drawable.photo_not_available)
                .into(ivBanner);

        ivMap.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int ivMapHeight = ivMap.getHeight();
                        int ivMapWidth = ivMap.getWidth();
                        double latitude = placeResponse.getLatitude();

                        double longitude = placeResponse.getLongitude();

                        String s1 = "https://maps.googleapis.com/maps/api/staticmap?center=";
                        String s2 = "&zoom=18&scale=2&size=";
                        String s3 = "&markers=size:big%7Ccolor:0xff0000%7Clabel:%7C";

                        String url = s1 + latitude + "," + longitude + s2 + AppUtil.dpToPx(PlaceDetailActivity.this, ivMapWidth) + "x" + AppUtil.dpToPx(PlaceDetailActivity.this, ivMapHeight) + s3 + latitude + "," + longitude;
                        Picasso.with(PlaceDetailActivity.this).load(url).into(ivMap);

                        if (ivMap.getViewTreeObserver().isAlive()) {
                            ivMap.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    }
                });

        tvplaceName.setText(placeResponse.getName());
        tvAddress.setText(placeResponse.getAddress());
        tvIntro.setText(placeResponse.getDescription());
        if (presenter.getCurrentUser() != null) {
            llIsLogin.setVisibility(View.VISIBLE);
            llNotLogin.setVisibility(View.GONE);
            tvSendFeedback.setBackground(getResources().getDrawable(R.drawable.bg_btn_green_radius_3dp));
            Picasso.with(this).load(presenter.getCurrentUser().getAvatar()).into(civUserAvatar);
            tvUserName.setText(presenter.getCurrentUser().getUsername());
        } else {
            llIsLogin.setVisibility(View.GONE);
            llNotLogin.setVisibility(View.VISIBLE);
            tvSendFeedback.setBackground(getResources().getDrawable(R.drawable.bg_btn_gray_radius_3dp_not_press));
        }

        rcvPhoto.setHasFixedSize(true);
        rcvPhoto.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rcvPhoto.setAdapter(new RecyclerImageAdapter(this, placeResponse.getPlaceImages()));
    }

    @NonNull
    @Override
    public PlaceDetailPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void apiError(Throwable throwable) {

    }

    @Click(R.id.activity_place_detail_iv_map)
    void ivMapClick(){
        MapActivity_.intent(this)
                .title(placeResponse.getName())
                .latitude(placeResponse.getLatitude())
                .longitude(placeResponse.getLongitude())
                .start();
    }

    @Override
    public void getPlaceFeedbackSuccess(List<FeedbackResponse> feedbackResponses) {

    }

    @Override
    public void postPlaceFeedbackSuccess(FeedbackResponse feedbackResponse) {

    }
}
