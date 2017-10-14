package com.dfa.vinatrip.services.deal;

import com.dfa.vinatrip.models.response.Deal;

import java.util.List;

import rx.Observable;

/**
 * Created by duytq on 10/14/2017.
 */

public interface DealService {
    Observable<List<Deal>> getDeal(String where, int page, int pageSize);
}
