package com.dfa.vinatrip.domains.province_detail.view_all.hotel.hotel_detail;

import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.models.response.user.User;
import com.dfa.vinatrip.services.account.AccountService;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

/**
 * Created by duonghd on 10/7/2017.
 */

public class HotelDetailPresenter extends BasePresenter<HotelDetailView> {
    private AccountService accountService;
    
    @Inject
    public HotelDetailPresenter(EventBus eventBus, AccountService accountService) {
        super(eventBus);
        this.accountService = accountService;
    }
    
    public User getCurrentUser() {
        return accountService.getCurrentUser();
    }
    
    public void sendFeedback() {
    
    }
}
