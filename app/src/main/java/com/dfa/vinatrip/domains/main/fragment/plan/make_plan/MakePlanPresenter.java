package com.dfa.vinatrip.domains.main.fragment.plan.make_plan;

import com.beesightsoft.caf.services.schedulers.RxScheduler;
import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.models.response.User;
import com.dfa.vinatrip.services.account.AccountService;
import com.dfa.vinatrip.services.friend.FriendService;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by duytq on 10/30/2017.
 */

public class MakePlanPresenter extends BasePresenter<MakePlanView> {

    private Subscription subscription;
    private AccountService accountService;
    private FriendService friendService;

    @Inject
    public MakePlanPresenter(EventBus eventBus, AccountService accountService, FriendService friendService) {
        super(eventBus);
        this.accountService = accountService;
        this.friendService = friendService;
    }

    public User getCurrentUser() {
        return accountService.getCurrentUser();
    }

    public void getListFriend() {
        RxScheduler.onStop(subscription);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscription = friendService.getListFriend()
                .compose(RxScheduler.applyIoSchedulers())
                .subscribe(friendList -> {
                    if (isViewAttached()) {
                        getView().getListFriendSuccess(friendList);
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().getListFriendFail(throwable);
                    }
                });
    }
}
