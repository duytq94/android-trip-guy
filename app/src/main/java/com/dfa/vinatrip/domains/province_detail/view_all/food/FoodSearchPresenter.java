package com.dfa.vinatrip.domains.province_detail.view_all.food;

import com.beesightsoft.caf.services.schedulers.RxScheduler;
import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.services.default_data.DataService;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import rx.Subscription;

public class FoodSearchPresenter extends BasePresenter<FoodSearchView> {
    private DataService dataService;
    private Subscription subscription;
    
    @Inject
    public FoodSearchPresenter(EventBus eventBus, DataService dataService) {
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
