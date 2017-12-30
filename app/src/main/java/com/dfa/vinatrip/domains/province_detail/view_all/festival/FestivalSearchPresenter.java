package com.dfa.vinatrip.domains.province_detail.view_all.festival;

import com.beesightsoft.caf.services.schedulers.RxScheduler;
import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.services.default_data.DataService;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by duonghd on 12/29/2017.
 * duonghd1307@gmail.com
 */

public class FestivalSearchPresenter extends BasePresenter<FestivalSearchView>{

    private DataService dataService;
    private Subscription subscription;

    @Inject
    public FestivalSearchPresenter(EventBus eventBus, DataService dataService) {
        super(eventBus);
        this.dataService = dataService;
    }

    public void getFestivals(int province_id, long page, long per_page) {
        RxScheduler.onStop(subscription);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscription = dataService.getFestivals(province_id, page, per_page)
                .compose(RxScheduler.applyIoSchedulers())
                .doOnTerminate(() -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                    }
                }).subscribe(placeResponses -> {
                    if (isViewAttached()) {
                        getView().getFestivalsSuccess(placeResponses);
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().apiError(throwable);
                    }
                });
    }
}
