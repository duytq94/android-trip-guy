package com.dfa.vinatrip.services.default_data;

import com.dfa.vinatrip.models.response.Province;
import com.dfa.vinatrip.models.response.event.EventResponse;
import com.dfa.vinatrip.models.response.food.FoodResponse;
import com.dfa.vinatrip.models.response.hotel.HotelResponse;
import com.dfa.vinatrip.models.response.place.PlaceResponse;
import com.dfa.vinatrip.models.response.province_image.ProvinceImageResponse;

import java.util.List;

import rx.Observable;

/**
 * Created by duonghd on 10/6/2017.
 */

public interface DataService {
    Observable<List<Province>> getProvinces(long page, long per_page);
    
    Observable<List<EventResponse>> getEvents(int province_id, long page, long per_page);
    
    Observable<List<HotelResponse>> getHotels(int province_id, long page, long per_page);
    
    Observable<List<FoodResponse>> getFoods(int province_id, long page, long per_page);
    
    Observable<List<PlaceResponse>> getPlaces(int province_id, long page, long per_page);
    
    Observable<List<ProvinceImageResponse>> getImages(int province_id, long page, long per_page);
}
