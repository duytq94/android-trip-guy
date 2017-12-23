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

import static com.dfa.vinatrip.utils.Constants.PAGE_SIZE;

@EFragment(R.layout.fragment_deal)
public class DealFragment extends BaseFragment<DealView, DealPresenter>
        implements DealView {

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
    private float priceMin = 0;
    private float priceMax = 100000000;
    private int dayMax = 30;
    private int dayMin = 0;

    @App
    protected MainApplication mainApplication;
    @Inject
    protected DealPresenter presenter;

    @Override
    public DealPresenter createPresenter() {
        return presenter;
    }

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
        presenter.getDeal(strQuery, priceMin, priceMax, dayMin, dayMax, 1, PAGE_SIZE);
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
                presenter.getDeal(strQuery, priceMin, priceMax, dayMin, dayMax, page, PAGE_SIZE);
            }
        };
        rvItem.addOnScrollListener(scrollListener);

        srlReload.setColorSchemeResources(R.color.colorMain);
        srlReload.setOnRefreshListener(() -> {
            presenter.getDeal(strQuery, priceMin, priceMax, dayMin, dayMax, 1, PAGE_SIZE);
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
                presenter.getDeal(strQuery, priceMin, priceMax, dayMin, dayMax, 1, PAGE_SIZE);
                try {
                    getActivity().getCurrentFocus().clearFocus();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                R.array.range_price, R.layout.item_spinner_deal);
        spPrice.setAdapter(priceAdapter);
        spPrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        priceMin = 0;
                        priceMax = 100000000;
                        break;
                    case 1:
                        priceMin = 0;
                        priceMax = 2000000;
                        break;
                    case 2:
                        priceMin = 2000000;
                        priceMax = 3000000;
                        break;
                    case 3:
                        priceMin = 3000000;
                        priceMax = 5000000;
                        break;
                    case 4:
                        priceMin = 5000000;
                        priceMax = 10000000;
                        break;
                }
                presenter.getDeal(strQuery, priceMin, priceMax, dayMin, dayMax, 1, PAGE_SIZE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> duringAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.range_during, R.layout.item_spinner_deal);
        spDuring.setAdapter(duringAdapter);
        spDuring.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        dayMin = 0;
                        dayMax = 30;
                        break;
                    case 1:
                        dayMin = 0;
                        dayMax = 2;
                        break;
                    case 2:
                        dayMin = 2;
                        dayMax = 4;
                        break;
                    case 3:
                        dayMin = 4;
                        dayMax = 6;
                        break;
                }
                presenter.getDeal(strQuery, priceMin, priceMax, dayMin, dayMax, 1, PAGE_SIZE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void showLoading() {
        //showHUD();
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
}
