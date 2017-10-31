package com.dfa.vinatrip.domains.auth.sign_in;

import com.beesightsoft.caf.services.schedulers.RxScheduler;
import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.models.request.AuthRequest;
import com.dfa.vinatrip.services.account.AccountService;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by duonghd on 9/27/2017.
 */

public class SignInPresenter extends BasePresenter<SignInView> {
    private AccountService accountService;
    private Subscription subscription;

    @Inject
    public SignInPresenter(EventBus eventBus, AccountService accountService) {
        super(eventBus);
        this.accountService = accountService;
    }

    public void loginWithEmail(AuthRequest authRequest) {
        RxScheduler.onStop(subscription);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscription = accountService.login(authRequest)
                .compose(RxScheduler.applyIoSchedulers())
                .subscribe(user -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        getView().signInSuccess(user);
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        getView().signInFail(throwable);
                    }
                });
    }
}
