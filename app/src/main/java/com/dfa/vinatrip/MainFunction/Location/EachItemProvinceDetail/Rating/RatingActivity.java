package com.dfa.vinatrip.MainFunction.Location.EachItemProvinceDetail.Rating;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dfa.vinatrip.Login.SignInActivity_;
import com.dfa.vinatrip.MainFunction.Location.ProvinceDetail.ProvinceDestination.ProvinceDestination;
import com.dfa.vinatrip.MainFunction.Me.UserProfile;
import com.dfa.vinatrip.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@EActivity(R.layout.activity_rating)
public class RatingActivity extends AppCompatActivity {

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

    @ViewById(R.id.activity_rating_et_content)
    EditText etContent;

    @Click
    void activity_rating_btn_submit() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = simpleDateFormat.format(calendar.getTime());

        for (int i = 0; i < listUserProfiles.size(); i++) {
            if (listUserProfiles.get(i).getUid().equals(firebaseUser.getUid())) {
                UserProfile userProfile = listUserProfiles.get(i);
                UserRating userRating = new UserRating(userProfile.getUid(),
                        userProfile.getNickname(),
                        userProfile.getAvatar(),
                        userProfile.getEmail(),
                        etContent.getText().toString(),
                        (int) ratingBar.getRating() + "",
                        date);

                databaseReference.child("ProvinceDestinationRating")
                        .child(detailDestination.getProvince())
                        .child(detailDestination.getName())
                        .child(firebaseUser.getUid())
                        .setValue(userRating);
                finish();
            }
        }
    }

    @Click
    void activity_rating_btn_cancel() {
        finish();
    }

    private ActionBar actionBar;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private ProvinceDestination detailDestination;
    private List<UserProfile> listUserProfiles;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private List<UserRating> listUserRatings;

    @AfterViews()
    void onCreate() {
        setupActionBar();
        changeColorStatusBar();
        loadUserProfileFromFirebase();
        setContentViews();
    }

    public void changeColorStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorStatusBar));
        }
    }

    public void loadUserProfileFromFirebase() {
        btnSubmit.setVisibility(View.GONE);
        listUserProfiles = new ArrayList<>();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        // If no Internet, this method will not run
        databaseReference.child("UserProfile").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String avatar, city, introduceYourSelf, nickname, birthday, uid, sex, email;

                city = dataSnapshot.child("city").getValue().toString();
                introduceYourSelf = dataSnapshot.child("introduceYourSelf").getValue().toString();
                avatar = dataSnapshot.child("avatar").getValue().toString();
                nickname = dataSnapshot.child("nickname").getValue().toString();
                birthday = dataSnapshot.child("birthday").getValue().toString();
                sex = dataSnapshot.child("sex").getValue().toString();
                email = dataSnapshot.child("email").getValue().toString();
                uid = dataSnapshot.getKey();

                UserProfile userProfile = new UserProfile(nickname, avatar, introduceYourSelf,
                        city, birthday, uid, sex, email);
                listUserProfiles.add(userProfile);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // This method to be called after all the onChildAdded() calls have happened
        databaseReference.child("UserProfile")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // When listUserProfiles done loading, user can submit
                        btnSubmit.setVisibility(View.VISIBLE);

                        // If user has comment, load old data to views
                        listUserRatings = new ArrayList<>();
                        listUserRatings = (List<UserRating>) getIntent().getSerializableExtra("ListUserRatings");
                        for (int i = 0; i < listUserRatings.size(); i++) {
                            UserRating userRating = listUserRatings.get(i);
                            // If don't have user, content of view will be empty
                            if (firebaseUser != null) {
                                if (userRating.getUid().equals(firebaseUser.getUid())) {
                                    ratingBar.setRating(Float.parseFloat(userRating.getNumStars()));
                                    etContent.setText(userRating.getContent());
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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

    public void setContentViews() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            rlMain.setVisibility(View.VISIBLE);
            tvRate.setText("Bình thường");

            detailDestination = (ProvinceDestination) getIntent().getSerializableExtra("DetailDestination");

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();

        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setQueryHint("Tìm kiếm...");

        return true;
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
}
