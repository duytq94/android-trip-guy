package com.dfa.vinatrip.services.feedback;

import com.beesightsoft.caf.services.common.RestMessageResponse;
import com.dfa.vinatrip.models.request.FeedbackRequest;
import com.dfa.vinatrip.models.response.feedback.FeedbackResponse;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
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

    @GET("api/feedback/food/{id}/listing")
    Observable<RestMessageResponse<List<FeedbackResponse>>> getFoodFeedback(
            @Path("id") long foodId,
            @Query("page") long page,
            @Query("per_page") long per_page);

    @GET("api/feedback/place/{id}/listing")
    Observable<RestMessageResponse<List<FeedbackResponse>>> getPlaceFeedback(
            @Path("id") long placeId,
            @Query("page") long page,
            @Query("per_page") long per_page);

    @GET("api/feedback/event/{id}/listing")
    Observable<RestMessageResponse<List<FeedbackResponse>>> getFestivalFeedback(
            @Path("id") long eventId,
            @Query("page") long page,
            @Query("per_page") long per_page);

    @POST("api/feedback/hotel/{id}")
    Observable<RestMessageResponse<FeedbackResponse>> postHotelFeedback(
            @Header("access-token") String accessToken,
            @Path("id") long hotelId,
            @Body FeedbackRequest feedbackRequest);

    @POST("api/feedback/food/{id}")
    Observable<RestMessageResponse<FeedbackResponse>> postFoodFeedback(
            @Header("access-token") String accessToken,
            @Path("id") long foodId,
            @Body FeedbackRequest feedbackRequest);

    @POST("api/feedback/place/{id}")
    Observable<RestMessageResponse<FeedbackResponse>> postPlaceFeedback(
            @Header("access-token") String accessToken,
            @Path("id") long placeId,
            @Body FeedbackRequest feedbackRequest);

    @POST("api/feedback/event/{id}")
    Observable<RestMessageResponse<FeedbackResponse>> postFestivalFeedback(
            @Header("access-token") String accessToken,
            @Path("id") long eventId,
            @Body FeedbackRequest feedbackRequest);

    @GET("api/feedback/me")
    Observable<RestMessageResponse<List<FeedbackResponse>>> getMyFeedback(
            @Header("access-token") String accessToken,
            @Query("page") long page,
            @Query("per_page") long per_page);
}
