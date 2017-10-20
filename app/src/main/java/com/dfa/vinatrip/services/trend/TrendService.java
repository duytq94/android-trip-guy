package com.dfa.vinatrip.services.trend;

import com.dfa.vinatrip.domains.main.fragment.trend.Trend;

import java.util.List;

import rx.Observable;

/**
 * Created by duytq on 10/14/2017.
 */

public interface TrendService {
    Observable<List<Trend>> getTrend(int page, int pageSize);

    Observable<Trend> updateTrendCount(Trend trendUpdate);
}
