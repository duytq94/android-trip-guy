package com.dfa.vinatrip.domains.main.fragment.me.detail_me.make_friend;

import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.models.response.User;
import com.dfa.vinatrip.services.account.AccountService;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

/**
 * Created by duytq on 10/31/2017.
 */

public class MakeFriendPresenter extends BasePresenter<MakeFriendView> {

    private AccountService accountService;

    @Inject
    public MakeFriendPresenter(EventBus eventBus, AccountService accountService) {
        super(eventBus);
        this.accountService = accountService;
    }

    public User getCurrentUser() {
        return accountService.getCurrentUser();
    }

    public void getListUser() {

    }
}
