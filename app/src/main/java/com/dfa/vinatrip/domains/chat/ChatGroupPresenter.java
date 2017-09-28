package com.dfa.vinatrip.domains.chat;

import com.beesightsoft.caf.services.schedulers.RxScheduler;
import com.dfa.vinatrip.base.BasePresenter;
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

    @Inject
    public ChatGroupPresenter(EventBus eventBus, ChatService chatService) {
        super(eventBus);
        this.chatService = chatService;
    }

    public void getHistory(long groupId) {
        RxScheduler.onStop(subscriptionGetHistory);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscriptionGetHistory = chatService.getHistory(groupId)
                .compose(RxScheduler.applyIoSchedulers())
                .subscribe(baseMessageList -> {
                    if (isViewAttached()) {
                        getView().getHistorySuccess(baseMessageList);
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().getDataFail(throwable);
                    }
                });
    }
}
