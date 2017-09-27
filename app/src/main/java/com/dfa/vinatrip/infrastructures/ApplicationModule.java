package com.dfa.vinatrip.infrastructures;

import android.app.Application;

import com.beesightsoft.caf.BuildConfig;
import com.beesightsoft.caf.infrastructures.scope.ApplicationScope;
import com.beesightsoft.caf.services.log.DefaultLogService;
import com.beesightsoft.caf.services.log.LogService;
import com.beesightsoft.caf.services.network.DefaultNetworkProvider;
import com.beesightsoft.caf.services.network.HttpLoggingInterceptor;
import com.beesightsoft.caf.services.network.NetworkProvider;
import com.dfa.vinatrip.ApiUrls;
import com.dfa.vinatrip.services.account.AccountService;
import com.dfa.vinatrip.services.account.DefaultAccountService;
import com.dfa.vinatrip.services.account.RestAccountService;
import com.dfa.vinatrip.utils.Constants;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import dagger.Module;
import dagger.Provides;

/**
 * Created by DFA on 5/20/2017.
 */

@ApplicationScope
@Module
public class ApplicationModule {
    private Application application;
    
    public ApplicationModule(Application application) {
        this.application = application;
    }
    
    @Provides
    @ApplicationScope
    public Application provideApplication() {
        return application;
    }
    
    @Provides
    @ApplicationScope
    public EventBus providesEventBus() {
        return EventBus.getDefault();
    }
    
    @Provides
    @ApplicationScope
    public NetworkProvider provideNetworkProvider() {
        return new DefaultNetworkProvider(application, BuildConfig.DEBUG) {
            @Override
            public HttpLoggingInterceptor.Level getLevel() {
                return HttpLoggingInterceptor.Level.BODY;
            }
        };
    }
    
    @Provides
    @ApplicationScope
    public LogService provideLogService() {
        DefaultLogService defaultLogService = new DefaultLogService();
        try {
            defaultLogService.init(application, Constants.LOGENTRIES_APP_KEY);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defaultLogService;
    }
    
    @Provides
    @ApplicationScope
    public AccountService provideAccountService(NetworkProvider rxNetworkProvider) {
        RestAccountService restService =
                provideNetworkProvider().addDefaultHeader()
                        .provideApi(ApiUrls.SERVER_URL, RestAccountService.class);
        
        return new DefaultAccountService(rxNetworkProvider, restService);
    }
}
