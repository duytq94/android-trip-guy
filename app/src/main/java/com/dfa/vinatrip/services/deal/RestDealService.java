package com.dfa.vinatrip.services.deal;

import com.beesightsoft.caf.services.common.RestMessageResponse;
import com.dfa.vinatrip.domains.main.fragment.deal.Deal;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by duytq on 10/14/2017.
 */

public interface RestDealService {
    @GET("deal")
    Observable<RestMessageResponse<List<Deal>>> getDeal(
            @Query("where") String where,
            @Query("priceMin") float priceMin,
            @Query("priceMax") float priceMax,
            @Query("dayMin") int dayMin,
            @Query("dayMax") int dayMax,
            @Query("page") int page,
            @Query("pageSize") int pageSize
    );
}
