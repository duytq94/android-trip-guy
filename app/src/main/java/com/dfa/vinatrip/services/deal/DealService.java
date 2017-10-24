package com.dfa.vinatrip.services.deal;

import com.dfa.vinatrip.domains.main.fragment.deal.Deal;

import java.util.List;

import rx.Observable;

/**
 * Created by duytq on 10/14/2017.
 */

public interface DealService {
    Observable<List<Deal>> getDeal(String where, float priceMin, float priceMax, int dayMin, int dayMax, int page, int pageSize);
}
