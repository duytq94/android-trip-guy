package com.dfa.vinatrip.services.deal;

import com.beesightsoft.caf.services.network.NetworkProvider;
import com.dfa.vinatrip.domains.main.fragment.deal.Deal;
import com.dfa.vinatrip.services.filter.ApiErrorFilter;

import java.util.List;

import rx.Observable;

/**
 * Created by duytq on 10/14/2017.
 */

public class DefaultDealService implements DealService {

    private RestDealService restDealService;
    private NetworkProvider networkProvider;
    private ApiErrorFilter apiErrorFilter;

    public DefaultDealService(RestDealService restDealService, NetworkProvider networkProvider,
                              ApiErrorFilter apiErrorFilter) {
        this.restDealService = restDealService;
        this.networkProvider = networkProvider;
        this.apiErrorFilter = apiErrorFilter;
    }


    @Override
    public Observable<List<Deal>> getDeal(String where, int page, int pageSize) {
        return networkProvider
                .transformResponse(restDealService.getDeal(where, page, pageSize))
                .compose(apiErrorFilter.execute());
    }
}
