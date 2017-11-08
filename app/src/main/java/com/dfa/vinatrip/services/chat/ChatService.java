package com.dfa.vinatrip.services.chat;

import com.dfa.vinatrip.domains.chat.BaseMessage;
import com.dfa.vinatrip.domains.chat.StatusUserChat;

import java.util.List;

import rx.Observable;

/**
 * Created by duytq on 09/28/2017.
 */

public interface ChatService {
    Observable<List<BaseMessage>> getHistory(long planId, int page, int pageSize);

    Observable<List<StatusUserChat>> getStatus(long planId);
}
