package com.dfa.vinatrip.domains.auth.reset_password;

import android.app.Activity;

import com.beesightsoft.caf.infrastructures.scope.ActivityScope;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.infrastructures.ApplicationComponent;

import dagger.Component;

/**
 * Created by duonghd on 1/2/2018.
 * duonghd1307@gmail.com
 */

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ResetPasswordComponent {
    Activity activity();

    void inject(ResetPasswordActivity resetPasswordActivity);
}
