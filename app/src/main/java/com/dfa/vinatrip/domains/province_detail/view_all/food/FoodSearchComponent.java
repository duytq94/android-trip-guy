package com.dfa.vinatrip.domains.province_detail.view_all.food;

import android.app.Activity;

import com.beesightsoft.caf.infrastructures.scope.ActivityScope;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.infrastructures.ApplicationComponent;

import dagger.Component;

/**
 * Created by duonghd on 10/13/2017.
 */

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface FoodSearchComponent {
    Activity activity();
    
    void inject(FoodSearchActivity foodSearchActivity);
}
