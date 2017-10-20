package com.dfa.vinatrip.services.chat;

import com.beesightsoft.caf.services.common.RestMessageResponse;
import com.dfa.vinatrip.domains.chat.BaseMessage;
import com.dfa.vinatrip.domains.chat.StatusUserChat;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by duytq on 09/28/2017.
 */

public interface RestChatService {
    @GET("history/{groupId}")
    Observable<RestMessageResponse<List<BaseMessage>>> getHistory(
            @Path("groupId") String groupId,
            @Query("page") int page,
            @Query("pageSize") int pageSize
    );

    @GET("status/{groupId}")
    Observable<RestMessageResponse<List<StatusUserChat>>> getStatus(
            @Path("groupId") String groupId
    );
}
