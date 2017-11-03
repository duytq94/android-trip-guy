package com.dfa.vinatrip.domains.auth.sign_up;

import com.beesightsoft.caf.services.schedulers.RxScheduler;
import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.models.request.AuthRequest;
import com.dfa.vinatrip.services.account.AccountService;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by duytq on 10/30/2017.
 */

public class SignUpPresenter extends BasePresenter<SignUpView> {
    private AccountService accountService;
    private Subscription subscription;

    @Inject
    public SignUpPresenter(EventBus eventBus, AccountService accountService) {
        super(eventBus);
        this.accountService = accountService;
    }

    public void signUp(AuthRequest authRequest) {
        RxScheduler.onStop(subscription);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscription = accountService.signUp(authRequest)
                .compose(RxScheduler.applyIoSchedulers())
                .subscribe(user -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        accountService.setCurrentUser(user);
                        getView().signUpSuccess(user);
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        getView().apiError(throwable);
                    }
                });
    }
}
