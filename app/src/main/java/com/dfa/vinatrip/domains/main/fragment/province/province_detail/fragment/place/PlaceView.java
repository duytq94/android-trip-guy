package com.dfa.vinatrip.domains.main.province.province_detail.fragment.place;

import com.dfa.vinatrip.base.BaseMvpView;
import com.dfa.vinatrip.models.response.place.PlaceResponse;

import java.util.List;

/**
 * Created by duonghd on 10/7/2017.
 */

public interface PlaceView extends BaseMvpView {
    void getPlacesSuccess(List<PlaceResponse> placeResponses);
}
