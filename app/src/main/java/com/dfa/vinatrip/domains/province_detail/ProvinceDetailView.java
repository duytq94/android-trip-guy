package com.dfa.vinatrip.domains.province_detail;

import com.dfa.vinatrip.base.BaseMvpView;
import com.dfa.vinatrip.models.response.festival.FestivalResponse;
import com.dfa.vinatrip.models.response.food.FoodResponse;
import com.dfa.vinatrip.models.response.hotel.HotelResponse;
import com.dfa.vinatrip.models.response.place.PlaceResponse;
import com.dfa.vinatrip.models.response.province_image.ProvinceImageResponse;

import java.util.List;

/**
 * Created by duonghd on 10/6/2017.
 */

public interface ProvinceDetailView extends BaseMvpView {
    void getEventsSuccess(List<FestivalResponse> eventResponses);
    
    void getHotelsSuccess(List<HotelResponse> hotelResponses);
    
    void getFoodsSuccess(List<FoodResponse> foodResponses);
    
    void getPlacesSuccess(List<PlaceResponse> placeResponses);
    
    void getImagesSuccess(List<ProvinceImageResponse> provinceImageResponses);
}
