package com.dfa.vinatrip.services.default_data;

import com.beesightsoft.caf.services.network.NetworkProvider;
import com.dfa.vinatrip.models.response.Province;
import com.dfa.vinatrip.models.response.festival.FestivalResponse;
import com.dfa.vinatrip.models.response.food.FoodResponse;
import com.dfa.vinatrip.models.response.hotel.HotelResponse;
import com.dfa.vinatrip.models.response.place.PlaceResponse;
import com.dfa.vinatrip.models.response.province_image.ProvinceImageResponse;
import com.dfa.vinatrip.services.filter.ApiErrorFilter;

import java.util.List;

import rx.Observable;

/**
 * Created by duonghd on 10/6/2017.
 * duonghd1307@gmail.com
 */

public class DefaultDataService implements DataService {
    private NetworkProvider networkProvider;
    private RestDataService restDataService;
    private ApiErrorFilter apiErrorFilter;
    
    public DefaultDataService(NetworkProvider networkProvider, RestDataService restDataService, ApiErrorFilter apiErrorFilter) {
        this.networkProvider = networkProvider;
        this.restDataService = restDataService;
        this.apiErrorFilter = apiErrorFilter;
    }
    
    @Override
    public Observable<List<Province>> getProvinces(long page, long per_page) {
        return networkProvider.transformResponse(restDataService.getProvinces(page, per_page))
                .compose(apiErrorFilter.execute());
    }
    
    @Override
    public Observable<List<FestivalResponse>> getFestivals(int province_id, long page, long per_page) {
        return networkProvider.transformResponse(restDataService.getFestivals(province_id, page, per_page))
                .compose(apiErrorFilter.execute());
    }
    
    @Override
    public Observable<List<HotelResponse>> getHotels(int province_id, long page, long per_page) {
        return networkProvider.transformResponse(restDataService.getHotels(province_id, page, per_page))
                .compose(apiErrorFilter.execute());
    }
    
    @Override
    public Observable<List<FoodResponse>> getFoods(int province_id, long page, long per_page) {
        return networkProvider.transformResponse(restDataService.getFoods(province_id, page, per_page))
                .compose(apiErrorFilter.execute());
    }
    
    @Override
    public Observable<List<PlaceResponse>> getPlaces(int province_id, long page, long per_page) {
        return networkProvider.transformResponse(restDataService.getPlaces(province_id, page, per_page))
                .compose(apiErrorFilter.execute());
    }
    
    @Override
    public Observable<List<ProvinceImageResponse>> getImages(int province_id, long page, long per_page) {
        return networkProvider.transformResponse(restDataService.getImages(province_id, page, per_page))
                .compose(apiErrorFilter.execute());
    }
}
