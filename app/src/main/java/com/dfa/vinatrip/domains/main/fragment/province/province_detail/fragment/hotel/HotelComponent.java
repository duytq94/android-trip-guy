package com.dfa.vinatrip.domains.main.fragment.province.province_detail.fragment.hotel;

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
public interface HotelComponent {
    Activity activity();

    void inject(HotelFragment hotelFragment);
}
