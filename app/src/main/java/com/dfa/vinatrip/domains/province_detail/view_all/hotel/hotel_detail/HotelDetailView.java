package com.dfa.vinatrip.domains.province_detail.view_all.hotel.hotel_detail;

import com.dfa.vinatrip.base.BaseMvpView;
import com.dfa.vinatrip.models.response.feedback.FeedbackResponse;
import com.dfa.vinatrip.models.response.user.User;

import java.util.List;

/**
 * Created by duonghd on 10/7/2017.
 * duonghd1307@gmail.com
 */

public interface HotelDetailView extends BaseMvpView {
    void getHotelFeedbackSuccess(List<FeedbackResponse> feedbackResponses);
    
    void postHotelFeedbackSuccess(FeedbackResponse feedbackResponse);

    void signInSuccess(User user);
}
