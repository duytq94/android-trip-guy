package com.dfa.vinatrip.domains.province_detail.view_all.food;

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
import com.dfa.vinatrip.domains.province_detail.view_all.food.adapter.RecyclerFoodSearchAdapter;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.models.response.Province;
import com.dfa.vinatrip.models.response.food.FoodResponse;
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
 * Created by duonghd on 10/8/2017.
 * duonghd1307@gmail.com
 */

@SuppressLint("Registered")
@EActivity(R.layout.activity_food_search)
public class FoodSearchActivity extends BaseActivity<FoodSearchView, FoodSearchPresenter>
        implements FoodSearchView {
    @App
    protected MainApplication mainApplication;
    @Inject
    protected FoodSearchPresenter presenter;

    @ViewById(R.id.activity_food_list_ll_root)
    protected LinearLayout llRoot;
    @ViewById(R.id.activity_food_list_tb_toolbar)
    protected NToolbar nToolbar;
    @ViewById(R.id.activity_food_list_edt_search)
    protected EditText edtSearch;
    @ViewById(R.id.fragment_province_detail_foods_rcv_foods)
    protected RecyclerView rcvFoods;

    @Extra
    protected Province province;

    private List<FoodResponse> foodResponses;
    private RecyclerFoodSearchAdapter foodAdapter;
    private PublishSubject<String> publishSubject;
    private static final int page = 0;
    private static final int per_page = 0;

    @AfterInject
    void initInject() {
        DaggerFoodSearchComponent.builder()
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

        foodResponses = new ArrayList<>();
        foodAdapter = new RecyclerFoodSearchAdapter(this);
        rcvFoods.setHasFixedSize(true);
        rcvFoods.setLayoutManager(new LinearLayoutManager(this));
        rcvFoods.setAdapter(foodAdapter);

        presenter.getFoods(province.getId(), page, per_page);
    }

    private void showKeyboard() {
        KeyboardVisibility.setEventListener(this, isOpen -> {
            if (KeyboardVisibility.isKeyboardVisible(FoodSearchActivity.this)) {
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
    public FoodSearchPresenter createPresenter() {
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
                        foodAdapter.setFilter(filterFood(s));
                    } else {
                        foodAdapter.setFilter(foodResponses);
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

    private List<FoodResponse> filterFood(String query) {
        List<FoodResponse> foodFilter = new ArrayList<>();
        for (FoodResponse foodResponse : foodResponses) {
            if (AppUtil.convertStringQuery(foodResponse.getAddress()).contains(AppUtil.convertStringQuery(query)) ||
                    AppUtil.convertStringQuery(foodResponse.getName()).contains(AppUtil.convertStringQuery(query))) {
                foodFilter.add(foodResponse);
            }
        }
        return foodFilter;
    }

    //-----------------------------------------------------------------------------------------------------------------
    @Override
    public void getFoodsSuccess(List<FoodResponse> foodResponses) {
        this.foodResponses.addAll(foodResponses);
        this.foodAdapter.setFilter(foodResponses);

        setupSearchListener();
    }
}
