package com.dfa.vinatrip.domains.main.fragment.me.detail_me.update_profile;

import com.beesightsoft.caf.services.schedulers.RxScheduler;
import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.models.response.user.User;
import com.dfa.vinatrip.services.account.AccountService;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by duytq on 10/31/2017.
 */

public class UpdateUserProfilePresenter extends BasePresenter<UpdateUserProfileView> {

    private AccountService accountService;
    private Subscription subscription;

    @Inject
    public UpdateUserProfilePresenter(EventBus eventBus, AccountService accountService) {
        super(eventBus);
        this.accountService = accountService;
    }

    public User getCurrentUser() {
        return accountService.getCurrentUser();
    }

    public void setCurrentUser(User newUser) {
        accountService.setCurrentUser(newUser);
    }

    public void editProfile(User user) {
        RxScheduler.onStop(subscription);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscription = accountService.editProfile(user)
                .compose(RxScheduler.applyIoSchedulers())
                .subscribe(newUser -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        getView().editProfileSuccess(newUser);
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        getView().apiError(throwable);
                    }
                });
    }
}
