package com.dfa.vinatrip.domains.province_detail.view_all.place.place_detail;

import com.dfa.vinatrip.base.BaseMvpView;
import com.dfa.vinatrip.models.response.feedback.FeedbackResponse;
import com.dfa.vinatrip.models.response.user.User;

import java.util.List;

/**
 * Created by duonghd on 12/28/2017.
 * duonghd1307@gmail.com
 */

public interface PlaceDetailView extends BaseMvpView {
    void getPlaceFeedbackSuccess(List<FeedbackResponse> feedbackResponses);

    void postPlaceFeedbackSuccess(FeedbackResponse feedbackResponse);

    void signInSuccess(User user);
}
