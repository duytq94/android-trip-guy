package com.dfa.vinatrip.domains.province_detail.view_all.hotel;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.beesightsoft.caf.services.schedulers.RxScheduler;
import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseActivity;
import com.dfa.vinatrip.custom_view.NToolbar;
import com.dfa.vinatrip.domains.province_detail.view_all.hotel.adapter.RecyclerHotelListAdapter;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.models.response.Province;
import com.dfa.vinatrip.models.response.hotel.HotelResponse;
import com.dfa.vinatrip.utils.AppUtil;
import com.dfa.vinatrip.utils.KeyboardVisibility;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.subjects.PublishSubject;

/**
 * Created by duonghd on 10/6/2017.
 */

@SuppressLint("Registered")
@EActivity(R.layout.activity_hotel_list)
public class HotelListActivity extends BaseActivity<HotelListView, HotelListPresenter>
        implements HotelListView {
    @App
    protected MainApplication mainApplication;
    @Inject
    protected HotelListPresenter presenter;
    
    @ViewById(R.id.activity_hotel_list_ll_root)
    protected LinearLayout llRoot;
    @ViewById(R.id.activity_hotel_list_tb_toolbar)
    protected NToolbar nToolbar;
    @ViewById(R.id.activity_hotel_list_edt_search)
    protected EditText edtSearch;
    @ViewById(R.id.fragment_province_detail_hotels_rcv_hotels)
    protected RecyclerView rcvHotels;
    
    @Extra
    protected Province province;
    
    private List<HotelResponse> hotelResponses;
    private RecyclerHotelListAdapter hotelAdapter;
    private PublishSubject<String> publishSubject;
    
    @AfterInject
    void initInject() {
        DaggerHotelListComponent.builder()
                .applicationComponent(mainApplication.getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build().inject(this);
    }
    
    @AfterViews
    void init() {
        AppUtil.setupUI(llRoot);
        showKeyboard();
        
        nToolbar.setup(this, "TripGuy");
        nToolbar.showLeftIcon().showToolbarColor();
        nToolbar.setOnLeftClickListener(v -> {
            onBackPressed();
        });
        
        hotelResponses = new ArrayList<>();
        hotelAdapter = new RecyclerHotelListAdapter(this);
        rcvHotels.setHasFixedSize(true);
        rcvHotels.setLayoutManager(new LinearLayoutManager(this));
        rcvHotels.setAdapter(hotelAdapter);
        
        presenter.getHotels(province.getId(), 0, 0);
    }
    
    private void showKeyboard() {
        KeyboardVisibility.setEventListener(this, isOpen -> {
            if (KeyboardVisibility.isKeyboardVisible(HotelListActivity.this)) {
                Log.e("keyboard", "show");
            } else {
                if (getCurrentFocus() != null) {
                    getCurrentFocus().clearFocus();
                }
                Log.e("keyboard", "hide");
            }
        });
    }
    
    @NonNull
    @Override
    public HotelListPresenter createPresenter() {
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
    
    private void setupSearchListener() {
        publishSubject = PublishSubject.create();
        publishSubject
                .debounce(200, TimeUnit.MILLISECONDS)
                .compose(RxScheduler.applyIoSchedulers())
                .subscribe(s -> {
                    if (s.length() != 0) {
                        hotelAdapter.setFilter(filterHotel(s));
                    } else {
                        hotelAdapter.setFilter(hotelResponses);
                    }
                }, Throwable::printStackTrace);
        
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            
            }
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                publishSubject.onNext(s.toString());
            }
            
            @Override
            public void afterTextChanged(Editable s) {
            
            }
        });
    }
    
    private List<HotelResponse> filterHotel(String query) {
        List<HotelResponse> hotelFilter = new ArrayList<>();
        for (HotelResponse hotelResponse : hotelResponses) {
            if (AppUtil.convertStringQuery(hotelResponse.getLocation()).contains(AppUtil.convertStringQuery(query)) ||
                    AppUtil.convertStringQuery(hotelResponse.getName()).contains(AppUtil.convertStringQuery(query))) {
                hotelFilter.add(hotelResponse);
            }
        }
        return hotelFilter;
    }
    
    //-----------------------------------------------------------------------------------------------------------------
    @Override
    public void getHotelsSuccess(List<HotelResponse> hotelResponses) {
        this.hotelResponses.addAll(hotelResponses);
        this.hotelAdapter.setFilter(hotelResponses);
        
        setupSearchListener();
    }
}
