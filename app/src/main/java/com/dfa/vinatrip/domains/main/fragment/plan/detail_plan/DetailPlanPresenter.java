package com.dfa.vinatrip.domains.main.fragment.plan.detail_plan;

import com.beesightsoft.caf.services.schedulers.RxScheduler;
import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.models.response.User;
import com.dfa.vinatrip.services.account.AccountService;
import com.dfa.vinatrip.services.plan.PlanService;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import rx.Subscription;

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

    public void getPlanSchedule(long planId) {
        RxScheduler.onStop(subscription);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscription = planService.getPlanSchedule(planId)
                .compose(RxScheduler.applyIoSchedulers())
                .subscribe(planScheduleList -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        getView().getPlanScheduleSuccess(planScheduleList);
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        getView().apiError(throwable);
                    }
                });
    }
}
