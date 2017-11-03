package com.dfa.vinatrip.domains.main.fragment.deal;

import com.dfa.vinatrip.base.BaseMvpView;

import java.util.List;

/**
 * Created by duytq on 10/14/2017.
 */

public interface DealView extends BaseMvpView {
    void getDealSuccess(List<Deal> dealList, int page);
}
