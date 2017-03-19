package com.dfa.vinatrip.MainFunction.Plan;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import de.hdodenhof.circleimageview.CircleImageView;

@EActivity(R.layout.activity_detail_plan)
public class DetailPlanActivity extends AppCompatActivity {

    @ViewById(R.id.my_toolbar)
    Toolbar toolbar;

    @ViewById(R.id.activity_detail_plan_civ_avatar)
    CircleImageView civAvatar;

    @ViewById(R.id.activity_detail_plan_tv_nickname)
    TextView tvNickname;

    @ViewById(R.id.activity_detail_plan_tv_email)
    TextView tvEmail;

    @ViewById(R.id.activity_detail_plan_rv_friend_join)
    RecyclerView rvFriendJoin;

    @ViewById(R.id.activity_detail_plan_tv_destination)
    TextView tvDestination;

    @ViewById(R.id.activity_detail_plan_tv_date_go)
    TextView tvDateGo;

    @ViewById(R.id.activity_detail_plan_tv_date_back)
    TextView tvDateBack;

    @ViewById(R.id.activity_detail_plan_tv_schedule)
    TextView tvSchedule;

    private android.support.v7.app.ActionBar actionBar;
    private Plan plan;

    @AfterViews
    void onCreate() {
        // Get Plan from FragmentPlan
        plan = (Plan) getIntent().getSerializableExtra("Plan");

        changeColorStatusBar();
        setupAppBar();
        initView();
    }

    public void initView() {
        Picasso.with(this).load(plan.getUserMakePlan().getAvatar()).into(civAvatar);
    }

    public void changeColorStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorStatusBar));
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
}
