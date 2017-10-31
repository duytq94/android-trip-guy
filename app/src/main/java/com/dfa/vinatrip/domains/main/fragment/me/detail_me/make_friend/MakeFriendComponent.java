package com.dfa.vinatrip.domains.main.fragment.me.detail_me.make_friend;

import android.app.Activity;

import com.beesightsoft.caf.infrastructures.scope.ActivityScope;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.infrastructures.ApplicationComponent;

import dagger.Component;

/**
 * Created by duytq on 10/31/2017.
 */

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface MakeFriendComponent {
    Activity activity();

    void inject(MakeFriendFragment makeFriendFragment);
}
