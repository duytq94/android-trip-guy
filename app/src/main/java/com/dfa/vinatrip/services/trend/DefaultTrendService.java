package com.dfa.vinatrip.services.trend;

import com.beesightsoft.caf.services.network.NetworkProvider;
import com.dfa.vinatrip.domains.main.fragment.trend.Trend;
import com.dfa.vinatrip.services.filter.ApiErrorFilter;

import java.util.List;

import rx.Observable;

/**
 * Created by duytq on 10/14/2017.
 */

public class DefaultTrendService implements TrendService {

    private RestTrendService restTrendService;
    private NetworkProvider networkProvider;
    private ApiErrorFilter apiErrorFilter;

    public DefaultTrendService(RestTrendService restTrendService, NetworkProvider networkProvider,
                               ApiErrorFilter apiErrorFilter) {
        this.restTrendService = restTrendService;
        this.networkProvider = networkProvider;
        this.apiErrorFilter = apiErrorFilter;
    }

    @Override
    public Observable<List<Trend>> getTrend(String where, int season, int type, int page, int pageSize) {
        return networkProvider
                .transformResponse(restTrendService.getTrend(where, season, type, page, pageSize))
                .compose(apiErrorFilter.execute());
    }

    @Override
    public Observable<String> updateTrendCount(Trend trendUpdate) {
        return networkProvider
                .transformResponse(restTrendService.updateTrendCount(trendUpdate))
                .compose(apiErrorFilter.execute());
    }
}
