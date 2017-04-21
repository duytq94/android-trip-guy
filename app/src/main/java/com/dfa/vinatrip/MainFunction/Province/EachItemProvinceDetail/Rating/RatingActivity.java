package com.dfa.vinatrip.MainFunction.Province.EachItemProvinceDetail.Rating;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dfa.vinatrip.DataService.DataService;
import com.dfa.vinatrip.Login.SignInActivity_;
import com.dfa.vinatrip.MainFunction.Me.UserProfile;
import com.dfa.vinatrip.MainFunction.Province.ProvinceDetail.ProvinceDestination.ProvinceDestination;
import com.dfa.vinatrip.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@EActivity(R.layout.activity_rating)
public class RatingActivity extends AppCompatActivity implements Validator.ValidationListener {

    @Bean
    DataService dataService;

    @ViewById(R.id.my_toolbar)
    Toolbar toolbar;

    @ViewById(R.id.activity_rating_btn_submit)
    Button btnSubmit;

    @ViewById(R.id.activity_rating_rl_main)
    RelativeLayout rlMain;

    @ViewById(R.id.activity_rating_rating_bar)
    RatingBar ratingBar;

    @ViewById(R.id.activity_rating_tv_rating)
    TextView tvRate;

    @NotEmpty
    @ViewById(R.id.activity_rating_et_content)
    EditText etContent;

    @Click
    void activity_rating_btn_submit() {
        validator.validate();
    }

    @Click
    void activity_rating_btn_cancel() {
        finish();
    }

    private Validator validator;
    private ActionBar actionBar;
    private DatabaseReference databaseReference;
    private ProvinceDestination detailDestination;
    private List<UserProfile> listUserProfiles;
    private List<UserRating> listUserRatings;
    private UserProfile currentUser;

    @AfterViews()
    void onCreate() {
        setupActionBar();
        changeColorStatusBar();
        initView();
    }

    public void changeColorStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorStatusBar));
        }
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
        currentUser = dataService.getCurrentUser();

        listUserProfiles = new ArrayList<>();
        listUserProfiles = dataService.getUserProfileList();

        // If user has comment, load old data to views
        listUserRatings = new ArrayList<>();
        listUserRatings = getIntent().getParcelableArrayListExtra("ListUserRatings");
        for (int i = 0; i < listUserRatings.size(); i++) {
            UserRating userRating = listUserRatings.get(i);
            // If don't have user, content of view will be empty
            if (currentUser != null) {
                if (userRating.getUid().equals(currentUser.getUid())) {
                    ratingBar.setRating(Float.parseFloat(userRating.getNumStars()));
                    etContent.setText(userRating.getContent());
                }
            }
        }

        validator = new Validator(this);
        validator.setValidationListener(this);

        if (currentUser != null) {
            rlMain.setVisibility(View.VISIBLE);
            tvRate.setText("Bình thường");

            detailDestination = getIntent().getParcelableExtra("DetailDestination");

            databaseReference = FirebaseDatabase.getInstance().getReference();

            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    switch ((int) rating) {
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
            });
        } else {
            startActivity(new Intent(RatingActivity.this, SignInActivity_.class));
            finish();
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
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    @Override
    public void onValidationSucceeded() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = simpleDateFormat.format(calendar.getTime());

        for (int i = 0; i < listUserProfiles.size(); i++) {
            if (listUserProfiles.get(i).getUid().equals(currentUser.getUid())) {
                UserProfile userProfile = listUserProfiles.get(i);
                UserRating userRating = new UserRating(userProfile.getUid(),
                        userProfile.getNickname(),
                        userProfile.getAvatar(),
                        userProfile.getEmail(),
                        etContent.getText().toString(),
                        (int) ratingBar.getRating() + "",
                        date,
                        detailDestination.getName(),
                        detailDestination.getAvatar());

                databaseReference.child("ProvinceDestinationRating")
                        .child(detailDestination.getProvince())
                        .child(detailDestination.getName())
                        .child(currentUser.getUid())
                        .setValue(userRating);

                databaseReference.child("UserRating")
                        .child(currentUser.getUid())
                        .child(detailDestination.getName())
                        .setValue(userRating);

                finish();
            }
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            ((EditText) view).setError("Bạn không được để trống");
        }
    }
}
