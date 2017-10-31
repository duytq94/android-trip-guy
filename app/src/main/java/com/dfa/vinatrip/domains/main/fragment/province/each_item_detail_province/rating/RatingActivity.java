package com.dfa.vinatrip.domains.main.fragment.province.each_item_detail_province.rating;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseActivity;
import com.dfa.vinatrip.domains.auth.sign_in.SignInActivity_;
import com.dfa.vinatrip.domains.main.fragment.province.each_item_detail_province.each_province_destination.ProvinceDestinationDetail;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.models.response.User;
import com.dfa.vinatrip.utils.AppUtil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

@EActivity(R.layout.activity_rating)
public class RatingActivity extends BaseActivity<RatingView, RatingPresenter>
        implements RatingView, Validator.ValidationListener {

    @ViewById(R.id.my_toolbar)
    protected Toolbar toolbar;
    @ViewById(R.id.activity_rating_btn_submit)
    protected Button btnSubmit;
    @ViewById(R.id.activity_rating_ll_main)
    protected LinearLayout llMain;
    @ViewById(R.id.activity_rating_rating_bar)
    protected RatingBar ratingBar;
    @ViewById(R.id.activity_rating_tv_rating)
    protected TextView tvRate;
    @NotEmpty
    @ViewById(R.id.activity_rating_et_content)
    protected EditText etContent;

    private Validator validator;
    private ActionBar actionBar;
    private DatabaseReference databaseReference;
    private ProvinceDestinationDetail destinationDetail;
    private List<UserRating> listUserRatings;
    private User currentUser;
    private boolean isNewOrUpdate;

    @App
    protected MainApplication application;
    @Inject
    protected RatingPresenter presenter;

    @AfterInject
    protected void initInject() {
        DaggerRatingComponent.builder()
                .applicationComponent(application.getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build().inject(this);
    }

    @NonNull
    @Override
    public RatingPresenter createPresenter() {
        return presenter;
    }

    @AfterViews()
    void init() {
        setupActionBar();
        initView();
    }

    public void setupActionBar() {
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Đánh giá");

            // Set button back
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void initView() {
        isNewOrUpdate = true;
        currentUser = presenter.getCurrentUser();

        // If user has comment, load old data to views
        listUserRatings = new ArrayList<>();
//        listUserRatings = getIntent().getParcelableArrayListExtra("ListUserRatings");
        for (int i = 0; i < listUserRatings.size(); i++) {
            UserRating myRating = listUserRatings.get(i);
            // If don't have user, content of view will be empty
            if (currentUser != null) {
//                if (myRating.getUid() == currentUser.getId()) {
//                    ratingBar.setRating(Float.parseFloat(myRating.getNumStars()));
//                    etContent.setText(myRating.getContent());
//                    etContent.setSelection(etContent.getText().length());
//                    setTvRate(Integer.parseInt((myRating.getNumStars())));
//                    isNewOrUpdate = false;
//                }
            }
        }

        validator = new Validator(this);
        validator.setValidationListener(this);

        if (currentUser != null) {
            llMain.setVisibility(View.VISIBLE);

            destinationDetail = getIntent().getParcelableExtra("DetailDestination");

            databaseReference = FirebaseDatabase.getInstance().getReference();

            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    setTvRate((int) rating);
                }
            });
        } else {
            startActivity(new Intent(RatingActivity.this, SignInActivity_.class));
            finish();
        }
    }

    public void setTvRate(int rating) {
        switch (rating) {
            case 1:
                tvRate.setText("Rất tệ");
                break;
            case 2:
                tvRate.setText("Tệ");
                break;
            case 3:
                tvRate.setText("Bình thường");
                break;
            case 4:
                tvRate.setText("Tuyệt");
                break;
            case 5:
                tvRate.setText("Rất tuyệt");
                break;
        }
    }

    @Click
    void activity_rating_btn_submit() {
        validator.validate();
    }

    @Click
    void activity_rating_btn_cancel() {
        finish();
    }

    @Click(R.id.activity_rating_btn_clear)
    void onBtnClearClick() {
        etContent.setText(null);
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
    public void onValidationSucceeded() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = simpleDateFormat.format(calendar.getTime());

        //TODO post current user rating

//        if (isNewOrUpdate) {
//            dataService.addToMyRatingList(myRating);
//        } else {
//            dataService.updateToMyRatingList(myRating);
//        }

        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            ((EditText) view).setError("Bạn không được để trống");
        }
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
