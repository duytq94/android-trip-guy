package com.dfa.vinatrip.domains.main.fragment.province;

import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.beesightsoft.caf.exceptions.ApiThrowable;
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

import static com.dfa.vinatrip.utils.Constants.PAGE_SIZE;

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
    private PagerBannerAdapter bannerAdapter;

    private List<Province> provinceList;
    private RecyclerProvinceAdapter provinceAdapter;
    private int nsvHeight;
    private long pageProvince = 1;
    private boolean canLoadMore = false;

    private Handler handler;
    private Runnable runnable;
    private int bannerPosition = 0;

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
        banners = new ArrayList<>();
        bannerAdapter = new PagerBannerAdapter(getContext(), banners);
        vpBanner.setAdapter(bannerAdapter);
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

        provinceList = new ArrayList<>();
        provinceAdapter = new RecyclerProvinceAdapter(getContext(), provinceList);
        rcvProvince.setHasFixedSize(true);
        rcvProvince.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rcvProvince.setAdapter(provinceAdapter);

        nestedScrollView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        nsvHeight = nestedScrollView.getHeight();
                        if (nestedScrollView.getViewTreeObserver().isAlive()) {
                            nestedScrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    }
                });

        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener)
                (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                    int maxScrollY = rcvProvince.getMeasuredHeight() + AppUtil.dpToPx(getContext(), 158) - nsvHeight;
                    if (canLoadMore && scrollY > (maxScrollY - AppUtil.dpToPx(getContext(), 156))) {
                        pageProvince = pageProvince + 1;
                        presenter.getProvince(pageProvince, PAGE_SIZE);
                        canLoadMore = false;
                    }
                });

        handler = new Handler();
        presenter.getBanner();
        presenter.getProvince(pageProvince, PAGE_SIZE);

        srlReload.setColorSchemeResources(R.color.colorMain);
        srlReload.setOnRefreshListener(() -> {
            provinceList.clear();
            pageProvince = 1;
            presenter.getProvince(pageProvince, PAGE_SIZE);
            srlReload.setRefreshing(false);
        });
    }

    @NonNull
    @Override
    public ProvincePresenter createPresenter() {
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
        ApiThrowable apiThrowable = (ApiThrowable) throwable;
        Toast.makeText(getContext(), apiThrowable.firstErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getBannerSuccess(List<String> banners) {
        this.banners.addAll(banners);
        bannerAdapter.notifyDataSetChanged();

        for (int i = 0; i < this.banners.size(); i++) {
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(AppUtil.dpToPx(getContext(), 8f), AppUtil.dpToPx(getContext(), 8f));
            layoutParams.setMargins(AppUtil.dpToPx(getContext(), 4f), 0, AppUtil.dpToPx(getContext(), 4f), 0);

            ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(layoutParams);

            imageView.setBackgroundResource(i == 0 ? R.drawable.bg_oval_select : R.drawable.bg_oval_not_select);
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

        runnable = () -> {
            bannerPosition = bannerPosition % this.banners.size();
            if (vpBanner != null) {
                vpBanner.setCurrentItem(bannerPosition++, bannerPosition != 0);
                handler.postDelayed(runnable, 5000);
            } else {
                handler.removeCallbacks(runnable);
            }
        };
    }

    @Override
    public void getProvinceSuccess(List<Province> provinces) {
        provinceList.addAll(provinces);
        provinceAdapter.notifyDataSetChanged();
        canLoadMore = provinces.size() == PAGE_SIZE;
    }

    @Override
    public void onStop() {
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
        super.onStop();
    }

    @Override
    public void onStart() {
        if (handler != null && runnable != null){
            handler.post(runnable);
        }
        super.onStart();
    }
}
