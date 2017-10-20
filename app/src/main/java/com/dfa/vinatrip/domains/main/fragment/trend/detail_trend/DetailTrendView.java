package com.dfa.vinatrip.domains.main.fragment.trend.detail_trend;

import com.dfa.vinatrip.base.BaseMvpView;
import com.dfa.vinatrip.domains.main.fragment.trend.Trend;

/**
 * Created by duytq on 10/20/2017.
 */

public interface DetailTrendView extends BaseMvpView {
    void updateTrendCountSuccess(Trend trend);

    void getDataFail(Throwable throwable);
}
