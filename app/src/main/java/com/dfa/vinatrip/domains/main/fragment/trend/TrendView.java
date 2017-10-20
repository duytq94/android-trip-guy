package com.dfa.vinatrip.domains.main.fragment.trend;

import com.dfa.vinatrip.base.BaseMvpView;

import java.util.List;

/**
 * Created by duytq on 10/14/2017.
 */

public interface TrendView extends BaseMvpView {
    void getDataFail(Throwable throwable);

    void getTrendSuccess(List<Trend> trendList, int page);
}
