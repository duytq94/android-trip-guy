package com.dfa.vinatrip.domains.main.fragment.province.province_detail.view_all.hotel.hotel_detail;

import com.dfa.vinatrip.base.BasePresenter;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

/**
 * Created by duonghd on 10/7/2017.
 */

public class HotelDetailPresenter extends BasePresenter<HotelDetailView> {

    @Inject
    public HotelDetailPresenter(EventBus eventBus) {
        super(eventBus);
    }
}
