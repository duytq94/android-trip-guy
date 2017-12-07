package com.dfa.vinatrip.infrastructures;

import com.beesightsoft.caf.infrastructures.scope.ApplicationScope;
import com.dfa.vinatrip.services.account.AccountService;
import com.dfa.vinatrip.services.chat.ChatService;
import com.dfa.vinatrip.services.deal.DealService;
import com.dfa.vinatrip.services.default_data.DataService;
import com.dfa.vinatrip.services.feedback.FeedbackService;
import com.dfa.vinatrip.services.friend.FriendService;
import com.dfa.vinatrip.services.plan.PlanService;
import com.dfa.vinatrip.services.trend.TrendService;

import org.greenrobot.eventbus.EventBus;

import dagger.Component;

/**
 * Created by DFA on 5/20/2017.
 */

@ApplicationScope
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    EventBus eventBusProvider();

    AccountService accountService();

    ChatService chatService();

    DealService dealService();

    TrendService trendService();

    DataService dataService();

    FriendService friendService();

    PlanService planService();
    
    FeedbackService feedbackService();
}
