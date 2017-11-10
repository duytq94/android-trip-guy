package com.dfa.vinatrip.domains.main.fragment.province.each_item_detail_province.rating;

import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.models.response.user.User;
import com.dfa.vinatrip.services.account.AccountService;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

/**
 * Created by duytq on 10/31/2017.
 */

public class RatingPresenter extends BasePresenter<RatingView> {

    private AccountService accountService;

    @Inject
    public RatingPresenter(EventBus eventBus, AccountService accountService) {
        super(eventBus);
        this.accountService = accountService;
    }

    public User getCurrentUser() {
        return accountService.getCurrentUser();
    }

}
