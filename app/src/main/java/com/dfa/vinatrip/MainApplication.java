package com.dfa.vinatrip;

import android.app.Application;

import com.dfa.vinatrip.infrastructures.ApplicationComponent;
import com.dfa.vinatrip.infrastructures.ApplicationModule;
import com.dfa.vinatrip.infrastructures.DaggerApplicationComponent;
import com.dfa.vinatrip.utils.AppUtil;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;

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
    
        AppUtil.init(this);
    
        Hawk.init(this)
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setStorage(HawkBuilder.newSharedPrefStorage(this)).build();
        
        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
