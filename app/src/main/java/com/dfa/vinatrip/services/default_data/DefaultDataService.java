package com.dfa.vinatrip.services.default_data;

import com.beesightsoft.caf.services.network.NetworkProvider;
import com.dfa.vinatrip.models.response.Province;
import com.dfa.vinatrip.models.response.food.FoodResponse;
import com.dfa.vinatrip.models.response.hotel.HotelResponse;
import com.dfa.vinatrip.models.response.place.PlaceResponse;

import java.util.List;

import rx.Observable;

/**
 * Created by duonghd on 10/6/2017.
 */

public class DefaultDataService implements DataService {
    private NetworkProvider networkProvider;
    private RestDataService restDataService;
    
    public DefaultDataService(NetworkProvider networkProvider, RestDataService restDataService) {
        this.networkProvider = networkProvider;
        this.restDataService = restDataService;
    }
    
    @Override
    public Observable<List<Province>> getProvinces(long page, long per_page) {
        return networkProvider.transformResponse(restDataService.getProvinces(page, per_page));
    }
    
    @Override
    public Observable<List<HotelResponse>> getHotels(int province_id, long page, long per_page) {
        return networkProvider.transformResponse(restDataService.getHotels(province_id, page, per_page));
    }
    
    @Override
    public Observable<List<FoodResponse>> getFoods(int province_id, long page, long per_page) {
        return networkProvider.transformResponse(restDataService.getFoods(province_id, page, per_page));
    }
    
    @Override
    public Observable<List<PlaceResponse>> getPlaces(int province_id, long page, long per_page) {
        return networkProvider.transformResponse(restDataService.getPlaces(province_id, page, per_page));
    }
}
