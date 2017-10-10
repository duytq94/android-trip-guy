package com.dfa.vinatrip.domains.main.province.province_detail;

import com.dfa.vinatrip.base.BasePresenter;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

/**
 * Created by duonghd on 10/6/2017.
 */

public class ProvinceDetailPresenter extends BasePresenter<ProvinceDetailView> {
    
    
    @Inject
    public ProvinceDetailPresenter(EventBus eventBus) {
        super(eventBus);
    }
}
