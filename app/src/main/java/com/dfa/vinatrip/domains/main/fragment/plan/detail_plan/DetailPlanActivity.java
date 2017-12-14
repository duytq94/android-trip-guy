package com.dfa.vinatrip.domains.main.fragment.plan.detail_plan;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseActivity;
import com.dfa.vinatrip.domains.chat.ChatGroupActivity_;
import com.dfa.vinatrip.domains.location.LocationGroupActivity_;
import com.dfa.vinatrip.domains.main.fragment.plan.Plan;
import com.dfa.vinatrip.domains.main.fragment.plan.UserInPlan;
import com.dfa.vinatrip.domains.main.fragment.plan.make_plan.PlanSchedule;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.models.response.user.User;
import com.dfa.vinatrip.video_call.LoginVideoCallActivity_;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

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

    private User currentUser;

    private ImageLoader imageLoader;
    private DisplayImageOptions imageOptions;

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
    public void init() {
        imageLoader = ImageLoader.getInstance();
        imageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.bg_green)
                .showImageForEmptyUri(R.drawable.photo_not_available)
                .showImageOnFail(R.drawable.photo_not_available)
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.ARGB_4444)
                .build();

        currentUser = presenter.getCurrentUser();
        setupAppBar();
        presenter.getPlanDetail(plan.getId());
    }

    public void initView() {
        imageLoader.displayImage(plan.getAvatarUserMakePlan(), civAvatar, imageOptions);
        if (currentUser.getId() == plan.getIdUserMakePlan()) {
            tvNickname.setText("Tôi");
        } else {
            tvNickname.setText(plan.getUsernameUserMakePlan());
        }
        tvEmail.setText(plan.getEmailUserMakePlan());

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
        ScheduleAdapter adapter = new ScheduleAdapter(plan.getPlanScheduleList(), plan, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);

        rvSchedule.setLayoutManager(layoutManager);
        rvSchedule.setAdapter(adapter);
        rvSchedule.setHasFixedSize(true);
    }

    public void setupAdapterUserJoin() {
        ListFriendHorizontalAdapter adapter = new ListFriendHorizontalAdapter(this, plan, currentUser);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvFriendJoin.setAdapter(adapter);
        rvFriendJoin.setLayoutManager(layoutManager);
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
        ChatGroupActivity_.intent(this).plan(plan).start();
    }

    @Click(R.id.activity_detail_plan_ll_location_group)
    public void onLlLocationGroupClick() {
        LocationGroupActivity_.intent(this).plan(plan).start();
    }

    @Click(R.id.activity_detail_plan_ll_video_call)
    public void onLlVideoCallClick() {
        LoginVideoCallActivity_.intent(this)
                .plan(plan)
                .currentUser(presenter.getCurrentUser())
                .start();
    }

    @Override

    public void showLoading() {
        showHUD();
    }

    @Override
    public void hideLoading() {
        hideHUD();
    }

    @Override
    public void apiError(Throwable throwable) {
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getDetailPlanSuccess(List<PlanSchedule> planScheduleList, List<UserInPlan> invitedFriendList) {
        if (planScheduleList.size() > 0) {
            plan.setPlanScheduleList(planScheduleList);
            setupAdapterSchedule();
        }
        if (invitedFriendList.size() > 0) {
            tvFriendNotAvailable.setVisibility(View.GONE);
            plan.setInvitedFriendList(invitedFriendList);
            setupAdapterUserJoin();
        } else {
            tvFriendNotAvailable.setVisibility(View.VISIBLE);
        }
        initView();
    }
}
