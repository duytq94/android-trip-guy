package com.dfa.vinatrip.MainFunction.Plan.MakePlan;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dfa.vinatrip.DataService.DataService;
import com.dfa.vinatrip.MainFunction.Me.UserDetail.MakeFriend.UserFriend;
import com.dfa.vinatrip.MainFunction.Me.UserProfile;
import com.dfa.vinatrip.MainFunction.Plan.Plan;
import com.dfa.vinatrip.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
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
import java.util.Locale;

@EActivity(R.layout.activity_make_plan)
public class MakePlanActivity extends AppCompatActivity implements Validator.ValidationListener {

    @Bean
    DataService dataService;

    @ViewById(R.id.activity_make_plan_rv_list_friend)
    RecyclerView rvListFriend;

    @ViewById(R.id.activity_make_plan_nsv_root)
    NestedScrollView nsvRoot;

    @Length(max = 40)
    @NotEmpty
    @ViewById(R.id.activity_make_plan_et_trip_name)
    EditText etTripName;

    @NotEmpty
    @ViewById(R.id.activity_make_plan_et_destination)
    EditText etDestination;

    @NotEmpty
    @ViewById(R.id.item_day_schedule_tiet)
    TextInputEditText tietSchedule;

    @ViewById(R.id.activity_make_plan_tv_date_go)
    TextView tvDateGo;

    @ViewById(R.id.activity_make_plan_tv_date_back)
    TextView tvDateBack;

    @ViewById(R.id.activity_make_plan_tv_friend_not_available)
    TextView tvFriendNotAvailable;

    @ViewById(R.id.activity_make_plan_progressBar)
    ProgressBar progressBar;

    @ViewById(R.id.activity_make_plan_ll_schedule)
    LinearLayout llSchedule;

    private List<UserFriend> userFriendList;
    private InviteFriendAdapter inviteFriendAdapter;
    private Plan plan;
    private Calendar calendar;
    private DatePickerDialog dpdDateGo, dpdDateBack;
    private List<String> invitedFriendIdList;
    private List<String> invitedFriendIdListOld;
    private DatabaseReference databaseReference;
    private UserProfile currentUser;
    private Validator validator;
    private int countDaySchedule;
    private List<PlanSchedule> planScheduleList;
    private String planId;
    private Plan currentPlan;

    @AfterViews
    void onCreate() {
        changeColorStatusBar();

        // Get the current plan when user click to update plan on item recycler
        if (getIntent().getSerializableExtra("Plan") != null) {
            currentPlan = (Plan) getIntent().getSerializableExtra("Plan");
            etTripName.setText(currentPlan.getName());
            etDestination.setText(currentPlan.getDestination());
            tvDateGo.setText(currentPlan.getDateGo());
            tvDateBack.setText(currentPlan.getDateBack());

            for (int i = 0; i < currentPlan.getPlanScheduleList().size(); i++) {
                if (i == 0) {
                    tietSchedule.setText(currentPlan.getPlanScheduleList().get(i).getContent());
                } else {
                    TextInputLayout textInputLayout = (TextInputLayout) getLayoutInflater().inflate(R.layout.item_day_schedule, null);
                    textInputLayout.setHint("Ngày " + ++countDaySchedule);
                    textInputLayout.getEditText().setText(currentPlan.getPlanScheduleList().get(i).getContent());
                    llSchedule.addView(textInputLayout);
                }
                initViewForUpdatePlan();
            }
        } else {
            initViewForNewPlan();
            setCurrentDayForView();
        }
    }

