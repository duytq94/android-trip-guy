package com.dfa.vinatrip.domains.main.fragment.plan.detail_plan;

import com.dfa.vinatrip.base.BaseMvpView;
import com.dfa.vinatrip.domains.main.fragment.plan.make_plan.PlanSchedule;

import java.util.List;

/**
 * Created by duytq on 10/30/2017.
 */

public interface DetailPlanView extends BaseMvpView {
    void getPlanScheduleSuccess(List<PlanSchedule> planScheduleList);
}
