package com.dfa.vinatrip.domains.province_detail.view_all.food.food_detail;

import com.dfa.vinatrip.base.BaseMvpView;
import com.dfa.vinatrip.models.response.feedback.FeedbackResponse;
import com.dfa.vinatrip.models.response.user.User;

import java.util.List;

/**
 * Created by duonghd on 12/28/2017.
 * duonghd1307@gmail.com
 */

public interface FoodDetailView extends BaseMvpView {
    void getFoodFeedbackSuccess(List<FeedbackResponse> feedbackResponses);

    void postFoodFeedbackSuccess(FeedbackResponse feedbackResponse);

    void signInSuccess(User user);
}
