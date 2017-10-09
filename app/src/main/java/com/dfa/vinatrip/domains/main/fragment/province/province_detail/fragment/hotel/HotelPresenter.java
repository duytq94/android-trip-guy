package com.dfa.vinatrip.domains.main.fragment.province.province_detail.fragment.hotel;

import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.services.default_data.DataService;
import com.dfa.vinatrip.utils.RxHelper;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by duonghd on 10/7/2017.
 */

public class HotelPresenter extends BasePresenter<HotelView> {
    private DataService dataService;
    private Subscription subscription;

    @Inject
    public HotelPresenter(EventBus eventBus, DataService dataService) {
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
}
