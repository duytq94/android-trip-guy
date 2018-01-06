package com.dfa.vinatrip.domains.main.fragment.me.detail_me.my_rating;

import com.beesightsoft.caf.services.schedulers.RxScheduler;
import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.models.response.user.User;
import com.dfa.vinatrip.services.account.AccountService;
import com.dfa.vinatrip.services.feedback.FeedbackService;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by duonghd on 1/6/2018.
 * duonghd1307@gmail.com
 */

public class MyRatingPresenter extends BasePresenter<MyRatingView> {
    private Subscription subscription;
    private FeedbackService feedbackService;
    private AccountService accountService;
    private String token;

    @Inject
    public MyRatingPresenter(EventBus eventBus, FeedbackService feedbackService, AccountService accountService) {
        super(eventBus);
        this.feedbackService = feedbackService;
        this.accountService = accountService;
        this.token = accountService.getCurrentUser().getAccessToken();
    }

    public User getUser() {
        return accountService.getCurrentUser();
    }

    public void getMyFeedback() {
        RxScheduler.onStop(subscription);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscription = feedbackService.getMyFeedback(token, 0, 0)
                .compose(RxScheduler.applyIoSchedulers())
                .doOnTerminate(() -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                    }
                }).subscribe(feedbackResponses -> {
                    if (isViewAttached()) {
                        getView().getMyFeedbackSuccess(feedbackResponses);
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().apiError(throwable);
                    }
                });
    }
}
