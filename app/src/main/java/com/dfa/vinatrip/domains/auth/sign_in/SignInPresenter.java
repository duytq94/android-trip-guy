package com.dfa.vinatrip.domains.auth.sign_in;

import android.util.Log;

import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.models.request.AuthRequest;
import com.dfa.vinatrip.services.account.AccountService;
import com.dfa.vinatrip.utils.RxHelper;

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
        RxHelper.onStop(subscription);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscription = accountService.login(authRequest)
                .compose(RxHelper.applyIOSchedulers())
                .subscribe(user -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        Log.e("login", "success");
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        Log.e("login", "fail | " + throwable);
                    }
                });
    }
}
