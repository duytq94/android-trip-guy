package com.dfa.vinatrip.domains.main.fragment.trend;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseFragment;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.widgets.EndlessRecyclerViewScrollListener;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import javax.inject.Inject;

@EFragment(R.layout.fragment_trend)
public class TrendFragment extends BaseFragment<TrendView, TrendPresenter> implements TrendView {

    @ViewById(R.id.fragment_trend_rv_item)
    protected RecyclerView rvItem;
    @ViewById(R.id.fragment_trend_srl_reload)
    SwipeRefreshLayout srlReload;

    private TrendAdapter adapter;

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

    @AfterViews
    public void init() {
        setupAdapter();
    }

    public void setupAdapter() {
        adapter = new TrendAdapter(getActivity());
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);

        rvItem.setLayoutManager(layoutManager);
        rvItem.setAdapter(adapter);
        rvItem.setHasFixedSize(true);

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                presenter.getTrend(page, 10);
            }
        };
        rvItem.addOnScrollListener(scrollListener);
        presenter.getTrend(1, 10);

        srlReload.setColorSchemeResources(R.color.colorMain);
        srlReload.setOnRefreshListener(() -> {
            presenter.getTrend(1, 10);
            srlReload.setRefreshing(false);
        });
    }

    @Override
    public void showLoading() {
        showHUD();
    }

    @Override
    public void hideLoading() {
        hideHUD();
    }

    @Override
    public void apiError(Throwable throwable) {

    }

    @Override
    public TrendPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void getDataFail(Throwable throwable) {
        Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getTrendSuccess(List<Trend> trendList, int page) {
        if (trendList.size() > 0) {
            if (page == 1) {
                adapter.setDealList(trendList);
            } else {
                adapter.appendList(trendList);
            }
            adapter.notifyDataSetChanged();
        }
    }
}
