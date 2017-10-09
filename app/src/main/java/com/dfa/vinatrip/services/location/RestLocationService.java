package com.dfa.vinatrip.services.location;

import com.beesightsoft.caf.services.common.RestMessageResponse;
import com.dfa.vinatrip.domains.location.UserLocation;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by duytq on 10/8/2017.
 */

public interface RestLocationService {
    @GET("last_location/{groupId}")
    Observable<RestMessageResponse<List<UserLocation>>> getLastLocation(
            @Path("groupId") String groupId
    );
}
