package com.dfa.vinatrip.domains.main.fragment.me.detail_me.friend_request;

import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.models.response.User;
import com.dfa.vinatrip.services.account.AccountService;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

/**
 * Created by duytq on 10/31/2017.
 */

public class FriendRequestPresenter extends BasePresenter<FriendRequestView> {

    private AccountService accountService;

    @Inject
    public FriendRequestPresenter(EventBus eventBus, AccountService accountService) {
        super(eventBus);
        this.accountService = accountService;
    }

    public User getCurrentUser() {
        return accountService.getCurrentUser();
    }

    public void getListUser() {

    }
}
