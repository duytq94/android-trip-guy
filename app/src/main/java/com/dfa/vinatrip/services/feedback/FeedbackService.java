package com.dfa.vinatrip.services.feedback;

import com.dfa.vinatrip.models.request.FeedbackRequest;
import com.dfa.vinatrip.models.response.feedback.FeedbackResponse;

import java.util.List;

import rx.Observable;

/**
 * Created by duonghd on 12/7/2017.
 */

public interface FeedbackService {
    Observable<List<FeedbackResponse>> getHotelFeedback(int hotelId, int page, int pageSize);
    
    Observable<List<FeedbackResponse>> getFoodFeedback(int foodId, int page, int pageSize);
    
    Observable<List<FeedbackResponse>> getPlaceFeedback(int placeId, int page, int pageSize);
    
    Observable<List<FeedbackResponse>> getEventFeedback(int eventId, int page, int pageSize);
    
    Observable<FeedbackResponse> postHotelFeedback(String accessToken, int hotelId, FeedbackRequest feedbackRequest);
    
    Observable<FeedbackResponse> postFoodFeedback(String accessToken, int foodId, FeedbackRequest feedbackRequest);
    
    Observable<FeedbackResponse> postPlaceFeedback(String accessToken, int placeId, FeedbackRequest feedbackRequest);
    
    Observable<FeedbackResponse> postEventFeedback(String accessToken, int eventId, FeedbackRequest feedbackRequest);
}
