package com.dfa.vinatrip.domains.main.fragment.province;

import com.beesightsoft.caf.services.schedulers.RxScheduler;
import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.services.default_data.DataService;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

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
    
    public void getBanner(){
        List<String> banners = new ArrayList<>();
        banners.add("http://internetmarketinginpanama.com/wp-content/uploads/2014/02/travel-banner.jpg");
        banners.add("http://dulichnamachau.vn/wp-content/uploads/2015/08/banner-travel-1.jpg");
        banners.add("http://vietnamtourism.gov.vn/english/images/2017/258banner-hoian-1280x600.jpg");
        banners.add("http://www.vietnamtourism.com/imguploads/news/2016/T1/vinhhalong.jpg");
        
        getView().getBannerSuccess(banners);
    }
    
    public void getProvince(long page, long per_page) {
        RxScheduler.onStop(subscription);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscription = dataService.getProvinces(page, per_page)
                .compose(RxScheduler.applyIoSchedulers())
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
