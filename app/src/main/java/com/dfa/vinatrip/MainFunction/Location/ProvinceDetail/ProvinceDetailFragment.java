package com.dfa.vinatrip.MainFunction.Location.ProvinceDetail;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.dfa.vinatrip.MainFunction.Location.Province;
import com.dfa.vinatrip.MainFunction.Location.ProvinceDetail.ProvinceDescription.ProvinceDescriptionFragment;
import com.dfa.vinatrip.MainFunction.Location.ProvinceDetail.ProvinceDescription.ProvinceDescriptionFragment_;
import com.dfa.vinatrip.MainFunction.Location.ProvinceDetail.ProvinceDestination.ProvinceDestinationFragment;
import com.dfa.vinatrip.MainFunction.Location.ProvinceDetail.ProvinceDestination.ProvinceDestinationFragment_;
import com.dfa.vinatrip.MainFunction.Location.ProvinceDetail.ProvinceFood.ProvinceFoodFragment;
import com.dfa.vinatrip.MainFunction.Location.ProvinceDetail.ProvinceFood.ProvinceFoodFragment_;
import com.dfa.vinatrip.MainFunction.Location.ProvinceDetail.ProvinceHotel.ProvinceHotelFragment;
import com.dfa.vinatrip.MainFunction.Location.ProvinceDetail.ProvinceHotel.ProvinceHotelFragment_;
import com.dfa.vinatrip.MainFunction.Location.ProvinceDetail.ProvincePhoto.ProvincePhotoFragment;
import com.dfa.vinatrip.MainFunction.Location.ProvinceDetail.ProvincePhoto.ProvincePhotoFragment_;
import com.dfa.vinatrip.MainFunction.ViewPagerSwipeFragmentAdapter;
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
        province = (Province) getArguments().getSerializable("Province");

        setupViewPager(vpProvinceDetail, province);
        tlMenu.setupWithViewPager(vpProvinceDetail);
    }

    public void setupViewPager(ViewPager vpProvinceDetail, Province province) {
        ViewPagerSwipeFragmentAdapter adapter =
                new ViewPagerSwipeFragmentAdapter(getChildFragmentManager());

        // Send Province to each Fragment
        Bundle bundleProvince = new Bundle();
        bundleProvince.putSerializable("Province", province);

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