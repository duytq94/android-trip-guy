package com.dfa.vinatrip.domains.main.fragment.province.province_detail;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseActivity;
import com.dfa.vinatrip.domains.main.fragment.province.province_detail.adapter.ProvinceDetailPagerAdapter;
import com.dfa.vinatrip.domains.main.province.province_detail.ProvinceDetailPresenter;
import com.dfa.vinatrip.domains.main.province.province_detail.ProvinceDetailView;
import com.dfa.vinatrip.domains.main.province.province_detail.fragment.food.FoodFragment_;
import com.dfa.vinatrip.domains.main.province.province_detail.fragment.hotel.HotelFragment_;
import com.dfa.vinatrip.domains.main.province.province_detail.fragment.images.ImageFragment_;
import com.dfa.vinatrip.domains.main.province.province_detail.fragment.intro.IntroFragment_;
import com.dfa.vinatrip.domains.main.province.province_detail.fragment.place.PlaceFragment_;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.models.response.Province;

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

    @ViewById(R.id.activity_province_detail_vp_viewpager_banner)
    protected ViewPager vpBanner;
    @ViewById(R.id.activity_province_detail_tl_tab_layout)
    protected TabLayout tabLayout;
    @ViewById(R.id.activity_province_detail_vp_viewpager_fragment)
    protected ViewPager vpFragment;

    @StringArrayRes(R.array.province_detail_tab_pager)
    protected String[] tabPager;

    @Extra
    protected Province province;

    @AfterInject
    void initInject() {
        DaggerProvinceDetailComponent.builder()
                .applicationComponent(mainApplication.getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build().inject(this);
    }

    @AfterViews
    void init() {
        List<Fragment> arrayFragment = new ArrayList<>();
        arrayFragment.add(IntroFragment_.builder().build());
        arrayFragment.add(HotelFragment_.builder().province(province).build());
        arrayFragment.add(FoodFragment_.builder().build());
        arrayFragment.add(PlaceFragment_.builder().province(province).build());
        arrayFragment.add(ImageFragment_.builder().build());

        vpFragment.setAdapter(new ProvinceDetailPagerAdapter(getSupportFragmentManager(), arrayFragment, tabPager));
        vpFragment.setOffscreenPageLimit(arrayFragment.size());
        tabLayout.setupWithViewPager(vpFragment);

        vpFragment.setCurrentItem(0, true);
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
}
