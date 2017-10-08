package com.dfa.vinatrip.domains.location;

import com.beesightsoft.caf.services.schedulers.RxScheduler;
import com.dfa.vinatrip.base.BasePresenter;
import com.dfa.vinatrip.services.location.LocationService;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by duytq on 10/8/2017.
 */

public class LocationGroupPresenter extends BasePresenter<LocationGroupView> {

    private Subscription subscriptionGetLastLocation;
    private LocationService locationService;

    @Inject
    public LocationGroupPresenter(EventBus eventBus, LocationService locationService) {
        super(eventBus);
        this.locationService = locationService;
    }

    public void getLastLocation(String groupId) {
        RxScheduler.onStop(subscriptionGetLastLocation);
        if (isViewAttached()) {
            getView().showLoading();
        }
        subscriptionGetLastLocation = locationService.getLastLocation(groupId)
                .compose(RxScheduler.applyIoSchedulers())
                .subscribe(userLocationList -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        getView().getLastLocationSuccess(userLocationList);
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().hideLoading();
                        getView().getDataFail(throwable);
                    }
                });
    }
}
