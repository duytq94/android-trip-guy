package com.dfa.vinatrip.services.account;

import com.beesightsoft.caf.services.common.RestMessageResponse;
import com.dfa.vinatrip.models.request.SignUpRequest;
import com.dfa.vinatrip.models.response.UserResponse;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by duytq on 9/17/2017.
 */

public interface AccountServiceApi {
    @POST("auth/sign-up")
    Observable<RestMessageResponse<UserResponse>> signUp(@Body SignUpRequest signUpRequest);
}
