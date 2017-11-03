package com.dfa.vinatrip.domains.main.fragment.me;

import com.beesightsoft.caf.services.schedulers.RxScheduler;
import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.models.response.User;
import com.dfa.vinatrip.services.account.AccountService;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by duytq on 10/30/2017.
 */

public class MePresenter extends BasePresenter<MeView> {

    private AccountService accountService;
    private Subscription subscription;

    @Inject
    public MePresenter(EventBus eventBus, AccountService accountService) {
        super(eventBus);
        this.accountService = accountService;
    }

    public boolean isLogin() {
        return accountService.loadFromStorage();
    }

    public User getCurrentUser() {
        return accountService.getCurrentUser();
    }

    public void signOut() {
        RxScheduler.onStop(subscription);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscription = accountService.logout()
                .compose(RxScheduler.applyIoSchedulers())
                .subscribe(s -> {
                    if (isViewAttached()) {
                        getView().signOutSuccess();
                    }
                }, throwable -> {
                    getView().apiError(throwable);
                });
    }
}
