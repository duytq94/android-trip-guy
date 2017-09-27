package com.dfa.vinatrip.services.account;

import com.dfa.vinatrip.models.request.AuthRequest;
import com.dfa.vinatrip.models.response.UserResponse;

import rx.Observable;


/**
 * Created by duytq on 9/17/2017.
 */

public interface AccountService {
    Observable<Object> signUp(AuthRequest signUpRequest);
}
