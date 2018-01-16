package com.dfa.vinatrip.domains.province_detail.view_all.food.food_detail;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beesightsoft.caf.exceptions.ApiThrowable;
import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseActivity;
import com.dfa.vinatrip.base.LoginDialog;
import com.dfa.vinatrip.custom_view.NToolbar;
import com.dfa.vinatrip.custom_view.SimpleRatingBar;
import com.dfa.vinatrip.domains.province_detail.view_all.food.food_detail.adapter.RecyclerFoodFeedbackAdapter;
import com.dfa.vinatrip.domains.province_detail.view_all.food.food_detail.adapter.RecyclerImageAdapter;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.models.request.AuthRequest;
import com.dfa.vinatrip.models.request.FeedbackRequest;
import com.dfa.vinatrip.models.response.feedback.FeedbackResponse;
import com.dfa.vinatrip.models.response.food.FoodResponse;
import com.dfa.vinatrip.models.response.user.User;
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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.dfa.vinatrip.utils.Constants.FEEDBACK_PAGE_SIZE;

/**
 * Created by duonghd on 12/28/2017.
 * duonghd1307@gmail.com
 */

@SuppressLint("Registered")
@EActivity(R.layout.activity_food_detail)
public class FoodDetailActivity extends BaseActivity<FoodDetailView, FoodDetailPresenter>
        implements FoodDetailView, LoginDialog.CallbackActivity {
    @App
    protected MainApplication mainApplication;
    @Inject
    protected FoodDetailPresenter presenter;

    @ViewById(R.id.activity_food_detail_ll_root)
    protected LinearLayout llRoot;
    @ViewById(R.id.activity_province_food_detail_tb_toolbar)
    protected NToolbar nToolbar;
    @ViewById(R.id.activity_food_detail_tv_food_name)
    protected TextView tvFoodName;
    @ViewById(R.id.item_list_food_srb_rate)
    protected SimpleRatingBar srbFoodRate;
    @ViewById(R.id.activity_food_detail_tv_number_of_feedback)
    protected TextView tvNumberOfFeedback;
    @ViewById(R.id.activity_food_detail_iv_banner)
    protected ImageView ivBanner;
    @ViewById(R.id.activity_food_detail_tv_description)
    protected TextView tvDescription;
    @ViewById(R.id.activity_food_detail_tv_address)
    protected TextView tvAddress;
    @ViewById(R.id.activity_food_detail_iv_map)
    protected ImageView ivMap;
    @ViewById(R.id.activity_food_detail_rcv_feedback)
    protected RecyclerView rcvFeedback;
    @ViewById(R.id.activity_food_detail_iv_feedback_more)
    protected TextView tvFeedbackMore;
    @ViewById(R.id.activity_food_detail_tv_none_feedback)
    protected TextView tvNoneFeedback;
    @ViewById(R.id.activity_food_detail_rcv_photo)
    protected RecyclerView rcvPhoto;

    @ViewById(R.id.activity_food_detail_ll_is_login)
    protected LinearLayout llIsLogin;
    @ViewById(R.id.activity_food_detail_ll_not_login)
    protected LinearLayout llNotLogin;
    @ViewById(R.id.activity_food_detail_civ_user_avatar)
    protected CircleImageView civUserAvatar;
    @ViewById(R.id.activity_food_detail_tv_user_name)
    protected TextView tvUserName;
    @ViewById(R.id.activity_food_detail_edt_feedback_content)
    protected TextView edtFeedbackContent;
    @ViewById(R.id.activity_food_detail_srb_feedback_rating)
    protected SimpleRatingBar srbFeedbackRating;
    @ViewById(R.id.activity_food_detail_tv_send_feedback)
    protected TextView tvSendFeedback;

    @Extra
    protected FoodResponse foodResponse;

    private LoginDialog loginDialog;
    private RecyclerFoodFeedbackAdapter feedbackAdapter;
    private List<FeedbackResponse> feedbackResponses;
    private List<FeedbackResponse> feedbackResponsesTerm;

    @AfterInject
    void initInject() {
        DaggerFoodDetailComponent.builder()
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
        presenter.getFoodFeedback(foodResponse.getId(), 0, 0);
        loginDialog = new LoginDialog(this);
        loginDialog.setCancelable(false);
        loginDialog.setCanceledOnTouchOutside(false);
        loginDialog.setOnDismissListener(dialog -> loginDialog.clearData());

        feedbackResponses = new ArrayList<>();
        feedbackResponsesTerm = new ArrayList<>();
        feedbackAdapter = new RecyclerFoodFeedbackAdapter(this, feedbackResponsesTerm);
        rcvFeedback.setHasFixedSize(true);
        rcvFeedback.setLayoutManager(new LinearLayoutManager(this));
        rcvFeedback.setAdapter(feedbackAdapter);
    }

    private void showKeyboard() {
        KeyboardVisibility.setEventListener(this, isOpen -> {
            if (KeyboardVisibility.isKeyboardVisible(com.dfa.vinatrip.domains.province_detail.view_all.food.food_detail.FoodDetailActivity.this)) {
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
        Picasso.with(this).load(foodResponse.getAvatar())
                .error(R.drawable.photo_not_available)
                .into(ivBanner);

        ivMap.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int ivMapHeight = ivMap.getHeight();
                        int ivMapWidth = ivMap.getWidth();
                        double latitude = foodResponse.getLatitude();

                        double longitude = foodResponse.getLongitude();

                        String s1 = "https://maps.googleapis.com/maps/api/staticmap?center=";
                        String s2 = "&zoom=18&scale=2&size=";
                        String s3 = "&markers=size:big%7Ccolor:0xff0000%7Clabel:%7C";

                        String url = s1 + latitude + "," + longitude + s2 + AppUtil.dpToPx(FoodDetailActivity.this, ivMapWidth) + "x" + AppUtil.dpToPx(FoodDetailActivity.this, ivMapHeight) + s3 + latitude + "," + longitude;
                        Picasso.with(FoodDetailActivity.this).load(url).into(ivMap);

                        if (ivMap.getViewTreeObserver().isAlive()) {
                            ivMap.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    }
                });

        tvFoodName.setText(foodResponse.getName());
        srbFoodRate.setRating(foodResponse.getStar());
        tvNumberOfFeedback.setText(String.format("%s đánh giá", foodResponse.getReview()));
        tvDescription.setText(foodResponse.getDescription());
        tvAddress.setText(foodResponse.getAddress());
        if (presenter.getCurrentUser() != null) {
            llIsLogin.setVisibility(View.VISIBLE);
            llNotLogin.setVisibility(View.GONE);
            tvSendFeedback.setBackground(getResources().getDrawable(R.drawable.bg_btn_green_radius_3dp));
            if (presenter.getCurrentUser().getAvatar() != null) {
                Picasso.with(this).load(presenter.getCurrentUser().getAvatar())
                        .error(R.drawable.photo_not_available)
                        .into(civUserAvatar);
            } else {
                civUserAvatar.setImageResource(R.drawable.ic_avatar);
            }
            tvUserName.setText(presenter.getCurrentUser().getUsername());
        } else {
            llIsLogin.setVisibility(View.GONE);
            llNotLogin.setVisibility(View.VISIBLE);
            tvSendFeedback.setBackground(getResources().getDrawable(R.drawable.bg_btn_gray_radius_3dp_not_press));
        }

        rcvPhoto.setHasFixedSize(true);
        rcvPhoto.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rcvPhoto.setAdapter(new RecyclerImageAdapter(this, foodResponse.getImages()));
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
        switch (apiThrowable.firstErrorCode()) {
            case 2103:
                Toast.makeText(this, "Mật khẩu không chính xác.", Toast.LENGTH_SHORT).show();
                break;

            case 2105:
                Toast.makeText(this, "Email không tồn tại.", Toast.LENGTH_SHORT).show();
                break;

            default:
                Toast.makeText(this, apiThrowable.firstErrorMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @NonNull
    @Override
    public FoodDetailPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Click(R.id.activity_food_detail_tv_send_feedback)
    void sendFeedbackClick() {
        if (presenter.getCurrentUser() != null && validateFeedbackInput()) {
            presenter.sendFeedback(foodResponse.getId(),
                    new FeedbackRequest(edtFeedbackContent.getText().toString(), srbFeedbackRating.getRating()));
        }
    }

    private boolean validateFeedbackInput() {
        boolean validateResult = false;
        if (edtFeedbackContent.getText().toString().length() == 0) {
            Toast.makeText(this, "Nội dung còn trống!", Toast.LENGTH_SHORT).show();
        } else if (srbFeedbackRating.getRating() == 0) {
            Toast.makeText(this, "Bạn chưa chọn số sao!", Toast.LENGTH_SHORT).show();
        } else if (!AppUtil.isCleanInput(edtFeedbackContent.getText().toString())) {
            Toast.makeText(this, "Nội dung chứa từ không hợp lệ!", Toast.LENGTH_SHORT).show();
        } else {
            validateResult = true;
        }
        return validateResult;
    }

    @Click(R.id.activity_food_detail_iv_map)
    void ivMapClick() {
        MapActivity_.intent(this)
                .title(foodResponse.getName())
                .latitude(foodResponse.getLatitude())
                .longitude(foodResponse.getLongitude())
                .start();
    }

    @Click(R.id.activity_food_detail_tv_login)
    void tvLoginCLick() {
        loginDialog.show();
    }

    @Click(R.id.activity_food_detail_iv_feedback_more)
    void loadFeedbackClick() {
        showHUD();
        new Handler().postDelayed(() -> {
            if (this.feedbackResponses.size() - this.feedbackResponsesTerm.size() > FEEDBACK_PAGE_SIZE) {
                tvFeedbackMore.setVisibility(View.VISIBLE);
                int size = this.feedbackResponsesTerm.size();
                for (int i = size; i < size + FEEDBACK_PAGE_SIZE; i++) {
                    this.feedbackResponsesTerm.add(this.feedbackResponses.get(i));
                }
            } else {
                tvFeedbackMore.setVisibility(View.GONE);
                for (int i = this.feedbackResponsesTerm.size(); i < this.feedbackResponses.size(); i++) {
                    this.feedbackResponsesTerm.add(this.feedbackResponses.get(i));
                }
            }
            feedbackAdapter.notifyDataSetChanged();
            hideHUD();
        }, 400);
    }

    @Override
    public void loginInfo(String email, String password) {
        loginDialog.dismiss();
        presenter.loginWithEmail(new AuthRequest(email, password));
    }

    @Override
    public void signInSuccess(User user) {
        llIsLogin.setVisibility(View.VISIBLE);
        llNotLogin.setVisibility(View.GONE);
        tvSendFeedback.setBackground(getResources().getDrawable(R.drawable.bg_btn_green_radius_3dp));
        if (user.getAvatar() != null) {
            Picasso.with(this).load(user.getAvatar())
                    .error(R.drawable.photo_not_available)
                    .into(civUserAvatar);
        } else {
            civUserAvatar.setImageResource(R.drawable.ic_avatar);
        }
        tvUserName.setText(user.getUsername());
        Toast.makeText(this, "Đăng nhập thành công.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getFoodFeedbackSuccess(List<FeedbackResponse> feedbackResponses) {
        this.feedbackResponses.addAll(feedbackResponses);
        if (this.feedbackResponses.size() < FEEDBACK_PAGE_SIZE) {
            tvFeedbackMore.setVisibility(View.GONE);
            for (int i = 0; i < feedbackResponses.size(); i++) {
                this.feedbackResponsesTerm.add(this.feedbackResponses.get(i));
            }
        } else {
            tvFeedbackMore.setVisibility(View.VISIBLE);
            for (int i = 0; i < FEEDBACK_PAGE_SIZE; i++) {
                this.feedbackResponsesTerm.add(this.feedbackResponses.get(i));
            }
        }
        this.feedbackAdapter.notifyDataSetChanged();
        tvNumberOfFeedback.setText(String.format("%s đánh giá", feedbackResponses.size()));
        srbFoodRate.setRating(countStar());
        if (feedbackResponses.size() != 0) {
            rcvFeedback.setVisibility(View.VISIBLE);
            tvNoneFeedback.setVisibility(View.GONE);
        } else {
            rcvFeedback.setVisibility(View.GONE);
            tvNoneFeedback.setVisibility(View.VISIBLE);
        }
    }

    private float countStar() {
        if (feedbackResponses.size() != 0) {
            float sum = 0;
            for (FeedbackResponse feedbackResponse : feedbackResponses) {
                sum += feedbackResponse.getRate();
            }
            sum = 1f * sum / feedbackResponses.size();
            sum = Math.round(sum * 100) / 100f;
            float term = sum % 0.5f;
            return term < 0.25f ? (sum - term) : (sum - term + 0.5f);
        } else {
            return 0f;
        }
    }

    @Override
    public void postFoodFeedbackSuccess(FeedbackResponse feedbackResponse) {
        rcvFeedback.setVisibility(View.VISIBLE);
        tvNoneFeedback.setVisibility(View.GONE);
        this.feedbackResponses.add(0, feedbackResponse);
        this.feedbackResponsesTerm.add(0, feedbackResponse);
        this.feedbackAdapter.notifyDataSetChanged();
        edtFeedbackContent.setText("");
        srbFeedbackRating.setRating(0f);
        tvNumberOfFeedback.setText(String.format("%s đánh giá", this.feedbackResponses.size()));
        srbFoodRate.setRating(countStar());
        Toast.makeText(this, "Cảm ơn bạn đã gửi đánh giá.", Toast.LENGTH_SHORT).show();
    }
}