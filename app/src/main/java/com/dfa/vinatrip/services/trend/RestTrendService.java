package com.dfa.vinatrip.services.trend;

import com.beesightsoft.caf.services.common.RestMessageResponse;
import com.dfa.vinatrip.models.response.place.Trend;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by duytq on 10/14/2017.
 */

public interface RestTrendService {
    @GET("trend")
    Observable<RestMessageResponse<List<Trend>>> getTrend(
            @Query("page") int page,
            @Query("pageSize") int pageSize
    );
}
