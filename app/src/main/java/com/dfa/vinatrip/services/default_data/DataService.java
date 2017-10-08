package com.dfa.vinatrip.services.default_data;

import com.beesightsoft.caf.services.common.RestMessageResponse;
import com.dfa.vinatrip.models.response.Province;

import java.util.List;

import rx.Observable;

/**
 * Created by duonghd on 10/6/2017.
 */

public interface DataService {
    Observable<List<Province>> getProvinces(long page, long per_page);
}
