package com.dfa.vinatrip.domains.main.province.province_detail.fragment.hotel.hotel_detail;

import android.app.Activity;

import com.beesightsoft.caf.infrastructures.scope.ActivityScope;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.infrastructures.ApplicationComponent;

import dagger.Component;

/**
 * Created by duonghd on 10/7/2017.
 */

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface HotelDetailComponent {
    Activity activity();

    void inject(HotelDetailActivity hotelDetailActivity);
}
