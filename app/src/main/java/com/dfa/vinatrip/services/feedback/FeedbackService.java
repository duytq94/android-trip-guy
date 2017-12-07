package com.dfa.vinatrip.services.feedback;

import com.dfa.vinatrip.models.response.feedback.FeedbackResponse;

import java.util.List;

import rx.Observable;

/**
 * Created by duonghd on 12/7/2017.
 */

public interface FeedbackService {
    Observable<List<FeedbackResponse>> getHotelFeedback(int hotelId, int page, int pageSize);
}
