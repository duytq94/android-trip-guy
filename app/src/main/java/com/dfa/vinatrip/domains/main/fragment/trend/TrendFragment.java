package com.dfa.vinatrip.domains.main.fragment.trend;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.TextView;
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

import static com.dfa.vinatrip.utils.Constants.PAGE_SIZE;

@EFragment(R.layout.fragment_trend)
public class TrendFragment extends BaseFragment<TrendView, TrendPresenter> implements TrendView {

    @ViewById(R.id.fragment_trend_srl_reload)
    protected SwipeRefreshLayout srlReload;
    @ViewById(R.id.fragment_trend_rv_item)
    protected RecyclerView rvItem;
    @ViewById(R.id.fragment_trend_tv_no_content)
    protected TextView tvNoContent;
    @ViewById(R.id.fragment_trend_sv)
    protected SearchView searchView;

    private TrendAdapter adapter;
    private String strQuery = "";

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
        setupSearch();
        presenter.getTrend(strQuery, 1, PAGE_SIZE);
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
                presenter.getTrend(strQuery, page, PAGE_SIZE);
            }
        };
        rvItem.addOnScrollListener(scrollListener);

        srlReload.setColorSchemeResources(R.color.colorMain);
        srlReload.setOnRefreshListener(() -> {
            presenter.getTrend(strQuery, 1, PAGE_SIZE);
            srlReload.setRefreshing(false);
        });
    }

    public void setupSearch() {
        searchView.setQueryHint("Nơi đến...");
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                strQuery = query;
                presenter.getTrend(strQuery, 1, PAGE_SIZE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (searchView.getQuery().length() == 0) {
                    strQuery = "";
                }
                return false;
            }
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
        Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public TrendPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void getTrendSuccess(List<Trend> trendList, int page) {
        if (trendList.size() > 0) {
            tvNoContent.setVisibility(View.GONE);
            rvItem.setVisibility(View.VISIBLE);

            if (page == 1) {
                adapter.setDealList(trendList);
            } else {
                adapter.appendList(trendList);
            }
            adapter.notifyDataSetChanged();
        } else {
            if (page == 1) {
                tvNoContent.setVisibility(View.VISIBLE);
                rvItem.setVisibility(View.GONE);
            }
        }
    }
}
