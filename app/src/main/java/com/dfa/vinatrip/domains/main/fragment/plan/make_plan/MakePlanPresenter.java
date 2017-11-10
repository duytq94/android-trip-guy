package com.dfa.vinatrip.domains.main.fragment.plan.make_plan;

import com.beesightsoft.caf.services.schedulers.RxScheduler;
import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.domains.main.fragment.plan.Plan;
import com.dfa.vinatrip.domains.main.fragment.plan.UserInPlan;
import com.dfa.vinatrip.models.response.user.User;
import com.dfa.vinatrip.services.account.AccountService;
import com.dfa.vinatrip.services.friend.FriendService;
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

public class MakePlanPresenter extends BasePresenter<MakePlanView> {

    private Subscription subscription;
    private AccountService accountService;
    private FriendService friendService;
    private PlanService planService;

    @Inject
    public MakePlanPresenter(EventBus eventBus, AccountService accountService,
                             FriendService friendService, PlanService planService) {
        super(eventBus);
        this.accountService = accountService;
        this.friendService = friendService;
        this.planService = planService;
    }

    public User getCurrentUser() {
        return accountService.getCurrentUser();
    }

    public void getListFriend(int page, int pageSize) {
        RxScheduler.onStop(subscription);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscription = friendService.getListFriend(page, pageSize)
                .compose(RxScheduler.applyIoSchedulers())
                .subscribe(friendList -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        getView().getListFriendSuccess(friendList);
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        getView().apiError(throwable);
                    }
                });
    }

    public void createPlan(Plan newPlan) {
        RxScheduler.onStop(subscription);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscription = planService.createPlan(newPlan)
                .compose(RxScheduler.applyIoSchedulers())
                .subscribe(s -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        getView().createPlanSuccess(s);
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        getView().apiError(throwable);
                    }
                });
    }

    public void updatePlan(Plan newPlan) {
        RxScheduler.onStop(subscription);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscription = planService.updatePlan(newPlan)
                .compose(RxScheduler.applyIoSchedulers())
                .subscribe(s -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        getView().updatePlanSuccess(s);
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        getView().apiError(throwable);
                    }
                });
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
