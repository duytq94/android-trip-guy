package com.dfa.vinatrip.services.account;

import com.dfa.vinatrip.models.request.AuthRequest;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by duytq on 9/17/2017.
 */

public interface RestAccountService {
    @POST("api/auth/sign-in")
    Observable<Object> signIn(@Body AuthRequest authRequest);
}
