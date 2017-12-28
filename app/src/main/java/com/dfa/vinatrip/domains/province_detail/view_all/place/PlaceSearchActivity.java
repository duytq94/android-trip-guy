package com.dfa.vinatrip.domains.province_detail.view_all.place;

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
import com.dfa.vinatrip.domains.province_detail.view_all.place.adapter.RecyclerPlaceSearchAdapter;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.models.response.Province;
import com.dfa.vinatrip.models.response.place.PlaceResponse;
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
 * duonghd1307@gmail.com
 */

@SuppressLint("Registered")
@EActivity(R.layout.activity_place_search)
public class PlaceSearchActivity extends BaseActivity<PlaceSearchView, PlaceSearchPresenter>
        implements PlaceSearchView {
    @App
    protected MainApplication mainApplication;
    @Inject
    protected PlaceSearchPresenter presenter;

    @ViewById(R.id.activity_place_list_ll_root)
    protected LinearLayout llRoot;
    @ViewById(R.id.activity_place_list_tb_toolbar)
    protected NToolbar nToolbar;
    @ViewById(R.id.activity_place_list_edt_search)
    protected EditText edtSearch;
    @ViewById(R.id.fragment_province_detail_places_rcv_places)
    protected RecyclerView rcvPlaces;

    @Extra
    protected Province province;

    private List<PlaceResponse> placeResponses;
    private RecyclerPlaceSearchAdapter placeAdapter;
    private PublishSubject<String> publishSubject;
    private static final int page = 0;
    private static final int per_page = 0;

    @AfterInject
    void initInject() {
        DaggerPlaceSearchComponent.builder()
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
        nToolbar.setOnLeftClickListener(v -> onBackPressed());

        placeResponses = new ArrayList<>();
        placeAdapter = new RecyclerPlaceSearchAdapter(this);
        rcvPlaces.setHasFixedSize(true);
        rcvPlaces.setLayoutManager(new LinearLayoutManager(this));
        rcvPlaces.setAdapter(placeAdapter);

        presenter.getPlaces(province.getId(), page, per_page);
    }

    private void showKeyboard() {
        KeyboardVisibility.setEventListener(this, isOpen -> {
            if (KeyboardVisibility.isKeyboardVisible(PlaceSearchActivity.this)) {
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
    public PlaceSearchPresenter createPresenter() {
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
                        placeAdapter.setFilter(filterPlace(s));
                    } else {
                        placeAdapter.setFilter(placeResponses);
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

    private List<PlaceResponse> filterPlace(String query) {
        List<PlaceResponse> placeFilter = new ArrayList<>();
        for (PlaceResponse placeResponse : placeResponses) {
            if (AppUtil.convertStringQuery(placeResponse.getAddress()).contains(AppUtil.convertStringQuery(query)) ||
                    AppUtil.convertStringQuery(placeResponse.getName()).contains(AppUtil.convertStringQuery(query))) {
                placeFilter.add(placeResponse);
            }
        }
        return placeFilter;
    }

    //-----------------------------------------------------------------------------------------------------------------
    @Override
    public void getPlacesSuccess(List<PlaceResponse> placeResponses) {
        this.placeResponses.addAll(placeResponses);
        this.placeAdapter.setFilter(placeResponses);

        setupSearchListener();
    }
}
