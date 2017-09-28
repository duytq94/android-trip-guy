package com.dfa.vinatrip.services.chat;

import com.beesightsoft.caf.services.common.RestMessageResponse;
import com.dfa.vinatrip.models.response.BaseMessage;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by duytq on 09/28/2017.
 */

public interface RestChatService {
    @GET("history/{groupId}")
    Observable<RestMessageResponse<List<BaseMessage>>> getHistory(
            @Path("groupId") long groupId
    );
}
