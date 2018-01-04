package com.dfa.vinatrip.domains.main.fragment.me;

import com.beesightsoft.caf.services.schedulers.RxScheduler;
import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.models.request.ChangePasswordRequest;
import com.dfa.vinatrip.models.response.user.User;
import com.dfa.vinatrip.services.account.AccountService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by duytq on 10/30/2017.
 */

public class MePresenter extends BasePresenter<MeView> {
    private AccountService accountService;
    private Subscription subscription1;
    private Subscription subscription2;

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

    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        RxScheduler.onStop(subscription2);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscription2 = accountService.changePassword(changePasswordRequest)
                .compose(RxScheduler.applyIoSchedulers())
                .doOnTerminate(() -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                    }
                }).subscribe(s -> {
                    if (isViewAttached()) {
                        getView().changePasswordSuccess();
                        signOut();
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().apiError(throwable);
                    }
                });
    }

    public void signOut() {
        RxScheduler.onStop(subscription1);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscription1 = accountService.logout()
                .compose(RxScheduler.applyIoSchedulers())
                .subscribe(s -> {
                    if (isViewAttached()) {
                        getView().signOutSuccess();
                    }
                }, throwable -> {
                    getView().apiError(throwable);
                });
    }

    //-----------------------------------------------------------------------------------------------------------------
    @Subscribe
    public void onMessageMakeRequestEvent(User user) {
        if (isViewAttached()) {
            getView().loginOtherActivity();
        }
    }
}
