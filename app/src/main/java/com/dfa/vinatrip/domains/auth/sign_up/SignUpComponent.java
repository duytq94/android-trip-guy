package com.dfa.vinatrip.domains.auth.sign_up;

import android.app.Activity;

import com.beesightsoft.caf.infrastructures.scope.ActivityScope;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.infrastructures.ApplicationComponent;

import dagger.Component;

/**
 * Created by duytq on 10/30/2017.
 */

@ActivityScope
@Component(modules = ActivityModule.class, dependencies = ApplicationComponent.class)
public interface SignUpComponent {
    Activity activity();

    void inject(SignUpActivity activity);
}
