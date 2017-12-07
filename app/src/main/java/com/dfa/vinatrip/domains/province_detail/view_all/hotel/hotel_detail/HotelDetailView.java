package com.dfa.vinatrip.domains.province_detail.view_all.hotel.hotel_detail;

import com.dfa.vinatrip.base.BaseMvpView;
import com.dfa.vinatrip.models.response.feedback.FeedbackResponse;

import java.util.List;

/**
 * Created by duonghd on 10/7/2017.
 */

public interface HotelDetailView extends BaseMvpView {
    void getHotelFeedbackSuccess(List<FeedbackResponse> feedbackResponses);
}
