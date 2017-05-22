package com.dfa.vinatrip;

import android.app.Application;

import com.dfa.vinatrip.infrastructures.ApplicationComponent;
import com.dfa.vinatrip.infrastructures.ApplicationModule;
import com.dfa.vinatrip.infrastructures.DaggerApplicationComponent;

import org.androidannotations.annotations.EApplication;

/**
 * Created by DFA on 5/20/2017.
 */

@EApplication
public class MainApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();

    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    public void setApplicationComponent(ApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
    }
}
