package com.dfa.vinatrip.domains.main.fragment.province.province_detail;

import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.services.default_data.DataService;
import com.dfa.vinatrip.utils.RxHelper;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by duonghd on 10/6/2017.
 */

public class ProvinceDetailPresenter extends BasePresenter<ProvinceDetailView> {
    private DataService dataService;
    private Subscription subscription;
    private Subscription subscription2;
    
    @Inject
    public ProvinceDetailPresenter(EventBus eventBus, DataService dataService) {
        super(eventBus);
        this.dataService = dataService;
    }
    
    public void getHotels(int province_id, long page, long per_page) {
        RxHelper.onStop(subscription);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscription = dataService.getHotels(province_id, page, per_page)
                .compose(RxHelper.applyIOSchedulers())
                .doOnTerminate(() -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                    }
                }).subscribe(hotelResponses -> {
                    if (isViewAttached()) {
                        getView().getHotelsSuccess(hotelResponses);
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().apiError(throwable);
                    }
                });
    }
    
    public void getFoods(int province_id, long page, long per_page) {
        RxHelper.onStop(subscription2);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscription2 = dataService.getFoods(province_id, page, per_page)
                .compose(RxHelper.applyIOSchedulers())
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
