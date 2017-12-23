package com.dfa.vinatrip.domains.chat;

import com.beesightsoft.caf.services.schedulers.RxScheduler;
import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.models.response.user.User;
import com.dfa.vinatrip.services.account.AccountService;
import com.dfa.vinatrip.services.chat.ChatService;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by duytq on 09/28/2017.
 */

public class ChatGroupPresenter extends BasePresenter<ChatGroupView> {

    private Subscription subscriptionGetHistory;
    private ChatService chatService;
    private AccountService accountService;

    @Inject
    public ChatGroupPresenter(EventBus eventBus, ChatService chatService, AccountService accountService) {
        super(eventBus);
        this.chatService = chatService;
        this.accountService = accountService;
    }

    public User getCurrentUser() {
        return accountService.getCurrentUser();
    }

    public void getHistory(long groupId, int page, int pageSize) {
        RxScheduler.onStop(subscriptionGetHistory);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscriptionGetHistory = chatService.getHistory(groupId, page, pageSize)
                .compose(RxScheduler.applyIoSchedulers())
                .subscribe(baseMessageList -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        getView().getHistorySuccess(baseMessageList, page);
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        getView().apiError(throwable);
                    }
                });
    }
}
