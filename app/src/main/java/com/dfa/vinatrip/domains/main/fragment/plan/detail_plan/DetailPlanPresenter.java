package com.dfa.vinatrip.domains.main.fragment.plan.detail_plan;

import com.beesightsoft.caf.services.schedulers.RxScheduler;
import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.domains.main.fragment.plan.UserInPlan;
import com.dfa.vinatrip.domains.main.fragment.plan.make_plan.PlanSchedule;
import com.dfa.vinatrip.models.response.User;
import com.dfa.vinatrip.services.account.AccountService;
import com.dfa.vinatrip.services.plan.PlanService;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func2;

/**
 * Created by duytq on 10/30/2017.
 */

public class DetailPlanPresenter extends BasePresenter<DetailPlanView> {

    private AccountService accountService;
    private PlanService planService;
    private Subscription subscription;

    @Inject
    public DetailPlanPresenter(EventBus eventBus, AccountService accountService, PlanService planService) {
        super(eventBus);
        this.accountService = accountService;
        this.planService = planService;
    }

    public User getCurrentUser() {
        return accountService.getCurrentUser();
    }

    public void getPlanDetail(long planId) {
        RxScheduler.onStop(subscription);
        if (isViewAttached()) {
            getView().showLoading();
        }

        subscription = Observable.zip(
                planService.getPlanSchedule(planId),
                planService.getPlanUser(planId),
                new Func2<List<PlanSchedule>, List<UserInPlan>, String>() {
                    @Override
                    public String call(List<PlanSchedule> planScheduleList, List<UserInPlan> invitedFriendList) {
                        if (isViewAttached()) {
                            getView().hideLoading();
                            getView().getDetailPlanSuccess(planScheduleList, invitedFriendList);
                        }
                        return null;
                    }
                }
        ).compose(RxScheduler.applyIoSchedulers())
                .subscribe(s -> {

                }, throwable -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        getView().apiError(throwable);
                    }
                });
    }
}
