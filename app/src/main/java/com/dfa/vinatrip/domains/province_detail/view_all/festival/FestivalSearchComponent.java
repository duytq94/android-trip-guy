package com.dfa.vinatrip.domains.province_detail.view_all.festival;

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
public interface FestivalSearchComponent {
    Activity activity();

    void inject(FestivalSearchActivity festivalSearchActivity);
}
