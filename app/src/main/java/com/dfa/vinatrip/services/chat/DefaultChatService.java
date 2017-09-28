package com.dfa.vinatrip.services.chat;

import com.beesightsoft.caf.services.network.NetworkProvider;
import com.dfa.vinatrip.models.response.BaseMessage;
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
    public Observable<List<BaseMessage>> getHistory(long groupId) {
        return networkProvider
                .transformResponse(restChatService.getHistory(groupId))
                .compose(apiErrorFilter.execute());
    }
}
