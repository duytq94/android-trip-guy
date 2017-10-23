package com.dfa.vinatrip.domains.main.fragment.deal;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
        implements DealView, AdapterView.OnItemSelectedListener {

    @ViewById(R.id.fragment_deal_srl_reload)
    protected SwipeRefreshLayout srlReload;
    @ViewById(R.id.fragment_deal_rv_item)
    protected RecyclerView rvItem;
    @ViewById(R.id.fragment_deal_tv_no_content)
    protected TextView tvNoContent;
    @ViewById(R.id.fragment_deal_sv)
    protected SearchView searchView;
    @ViewById(R.id.fragment_deal_sp_price)
    protected Spinner spPrice;
    @ViewById(R.id.fragment_deal_sp_during)
    protected Spinner spDuring;

    private DealAdapter adapter;
    private String strQuery = "";

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
        setupSpinner();
        presenter.getDeal(strQuery, 1, 10);
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

        srlReload.setColorSchemeResources(R.color.colorMain);
        srlReload.setOnRefreshListener(() -> {
            presenter.getDeal(strQuery, 1, 10);
            srlReload.setRefreshing(false);
        });
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
                if (searchView.getQuery().length() == 0) {
                    strQuery = "";
                }
                return false;
            }
        });
    }

    public void setupSpinner() {
        ArrayAdapter<CharSequence> priceAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.range_price, android.R.layout.simple_spinner_item);
        priceAdapter.setDropDownViewResource(R.layout.item_spinner_deal);
        spPrice.setAdapter(priceAdapter);

        ArrayAdapter<CharSequence> duringAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.range_during, android.R.layout.simple_spinner_item);
        duringAdapter.setDropDownViewResource(R.layout.item_spinner_deal);
        spDuring.setAdapter(duringAdapter);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
