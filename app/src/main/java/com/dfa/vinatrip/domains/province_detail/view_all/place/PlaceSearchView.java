package com.dfa.vinatrip.domains.province_detail.view_all.place;

import com.dfa.vinatrip.base.BaseMvpView;
import com.dfa.vinatrip.models.response.place.PlaceResponse;

import java.util.List;

/**
 * Created by duonghd on 10/7/2017.
 * duonghd1307@gmail.com
 */

public interface PlaceSearchView extends BaseMvpView {
    void getPlacesSuccess(List<PlaceResponse> placeResponses);
}
