package com.dfa.vinatrip.MainFunction.Plan;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dfa.vinatrip.MainFunction.Plan.MakePlan.MakePlanActivity_;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.SplashScreen.DataService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_plan)
public class PlanFragment extends Fragment {

    @Bean
    DataService dataService;

    @ViewById(R.id.fragment_plan_rv_plan)
    RecyclerView rvPlan;

    @ViewById(R.id.fragment_plan_srl_reload)
    SwipeRefreshLayout srlReload;

    private List<Plan> planList;
    private PlanAdapter planAdapter;

    @AfterViews
    void onCreateView() {
        srlReload.setColorSchemeResources(R.color.colorMain);

        if (dataService.getPlanList() != null) {
            planList = new ArrayList<>();
            planList.addAll(dataService.getPlanList());
            planAdapter = new PlanAdapter(getActivity(), planList, srlReload);
            rvPlan.setAdapter(planAdapter);
            StaggeredGridLayoutManager staggeredGridLayoutManager =
                    new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
            rvPlan.setLayoutManager(staggeredGridLayoutManager);
        }
    }

    @Click(R.id.fragment_plan_fab_make_new_plan)
    void onFabMakeNewPlanClick() {
        if (dataService.getCurrentUser() != null) {
            Intent intent = new Intent(getActivity(), MakePlanActivity_.class);
            startActivity(intent);
        } else {
            Toast.makeText(getActivity(), "Bạn phải đăng nhập mới sử dụng được tính năng này", Toast.LENGTH_SHORT).show();
        }

    }

}
