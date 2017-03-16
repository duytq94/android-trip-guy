package com.dfa.vinatrip.MainFunction.Plan;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.dfa.vinatrip.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.fragment_plan)
public class PlanFragment extends Fragment {

    @Click(R.id.fragment_plan_fab_make_new_plan)
    void onFabMakeNewPlanClick() {
        Intent intent = new Intent(getActivity(), MakePlanActivity_.class);
        startActivity(intent);
    }

    @AfterViews
    void onCreateView() {


    }


}
