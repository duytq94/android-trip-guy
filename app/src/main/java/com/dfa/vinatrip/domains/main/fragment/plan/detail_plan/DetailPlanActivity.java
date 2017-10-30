package com.dfa.vinatrip.domains.main.fragment.plan.detail_plan;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseActivity;
import com.dfa.vinatrip.domains.chat.ChatGroupActivity_;
import com.dfa.vinatrip.domains.location.LocationGroupActivity_;
import com.dfa.vinatrip.domains.main.fragment.me.detail_me.make_friend.UserFriend;
import com.dfa.vinatrip.domains.main.fragment.plan.Plan;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.models.response.User;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;

@EActivity(R.layout.activity_detail_plan)
public class DetailPlanActivity extends BaseActivity<DetailPlanView, DetailPlanPresenter>
        implements DetailPlanView {

    @Extra
    protected Plan plan;

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
    @ViewById(R.id.activity_detail_plan_tv_friend_not_available)
    protected TextView tvFriendNotAvailable;
    @ViewById(R.id.activity_detail_plan_rv_schedule)
    protected RecyclerView rvSchedule;

    private List<UserFriend> userFriendList;
    private List<UserFriend> friendInvitedList;
    private ListFriendHorizontalAdapter listFriendHorizontalAdapter;
    private User currentUser;
    private ScheduleAdapter adapter;

    @App
    protected MainApplication application;
    @Inject
    protected DetailPlanPresenter presenter;

    @AfterInject
    protected void initInject() {
        DaggerDetailPlanComponent.builder()
                .applicationComponent(application.getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build().inject(this);
    }

    @NonNull
    @Override
    public DetailPlanPresenter createPresenter() {
        return presenter;
    }

    @AfterViews
    void init() {
        currentUser = presenter.getCurrentUser();
        setupAppBar();
        initView();
        setupAdapterSchedule();
    }

    public void initView() {
        Picasso.with(this).load(plan.getUserMakePlan().getAvatar()).into(civAvatar);
        if (currentUser.getId() == plan.getUserMakePlan().getId()) {
            tvNickname.setText("Tôi");
        } else {
            tvNickname.setText(plan.getUserMakePlan().getUsername());
        }
        tvEmail.setText(plan.getUserMakePlan().getEmail());

        // Filter the friends be invited from List friend
//        userFriendList = dataService.getUserFriendList();
        friendInvitedList = new ArrayList<>();
        // Add the current user to friendInvitedList (if they're not the user make this plan)
        if (currentUser.getId() != plan.getUserMakePlan().getId()) {
//            friendInvitedList.add(new UserFriend(currentUser.getId(), "Tôi",
//                    currentUser.getAvatar(), currentUser.getEmail(), "true"));
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvFriendJoin.setLayoutManager(layoutManager);

        tvDestination.setText(plan.getDestination());
        tvDateGo.setText(plan.getDateGo());
        tvDateBack.setText(plan.getDateBack());
    }

    public void setupAppBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(plan.getName());

            // Set button back
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void setupAdapterSchedule() {
        adapter = new ScheduleAdapter(plan.getPlanScheduleList(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);

        rvSchedule.setLayoutManager(layoutManager);
        rvSchedule.setAdapter(adapter);
        rvSchedule.setHasFixedSize(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return false;
    }

    @Click(R.id.activity_detail_plan_ll_chat_group)
    public void onLlChatGroupClick() {
        ChatGroupActivity_.intent(this).userFriendList((ArrayList<UserFriend>) userFriendList).plan(plan).start();
    }

    @Click(R.id.activity_detail_plan_ll_location_group)
    public void onLlLocationGroup() {
        LocationGroupActivity_.intent(this).plan(plan).start();
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
