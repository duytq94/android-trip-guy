package com.dfa.vinatrip.domains.auth.sign_in;

import android.app.Activity;

import com.dfa.vinatrip.domains.auth.SignInActivity;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.infrastructures.ActivityScope;
import com.dfa.vinatrip.infrastructures.ApplicationComponent;

import dagger.Component;

/**
 * Created by duytq on 9/17/2017.
 */

@ActivityScope
@Component(modules = {ActivityModule.class},
        dependencies = ApplicationComponent.class)
public interface SignInComponent {
    Activity activity();

    void inject(SignInActivity activity);
}
