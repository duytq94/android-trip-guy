package com.dfa.vinatrip.domains.main.fragment.me.detail_me;

import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.models.response.user.User;
import com.dfa.vinatrip.services.account.AccountService;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

/**
 * Created by duytq on 10/31/2017.
 */

public class UserProfileDetailPresenter extends BasePresenter<UserProfileDetailView> {

    private AccountService accountService;

    @Inject
    public UserProfileDetailPresenter(EventBus eventBus, AccountService accountService) {
        super(eventBus);
        this.accountService = accountService;
    }

    public User getCurrentUser() {
        return accountService.getCurrentUser();
    }

}
