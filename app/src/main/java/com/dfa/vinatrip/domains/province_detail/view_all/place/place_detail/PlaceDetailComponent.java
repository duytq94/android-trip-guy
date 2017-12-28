package com.dfa.vinatrip.domains.province_detail.view_all.place.place_detail;

import android.app.Activity;

import com.beesightsoft.caf.infrastructures.scope.ActivityScope;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.infrastructures.ApplicationComponent;

import dagger.Component;

/**
 * Created by duonghd on 12/28/2017.
 */

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface PlaceDetailComponent {
    Activity activity();

    void inject(PlaceDetailActivity placeDetailActivity);
}
