package com.dfa.vinatrip.services.feedback;

import com.beesightsoft.caf.services.common.RestMessageResponse;
import com.dfa.vinatrip.models.response.feedback.FeedbackResponse;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by duonghd on 12/7/2017.
 */

public interface RestFeedbackService {
    @GET("api/feedback/hotel/{id}/listing")
    Observable<RestMessageResponse<List<FeedbackResponse>>> getHotelFeedback(
            @Path("id") long hotelId,
            @Query("page") long page,
            @Query("per_page") long per_page);
}
