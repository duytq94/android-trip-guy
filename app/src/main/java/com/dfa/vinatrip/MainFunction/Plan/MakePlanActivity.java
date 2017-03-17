package com.dfa.vinatrip.MainFunction.Plan;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dfa.vinatrip.MainFunction.Me.UserDetail.MakeFriend.UserFriend;
import com.dfa.vinatrip.MainFunction.Me.UserProfile;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.SplashScreen.DataService;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

@EActivity(R.layout.activity_make_plan)
public class MakePlanActivity extends AppCompatActivity {

    @Bean
    DataService dataService;

    @ViewById(R.id.activity_make_plan_rv_list_friend)
    RecyclerView rvListFriend;

    @ViewById(R.id.activity_make_plan_et_trip_name)
    EditText etTripName;

    @ViewById(R.id.activity_make_plan_et_destination)
    EditText etDestination;

    @ViewById(R.id.activity_make_plan_et_schedule)
    EditText etSchedule;

    @ViewById(R.id.activity_make_plan_tv_date_go)
    TextView tvDateGo;

    @ViewById(R.id.activity_make_plan_tv_date_back)
    TextView tvDateBack;

    @ViewById(R.id.activity_make_plan_tv_friend_not_available)
    TextView tvFriendNotAvailable;

    private List<UserFriend> userFriendList;
    private InviteFriendAdapter inviteFriendAdapter;
    private TripPlan tripPlan;
    private Calendar calendar;
    private DatePickerDialog dpdDateGo, dpdDateBack;
    private List<String> invitedFriendIdList;
    private DatabaseReference databaseReference;
    private UserProfile currentUser;

    @AfterViews
    void onCreate() {
        initViews();
        changeColorStatusBar();
        setCurrentDayForView();
    }

    public void initViews() {
        currentUser = dataService.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        calendar = Calendar.getInstance();
        tripPlan = new TripPlan();
        invitedFriendIdList = new ArrayList<>();

        userFriendList = new ArrayList<>();
        userFriendList.addAll(dataService.getUserFriendList());
        if (userFriendList.size() != 0) {
            tvFriendNotAvailable.setVisibility(View.GONE);
        }

        inviteFriendAdapter = new InviteFriendAdapter(this, userFriendList, invitedFriendIdList);
        rvListFriend.setAdapter(inviteFriendAdapter);
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        rvListFriend.setLayoutManager(staggeredGridLayoutManager);
    }

    public void setCurrentDayForView() {
        // Set current day for tvBirthday
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String strDate = simpleDateFormat.format(calendar.getTime());
        tvDateGo.setText(strDate);
        tvDateBack.setText(strDate);
    }

    public void changeColorStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorStatusBar));
        }
    }

    @Click(R.id.activity_make_plan_btn_cancel)
    void onBtnCancelClick() {
        super.onBackPressed();
    }

    @Click(R.id.activity_make_plan_btn_done)
    void onBtnDoneClick() {
        tripPlan.setName(etTripName.getText().toString());
        tripPlan.setDestination(etDestination.getText().toString());
        tripPlan.setSchedule(etSchedule.getText().toString());
        tripPlan.setDateGo(tvDateGo.getText().toString());
        tripPlan.setDateBack(tvDateBack.getText().toString());
        tripPlan.setUserMakePlan(dataService.getCurrentUser());
        tripPlan.setFriendInvitedList(invitedFriendIdList);

        // Send data to storage of current user (the user create this trip plan)
        databaseReference.child("UserPlan").child(currentUser.getUid()).push()
                .setValue(tripPlan, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Toast.makeText(MakePlanActivity.this,
                                    "Lỗi đường truyền, bạn hãy gửi lại!", Toast.LENGTH_SHORT).show();
                        } else {
                            // Send data to storage of friends be invited
                            sendTripPlanToFriends();
                        }
                    }
                });
    }

    public void sendTripPlanToFriends() {
        for (String friendId : invitedFriendIdList) {
            databaseReference.child("UserPlan").child(friendId).push()
                    .setValue(tripPlan, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                Toast.makeText(MakePlanActivity.this,
                                        "Lỗi đường truyền, bạn hãy gửi lại!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MakePlanActivity.this,
                                        "Kế hoạch của bạn đã được tạo!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
        }
    }

    @Click(R.id.activity_make_plan_ll_date_go)
    void onLlDateGoClick() {
        // When date be set
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tvDateGo.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                tvDateBack.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                calendar.set(year, month, dayOfMonth);
            }
        };

        // Set current position time when start dialog
        String strDate[] = tvDateGo.getText().toString().split("/");
        int day = Integer.parseInt(strDate[0]);
        int month = Integer.parseInt(strDate[1]) - 1;
        int year = Integer.parseInt(strDate[2]);
        dpdDateGo = new DatePickerDialog(this, listener, year, month, day);
        dpdDateGo.getDatePicker().setMinDate(System.currentTimeMillis());
        dpdDateGo.setTitle("Chọn ngày đi");
        dpdDateGo.show();
    }

    @Click(R.id.activity_make_plan_ll_date_back)
    void onLlDateBackClick() {
        // When date be set
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tvDateBack.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                calendar.set(year, month, dayOfMonth);
            }
        };

        // Set current position time when start dialog
        String strDate[] = tvDateBack.getText().toString().split("/");
        int day = Integer.parseInt(strDate[0]);
        int month = Integer.parseInt(strDate[1]) - 1;
        int year = Integer.parseInt(strDate[2]);
        dpdDateBack = new DatePickerDialog(this, listener, year, month, day);
        dpdDateBack.getDatePicker().setMinDate(calendar.getTimeInMillis());
        dpdDateBack.setTitle("Chọn ngày về");
        dpdDateBack.show();
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
