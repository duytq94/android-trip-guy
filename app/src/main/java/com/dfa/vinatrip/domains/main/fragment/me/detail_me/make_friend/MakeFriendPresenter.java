package com.dfa.vinatrip.domains.main.fragment.me.detail_me.make_friend;

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

public class MakeFriendPresenter extends BasePresenter<MakeFriendView> {

    private AccountService accountService;
    private FriendService friendService;
    private Subscription subscription;

    @Inject
    public MakeFriendPresenter(EventBus eventBus, AccountService accountService, FriendService friendService) {
        super(eventBus);
        this.accountService = accountService;
        this.friendService = friendService;
    }

    public User getCurrentUser() {
        return accountService.getCurrentUser();
    }

    public void getListUser(int page, int pageSize) {
        RxScheduler.onStop(subscription);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscription = friendService.getListUser(page, pageSize)
                .compose(RxScheduler.applyIoSchedulers())
                .subscribe(userList -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        getView().getListUserSuccess(userList, page);
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        getView().apiError(throwable);
                    }
                });
    }

    public void addFriendRequest(long peerId) {
        RxScheduler.onStop(subscription);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscription = friendService.addFriendRequest(peerId)
                .compose(RxScheduler.applyIoSchedulers())
                .subscribe(friendResponse -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        getView().addFriendRequestSuccess(friendResponse);
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        getView().apiError(throwable);
                    }
                });
    }
}
