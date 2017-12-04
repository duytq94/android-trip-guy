package com.dfa.vinatrip.domains.main.fragment.province.province_detail.view_all.hotel;

import com.dfa.vinatrip.base.BaseMvpView;
import com.dfa.vinatrip.models.response.hotel.HotelResponse;

import java.util.List;

/**
 * Created by duonghd on 10/7/2017.
 */

public interface HotelListView extends BaseMvpView {
    void getHotelsSuccess(List<HotelResponse> hotelResponses);
}
