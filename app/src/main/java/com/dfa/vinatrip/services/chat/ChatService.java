package com.dfa.vinatrip.services.chat;

import com.dfa.vinatrip.models.response.BaseMessage;
import com.dfa.vinatrip.models.response.StatusUserChat;

import java.util.List;

import rx.Observable;

/**
 * Created by duytq on 09/28/2017.
 */

public interface ChatService {
    Observable<List<BaseMessage>> getHistory(String groupId, int page, int pageSize);

    Observable<List<StatusUserChat>> getStatus(String groupId);
}
