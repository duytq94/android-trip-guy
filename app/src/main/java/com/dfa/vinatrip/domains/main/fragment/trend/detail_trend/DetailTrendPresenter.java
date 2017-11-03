package com.dfa.vinatrip.domains.main.fragment.trend.detail_trend;

import com.beesightsoft.caf.services.schedulers.RxScheduler;
import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.domains.main.fragment.trend.Trend;
import com.dfa.vinatrip.services.trend.TrendService;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by duytq on 10/20/2017.
 */

public class DetailTrendPresenter extends BasePresenter<DetailTrendView> {

    private Subscription subscriptionUpdateTrend;
    private TrendService trendService;

    @Inject
    public DetailTrendPresenter(EventBus eventBus, TrendService trendService) {
        super(eventBus);
        this.trendService = trendService;
    }

    public void updateTrendCount(Trend trendUpdate) {
        RxScheduler.onStop(subscriptionUpdateTrend);
        subscriptionUpdateTrend = trendService.updateTrendCount(trendUpdate)
                .compose(RxScheduler.applyIoSchedulers())
                .subscribe(str -> {
                    if (isViewAttached()) {
                        getView().updateTrendCountSuccess(str);
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().apiError(throwable);
                    }
                });
    }
}