    public void initViewForNewPlan() {
        countDaySchedule = 1;
        planScheduleList = new ArrayList<>();

        validator = new Validator(this);
        validator.setValidationListener(this);

        currentUser = dataService.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        calendar = Calendar.getInstance();
        plan = new Plan();
        invitedFriendIdList = new ArrayList<>();

        userFriendList = new ArrayList<>();
        if (dataService.getUserFriendList() != null) {
            tvFriendNotAvailable.setVisibility(View.GONE);
            userFriendList.addAll(dataService.getUserFriendList());
        }

        inviteFriendAdapter = new InviteFriendAdapter(this, userFriendList, invitedFriendIdList, currentPlan);
        rvListFriend.setAdapter(inviteFriendAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvListFriend.setLayoutManager(manager);
    }

    public void initViewForUpdatePlan() {
        countDaySchedule = 1;
        planScheduleList = new ArrayList<>();

        validator = new Validator(this);
        validator.setValidationListener(this);

        currentUser = dataService.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        calendar = Calendar.getInstance();
        plan = new Plan();
        invitedFriendIdList = new ArrayList<>();
        invitedFriendIdListOld = new ArrayList<>();
        if (currentPlan.getFriendInvitedList() != null) {
            invitedFriendIdList.addAll(currentPlan.getFriendInvitedList());
            invitedFriendIdListOld.addAll(currentPlan.getFriendInvitedList());
        }

        userFriendList = new ArrayList<>();
        if (dataService.getUserFriendList() != null) {
            tvFriendNotAvailable.setVisibility(View.GONE);
            userFriendList.addAll(dataService.getUserFriendList());
        }

        inviteFriendAdapter = new InviteFriendAdapter(this, userFriendList, invitedFriendIdList, currentPlan);
        rvListFriend.setAdapter(inviteFriendAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvListFriend.setLayoutManager(manager);
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

    public void sendTripPlanToFriends() {
        // Remove value from old friend be invited
        if (invitedFriendIdListOld != null) {
            boolean isExist;
            for (int i = 0; i < invitedFriendIdListOld.size(); i++) {
                isExist = false;
                for (String friendId : invitedFriendIdList) {
                    if (invitedFriendIdListOld.get(i).equals(friendId)) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    databaseReference.child("Plan").child(invitedFriendIdListOld.get(i)).removeValue();
                }
            }
        }

        if (invitedFriendIdList.size() == 0) {
            String message;
            if (currentPlan != null) {
                message = "Kế hoạch của bạn đã được cập nhật";
            } else {
                message = "Kế hoạch của bạn đã được tạo";
            }
            Toast.makeText(MakePlanActivity.this, message, Toast.LENGTH_SHORT).show();
            finish();
        }

        // Add value to new friend be invited
        for (String friendId : invitedFriendIdList) {
            databaseReference.child("Plan").child(friendId).child(planId)
                    .setValue(plan, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                Toast.makeText(MakePlanActivity.this,
                                        "Lỗi đường truyền, bạn hãy gửi lại!", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            } else {
                                progressBar.setVisibility(View.GONE);
                                String message;
                                if (currentPlan != null) {
                                    message = "Kế hoạch của bạn đã được cập nhật";
                                } else {
                                    message = "Kế hoạch của bạn đã được tạo";
                                }
                                Toast.makeText(MakePlanActivity.this, message, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
        }
    }

    @Click(R.id.activity_make_plan_btn_cancel)
    void onBtnCancelClick() {
        super.onBackPressed();
    }

    @Click(R.id.activity_make_plan_btn_done)
    void onBtnDoneClick() {
        validator.validate();
    }

    @Click(R.id.activity_make_plan_btn_add_schedule)
    void onBtnAddScheduleClick() {
        TextInputLayout textInputLayout = (TextInputLayout) getLayoutInflater().inflate(R.layout.item_day_schedule, null);
        textInputLayout.setHint("Ngày " + ++countDaySchedule);
        llSchedule.addView(textInputLayout);
    }

    @Click(R.id.activity_make_plan_btn_remove_schedule)
    void onBtnRemoveScheduleClick() {
        countDaySchedule = llSchedule.getChildCount();
        for (int i = countDaySchedule; i > 1; i--) {
            llSchedule.removeView(llSchedule.getChildAt(i - 1));
            --countDaySchedule;
            break;
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

    @Override
    public void onValidationSucceeded() {
        nsvRoot.scrollTo(0, nsvRoot.getBottom());
        progressBar.setVisibility(View.VISIBLE);
        for (int i = 0; i < countDaySchedule; i++) {
            TextInputEditText textInputEditText = (TextInputEditText) (llSchedule.getChildAt(i)).findViewById(R.id.item_day_schedule_tiet);
            planScheduleList.add(new PlanSchedule((i + 1) + "", textInputEditText.getText().toString()));
        }

        if (currentPlan == null) {
            // Set id by the time
            planId = System.currentTimeMillis() + "";
        } else {
            // Update to current plan
            planId = currentPlan.getId();
        }
        plan.setId(planId);
        plan.setName(etTripName.getText().toString());
        plan.setDestination(etDestination.getText().toString());
        plan.setPlanScheduleList(planScheduleList);
        plan.setDateGo(tvDateGo.getText().toString());
        plan.setDateBack(tvDateBack.getText().toString());
        plan.setUserMakePlan(dataService.getCurrentUser());
        plan.setFriendInvitedList(invitedFriendIdList);

        // Send data to storage of current user (the user create this trip plan)
        databaseReference.child("Plan").child(currentUser.getUid()).child(planId)
                .setValue(plan, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Toast.makeText(MakePlanActivity.this,
                                    "Lỗi đường truyền, bạn hãy gửi lại!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        } else {
                            // Send data to storage of friends be invited
                            sendTripPlanToFriends();
                        }
                    }
                });
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            switch (message) {
                case "Invalid length":
                    message = "Chiều dài quá 40 ký tự";
                    break;
                case "This field is required":
                    message = "Không được để trống";
                    break;
            }
            ((EditText) view).setError(message);
        }
    }
}
