package com.dfa.vinatrip.domains.main.fragment.trend;

import com.beesightsoft.caf.services.schedulers.RxScheduler;
import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.services.trend.TrendService;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by duytq on 10/14/2017.
 */

public class TrendPresenter extends BasePresenter<TrendView> {

    private Subscription subscriptionGetTrend;
    private TrendService trendService;

    @Inject
    public TrendPresenter(EventBus eventBus, TrendService trendService) {
        super(eventBus);
        this.trendService = trendService;
    }

    public void getTrend(String where, int season, int type, int page, int pageSize) {
        RxScheduler.onStop(subscriptionGetTrend);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscriptionGetTrend = trendService.getTrend(where, season, type, page, pageSize)
                .compose(RxScheduler.applyIoSchedulers())
                .subscribe(trendList -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        getView().getTrendSuccess(trendList, page);
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        getView().apiError(throwable);
                    }
                });
    }
}
