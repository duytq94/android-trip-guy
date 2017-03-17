package com.dfa.vinatrip.MainFunction.Plan;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.dfa.vinatrip.MainFunction.Plan.MakePlan.MakePlanActivity_;
import com.dfa.vinatrip.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_plan)
public class PlanFragment extends Fragment {

    @ViewById(R.id.fragment_plan_rv_plan)
    RecyclerView rvPlan;

    @ViewById(R.id.fragment_plan_srl_reload)
    SwipeRefreshLayout srlReload;

    private List<Plan> planList;

    @AfterViews
    void onCreateView() {
        srlReload.setColorSchemeResources(R.color.colorMain);

        planList = new ArrayList<>();
        
    }

    @Click(R.id.fragment_plan_fab_make_new_plan)
    void onFabMakeNewPlanClick() {
        Intent intent = new Intent(getActivity(), MakePlanActivity_.class);
        startActivity(intent);
    }

}
