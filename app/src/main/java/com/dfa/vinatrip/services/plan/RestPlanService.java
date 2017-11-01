package com.dfa.vinatrip.services.plan;

import com.beesightsoft.caf.services.common.RestMessageResponse;
import com.dfa.vinatrip.domains.main.fragment.plan.Plan;
import com.dfa.vinatrip.domains.main.fragment.plan.make_plan.PlanSchedule;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by duytq on 11/01/2017.
 */

public interface RestPlanService {
    @POST("plan")
    Observable<RestMessageResponse<String>> createPlan(
            @Body Plan newPlan
    );

    @GET("plan/{userId}")
    Observable<RestMessageResponse<List<Plan>>> getPlan(
            @Path("userId") long userId
    );

    @GET("planSchedule/{planId}")
    Observable<RestMessageResponse<List<PlanSchedule>>> getPlanSchedule(
            @Path("planId") long planId
    );
}
