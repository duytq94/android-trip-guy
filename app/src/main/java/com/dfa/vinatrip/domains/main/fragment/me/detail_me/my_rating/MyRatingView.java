package com.dfa.vinatrip.domains.main.fragment.me.detail_me.my_rating;

import com.dfa.vinatrip.base.BaseMvpView;
import com.dfa.vinatrip.models.response.feedback.FeedbackResponse;

import java.util.List;

/**
 * Created by duonghd on 1/6/2018.
 * duonghd1307@gmail.com
 */

public interface MyRatingView extends BaseMvpView {
    void getMyFeedbackSuccess(List<FeedbackResponse> feedbackResponses);
}