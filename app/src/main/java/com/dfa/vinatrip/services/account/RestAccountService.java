package com.dfa.vinatrip.services.account;

import com.beesightsoft.caf.services.common.RestMessageResponse;
import com.dfa.vinatrip.models.request.AuthRequest;
import com.dfa.vinatrip.models.response.User;

import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import rx.Observable;

/**
 * Created by duytq on 9/17/2017.
 */

public interface RestAccountService {
    @POST("api/auth/login")
    Observable<RestMessageResponse<User>> signIn(@Body AuthRequest authRequest);

    @POST("api/auth/register")
    Observable<RestMessageResponse<User>> signUp(@Body AuthRequest authRequest);

    @POST("api/auth/logout")
    Observable<RestMessageResponse<String>> signOut(@Header("access-token") String userToken);

    @PUT("api/user/edit")
    Observable<RestMessageResponse<User>> editProfile(@Header("access-token") String userToken, @Body User user);
}
