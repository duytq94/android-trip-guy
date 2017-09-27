package com.dfa.vinatrip.infrastructures;

import android.app.Activity;

import com.beesightsoft.caf.infrastructures.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by duytq on 9/17/2017.
 */

@ActivityScope
@Module
public class ActivityModule {
    private Activity activity;
    
    public ActivityModule(Activity activity) {
        this.activity = activity;
    }
    
    @Provides
    public Activity provideActivity() {
        return activity;
    }
}
