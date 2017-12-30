package com.dfa.vinatrip.domains.province_detail.view_all.festival;

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
import com.dfa.vinatrip.domains.province_detail.view_all.festival.adapter.RecyclerFestivalSearchAdapter;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.models.response.Province;
import com.dfa.vinatrip.models.response.festival.FestivalResponse;
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
 * Created by duonghd on 12/29/2017.
 * duonghd1307@gmail.com
 */

@SuppressLint("Registered")
@EActivity(R.layout.activity_festival_search)
public class FestivalSearchActivity extends BaseActivity<FestivalSearchView, FestivalSearchPresenter>
        implements FestivalSearchView {
    @App
    protected MainApplication mainApplication;
    @Inject
    protected FestivalSearchPresenter presenter;

    @ViewById(R.id.activity_festival_list_ll_root)
    protected LinearLayout llRoot;
    @ViewById(R.id.activity_festival_list_tb_toolbar)
    protected NToolbar nToolbar;
    @ViewById(R.id.activity_festival_list_edt_search)
    protected EditText edtSearch;
    @ViewById(R.id.fragment_province_detail_festivals_rcv_festivals)
    protected RecyclerView rcvFestivals;

    @Extra
    protected Province province;

    private List<FestivalResponse> festivalResponses;
    private RecyclerFestivalSearchAdapter festivalAdapter;
    private PublishSubject<String> publishSubject;
    private static final int page = 0;
    private static final int per_page = 0;

    @AfterInject
    void initInject() {
        DaggerFestivalSearchComponent.builder()
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

        festivalResponses = new ArrayList<>();
        festivalAdapter = new RecyclerFestivalSearchAdapter(this);
        rcvFestivals.setHasFixedSize(true);
        rcvFestivals.setLayoutManager(new LinearLayoutManager(this));
        rcvFestivals.setAdapter(festivalAdapter);

        presenter.getFestivals(province.getId(), page, per_page);
    }

    private void showKeyboard() {
        KeyboardVisibility.setEventListener(this, isOpen -> {
            if (KeyboardVisibility.isKeyboardVisible(FestivalSearchActivity.this)) {
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
    public FestivalSearchPresenter createPresenter() {
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
                        festivalAdapter.setFilter(filterFestival(s));
                    } else {
                        festivalAdapter.setFilter(festivalResponses);
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

    private List<FestivalResponse> filterFestival(String query) {
        List<FestivalResponse> festivalFilter = new ArrayList<>();
        for (FestivalResponse festivalResponse : festivalResponses) {
            if (AppUtil.convertStringQuery(festivalResponse.getAddress()).contains(AppUtil.convertStringQuery(query)) ||
                    AppUtil.convertStringQuery(festivalResponse.getName()).contains(AppUtil.convertStringQuery(query))) {
                festivalFilter.add(festivalResponse);
            }
        }
        return festivalFilter;
    }

    //-----------------------------------------------------------------------------------------------------------------
    @Override
    public void getFestivalsSuccess(List<FestivalResponse> festivalResponses) {
        this.festivalResponses.addAll(festivalResponses);
        this.festivalAdapter.setFilter(festivalResponses);

        setupSearchListener();
    }
}
