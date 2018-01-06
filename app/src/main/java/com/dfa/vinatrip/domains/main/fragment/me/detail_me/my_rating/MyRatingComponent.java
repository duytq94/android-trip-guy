package com.dfa.vinatrip.domains.main.fragment.me.detail_me.my_rating;

import android.app.Activity;

import com.beesightsoft.caf.infrastructures.scope.ActivityScope;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.infrastructures.ApplicationComponent;

import dagger.Component;

/**
 * Created by duonghd on 1/6/2018.
 * duonghd1307@gmail.com
 */

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface MyRatingComponent {
    Activity activity();

    void inject(MyRatingFragment myRatingFragment);
}
