package com.dfa.vinatrip.domains.main.fragment.me.detail_me.my_friend;

import com.beesightsoft.caf.services.schedulers.RxScheduler;
import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.models.response.User;
import com.dfa.vinatrip.services.account.AccountService;
import com.dfa.vinatrip.services.friend.FriendService;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by duytq on 10/31/2017.
 */

public class MyFriendPresenter extends BasePresenter<MyFriendView> {

    private AccountService accountService;
    private FriendService friendService;
    private Subscription subscription;

    @Inject
    public MyFriendPresenter(EventBus eventBus, AccountService accountService, FriendService friendService) {
        super(eventBus);
        this.accountService = accountService;
        this.friendService = friendService;
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
                .subscribe(userList -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        getView().getListFriendSuccess(userList);
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        getView().apiError(throwable);
                    }
                });
    }
}
