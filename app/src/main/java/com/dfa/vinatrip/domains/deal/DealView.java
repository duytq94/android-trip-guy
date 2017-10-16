package com.dfa.vinatrip.domains.deal;

import com.dfa.vinatrip.base.BaseMvpView;
import com.dfa.vinatrip.models.response.Deal;

import java.util.List;

/**
 * Created by duytq on 10/14/2017.
 */

public interface DealView extends BaseMvpView {
    void getDataFail(Throwable throwable);

    void getDealSuccess(List<Deal> dealList, int page);
}
