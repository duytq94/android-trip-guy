package com.dfa.vinatrip.domains.province_detail.view_all.place;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseFragment;
import com.dfa.vinatrip.domains.province_detail.view_all.place.adapter.RecyclerPlaceAdapter;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.models.response.Province;
import com.dfa.vinatrip.models.response.place.PlaceResponse;

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

@EFragment(R.layout.fragment_province_detail_places)
public class PlaceFragment extends BaseFragment<PlaceView, PlacePresenter>
        implements PlaceView {
    @App
    protected MainApplication mainApplication;
    @Inject
    protected PlacePresenter presenter;

    @ViewById(R.id.fragment_province_place_rcv_places)
    protected RecyclerView rcvPlaces;

    @FragmentArg
    protected Province province;

    private int page = 1;
    private int per_page = 10;

    private List<PlaceResponse> placeResponses;
    private RecyclerPlaceAdapter placeAdapter;

    @AfterInject
    void initInject() {
        DaggerPlaceComponent.builder()
                .applicationComponent(mainApplication.getApplicationComponent())
                .activityModule(new ActivityModule(getActivity()))
                .build().inject(this);
    }

    @AfterViews
    void init() {
        placeResponses = new ArrayList<>();
        placeAdapter = new RecyclerPlaceAdapter(getContext(), placeResponses);
        rcvPlaces.setHasFixedSize(true);
        rcvPlaces.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvPlaces.setAdapter(placeAdapter);

        presenter.getPlaces(province.getId(), page, per_page);
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
    public PlacePresenter createPresenter() {
        return presenter;
    }

    @Override
    public void getPlacesSuccess(List<PlaceResponse> placeResponses) {
        this.placeResponses.addAll(placeResponses);
        this.placeAdapter.notifyDataSetChanged();
    }
}
