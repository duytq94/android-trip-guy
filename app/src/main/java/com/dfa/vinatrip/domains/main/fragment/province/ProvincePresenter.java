package com.dfa.vinatrip.domains.main.fragment.province;

import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.services.default_data.DataService;
import com.dfa.vinatrip.utils.RxHelper;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by duonghd on 10/6/2017.
 */

public class ProvincePresenter extends BasePresenter<ProvinceView> {
    private DataService dataService;
    private Subscription subscription;
    
    @Inject
    public ProvincePresenter(EventBus eventBus, DataService dataService) {
        super(eventBus);
        this.dataService = dataService;
    }
    
    public void getProvince(long page, long per_page) {
        RxHelper.onStop(subscription);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscription = dataService.getProvinces(page, per_page)
                .compose(RxHelper.applyIOSchedulers())
                .doOnTerminate(() -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                    }
                })
                .subscribe(provinces -> {
                    if (isViewAttached()) {
                        getView().getProvinceSuccess(provinces);
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().apiError(throwable);
                    }
                });
    }
}
