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

import com.dfa.vinatrip.MainFunction.Me.UserDetail.MakeFriend.UserFriend;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.SplashScreen.DataService;

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

    @Click(R.id.activity_make_plan_btn_cancel)
    void onBtnCancelClick() {
        super.onBackPressed();
    }

    @Click(R.id.activity_make_plan_btn_done)
    void onBtnDoneClick() {
        tripPlan.setName(etTripName.getText().toString());
        tripPlan.setDestination(etDestination.getText().toString());
        tripPlan.setSchedule(etSchedule.getText().toString());
        tripPlan.setUserMakePlan(dataService.getCurrentUser());
    }

    @Click(R.id.activity_make_plan_ll_date_go)
    void onLlDateGoClick() {
        // When date be set
        DatePickerDialog.OnDateSetListener listener
                = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tvDateGo.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                calendar.set(year, month, dayOfMonth);
            }
        };

        // Set current position time when start dialog
        String strDate[] = tvDateGo.getText().toString().split("/");
        int day = Integer.parseInt(strDate[0]);
        int month = Integer.parseInt(strDate[1]) - 1;
        int year = Integer.parseInt(strDate[2]);
        DatePickerDialog dialog = new DatePickerDialog(this, listener, year, month, day);
        dialog.setTitle("Chọn ngày đi");
        dialog.show();
    }

    @Click(R.id.activity_make_plan_ll_date_back)
    void onLlDateBackClick() {
        // When date be set
        DatePickerDialog.OnDateSetListener listener
                = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tvDateGo.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                calendar.set(year, month, dayOfMonth);
            }
        };

        // Set current position time when start dialog
        String strDate[] = tvDateGo.getText().toString().split("/");
        int day = Integer.parseInt(strDate[0]);
        int month = Integer.parseInt(strDate[1]) - 1;
        int year = Integer.parseInt(strDate[2]);
        DatePickerDialog dialog = new DatePickerDialog(this, listener, year, month, day);
        dialog.setTitle("Chọn ngày về");
        dialog.show();
    }

    private List<UserFriend> userFriendList;
    private InviteFriendAdapter inviteFriendAdapter;
    private TripPlan tripPlan;
    private Calendar calendar;

    @AfterViews
    void onCreate() {
        changeColorStatusBar();
        calendar = Calendar.getInstance();
        setCurrentDayForView();
        tripPlan = new TripPlan();
        userFriendList = new ArrayList<>();
        userFriendList.addAll(dataService.getUserFriendList());
        if (userFriendList.size() != 0) {
            tvFriendNotAvailable.setVisibility(View.GONE);
        }
        inviteFriendAdapter = new InviteFriendAdapter(this, userFriendList);
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
