package com.dfa.vinatrip.domains.province_detail.view_all.hotel.hotel_detail;

import com.beesightsoft.caf.services.schedulers.RxScheduler;
import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.models.request.AuthRequest;
import com.dfa.vinatrip.models.request.FeedbackRequest;
import com.dfa.vinatrip.models.response.user.User;
import com.dfa.vinatrip.services.account.AccountService;
import com.dfa.vinatrip.services.feedback.FeedbackService;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by duonghd on 10/7/2017.
 * duonghd1307@gmail.com
 */

public class HotelDetailPresenter extends BasePresenter<HotelDetailView> {
    private AccountService accountService;
    private FeedbackService feedbackService;
    private Subscription subscription;

    @Inject
    public HotelDetailPresenter(EventBus eventBus, AccountService accountService, FeedbackService feedbackService) {
        super(eventBus);
        this.accountService = accountService;
        this.feedbackService = feedbackService;
    }

    public User getCurrentUser() {
        return accountService.getCurrentUser();
    }

    public void sendFeedback(int hotelId, FeedbackRequest feedbackRequest) {
        RxScheduler.onStop(subscription);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscription = feedbackService.postHotelFeedback(accountService.getCurrentUser().getAccessToken(), hotelId, feedbackRequest)
                .compose(RxScheduler.applyIoSchedulers())
                .doOnTerminate(() -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                    }
                })
                .subscribe(feedbackResponse -> {
                    if (isViewAttached()) {
                        feedbackResponse.setProfile(accountService.getCurrentUser());
                        getView().postHotelFeedbackSuccess(feedbackResponse);
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().apiError(throwable);
                    }
                });
    }

    public void getHotelFeedback(int hotelId, int page, int per_page) {
        RxScheduler.onStop(subscription);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscription = feedbackService.getHotelFeedback(hotelId, page, per_page)
                .compose(RxScheduler.applyIoSchedulers())
                .doOnTerminate(() -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                    }
                })
                .subscribe(feedbackResponses -> {
                    if (isViewAttached()) {
                        getView().getHotelFeedbackSuccess(feedbackResponses);
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().apiError(throwable);
                    }
                });
    }

    public void loginWithEmail(AuthRequest authRequest) {
        RxScheduler.onStop(subscription);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscription = accountService.login(authRequest)
                .compose(RxScheduler.applyIoSchedulers())
                .doOnTerminate(() -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                    }
                }).subscribe(user -> {
                    if (isViewAttached()) {
                        getView().signInSuccess(user);
                        EventBus.getDefault().post(user);

                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().apiError(throwable);
                    }
                });
    }
}
