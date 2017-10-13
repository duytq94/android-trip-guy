package com.dfa.vinatrip.domains.main.fragment.province.province_detail;

import com.dfa.vinatrip.base.BaseMvpView;
import com.dfa.vinatrip.models.response.food.FoodResponse;
import com.dfa.vinatrip.models.response.hotel.HotelResponse;

import java.util.List;

/**
 * Created by duonghd on 10/6/2017.
 */

public interface ProvinceDetailView extends BaseMvpView {
    void getHotelsSuccess(List<HotelResponse> hotelResponses);
    
    void getFoodsSuccess(List<FoodResponse> foodResponses);
}
