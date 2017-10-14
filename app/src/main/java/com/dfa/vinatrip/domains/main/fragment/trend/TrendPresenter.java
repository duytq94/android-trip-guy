package com.dfa.vinatrip.domains.main.fragment.trend;

import com.dfa.vinatrip.base.BasePresenter;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

/**
 * Created by duytq on 10/14/2017.
 */

public class TrendPresenter extends BasePresenter<TrendView> {
    @Inject
    public TrendPresenter(EventBus eventBus) {
        super(eventBus);
    }
}
