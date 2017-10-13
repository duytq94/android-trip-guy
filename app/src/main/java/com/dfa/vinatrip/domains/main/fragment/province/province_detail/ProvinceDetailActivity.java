package com.dfa.vinatrip.domains.main.fragment.province.province_detail;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseActivity;
import com.dfa.vinatrip.custom_view.NToolbar;
import com.dfa.vinatrip.domains.main.fragment.province.province_detail.adapter.RecyclerFoodAdapter;
import com.dfa.vinatrip.domains.main.fragment.province.province_detail.adapter.RecyclerHotelAdapter;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.models.response.Province;
import com.dfa.vinatrip.models.response.food.FoodResponse;
import com.dfa.vinatrip.models.response.hotel.HotelResponse;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@EActivity(R.layout.activity_province_detail)
public class ProvinceDetailActivity extends BaseActivity<ProvinceDetailView, ProvinceDetailPresenter>
        implements ProvinceDetailView {
    @App
    protected MainApplication mainApplication;
    @Inject
    protected ProvinceDetailPresenter presenter;
    
    @ViewById(R.id.activity_province_detail_tb_toolbar)
    protected NToolbar nToolbar;
    @ViewById(R.id.activity_province_detail_vp_viewpager_banner)
    protected ViewPager vpBanner;
    @ViewById(R.id.activity_province_detail_tv_intro)
    protected TextView tvIntro;
    @ViewById(R.id.activity_province_detail_rcv_hotel)
    protected RecyclerView rcvHotels;
    @ViewById(R.id.activity_province_detail_rcv_food)
    protected RecyclerView rcvFoods;
    
    @StringArrayRes(R.array.province_detail_tab_pager)
    protected String[] tabPager;
    
    @Extra
    protected Province province;
    
    private int hotel_page = 1;
    private int hotel_per_page = 5;
    private List<HotelResponse> hotelResponses;
    private RecyclerHotelAdapter hotelAdapter;
    
    private int food_page = 1;
    private int food_per_page = 5;
    private List<FoodResponse> foodResponses;
    private RecyclerFoodAdapter foodAdapter;
    
    @AfterInject
    void initInject() {
        DaggerProvinceDetailComponent.builder()
                .applicationComponent(mainApplication.getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build().inject(this);
    }
    
    @AfterViews
    void init() {
        nToolbar.setup(this, "TripGuy");
        nToolbar.showLeftIcon().showToolbarColor();
        nToolbar.setOnLeftClickListener(v -> {
            onBackPressed();
        });
        
        tvIntro.setText(province.getDescription());
        
        hotelResponses = new ArrayList<>();
        hotelAdapter = new RecyclerHotelAdapter(this, hotelResponses);
        rcvHotels.setHasFixedSize(true);
        rcvHotels.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rcvHotels.setAdapter(hotelAdapter);
        
        foodResponses = new ArrayList<>();
        foodAdapter = new RecyclerFoodAdapter(this, foodResponses);
        rcvFoods.setHasFixedSize(true);
        rcvFoods.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rcvFoods.setAdapter(foodAdapter);
        
        presenter.getHotels(province.getId(), hotel_page, hotel_per_page);
        presenter.getFoods(province.getId(), food_page, food_per_page);
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
    
    @NonNull
    @Override
    public ProvinceDetailPresenter createPresenter() {
        return presenter;
    }
    
    @Override
    public void onBackPressed() {
        finish();
    }
    
    @Override
    public void getHotelsSuccess(List<HotelResponse> hotelResponses) {
        this.hotelResponses.addAll(hotelResponses);
        this.hotelAdapter.notifyDataSetChanged();
    }
    
    @Override
    public void getFoodsSuccess(List<FoodResponse> foodResponses) {
        this.foodResponses.addAll(foodResponses);
        this.foodAdapter.notifyDataSetChanged();
    }
}
