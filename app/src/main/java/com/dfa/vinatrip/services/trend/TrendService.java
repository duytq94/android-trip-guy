package com.dfa.vinatrip.services.trend;

import com.dfa.vinatrip.models.response.place.Trend;

import java.util.List;

import rx.Observable;

/**
 * Created by duytq on 10/14/2017.
 */

public interface TrendService {
    Observable<List<Trend>> getTrend(int page, int pageSize);
}
