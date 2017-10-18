package com.dfa.vinatrip.domains.main.fragment.deal;

import com.beesightsoft.caf.services.schedulers.RxScheduler;
import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.services.deal.DealService;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by duytq on 10/14/2017.
 */

public class DealPresenter extends BasePresenter<DealView> {

    private Subscription subscriptionGetDeal;
    private DealService dealService;

    @Inject
    public DealPresenter(EventBus eventBus, DealService dealService) {
        super(eventBus);
        this.dealService = dealService;
    }

    public void getDeal(String where, int page, int pageSize) {
        RxScheduler.onStop(subscriptionGetDeal);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscriptionGetDeal = dealService.getDeal(where, page, pageSize)
                .compose(RxScheduler.applyIoSchedulers())
                .subscribe(dealList -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        getView().getDealSuccess(dealList, page);
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        getView().getDataFail(throwable);
                    }
                });
    }
}
