package com.dfa.vinatrip.services.account;

import com.beesightsoft.caf.services.common.RestMessageResponse;
import com.dfa.vinatrip.models.request.AuthRequest;
import com.dfa.vinatrip.models.response.User;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by duytq on 9/17/2017.
 */

public interface RestAccountService {
    @POST("api/auth/sign-in")
    Observable<RestMessageResponse<User>> signIn(@Body AuthRequest authRequest);
}
