package com.dfa.vinatrip.services.account;

import com.beesightsoft.caf.services.network.NetworkProvider;
import com.dfa.vinatrip.models.request.AuthRequest;
import com.dfa.vinatrip.models.response.UserResponse;

import rx.Observable;


/**
 * Created by duytq on 9/17/2017.
 */

public class DefaultAccountService implements AccountService {
    
    private NetworkProvider networkProvider;
    private RestAccountService restAccountService;
    
    public DefaultAccountService(NetworkProvider networkProvider, RestAccountService restAccountService) {
        this.networkProvider = networkProvider;
        this.restAccountService = restAccountService;
    }
    
    @Override
    public Observable<Object> signUp(AuthRequest signUpRequest) {
        return restAccountService.signIn(signUpRequest);
    }
}
