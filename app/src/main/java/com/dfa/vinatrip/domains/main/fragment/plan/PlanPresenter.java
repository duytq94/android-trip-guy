package com.dfa.vinatrip.domains.main.fragment.plan;

import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.models.response.User;
import com.dfa.vinatrip.services.account.AccountService;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

/**
 * Created by duytq on 10/30/2017.
 */

public class PlanPresenter extends BasePresenter<PlanView> {

    private AccountService accountService;

    @Inject
    public PlanPresenter(EventBus eventBus, AccountService accountService) {
        super(eventBus);
        this.accountService = accountService;
    }

    public boolean isLogin() {
        return accountService.loadFromStorage();
    }

    public User getCurrentUser() {
        return accountService.getCurrentUser();
    }
}
