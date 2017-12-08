package com.dfa.vinatrip.domains.province_detail;

import com.beesightsoft.caf.services.schedulers.RxScheduler;
import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.services.default_data.DataService;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by duonghd on 10/6/2017.
 */

public class ProvinceDetailPresenter extends BasePresenter<ProvinceDetailView> {
    private DataService dataService;
    private Subscription subscription1;
    private Subscription subscription2;
    private Subscription subscription3;
    private Subscription subscription4;
    private Subscription subscription5;
    
    @Inject
    public ProvinceDetailPresenter(EventBus eventBus, DataService dataService) {
        super(eventBus);
        this.dataService = dataService;
    }
    
    public void getEvents(int province_id, long page, long per_page) {
        RxScheduler.onStop(subscription1);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscription1 = dataService.getEvents(province_id, page, per_page)
                .compose(RxScheduler.applyIoSchedulers())
                .doOnTerminate(() -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                    }
                }).subscribe(eventResponses -> {
                    if (isViewAttached()) {
                        getView().getEventsSuccess(eventResponses);
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().apiError(throwable);
                    }
                });
    }
    
    public void getHotels(int province_id, long page, long per_page) {
        RxScheduler.onStop(subscription2);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscription2 = dataService.getHotels(province_id, page, per_page)
                .compose(RxScheduler.applyIoSchedulers())
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
        RxScheduler.onStop(subscription3);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscription3 = dataService.getFoods(province_id, page, per_page)
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
    
    public void getPlaces(int province_id, long page, long per_page) {
        RxScheduler.onStop(subscription4);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscription4 = dataService.getPlaces(province_id, page, per_page)
                .compose(RxScheduler.applyIoSchedulers())
                .doOnTerminate(() -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                    }
                }).subscribe(placeResponses -> {
                    if (isViewAttached()) {
                        getView().getPlacesSuccess(placeResponses);
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().apiError(throwable);
                    }
                });
    }
    
    public void getImages(int province_id, long page, long per_page) {
        RxScheduler.onStop(subscription5);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscription5 = dataService.getImages(province_id, page, per_page)
                .compose(RxScheduler.applyIoSchedulers())
                .doOnTerminate(() -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                    }
                }).subscribe(provinceImageResponses -> {
                    if (isViewAttached()) {
                        getView().getImagesSuccess(provinceImageResponses);
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().apiError(throwable);
                    }
                });
    }
}
