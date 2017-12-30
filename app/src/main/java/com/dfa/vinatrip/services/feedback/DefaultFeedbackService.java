package com.dfa.vinatrip.services.feedback;

import com.beesightsoft.caf.services.network.NetworkProvider;
import com.dfa.vinatrip.models.request.FeedbackRequest;
import com.dfa.vinatrip.models.response.feedback.FeedbackResponse;
import com.dfa.vinatrip.services.filter.ApiErrorFilter;

import java.util.List;

import rx.Observable;

/**
 * Created by duonghd on 12/7/2017.
 */

public class DefaultFeedbackService implements FeedbackService {
    private RestFeedbackService restFeedbackService;
    private NetworkProvider networkProvider;
    private ApiErrorFilter apiErrorFilter;

    public DefaultFeedbackService(RestFeedbackService restFeedbackService, NetworkProvider networkProvider, ApiErrorFilter apiErrorFilter) {
        this.restFeedbackService = restFeedbackService;
        this.networkProvider = networkProvider;
        this.apiErrorFilter = apiErrorFilter;
    }

    @Override
    public Observable<List<FeedbackResponse>> getHotelFeedback(int hotelId, int page, int pageSize) {
        return networkProvider.transformResponse(restFeedbackService.getHotelFeedback(hotelId, page, pageSize))
                .compose(apiErrorFilter.execute());
    }

    @Override
    public Observable<List<FeedbackResponse>> getFoodFeedback(int foodId, int page, int pageSize) {
        return networkProvider.transformResponse(restFeedbackService.getFoodFeedback(foodId, page, pageSize))
                .compose(apiErrorFilter.execute());
    }

    @Override
    public Observable<List<FeedbackResponse>> getPlaceFeedback(int placeId, int page, int pageSize) {
        return networkProvider.transformResponse(restFeedbackService.getPlaceFeedback(placeId, page, pageSize))
                .compose(apiErrorFilter.execute());
    }

    @Override
    public Observable<List<FeedbackResponse>> getFestivalFeedback(int eventId, int page, int pageSize) {
        return networkProvider.transformResponse(restFeedbackService.getFestivalFeedback(eventId, page, pageSize))
                .compose(apiErrorFilter.execute());
    }

    @Override
    public Observable<FeedbackResponse> postHotelFeedback(String accessToken, int hotelId, FeedbackRequest feedbackRequest) {
        return networkProvider.transformResponse(restFeedbackService.postHotelFeedback(accessToken, hotelId, feedbackRequest))
                .compose(apiErrorFilter.execute());
    }

    @Override
    public Observable<FeedbackResponse> postFoodFeedback(String accessToken, int foodId, FeedbackRequest feedbackRequest) {
        return networkProvider.transformResponse(restFeedbackService.postHotelFeedback(accessToken, foodId, feedbackRequest))
                .compose(apiErrorFilter.execute());
    }

    @Override
    public Observable<FeedbackResponse> postPlaceFeedback(String accessToken, int placeId, FeedbackRequest
            feedbackRequest) {
        return networkProvider.transformResponse(restFeedbackService.postHotelFeedback(accessToken, placeId, feedbackRequest))
                .compose(apiErrorFilter.execute());
    }

    @Override
    public Observable<FeedbackResponse> postFestivalFeedback(String accessToken, int eventId, FeedbackRequest
            feedbackRequest) {
        return networkProvider.transformResponse(restFeedbackService.postHotelFeedback(accessToken, eventId, feedbackRequest))
                .compose(apiErrorFilter.execute());
    }
}
