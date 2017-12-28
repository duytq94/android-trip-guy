package com.dfa.vinatrip.domains.province_detail.view_all.food.food_detail;

import android.app.Activity;

import com.beesightsoft.caf.infrastructures.scope.ActivityScope;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.infrastructures.ApplicationComponent;

import dagger.Component;

/**
 * Created by duonghd on 12/28/2017.
 * duonghd1307@gmail.com
 */

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface FoodDetailComponent {
    Activity activity();

    void inject(FoodDetailActivity foodDetailActivity);
}