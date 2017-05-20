package com.dfa.vinatrip.domains.main.province.detail_province;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;

import com.dfa.vinatrip.domains.main.province.Province;
import com.dfa.vinatrip.domains.main.province.detail_province.province_description.ProvinceDescriptionFragment;
import com.dfa.vinatrip.domains.main.province.detail_province.province_description.ProvinceDescriptionFragment_;
import com.dfa.vinatrip.domains.main.province.detail_province.province_destination.ProvinceDestinationFragment;
import com.dfa.vinatrip.domains.main.province.detail_province.province_destination.ProvinceDestinationFragment_;
import com.dfa.vinatrip.domains.main.province.detail_province.province_food.ProvinceFoodFragment;
import com.dfa.vinatrip.domains.main.province.detail_province.province_food.ProvinceFoodFragment_;
import com.dfa.vinatrip.domains.main.province.detail_province.province_hotel.ProvinceHotelFragment;
import com.dfa.vinatrip.domains.main.province.detail_province.province_hotel.ProvinceHotelFragment_;
import com.dfa.vinatrip.domains.main.province.detail_province.province_photo.ProvincePhotoFragment;
import com.dfa.vinatrip.domains.main.province.detail_province.province_photo.ProvincePhotoFragment_;
import com.dfa.vinatrip.utils.ViewPagerSwipeFragmentAdapter;
import com.dfa.vinatrip.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_province_detail)
public class ProvinceDetailFragment extends Fragment {

    @ViewById(R.id.fragment_province_detail_vp_province_detail)
    ViewPager vpProvinceDetail;

    @ViewById(R.id.fragment_province_detail_tl_menu)
    TabLayout tlMenu;

    private ProvinceDescriptionFragment provinceDescriptionFragment;
    private ProvinceHotelFragment provinceHotelFragment;
    private ProvinceFoodFragment provinceFoodFragment;
    private ProvinceDestinationFragment provinceDestinationFragment;
    private ProvincePhotoFragment provincePhotoFragment;
    private Province province;

    @AfterViews
    void onCreateView() {
        // Get Province from ProvinceDetailActivity
        province = getArguments().getParcelable("Province");

        setupViewPager(vpProvinceDetail, province);
        tlMenu.setupWithViewPager(vpProvinceDetail);

        // Notify ProvinceDetailActivity know to change image header
        vpProvinceDetail.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent("com.dfa.vinatrip.action.IMAGE_HEADER");
                        intent.putExtra("POSITION", 0);
                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                        break;
                    case 1:
                        intent = new Intent("com.dfa.vinatrip.action.IMAGE_HEADER");
                        intent.putExtra("POSITION", 1);
                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                        break;
                    case 2:
                        intent = new Intent("com.dfa.vinatrip.action.IMAGE_HEADER");
                        intent.putExtra("POSITION", 2);
                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                        break;
                    case 3:
                        intent = new Intent("com.dfa.vinatrip.action.IMAGE_HEADER");
                        intent.putExtra("POSITION", 3);
                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                        break;
                    case 4:
                        intent = new Intent("com.dfa.vinatrip.action.IMAGE_HEADER");
                        intent.putExtra("POSITION", 4);
                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setupViewPager(ViewPager vpProvinceDetail, Province province) {
        ViewPagerSwipeFragmentAdapter adapter =
                new ViewPagerSwipeFragmentAdapter(getChildFragmentManager());

        // Send Province to each Fragment
        Bundle bundleProvince = new Bundle();
        bundleProvince.putParcelable("Province", province);

        provinceDescriptionFragment = new ProvinceDescriptionFragment_();
        provinceDescriptionFragment.setArguments(bundleProvince);

        provinceHotelFragment = new ProvinceHotelFragment_();
        provinceHotelFragment.setArguments(bundleProvince);

        provinceFoodFragment = new ProvinceFoodFragment_();
        provinceFoodFragment.setArguments(bundleProvince);

        provinceDestinationFragment = new ProvinceDestinationFragment_();
        provinceDestinationFragment.setArguments(bundleProvince);

        provincePhotoFragment = new ProvincePhotoFragment_();
        provincePhotoFragment.setArguments(bundleProvince);

        adapter.addFragment(provinceDescriptionFragment, "GIỚI THIỆU");
        adapter.addFragment(provinceHotelFragment, "KHÁCH SẠN");
        adapter.addFragment(provinceFoodFragment, "ẨM THỰC");
        adapter.addFragment(provinceDestinationFragment, "THAM QUAN");
        adapter.addFragment(provincePhotoFragment, "ẢNH ĐẸP");

        vpProvinceDetail.setAdapter(adapter);
    }
}