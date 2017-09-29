package com.dfa.vinatrip.services.chat;

import com.dfa.vinatrip.models.response.BaseMessage;

import java.util.List;

import rx.Observable;

/**
 * Created by duytq on 09/28/2017.
 */

public interface ChatService {
    Observable<List<BaseMessage>> getHistory(long groupId, int page, int pageSize);
}
