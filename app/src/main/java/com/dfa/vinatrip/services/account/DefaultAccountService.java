package com.dfa.vinatrip.services.account;

import com.beesightsoft.caf.services.network.NetworkProvider;
import com.dfa.vinatrip.models.request.SignUpRequest;
import com.dfa.vinatrip.models.response.UserResponse;

import rx.Observable;


/**
 * Created by duytq on 9/17/2017.
 */

public class DefaultAccountService implements AccountService {

    private NetworkProvider networkProvider;
    private AccountServiceApi accountServiceApi;


    @Override
    public Observable<UserResponse> signUp(SignUpRequest signUpRequest) {
        return null;
    }
}
