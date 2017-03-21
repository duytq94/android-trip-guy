package com.dfa.vinatrip.DataService;

import com.dfa.vinatrip.MainFunction.Province.Province;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FirebaseAPI {
    @GET("/Province.json")
    Call<HashMap<String, Province>> loadProvince();
}
