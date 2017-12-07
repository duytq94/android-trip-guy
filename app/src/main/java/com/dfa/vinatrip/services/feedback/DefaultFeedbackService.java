package com.dfa.vinatrip.services.feedback;

import com.beesightsoft.caf.services.network.NetworkProvider;
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
}
