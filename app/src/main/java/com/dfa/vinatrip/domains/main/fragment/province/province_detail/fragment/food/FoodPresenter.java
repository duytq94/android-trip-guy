package com.dfa.vinatrip.domains.main.fragment.province.province_detail.fragment.food;

import com.beesightsoft.caf.services.schedulers.RxScheduler;
import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.services.default_data.DataService;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by duonghd on 10/13/2017.
 */

public class FoodPresenter extends BasePresenter<FoodView> {
    private DataService dataService;
    private Subscription subscription;
    
    @Inject
    public FoodPresenter(EventBus eventBus, DataService dataService) {
        super(eventBus);
        this.dataService = dataService;
    }
    
    public void getFoods(int province_id, long page, long per_page) {
        RxScheduler.onStop(subscription);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscription = dataService.getFoods(province_id, page, per_page)
                .compose(RxScheduler.applyIoSchedulers())
                .doOnTerminate(() -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                    }
                }).subscribe(foodResponses -> {
                    if (isViewAttached()) {
                        getView().getFoodsSuccess(foodResponses);
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().apiError(throwable);
                    }
                });
    }
}
