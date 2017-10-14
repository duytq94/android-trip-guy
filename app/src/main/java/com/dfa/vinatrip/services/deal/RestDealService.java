package com.dfa.vinatrip.services.deal;

import com.beesightsoft.caf.services.common.RestMessageResponse;
import com.dfa.vinatrip.models.response.Deal;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by duytq on 10/14/2017.
 */

public interface RestDealService {
    @GET("deal/{where}")
    Observable<RestMessageResponse<List<Deal>>> getDeal(
            @Path("where") String where,
            @Query("page") int page,
            @Query("pageSize") int pageSize
    );
}
