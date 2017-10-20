package com.dfa.vinatrip.services.chat;

import com.beesightsoft.caf.services.network.NetworkProvider;
import com.dfa.vinatrip.domains.chat.BaseMessage;
import com.dfa.vinatrip.domains.chat.StatusUserChat;
import com.dfa.vinatrip.services.filter.ApiErrorFilter;

import java.util.List;

import rx.Observable;

/**
 * Created by duytq on 09/28/2017.
 */

public class DefaultChatService implements ChatService {

    private RestChatService restChatService;
    private NetworkProvider networkProvider;
    private ApiErrorFilter apiErrorFilter;

    public DefaultChatService(RestChatService restChatService, NetworkProvider networkProvider,
                              ApiErrorFilter apiErrorFilter) {
        this.restChatService = restChatService;
        this.networkProvider = networkProvider;
        this.apiErrorFilter = apiErrorFilter;
    }

    @Override
    public Observable<List<BaseMessage>> getHistory(String groupId, int page, int pageSize) {
        return networkProvider
                .transformResponse(restChatService.getHistory(groupId, page, pageSize))
                .compose(apiErrorFilter.execute());
    }

    @Override
    public Observable<List<StatusUserChat>> getStatus(String groupId) {
        return networkProvider
                .transformResponse(restChatService.getStatus(groupId))
                .compose(apiErrorFilter.execute());
    }
}
