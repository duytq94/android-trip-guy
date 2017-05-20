package com.dfa.vinatrip;

import android.app.Application;

import com.dfa.vinatrip.infrastructures.ApplicationComponent;

import org.androidannotations.annotations.EApplication;

/**
 * Created by DFA on 5/20/2017.
 */

@EApplication
public class MainApplication extends Application {

    private ApplicationComponent applicationComponent;

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    public void setApplicationComponent(ApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
    }
}
