package com.dfa.vinatrip.domains.main.fragment.plan.make_plan;

import com.dfa.vinatrip.base.BaseMvpView;
import com.dfa.vinatrip.domains.main.fragment.plan.UserInPlan;
import com.dfa.vinatrip.models.response.User;

import java.util.List;

/**
 * Created by duytq on 10/30/2017.
 */

public interface MakePlanView extends BaseMvpView {
    void getListFriendSuccess(List<User> friendList);

    void createPlanSuccess(String message);

    void getDetailPlanSuccess(List<PlanSchedule> planScheduleList, List<UserInPlan> invitedFriendList);
}
