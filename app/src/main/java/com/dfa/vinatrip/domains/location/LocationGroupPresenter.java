package com.dfa.vinatrip.domains.location;

import com.beesightsoft.caf.services.schedulers.RxScheduler;
import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.models.response.user.User;
import com.dfa.vinatrip.services.account.AccountService;
import com.dfa.vinatrip.services.plan.PlanService;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by duytq on 10/8/2017.
 */

public class LocationGroupPresenter extends BasePresenter<LocationGroupView> {

    private Subscription subscription;
    private PlanService planService;
    private AccountService accountService;

    @Inject
    public LocationGroupPresenter(EventBus eventBus, PlanService planService, AccountService accountService) {
        super(eventBus);
        this.planService = planService;
        this.accountService = accountService;
    }

    public User getCurrentUser() {
        return accountService.getCurrentUser();
    }

    public void getPlanUser(long planId) {
        RxScheduler.onStop(subscription);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscription = planService.getPlanUser(planId)
                .compose(RxScheduler.applyIoSchedulers())
                .subscribe(userInPlanList -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        getView().getPlanUserSuccess(userInPlanList);
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        getView().apiError(throwable);
                    }
                });
    }
}
