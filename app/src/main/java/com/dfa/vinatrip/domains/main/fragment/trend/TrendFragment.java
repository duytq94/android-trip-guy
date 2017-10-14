package com.dfa.vinatrip.domains.main.fragment.trend;

import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseFragment;
import com.dfa.vinatrip.domains.deal.DealActivity_;
import com.dfa.vinatrip.infrastructures.ActivityModule;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

import javax.inject.Inject;

@EFragment(R.layout.fragment_trend)
public class TrendFragment extends BaseFragment<TrendView, TrendPresenter> implements TrendView {

    @App
    protected MainApplication mainApplication;
    @Inject
    protected TrendPresenter presenter;

    @AfterInject
    protected void initInject() {
        DaggerTrendComponent.builder()
                .activityModule(new ActivityModule(getActivity()))
                .applicationComponent(mainApplication.getApplicationComponent())
                .build().inject(this);
    }

    @Click(R.id.fragment_trend_btn)
    public void onBtnClick() {
        DealActivity_.intent(getActivity()).start();
    }

    @AfterViews
    public void init() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void apiError(Throwable throwable) {

    }

    @Override
    public TrendPresenter createPresenter() {
        return presenter;
    }
}
