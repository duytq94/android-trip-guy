package com.dfa.vinatrip.domains.main.fragment.deal;

import android.support.v7.widget.LinearLayoutManager;
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

@EFragment(R.layout.fragment_deal)
public class DealFragment extends BaseFragment<DealView, DealPresenter>
        implements DealView {

    @ViewById(R.id.fragment_deal_rv_item)
    protected RecyclerView rvItem;
    @ViewById(R.id.fragment_deal_tv_no_content)
    protected TextView tvNoContent;
    @ViewById(R.id.fragment_deal_sv)
    protected SearchView searchView;

    private DealAdapter adapter;
    private String strQuery;

    @App
    protected MainApplication mainApplication;
    @Inject
    protected DealPresenter presenter;

    @AfterInject
    void initInject() {
        DaggerDealComponent.builder()
                .activityModule(new ActivityModule(getActivity()))
                .applicationComponent(mainApplication.getApplicationComponent())
                .build().inject(this);
    }

    @AfterViews
    public void init() {
        setupAdapter();
        setupSearch();
    }

    public void setupAdapter() {
        adapter = new DealAdapter(getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        rvItem.setLayoutManager(layoutManager);
        rvItem.setAdapter(adapter);
        rvItem.setHasFixedSize(true);

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                presenter.getDeal(strQuery, page, 10);
            }
        };
        rvItem.addOnScrollListener(scrollListener);
    }

    public void setupSearch() {
        searchView.setQueryHint("Tìm deal...");
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                strQuery = query;
                presenter.getDeal(strQuery, 1, 10);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
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

    }

    @Override
    public void getDataFail(Throwable throwable) {
        Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getDealSuccess(List<Deal> dealList, int page) {
        if (dealList.size() > 0) {
            tvNoContent.setVisibility(View.GONE);
            rvItem.setVisibility(View.VISIBLE);

            if (page == 1) {
                adapter.setDealList(dealList);
            } else {
                adapter.appendList(dealList);
            }
            adapter.notifyDataSetChanged();
        } else {
            if (page == 1) {
                tvNoContent.setVisibility(View.VISIBLE);
                rvItem.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public DealPresenter createPresenter() {
        return presenter;
    }
}
