package com.dfa.vinatrip.domains.main.plan.detail_plan;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.me.UserProfile;
import com.dfa.vinatrip.domains.main.me.detail_me.make_friend.UserFriend;
import com.dfa.vinatrip.domains.main.plan.Plan;
import com.dfa.vinatrip.domains.main.plan.make_plan.PlanSchedule;
import com.dfa.vinatrip.services.DataService;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

@EActivity(R.layout.activity_detail_plan)
public class DetailPlanActivity extends AppCompatActivity {

    @Bean
    DataService dataService;

    @ViewById(R.id.my_toolbar)
    protected Toolbar toolbar;

    @ViewById(R.id.activity_detail_plan_civ_avatar)
    protected CircleImageView civAvatar;

    @ViewById(R.id.activity_detail_plan_tv_nickname)
    protected TextView tvNickname;

    @ViewById(R.id.activity_detail_plan_tv_email)
    protected TextView tvEmail;

    @ViewById(R.id.activity_detail_plan_rv_friend_join)
    protected RecyclerView rvFriendJoin;

    @ViewById(R.id.activity_detail_plan_tv_destination)
    protected TextView tvDestination;

    @ViewById(R.id.activity_detail_plan_tv_date_go)
    protected TextView tvDateGo;

    @ViewById(R.id.activity_detail_plan_tv_date_back)
    protected TextView tvDateBack;

    @ViewById(R.id.activity_detail_plan_tv_schedule)
    protected TextView tvSchedule;

    @ViewById(R.id.activity_detail_plan_tv_friend_not_available)
    protected TextView tvFriendNotAvailable;

    private ActionBar actionBar;
    private Plan plan;
    private List<UserFriend> userFriendList;
    private List<UserFriend> friendInvitedList;
    private ListFriendHorizontalAdapter listFriendHorizontalAdapter;
    private UserProfile currentUser;

    @AfterViews
    void init() {
        currentUser = dataService.getCurrentUser();

        // Get Plan from FragmentPlan
        plan = getIntent().getParcelableExtra("Plan");

        setupAppBar();
        initView();
    }

    public void initView() {
        Picasso.with(this).load(plan.getUserMakePlan().getAvatar()).into(civAvatar);
        if (currentUser.getUid().equals(plan.getUserMakePlan().getUid())) {
            tvNickname.setText("Tôi");
        } else {
            tvNickname.setText(plan.getUserMakePlan().getNickname());
        }
        tvEmail.setText(plan.getUserMakePlan().getEmail());

        // Filter the friends be invited from List friend
        userFriendList = dataService.getUserFriendList();
        friendInvitedList = new ArrayList<>();
        // Add the current user to friendInvitedList (if they're not the user make this plan)
        if (!currentUser.getUid().equals(plan.getUserMakePlan().getUid())) {
            friendInvitedList.add(new UserFriend(currentUser.getUid(), "Tôi",
                    currentUser.getAvatar(), currentUser.getEmail(), "true"));
        }
        if (plan.getFriendInvitedList() != null) {
            for (String friendInvitedId : plan.getFriendInvitedList()) {
                for (UserFriend userFriend : userFriendList) {
                    if (userFriend.getFriendId().equals(friendInvitedId)) {
                        friendInvitedList.add(userFriend);
                        break;
                    }
                }
            }
        }

        listFriendHorizontalAdapter = new ListFriendHorizontalAdapter(this, friendInvitedList, tvFriendNotAvailable);
        rvFriendJoin.setAdapter(listFriendHorizontalAdapter);
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        rvFriendJoin.setLayoutManager(staggeredGridLayoutManager);

        tvDestination.setText(plan.getDestination());
        tvDateGo.setText(plan.getDateGo());
        tvDateBack.setText(plan.getDateBack());
        for (PlanSchedule planSchedule : plan.getPlanScheduleList()) {
            tvSchedule.append("Ngày " + planSchedule.getDayOrder() + ": " + planSchedule.getContent() + "\n");
        }
    }

    public void setupAppBar() {
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(plan.getName());

            // Set button back
            actionBar.setDisplayHomeAsUpEnabled(true);
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

    @Click(R.id.activity_detail_plan_btn_chat_group)
    public void onBtnChatGroupClick() {
        
    }
}
