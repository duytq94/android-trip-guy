package com.dfa.vinatrip.domains.province_detail.view_all.hotel.hotel_detail;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseActivity;
import com.dfa.vinatrip.custom_view.NToolbar;
import com.dfa.vinatrip.custom_view.SimpleRatingBar;
import com.dfa.vinatrip.domains.province_detail.view_all.hotel.hotel_detail.adapter.RecyclerImageAdapter;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.models.response.hotel.HotelResponse;
import com.dfa.vinatrip.utils.AppUtil;
import com.dfa.vinatrip.utils.KeyboardVisibility;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;

@SuppressLint("Registered")
@EActivity(R.layout.activity_hotel_detail)
public class HotelDetailActivity extends BaseActivity<HotelDetailView, HotelDetailPresenter>
        implements HotelDetailView {
    @App
    protected MainApplication mainApplication;
    @Inject
    protected HotelDetailPresenter presenter;
    
    @ViewById(R.id.activity_hotel_detail_ll_root)
    protected LinearLayout llRoot;
    @ViewById(R.id.activity_province_hotel_detail_tb_toolbar)
    protected NToolbar nToolbar;
    @ViewById(R.id.activity_hotel_detail_tv_name)
    protected TextView tvName;
    @ViewById(R.id.activity_hotel_detail_iv_banner)
    protected ImageView ivBanner;
    @ViewById(R.id.activity_hotel_detail_tv_phone)
    protected TextView tvPhone;
    @ViewById(R.id.activity_hotel_detail_tv_address)
    protected TextView tvAddress;
    @ViewById(R.id.activity_hotel_detail_rcv_photo)
    protected RecyclerView rcvPhoto;
    
    @ViewById(R.id.activity_hotel_detail_ll_is_login)
    protected LinearLayout llIsLogin;
    @ViewById(R.id.activity_hotel_detail_ll_not_login)
    protected LinearLayout llNotLogin;
    @ViewById(R.id.activity_hotel_detail_civ_user_avatar)
    protected CircleImageView civUserAvatar;
    @ViewById(R.id.activity_hotel_detail_tv_user_name)
    protected TextView tvUserName;
    @ViewById(R.id.activity_hotel_detail_edt_feedback_content)
    protected TextView edtFeedbackContent;
    @ViewById(R.id.activity_hotel_detail_srb_feedback_rating)
    protected SimpleRatingBar srbFeedbackRating;
    @ViewById(R.id.activity_hotel_detail_tv_send_feedback)
    protected TextView tvSendFeedback;
    
    @Extra
    protected HotelResponse hotelResponse;
    
    @AfterInject
    void initInject() {
        DaggerHotelDetailComponent.builder()
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
    }
    
    private void showKeyboard() {
        KeyboardVisibility.setEventListener(this, isOpen -> {
            if (KeyboardVisibility.isKeyboardVisible(HotelDetailActivity.this)) {
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
        Picasso.with(this).load(hotelResponse.getAvatar())
                .error(R.drawable.photo_not_available)
                .into(ivBanner);
        
        tvName.setText(hotelResponse.getName());
        tvPhone.setText(hotelResponse.getPhone_number());
        tvAddress.setText(hotelResponse.getLocation());
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
        rcvPhoto.setAdapter(new RecyclerImageAdapter(this, hotelResponse.getImages()));
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
    
    @NonNull
    @Override
    public HotelDetailPresenter createPresenter() {
        return presenter;
    }
    
    @Override
    public void onBackPressed() {
        finish();
    }
    
    @Click(R.id.activity_hotel_detail_tv_send_feedback)
    void sendFeedbackClick() {
        if (presenter.getCurrentUser() != null && validateRatingInput()) {
            presenter.sendFeedback();
        }
    }
    
    private boolean validateRatingInput() {
        boolean validateResult = false;
        if (edtFeedbackContent.getText().toString().length() == 0) {
            Toast.makeText(this, "Nội dung còn trống!", Toast.LENGTH_SHORT).show();
        } else if (srbFeedbackRating.getRating() == 0) {
            Toast.makeText(this, "Bạn chưa chọn số sao!", Toast.LENGTH_SHORT).show();
        } else {
            validateResult = true;
        }
        return validateResult;
    }
}
