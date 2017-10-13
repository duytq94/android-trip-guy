package com.dfa.vinatrip.domains.main.fragment.province.province_detail.fragment.food;

import android.util.Log;

import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseFragment;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.models.response.Province;
import com.dfa.vinatrip.models.response.food.FoodResponse;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by duonghd on 10/6/2017.
 */

@EFragment(R.layout.fragment_province_detail_foods)
public class FoodFragment extends BaseFragment<FoodView, FoodPresenter> implements FoodView {
    @App
    protected MainApplication mainApplication;
    @Inject
    protected FoodPresenter presenter;
    
    @FragmentArg
    protected Province province;
    
    private int page = 1;
    private int per_page = 10;
    
    @AfterInject
    void initInject() {
        DaggerFoodComponent.builder()
                .applicationComponent(mainApplication.getApplicationComponent())
                .activityModule(new ActivityModule(getActivity()))
                .build().inject(this);
    }
    
    @AfterViews
    void init() {
        presenter.getFoods(province.getId(), page, per_page);
    }
    
    @Override
    public FoodPresenter createPresenter() {
        return presenter;
    }
    
    @Override
    public void showLoading() {
        
    }
    
    @Override
    public void hideLoading() {
        
    }
    
    @Override
    public void apiError(Throwable throwable) {
        
    }
    
    @Override
    public void getFoodsSuccess(List<FoodResponse> foodResponses) {
        Log.e("test","ok");
    }
}
