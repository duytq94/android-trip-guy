package com.dfa.vinatrip.domains.main.plan.make_plan;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.me.UserProfile;
import com.dfa.vinatrip.domains.main.me.detail_me.make_friend.UserFriend;
import com.dfa.vinatrip.domains.main.plan.Plan;
import com.dfa.vinatrip.services.DataService;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
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
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import static com.dfa.vinatrip.utils.AppUtil.REQUEST_PLACE_AUTO_COMPLETE;

@EActivity(R.layout.activity_make_plan)
public class MakePlanActivity extends AppCompatActivity implements Validator.ValidationListener {

    public static final int REQUEST_BACKGROUND = 1;

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
    @ViewById(R.id.activity_make_plan_tv_destination)
    TextView tvDestination;

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

    @ViewById(R.id.activity_make_plan_ll_background)
    LinearLayout llBackground;

    @ViewById(R.id.activity_make_plan_civ_background)
    CircleImageView civBackground;

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
    private int idBackground;

    @AfterViews
    void init() {
        // Get the current plan when user click to update plan on item recycler
        if (getIntent().getParcelableExtra("Plan") != null) {
            currentPlan = getIntent().getParcelableExtra("Plan");
            etTripName.setText(currentPlan.getName());
            tvDestination.setText(currentPlan.getDestination());
            tvDateGo.setText(currentPlan.getDateGo());
            tvDateBack.setText(currentPlan.getDateBack());
            idBackground = currentPlan.getIdBackground();
            civBackground.setImageResource(idBackground);

            for (int i = 0; i < currentPlan.getPlanScheduleList().size(); i++) {
                if (i == 0) {
                    tietSchedule.setText(currentPlan.getPlanScheduleList().get(i).getContent());
                } else {
                    TextInputLayout textInputLayout = (TextInputLayout) getLayoutInflater()
                            .inflate(R.layout.item_day_schedule, null);
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
        idBackground = R.drawable.bg_test3;
        civBackground.setImageResource(idBackground);

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
                             .setValue(plan, new DatabaseReference.CompletionListener() {
                                 @Override
                                 public void onComplete(DatabaseError databaseError,
                                                        DatabaseReference databaseReference) {
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
        TextInputLayout textInputLayout = (TextInputLayout) getLayoutInflater()
                .inflate(R.layout.item_day_schedule, null);
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

    @Click(R.id.activity_make_plan_ll_background)
    void onLlBackgroundClick() {
        Intent intent = new Intent(this, ChooseBackgroundPlanActivity_.class);
        startActivityForResult(intent, REQUEST_BACKGROUND);
    }

    @Click(R.id.activity_make_plan_ll_destination)
    void onLlDestinationClick() {
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
    void onResultBackground(int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            idBackground = data.getIntExtra("idBackground", R.drawable.bg_test3);
            civBackground.setImageResource(idBackground);
        }
    }

    @OnActivityResult(REQUEST_PLACE_AUTO_COMPLETE)
    void onResultPlace(int resultCode, Intent data) {
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
            TextInputEditText textInputEditText = (TextInputEditText) (llSchedule.getChildAt(i))
                    .findViewById(R.id.item_day_schedule_tiet);
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
        plan.setDestination(tvDestination.getText().toString());
        plan.setPlanScheduleList(planScheduleList);
        plan.setDateGo(tvDateGo.getText().toString());
        plan.setDateBack(tvDateBack.getText().toString());
        plan.setUserMakePlan(dataService.getCurrentUser());
        plan.setFriendInvitedList(invitedFriendIdList);
        plan.setIdBackground(idBackground);

        // Send data to storage of current user (the user create this trip plan)
        databaseReference.child("Plan").child(currentUser.getUid()).child(planId)
                         .setValue(plan, new DatabaseReference.CompletionListener() {
                             @Override
                             public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                 if (databaseError != null) {
                                     Toasty.error(MakePlanActivity.this,
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
            if (view instanceof TextView) {
                ((TextView) view).setError(message);
            } else {
                ((EditText) view).setError(message);
            }
        }
    }
}
