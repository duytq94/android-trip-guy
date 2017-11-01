package com.dfa.vinatrip.domains.main.fragment.plan.make_plan;

import com.dfa.vinatrip.base.BaseMvpView;
import com.dfa.vinatrip.models.response.User;

import java.util.List;

/**
 * Created by duytq on 10/30/2017.
 */

public interface MakePlanView extends BaseMvpView {
    void getListFriendSuccess(List<User> friendList);

    void getListFriendFail(Throwable throwable);

    void createPlanSuccess(String message);

    void createPlanFail(Throwable throwable);
}
