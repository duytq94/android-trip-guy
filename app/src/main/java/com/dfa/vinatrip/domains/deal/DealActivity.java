package com.dfa.vinatrip.domains.deal;

import android.app.SearchManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseActivity;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.models.response.Deal;
import com.dfa.vinatrip.widgets.EndlessRecyclerViewScrollListener;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import javax.inject.Inject;

@EActivity(R.layout.activity_deal)
public class DealActivity extends BaseActivity<DealView, DealPresenter> implements DealView {

    @ViewById(R.id.my_toolbar)
    protected Toolbar toolbar;
    @ViewById(R.id.activity_deal_rv_item)
    protected RecyclerView rvItem;
    @ViewById(R.id.activity_deal_tv_no_content)
    protected TextView tvNoContent;

    private DealAdapter adapter;
    private String strQuery;

    @App
    protected MainApplication application;

    @Inject
    protected DealPresenter presenter;

    @AfterInject
    protected void initInject() {
        DaggerDealComponent.builder()
                .applicationComponent(application.getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build().inject(this);
    }

    @AfterViews
    public void init() {
        setupAppBar();
        setupAdapter();
    }

    public void setupAppBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Set button back
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void setupAdapter() {
        adapter = new DealAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

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

    @Override
    public void showLoading() {
        showHUD();
    }

    @Override
    public void hideLoading() {
        hideHUD();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_menu_menuSearch).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Tìm deal");

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

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void apiError(Throwable throwable) {

    }

    @Override
    public void getDataFail(Throwable throwable) {
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
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

    @NonNull
    @Override
    public DealPresenter createPresenter() {
        return presenter;
    }
}
