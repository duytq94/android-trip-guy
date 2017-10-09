package com.dfa.vinatrip.domains.main.province.province_detail.fragment.hotel;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseFragment;
import com.dfa.vinatrip.domains.main.province.province_detail.fragment.hotel.adapter.RecyclerHotelAdapter;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.models.response.Province;
import com.dfa.vinatrip.models.response.hotel.HotelResponse;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by duonghd on 10/6/2017.
 */

@EFragment(R.layout.fragment_province_detail_hotels)
public class HotelFragment extends BaseFragment<HotelView, HotelPresenter>
        implements HotelView {
    @App
    protected MainApplication mainApplication;
    @Inject
    protected HotelPresenter presenter;

    @ViewById(R.id.fragment_province_detail_hotels_rcv_hotels)
    protected RecyclerView rcvHotels;

    @FragmentArg
    protected Province province;

    private int page = 1;
    private int per_page = 10;

    private List<HotelResponse> hotelResponses;
    private RecyclerHotelAdapter hotelAdapter;

    @AfterInject
    void initInject() {
        DaggerHotelComponent.builder()
                .applicationComponent(mainApplication.getApplicationComponent())
                .activityModule(new ActivityModule(getActivity()))
                .build().inject(this);
    }

    @AfterViews
    void init() {
        hotelResponses = new ArrayList<>();
        hotelAdapter = new RecyclerHotelAdapter(getContext(), hotelResponses);
        rcvHotels.setHasFixedSize(true);
        rcvHotels.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvHotels.setAdapter(hotelAdapter);

        presenter.getHotels(province.getId(), page, per_page);
    }

    @Override
    public HotelPresenter createPresenter() {
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
    public void getHotelsSuccess(List<HotelResponse> hotelResponses) {
        this.hotelResponses.addAll(hotelResponses);
        this.hotelAdapter.notifyDataSetChanged();
    }
}
