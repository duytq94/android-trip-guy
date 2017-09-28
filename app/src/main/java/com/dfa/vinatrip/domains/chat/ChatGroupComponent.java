package com.dfa.vinatrip.domains.chat;

import android.app.Activity;

import com.beesightsoft.caf.infrastructures.scope.ActivityScope;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.infrastructures.ApplicationComponent;

import dagger.Component;

/**
 * Created by duytq on 09/28/2017.
 */

@ActivityScope
@Component(modules = ActivityModule.class, dependencies = ApplicationComponent.class)
public interface ChatGroupComponent {
    Activity activity();

    void inject(ChatGroupActivity activity);
}
