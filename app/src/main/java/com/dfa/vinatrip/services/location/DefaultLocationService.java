package com.dfa.vinatrip.services.location;

import com.beesightsoft.caf.services.network.NetworkProvider;
import com.dfa.vinatrip.domains.main.location_my_friend.UserLocation;
import com.dfa.vinatrip.services.filter.ApiErrorFilter;

import java.util.List;

import rx.Observable;

/**
 * Created by duytq on 10/8/2017.
 */

public class DefaultLocationService implements LocationService {

    private RestLocationService restLocationService;
    private NetworkProvider networkProvider;
    private ApiErrorFilter apiErrorFilter;

    public DefaultLocationService(RestLocationService restLocationService, NetworkProvider networkProvider,
                                  ApiErrorFilter apiErrorFilter) {
        this.restLocationService = restLocationService;
        this.networkProvider = networkProvider;
        this.apiErrorFilter = apiErrorFilter;
    }

    @Override
    public Observable<List<UserLocation>> getLastLocation(String groupId) {
        return networkProvider
                .transformResponse(restLocationService.getLastLocation(groupId))
                .compose(apiErrorFilter.execute());
    }
}
