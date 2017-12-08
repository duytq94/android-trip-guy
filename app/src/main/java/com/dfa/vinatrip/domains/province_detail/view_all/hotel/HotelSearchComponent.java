package com.dfa.vinatrip.domains.province_detail.view_all.hotel;

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
public interface HotelSearchComponent {
    Activity activity();
    
    void inject(HotelSearchActivity hotelSearchActivity);
}
