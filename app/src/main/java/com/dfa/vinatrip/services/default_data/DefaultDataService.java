package com.dfa.vinatrip.services.default_data;

import com.beesightsoft.caf.services.network.NetworkProvider;
import com.dfa.vinatrip.models.response.Province;

import java.util.List;

import rx.Observable;

/**
 * Created by duonghd on 10/6/2017.
 */

public class DefaultDataService implements DataService {
    private NetworkProvider networkProvider;
    private RestDataService restDataService;
    
    public DefaultDataService(NetworkProvider networkProvider, RestDataService restDataService) {
        this.networkProvider = networkProvider;
        this.restDataService = restDataService;
    }
    
    @Override
    public Observable<List<Province>> getProvinces(long page, long per_page) {
        return networkProvider.transformResponse(restDataService.getProvinces(page, per_page));
    }
}
