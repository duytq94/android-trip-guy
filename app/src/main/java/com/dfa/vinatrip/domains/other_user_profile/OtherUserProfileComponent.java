package com.dfa.vinatrip.domains.other_user_profile;

import android.app.Activity;

import com.beesightsoft.caf.infrastructures.scope.ActivityScope;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.infrastructures.ApplicationComponent;

import dagger.Component;

/**
 * Created by duonghd on 1/5/2018.
 * duonghd1307@gmail.com
 */

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface OtherUserProfileComponent {
    Activity activity();

    void inject(OtherUserProfileActivity otherUserProfileActivity);
}
