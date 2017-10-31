package com.dfa.vinatrip.infrastructures;

import android.app.Application;

import com.beesightsoft.caf.infrastructures.scope.ApplicationScope;
import com.beesightsoft.caf.services.authentication.AuthenticationManagerConfiguration;
import com.beesightsoft.caf.services.log.DefaultLogService;
import com.beesightsoft.caf.services.log.LogService;
import com.beesightsoft.caf.services.network.DefaultNetworkProvider;
import com.beesightsoft.caf.services.network.NetworkProvider;
import com.dfa.vinatrip.ApiUrls;
import com.dfa.vinatrip.services.account.AccountService;
import com.dfa.vinatrip.services.account.DefaultAccountService;
import com.dfa.vinatrip.services.account.RestAccountService;
import com.dfa.vinatrip.services.chat.ChatService;
import com.dfa.vinatrip.services.chat.DefaultChatService;
import com.dfa.vinatrip.services.chat.RestChatService;
import com.dfa.vinatrip.services.deal.DealService;
import com.dfa.vinatrip.services.deal.DefaultDealService;
import com.dfa.vinatrip.services.deal.RestDealService;
import com.dfa.vinatrip.services.default_data.DataService;
import com.dfa.vinatrip.services.default_data.DefaultDataService;
import com.dfa.vinatrip.services.default_data.RestDataService;
import com.dfa.vinatrip.services.filter.ApiErrorFilter;
import com.dfa.vinatrip.services.friend.DefaultFriendService;
import com.dfa.vinatrip.services.friend.FriendService;
import com.dfa.vinatrip.services.friend.RestFriendService;
import com.dfa.vinatrip.services.location.DefaultLocationService;
import com.dfa.vinatrip.services.location.LocationService;
import com.dfa.vinatrip.services.location.RestLocationService;
import com.dfa.vinatrip.services.trend.DefaultTrendService;
import com.dfa.vinatrip.services.trend.RestTrendService;
import com.dfa.vinatrip.services.trend.TrendService;
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
    public NetworkProvider provideNetworkProvider(LogService logService) {
        NetworkProvider networkProvider = new DefaultNetworkProvider(application, true);
        networkProvider.enableFilter(true).addFilter(new ApiErrorFilter(networkProvider, logService));
        return networkProvider;
    }

    @Provides
    @ApplicationScope
    public ApiErrorFilter provideApiErrorFilter(NetworkProvider networkProvider, LogService logService) {
        return new ApiErrorFilter(networkProvider, logService);
    }

    @Provides
    @ApplicationScope
    public AccountService provideAccountService(NetworkProvider rxNetworkProvider, ApiErrorFilter apiErrorFilter) {
        RestAccountService restService = rxNetworkProvider.addDefaultHeader()
                .provideApi(ApiUrls.SERVER_API, RestAccountService.class);

        return new DefaultAccountService(AuthenticationManagerConfiguration.init()
                .useStorage(Constants.KEY_USER_AUTH), rxNetworkProvider, restService, apiErrorFilter);
    }

    @Provides
    @ApplicationScope
    public ChatService provideChatService(NetworkProvider rxNetworkProvider, ApiErrorFilter apiErrorFilter) {
        RestChatService restService = rxNetworkProvider.addDefaultHeader()
                .provideApi(ApiUrls.SERVER_API_CHAT, RestChatService.class);

        return new DefaultChatService(restService, rxNetworkProvider, apiErrorFilter);
    }

    @Provides
    @ApplicationScope
    public DealService provideDealService(NetworkProvider rxNetworkProvider, ApiErrorFilter apiErrorFilter) {
        RestDealService restService = rxNetworkProvider.addDefaultHeader()
                .provideApi(ApiUrls.SERVER_API_CHAT, RestDealService.class);

        return new DefaultDealService(restService, rxNetworkProvider, apiErrorFilter);
    }

    @Provides
    @ApplicationScope
    public TrendService provideTrendService(NetworkProvider rxNetworkProvider, ApiErrorFilter apiErrorFilter) {
        RestTrendService restService = rxNetworkProvider.addDefaultHeader()
                .provideApi(ApiUrls.SERVER_API_CHAT, RestTrendService.class);

        return new DefaultTrendService(restService, rxNetworkProvider, apiErrorFilter);
    }

    @Provides
    @ApplicationScope
    public LocationService provideLocationService(NetworkProvider rxNetworkProvider, ApiErrorFilter apiErrorFilter) {
        RestLocationService restLocationService = rxNetworkProvider.addDefaultHeader()
                .provideApi(ApiUrls.SERVER_API_CHAT, RestLocationService.class);

        return new DefaultLocationService(restLocationService, rxNetworkProvider, apiErrorFilter);
    }

    @Provides
    @ApplicationScope
    public DataService provideDataService(NetworkProvider rxNetworkProvider) {
        RestDataService restDataService = rxNetworkProvider.addDefaultHeader()
                .provideApi(ApiUrls.SERVER_API, RestDataService.class);

        return new DefaultDataService(rxNetworkProvider, restDataService);
    }

    @Provides
    @ApplicationScope
    public FriendService provideFriendService(AccountService accountService, NetworkProvider rxNetworkProvider, ApiErrorFilter apiErrorFilter) {
        RestFriendService restFriendService = rxNetworkProvider.addDefaultHeader()
                .provideApi(ApiUrls.SERVER_API, RestFriendService.class);

        return new DefaultFriendService(accountService, restFriendService, rxNetworkProvider, apiErrorFilter);
    }
}
