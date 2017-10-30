package com.dfa.vinatrip.domains.main.fragment.plan.make_plan;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseActivity;
import com.dfa.vinatrip.domains.main.fragment.me.detail_me.make_friend.UserFriend;
import com.dfa.vinatrip.domains.main.fragment.plan.Plan;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.models.response.User;
import com.dfa.vinatrip.services.DataService;
import com.dfa.vinatrip.utils.AppUtil;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import static com.dfa.vinatrip.utils.AppUtil.REQUEST_PLACE_AUTO_COMPLETE;
import static com.dfa.vinatrip.utils.Constants.MILLISECOND_IN_DAY;
import static com.dfa.vinatrip.utils.Constants.REQUEST_BACKGROUND;

@EActivity(R.layout.activity_make_plan)
public class MakePlanActivity extends BaseActivity<MakePlanView, MakePlanPresenter>
        implements MakePlanView, Validator.ValidationListener {

    @Bean
    DataService dataService;

    @ViewById(R.id.activity_make_plan_rv_list_friend)
    protected RecyclerView rvListFriend;
    @ViewById(R.id.activity_make_plan_nsv_root)
    protected NestedScrollView nsvRoot;
    @Length(max = 40)
    @NotEmpty
    @ViewById(R.id.activity_make_plan_et_trip_name)
    protected EditText etTripName;
    @NotEmpty
    @ViewById(R.id.activity_make_plan_tv_destination)
    protected TextView tvDestination;
    @ViewById(R.id.activity_make_plan_tv_date_go)
    protected TextView tvDateGo;
    @ViewById(R.id.activity_make_plan_tv_date_back)
    protected TextView tvDateBack;
    @ViewById(R.id.activity_make_plan_tv_friend_not_available)
    protected TextView tvFriendNotAvailable;
    @ViewById(R.id.activity_make_plan_progressBar)
    protected ProgressBar progressBar;
    @ViewById(R.id.activity_make_plan_ll_schedule)
    protected LinearLayout llSchedule;
    @ViewById(R.id.activity_make_plan_ll_background)
    protected LinearLayout llBackground;
    @ViewById(R.id.activity_make_plan_civ_background)
    protected CircleImageView civBackground;

    private List<UserFriend> userFriendList;
    private InviteFriendAdapter inviteFriendAdapter;
    private Plan plan;
    private Calendar calendar;
    private DatePickerDialog dpdDateGo, dpdDateBack;
    private List<String> invitedFriendIdList;
    private List<String> invitedFriendIdListOld;
    private DatabaseReference databaseReference;
    private User currentUser;
    private Validator validator;
    private List<PlanSchedule> planScheduleList;
    private String planId;
    private Plan currentPlan;
    private int idBackground;

    private String dateGo = "";
    private String dateBack = "";
    private long timestampGo;
    private long countDaySchedule;

    @App
    protected MainApplication application;
    @Inject
    protected MakePlanPresenter presenter;

    @AfterInject
    protected void initInject() {
        DaggerMakePlanComponent.builder()
                .applicationComponent(application.getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build().inject(this);
    }

    @NonNull
    @Override
    public MakePlanPresenter createPresenter() {
        return presenter;
    }

    @AfterViews
    public void init() {
        // Get the current plan when user click to update plan on item recycler
        if (getIntent().getSerializableExtra("Plan") != null) {
            currentPlan = (Plan) getIntent().getSerializableExtra("Plan");
            etTripName.setText(currentPlan.getName());
            tvDestination.setText(currentPlan.getDestination());
            tvDateGo.setText(currentPlan.getDateGo());
            tvDateBack.setText(currentPlan.getDateBack());
            idBackground = currentPlan.getIdBackground();
            civBackground.setImageResource(idBackground);

            for (int i = 0; i < currentPlan.getPlanScheduleList().size(); i++) {
                if (i == 0) {
//                    tietSchedule.setText(currentPlan.getPlanScheduleList().get(i).getContent());
                } else {
//                    TextInputLayout textInputLayout = (TextInputLayout) getLayoutInflater()
//                            .inflate(R.layout.item_add_schedule, null);
//                    textInputLayout.setHint("Ngày " + ++countDaySchedule);
//                    textInputLayout.getEditText().setText(currentPlan.getPlanScheduleList().get(i).getContent());
//                    llSchedule.addView(textInputLayout);
                }
                initViewForUpdatePlan();
            }
        } else {
            initViewForNewPlan();
            setCurrentDayForView();
        }
    }

    public void initViewForNewPlan() {
        idBackground = R.drawable.bg_test3;
        civBackground.setImageResource(idBackground);

        countDaySchedule = 1;
        planScheduleList = new ArrayList<>();

        validator = new Validator(this);
        validator.setValidationListener(this);

        currentUser = presenter.getCurrentUser();
        calendar = Calendar.getInstance();
        plan = new Plan();
        invitedFriendIdList = new ArrayList<>();

        userFriendList = new ArrayList<>();
        if (dataService.getUserFriendList().size() != 0) {
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

        currentUser = presenter.getCurrentUser();
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
        if (dataService.getUserFriendList().size() != 0) {
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
        dateGo = strDate;
        dateBack = strDate;
        initViewListSchedule();
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
            Toasty.success(MakePlanActivity.this, message, Toast.LENGTH_SHORT).show();
            finish();
        }

        // Add value to new friend be invited
        for (String friendId : invitedFriendIdList) {
            databaseReference.child("Plan").child(friendId).child(planId)
                    .setValue(plan, (databaseError, databaseReference1) -> {
                        if (databaseError != null) {
                            Toasty.error(MakePlanActivity.this, "Lỗi đường truyền, bạn hãy gửi lại!",
                                    Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        } else {
                            progressBar.setVisibility(View.GONE);
                            String message;
                            if (currentPlan != null) {
                                message = "Kế hoạch của bạn đã được cập nhật";
                            } else {
                                message = "Kế hoạch của bạn đã được tạo";
                            }
                            Toasty.success(MakePlanActivity.this, message, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
        }
    }

    public void initViewListSchedule() {
        timestampGo = AppUtil.stringDateToTimestamp(dateGo);
        countDaySchedule = AppUtil.stringDateToTimestamp(dateBack) - AppUtil.stringDateToTimestamp(dateGo);
        countDaySchedule = countDaySchedule / MILLISECOND_IN_DAY + 1;

        llSchedule.removeAllViews();
        for (int i = 0; i < countDaySchedule; i++) {
            LinearLayout llRoot = (LinearLayout) getLayoutInflater().inflate(R.layout.item_add_schedule, null);
            LinearLayout llMain = (LinearLayout) llRoot.getChildAt(1);
            EditText etTitle = (EditText) llMain.findViewById(R.id.item_add_schedule_et_title);
            EditText etContent = (EditText) llMain.findViewById(R.id.item_add_schedule_et_content);
            TextView tvDate = (TextView) llMain.findViewById(R.id.item_add_schedule_tv_date);

            etTitle.setHint(String.format("Ngày %s...", i + 1));
            etContent.setHint(String.format("Lịch trình ngày %s...", i + 1));
            tvDate.setText(AppUtil.formatTime("dd/MM/yyyy", timestampGo + MILLISECOND_IN_DAY * i));
            llSchedule.addView(llRoot);
        }
    }

    @Click(R.id.activity_make_plan_btn_cancel)
    public void onBtnCancelClick() {
        super.onBackPressed();
    }

    @Click(R.id.activity_make_plan_btn_done)
    public void onBtnDoneClick() {
        validator.validate();
    }

    @Click(R.id.activity_make_plan_ll_date_go)
    public void onLlDateGoClick() {
        // When date be set
        DatePickerDialog.OnDateSetListener listener = (view, year, month, dayOfMonth) -> {
            dateGo = dayOfMonth + "/" + (month + 1) + "/" + year;
            dateBack = dayOfMonth + "/" + (month + 1) + "/" + year;
            tvDateGo.setText(dateGo);
            tvDateBack.setText(dateGo);
            calendar.set(year, month, dayOfMonth);

            initViewListSchedule();
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
    public void onLlDateBackClick() {
        // When date be set
        DatePickerDialog.OnDateSetListener listener = (view, year, month, dayOfMonth) -> {
            dateBack = dayOfMonth + "/" + (month + 1) + "/" + year;
            tvDateBack.setText(dateBack);
            calendar.set(year, month, dayOfMonth);

            initViewListSchedule();
        };

        // Set current position time when start dialog
        String strDate[] = tvDateBack.getText().toString().split("/");
        int day = Integer.parseInt(strDate[0]);
        int month = Integer.parseInt(strDate[1]) - 1;
        int year = Integer.parseInt(strDate[2]);
        dpdDateBack = new DatePickerDialog(this, listener, year, month, day);
        dpdDateBack.getDatePicker().setMinDate(AppUtil.stringDateToTimestamp(dateGo));
        dpdDateBack.setTitle("Chọn ngày về");
        dpdDateBack.show();
    }

    @Click(R.id.activity_make_plan_ll_background)
    public void onLlBackgroundClick() {
        Intent intent = new Intent(this, ChooseBackgroundPlanActivity_.class);
        startActivityForResult(intent, REQUEST_BACKGROUND);
    }

    @Click(R.id.activity_make_plan_ll_destination)
    public void onLlDestinationClick() {
        try {
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().setCountry("VN").build();
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .setFilter(typeFilter).build(this);
            startActivityForResult(intent, REQUEST_PLACE_AUTO_COMPLETE);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @OnActivityResult(REQUEST_BACKGROUND)
    public void onResultBackground(int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            idBackground = data.getIntExtra("idBackground", R.drawable.bg_test3);
            civBackground.setImageResource(idBackground);
        }
    }

    @OnActivityResult(REQUEST_PLACE_AUTO_COMPLETE)
    public void onResultPlace(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            Place place = PlaceAutocomplete.getPlace(this, data);
            tvDestination.setText(place.getAddress());
        } else if (data == null) {
            Toasty.error(this, "Không chọn được địa điểm, bạn hãy thử lại", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onValidationSucceeded() {
        nsvRoot.scrollTo(0, nsvRoot.getBottom());
        progressBar.setVisibility(View.VISIBLE);
        for (int i = 0; i < countDaySchedule; i++) {
            String title = ((EditText) llSchedule.getChildAt(i).findViewById(R.id.item_add_schedule_et_title)).getText().toString();
            String content = ((EditText) llSchedule.getChildAt(i).findViewById(R.id.item_add_schedule_et_content)).getText().toString();
            planScheduleList.add(new PlanSchedule(content, title, timestampGo + MILLISECOND_IN_DAY * i));
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
        plan.setDestination(tvDestination.getText().toString());
        plan.setPlanScheduleList(planScheduleList);
        plan.setDateGo(tvDateGo.getText().toString());
        plan.setDateBack(tvDateBack.getText().toString());
//        plan.setUserMakePlan(dataService.getCurrentUser());
        plan.setFriendInvitedList(invitedFriendIdList);
        plan.setIdBackground(idBackground);

        // Send data to storage of current user (the user create this trip plan)
        databaseReference.child("Plan").child(String.valueOf(currentUser.getId())).child(planId)
                .setValue(plan, (databaseError, databaseReference1) -> {
                    if (databaseError != null) {
                        Toasty.error(MakePlanActivity.this, "Lỗi đường truyền, bạn hãy gửi lại!", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    } else {
                        // Send data to storage of friends be invited
                        sendTripPlanToFriends();
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
            if (view instanceof TextView) {
                ((TextView) view).setError(message);
            } else {
                ((EditText) view).setError(message);
            }
        }
    }

    @Override
    public void showLoading() {
//        showHUD();
    }

    @Override
    public void hideLoading() {
//        hideHUD();
    }

    @Override
    public void apiError(Throwable throwable) {

    }
}
