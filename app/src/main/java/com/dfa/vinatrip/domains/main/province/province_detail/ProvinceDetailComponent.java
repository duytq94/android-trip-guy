package com.dfa.vinatrip.domains.main.province.province_detail;

import android.app.Activity;

import com.beesightsoft.caf.infrastructures.scope.ActivityScope;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.infrastructures.ApplicationComponent;

import dagger.Component;

/**
 * Created by duonghd on 10/6/2017.
 */

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ProvinceDetailComponent {
    Activity activity();
    
    void inject(ProvinceDetailActivity provinceDetailActivity);
}
