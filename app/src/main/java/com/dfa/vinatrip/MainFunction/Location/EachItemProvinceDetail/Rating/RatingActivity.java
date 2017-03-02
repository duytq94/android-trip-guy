package com.dfa.vinatrip.MainFunction.Location.EachItemProvinceDetail.Rating;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dfa.vinatrip.Login.SignInActivity;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RatingActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private RelativeLayout rlMain;
    private RatingBar ratingBar;
    private TextView tvRate;
    private Button btnSubmit, btnCancel;
    private ProvinceDestination detailDestination;
    private EditText etContent;
    private List<UserProfile> listUserProfiles;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private List<UserRating> listUserRatings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        findViewByIds();
        setupActionBar();
        changeColorStatusBar();
        loadUserProfileFromFirebase();
        setContentViews();
        onClickListener();
    }

    public void changeColorStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorStatusBar));
        }
    }

    public void onClickListener() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Đánh giá");

            // Set button back
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void findViewByIds() {
        rlMain = (RelativeLayout) findViewById(R.id.activity_rating_rl_main);
        ratingBar = (RatingBar) findViewById(R.id.activity_rating_rating_bar);
        tvRate = (TextView) findViewById(R.id.activity_rating_tv_rating);
        btnSubmit = (Button) findViewById(R.id.activity_rating_btn_submit);
        btnCancel = (Button) findViewById(R.id.activity_rating_btn_cancel);
        etContent = (EditText) findViewById(R.id.activity_rating_et_content);
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
}
