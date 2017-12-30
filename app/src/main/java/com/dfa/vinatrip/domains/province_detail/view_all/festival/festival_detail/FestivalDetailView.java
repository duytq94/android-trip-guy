package com.dfa.vinatrip.domains.province_detail.view_all.festival.festival_detail;

import com.dfa.vinatrip.base.BaseMvpView;
import com.dfa.vinatrip.models.response.feedback.FeedbackResponse;
import com.dfa.vinatrip.models.response.user.User;

import java.util.List;

/**
 * Created by duonghd on 12/29/2017.
 * duonghd1307@gmail.com
 */

public interface FestivalDetailView extends BaseMvpView {
    void getFestivalFeedbackSuccess(List<FeedbackResponse> feedbackResponses);

    void postFestivalFeedbackSuccess(FeedbackResponse feedbackResponse);

    void signInSuccess(User user);
}
