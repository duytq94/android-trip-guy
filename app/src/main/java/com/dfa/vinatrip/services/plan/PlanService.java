package com.dfa.vinatrip.services.plan;

import com.dfa.vinatrip.domains.main.fragment.plan.Plan;
import com.dfa.vinatrip.domains.main.fragment.plan.UserInPlan;
import com.dfa.vinatrip.domains.main.fragment.plan.make_plan.PlanSchedule;

import java.util.List;

import rx.Observable;

/**
 * Created by duytq on 11/01/2017.
 */

public interface PlanService {
    Observable<String> createPlan(Plan newPlan);

    Observable<String> updatePlan(Plan newPlan);

    Observable<List<Plan>> getPlan(long userId, String title, int type, int expired, long currentTimestamp, int page, int pageSize);

    Observable<List<PlanSchedule>> getPlanSchedule(long planId);

    Observable<List<UserInPlan>> getPlanUser(long planId);

    Observable<String> cancelPlan(long userId, long planId);

    Observable<String> removePlan(long planId);
}
