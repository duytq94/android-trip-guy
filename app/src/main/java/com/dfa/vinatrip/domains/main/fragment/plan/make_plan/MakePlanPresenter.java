package com.dfa.vinatrip.domains.main.fragment.plan.make_plan;

import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.models.response.User;
import com.dfa.vinatrip.services.account.AccountService;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

/**
 * Created by duytq on 10/30/2017.
 */

public class MakePlanPresenter extends BasePresenter<MakePlanView> {

    private AccountService accountService;

    @Inject
    public MakePlanPresenter(EventBus eventBus, AccountService accountService) {
        super(eventBus);
        this.accountService = accountService;
    }

    public User getCurrentUser() {
        return accountService.getCurrentUser();
    }

}
