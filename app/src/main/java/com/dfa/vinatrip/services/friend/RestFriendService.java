package com.dfa.vinatrip.services.friend;

import com.beesightsoft.caf.services.common.RestMessageResponse;
import com.dfa.vinatrip.models.response.user.User;
import com.dfa.vinatrip.models.response.user.FriendStatus;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by duytq on 10/31/2017.
 */

public interface RestFriendService {
    @GET("api/friend/listing")
    Observable<RestMessageResponse<List<User>>> getListFriend(
            @Header("access-token") String userToken,
            @Query("page") int page,
            @Query("per_page") int pageSize
    );

    @GET("api/friend/request")
    Observable<RestMessageResponse<List<User>>> getListFriendReceive(
            @Header("access-token") String userToken,
            @Query("page") int page,
            @Query("per_page") int pageSize
    );

    @GET("api/friend/receive")
    Observable<RestMessageResponse<List<User>>> getListFriendRequest(
            @Header("access-token") String userToken,
            @Query("page") int page,
            @Query("per_page") int pageSize
    );

    @GET("api/user/listing")
    Observable<RestMessageResponse<List<User>>> getListUser(
            @Header("access-token") String userToken,
            @Query("page") int page,
            @Query("per_page") int pageSize
    );

    @POST("api/friend/{peerId}/request")
    Observable<RestMessageResponse<FriendStatus>> addFriendRequest(
            @Path("peerId") long peerId,
            @Header("access-token") String userToken
    );

    @POST("api/friend/{friendId}/cancel")
    Observable<RestMessageResponse<FriendStatus>> cancelFriendRequest(
            @Path("friendId") long friendId,
            @Header("access-token") String userToken
    );

    @POST("api/friend/{peerId}/unfriend")
    Observable<RestMessageResponse<FriendStatus>> unFriend(
            @Path("peerId") long peerId,
            @Header("access-token") String userToken
    );

    @POST("api/friend/{friendId}/accept")
    Observable<RestMessageResponse<FriendStatus>> acceptFriendRequest(
            @Path("friendId") long friendId,
            @Header("access-token") String userToken
    );
}
