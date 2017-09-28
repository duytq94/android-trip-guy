package com.dfa.vinatrip.infrastructures;

import com.beesightsoft.caf.infrastructures.scope.ApplicationScope;
import com.dfa.vinatrip.services.account.AccountService;
import com.dfa.vinatrip.services.chat.ChatService;

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
}
