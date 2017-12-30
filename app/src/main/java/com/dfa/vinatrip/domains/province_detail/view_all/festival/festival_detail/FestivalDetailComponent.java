package com.dfa.vinatrip.domains.province_detail.view_all.festival.festival_detail;

import android.app.Activity;

import com.beesightsoft.caf.infrastructures.scope.ActivityScope;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.infrastructures.ApplicationComponent;

import dagger.Component;

/**
 * Created by duonghd on 12/29/2017.
 * duonghd1307@gmail.com
 */

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface FestivalDetailComponent {
    Activity activity();

    void inject(FestivalDetailActivity festivalDetailActivity);
}
