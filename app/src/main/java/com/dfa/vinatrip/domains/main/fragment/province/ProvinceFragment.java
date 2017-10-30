package com.dfa.vinatrip.domains.main.fragment.province;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseFragment;
import com.dfa.vinatrip.domains.main.fragment.province.adapter.PagerBannerAdapter;
import com.dfa.vinatrip.domains.main.fragment.province.adapter.RecyclerProvinceAdapter;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.models.response.Province;
import com.dfa.vinatrip.utils.AppUtil;

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
    protected SwipeRefreshLayout srlReload;
    @ViewById(R.id.fragment_province_nsv_scrollview)
    protected NestedScrollView nestedScrollView;
    @ViewById(R.id.fragment_province_vp_banner)
    protected ViewPager vpBanner;
    @ViewById(R.id.fragment_province_ll_banner_position)
    protected LinearLayout llBannerPosition;
    @ViewById(R.id.fragment_province_rcv_recycler)
    protected RecyclerView rcvProvince;

    private List<String> banners;
    private PagerBannerAdapter pagerBannerAdapter;

    private List<Province> provinceList;
    private RecyclerProvinceAdapter recyclerProvinceAdapter;
    private int page = 1;
    private int per_page = 10;

    @AfterInject
    protected void initInject() {
        DaggerProvinceComponent.builder()
                .activityModule(new ActivityModule(getActivity()))
                .applicationComponent(mainApplication.getApplicationComponent())
                .build().inject(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @AfterViews
    public void init() {
        firstSetup();

        presenter.getBanner();
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

        srlReload.setColorSchemeResources(R.color.colorMain);
    }

    public void firstSetup() {
        provinceList = new ArrayList<>();
        recyclerProvinceAdapter = new RecyclerProvinceAdapter(getContext(), provinceList);
        rcvProvince.setHasFixedSize(true);
        rcvProvince.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rcvProvince.setAdapter(recyclerProvinceAdapter);

        banners = new ArrayList<>();
        pagerBannerAdapter = new PagerBannerAdapter(getContext(), banners);
        vpBanner.setAdapter(pagerBannerAdapter);
        vpBanner.setPageTransformer(true, new ViewPager.PageTransformer() {
            static final float MIN_SCALE = 0.75f;

            @Override
            public void transformPage(View page, float position) {
                int pageWidth = page.getWidth();
                if (position < -1) {
                    page.setAlpha(0);
                } else if (position <= 0) {
                    page.setAlpha(1);
                    page.setTranslationX(0);
                    page.setScaleX(1);
                    page.setScaleY(1);
                } else if (position <= 1) {
                    page.setAlpha(1 - position);
                    page.setTranslationX(pageWidth * -position);

                    float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
                    page.setScaleX(scaleFactor);
                    page.setScaleY(scaleFactor);
                } else {
                    page.setAlpha(0);
                }
            }
        });
    }

    @Override
    public ProvincePresenter createPresenter() {
        return presenter;
    }

    @Override
    public void showLoading() {
//        showHUD();
    }

    @Override
    public void hideLoading() {
//        hideHUD();
    }

    @Override
    public void apiError(Throwable throwable) {

    }

    @Override
    public void getBannerSuccess(List<String> banners) {
        this.banners.addAll(banners);
        pagerBannerAdapter.notifyDataSetChanged();

        for (int i = 0; i < this.banners.size(); i++) {
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(AppUtil.dpToPx(getContext(), 8f), AppUtil.dpToPx(getContext(), 8f));
            layoutParams.setMargins(AppUtil.dpToPx(getContext(), 4f), 0, AppUtil.dpToPx(getContext(), 4f), 0);

            ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(layoutParams);

            if (i == 0) {
                imageView.setBackgroundResource(R.drawable.bg_oval_select);
            } else {
                imageView.setBackgroundResource(R.drawable.bg_oval_not_select);
            }
            llBannerPosition.addView(imageView);
        }

        vpBanner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < banners.size(); i++) {
                    ImageView imageView = (ImageView) llBannerPosition.getChildAt(i);
                    if (i == position) {
                        imageView.setBackgroundResource(R.drawable.bg_oval_select);
                    } else {
                        imageView.setBackgroundResource(R.drawable.bg_oval_not_select);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void getProvinceSuccess(List<Province> provinces) {
        provinceList.addAll(provinces);
        recyclerProvinceAdapter.notifyDataSetChanged();
    }
}
