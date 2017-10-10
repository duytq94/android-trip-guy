package com.dfa.vinatrip.domains.main.fragment.province;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseFragment;
import com.dfa.vinatrip.domains.main.fragment.province.adapter.ProvinceAdapter;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.models.response.Province;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@EFragment(R.layout.fragment_province)
public class ProvinceFragment extends BaseFragment<ProvinceView, ProvincePresenter>
        implements ProvinceView {
    @App
    protected MainApplication mainApplication;
    @Inject
    protected ProvincePresenter presenter;
    
    @ViewById(R.id.fragment_province_srl_swipe)
    protected SwipeRefreshLayout swipeRefreshLayout;
    @ViewById(R.id.fragment_province_nsv_scrollview)
    protected NestedScrollView nestedScrollView;
    @ViewById(R.id.fragment_province_rcv_recycler)
    protected RecyclerView rcvProvince;
    
    private List<Province> provinceList;
    private ProvinceAdapter provinceAdapter;
    private int page = 1;
    private int per_page = 10;
    
    @AfterInject
    void initInject() {
        DaggerProvinceComponent.builder()
                .activityModule(new ActivityModule(getActivity()))
                .applicationComponent(mainApplication.getApplicationComponent())
                .build().inject(this);
    }
    
    @RequiresApi(api = Build.VERSION_CODES.M)
    @AfterViews
    void init() {
        provinceList = new ArrayList<>();
        provinceAdapter = new ProvinceAdapter(getContext(), provinceList);
        rcvProvince.setHasFixedSize(true);
        rcvProvince.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rcvProvince.setAdapter(provinceAdapter);
        presenter.getProvince(page, per_page);
        
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.e("test", scrollX + " " + scrollY + " " + oldScrollX + " " + oldScrollY);
            }
        });
        
        nestedScrollView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int i) {
                Log.e("test", " " + i);
            }
        });
    }
    
    @Override
    public ProvincePresenter createPresenter() {
        return presenter;
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
    public void getProvinceSuccess(List<Province> provinces) {
        provinceList.addAll(provinces);
        provinceAdapter.notifyDataSetChanged();
    }
}
