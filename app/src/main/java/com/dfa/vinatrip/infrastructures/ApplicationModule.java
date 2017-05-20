package com.dfa.vinatrip.infrastructures;

import android.app.Application;

import dagger.Provides;

/**
 * Created by DFA on 5/20/2017.
 */

public class ApplicationModule {
    private Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @ApplicationScope
    Application provideApplication() {return application;}
}
