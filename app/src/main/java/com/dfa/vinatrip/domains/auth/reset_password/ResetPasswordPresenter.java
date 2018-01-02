package com.dfa.vinatrip.domains.auth.reset_password;

import com.beesightsoft.caf.services.schedulers.RxScheduler;
import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.models.request.ResetPasswordRequest;
import com.dfa.vinatrip.services.account.AccountService;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by duonghd on 1/2/2018.
 * duonghd1307@gmail.com
 */

public class ResetPasswordPresenter extends BasePresenter<ResetPasswordView> {
    private AccountService accountService;
    private Subscription subscription;

    @Inject
    public ResetPasswordPresenter(EventBus eventBus, AccountService accountService) {
        super(eventBus);
        this.accountService = accountService;
    }

    public void sendResetPassword(String email) {
        if (isViewAttached()) {
            getView().showLoading();
        }
        RxScheduler.onStop(subscription);
        subscription = accountService.resetPassword(new ResetPasswordRequest(email))
                .compose(RxScheduler.applyIoSchedulers())
                .doOnTerminate(() -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                    }
                })
                .subscribe(s -> {
                    if (isViewAttached()) {
                        getView().sendResetSuccess(s);
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().apiError(throwable);
                    }
                });
    }
}
