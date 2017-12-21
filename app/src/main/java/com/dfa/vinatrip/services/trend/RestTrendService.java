package com.dfa.vinatrip.services.trend;

import com.beesightsoft.caf.services.common.RestMessageResponse;
import com.dfa.vinatrip.domains.main.fragment.trend.Trend;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by duytq on 10/14/2017.
 */

public interface RestTrendService {
    @GET("trend")
    Observable<RestMessageResponse<List<Trend>>> getTrend(
            @Query("where") String where,
            @Query("season") int season,
            @Query("type") int type,
            @Query("page") int page,
            @Query("pageSize") int pageSize
    );

    @PUT("trend/updateCount")
    Observable<RestMessageResponse<String>> updateTrendCount(
            @Body Trend trendUpdate
    );
}
