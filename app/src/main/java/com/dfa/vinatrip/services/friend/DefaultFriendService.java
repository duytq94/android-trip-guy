package com.dfa.vinatrip.services.friend;

import com.beesightsoft.caf.services.network.NetworkProvider;
import com.dfa.vinatrip.models.response.User;
import com.dfa.vinatrip.services.account.AccountService;
import com.dfa.vinatrip.services.filter.ApiErrorFilter;

import java.util.List;

import rx.Observable;

/**
 * Created by duytq on 10/31/2017.
 */

public class DefaultFriendService implements FriendService {

    private AccountService accountService;
    private RestFriendService restFriendService;
    private NetworkProvider networkProvider;
    private ApiErrorFilter apiErrorFilter;

    public DefaultFriendService(AccountService accountService, RestFriendService restFriendService,
                                NetworkProvider networkProvider, ApiErrorFilter apiErrorFilter) {
        this.accountService = accountService;
        this.restFriendService = restFriendService;
        this.networkProvider = networkProvider;
        this.apiErrorFilter = apiErrorFilter;
    }

    @Override
    public Observable<List<User>> getListFriend() {
        return networkProvider
                .transformResponse(restFriendService.getListFriend(accountService.getCurrentUser().getAccessToken()))
                .compose(apiErrorFilter.execute());
    }

    @Override
    public Observable<List<User>> getListFriendReceive() {
        return networkProvider
                .transformResponse(restFriendService.getListFriendReceive(accountService.getCurrentUser().getAccessToken()))
                .compose(apiErrorFilter.execute());
    }
}
