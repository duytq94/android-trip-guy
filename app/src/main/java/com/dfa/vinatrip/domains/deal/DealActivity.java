package com.dfa.vinatrip.domains.deal;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseActivity;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.models.response.Deal;
import com.dfa.vinatrip.widgets.RotateLoading;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

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
    @ViewById(R.id.activity_deal_lv_item)
    protected ListView lvItem;

    private QuickAdapter<Deal> adapter;
    private List<Deal> dealList;
    private ImageLoader imageLoader;
    private DisplayImageOptions imageOptions;

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

        imageLoader = ImageLoader.getInstance();
        imageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.bg_green)
                .showImageForEmptyUri(R.drawable.photo_not_available)
                .showImageOnFail(R.drawable.photo_not_available)
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.ARGB_4444)
                .build();
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
        adapter = new QuickAdapter<Deal>(this, R.layout.item_deal) {
            @Override
            protected void convert(BaseAdapterHelper helper, Deal item) {
                ImageView ivPhoto = helper.getView(R.id.item_deal_iv_photo);
                TextView tvTitle = helper.getView(R.id.item_deal_tv_title);
                TextView tvRoute = helper.getView(R.id.item_deal_tv_route);
                TextView tvContent = helper.getView(R.id.item_deal_tv_content);
                TextView tvPrice = helper.getView(R.id.item_deal_tv_price);
                Button btnDetail = helper.getView(R.id.item_deal_btn_detail);
                RotateLoading rotateLoading = helper.getView(R.id.item_deal_rotate_loading);

                tvTitle.setText(item.getTitle());
                tvRoute.setText(item.getRoute());
                tvContent.setText(item.getRoute());
                tvPrice.setText(String.valueOf(item.getPrice()));
                btnDetail.setOnClickListener(v -> {

                });
                imageLoader.displayImage(item.getImg(), ivPhoto, imageOptions, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        rotateLoading.setVisibility(View.VISIBLE);
                        rotateLoading.start();
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        rotateLoading.setVisibility(View.GONE);
                        rotateLoading.stop();
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        rotateLoading.setVisibility(View.GONE);
                        rotateLoading.stop();
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        rotateLoading.setVisibility(View.GONE);
                        rotateLoading.stop();
                    }
                });
            }
        };
        lvItem.setAdapter(adapter);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

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
                presenter.getDeal(query, 1, 10);
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
    public void getDealSuccess(List<Deal> dealList) {
        this.dealList = dealList;
        adapter.clear();
        adapter.addAll(this.dealList);
        adapter.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DealPresenter createPresenter() {
        return presenter;
    }
}
