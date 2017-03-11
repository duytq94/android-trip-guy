package com.dfa.vinatrip.MainFunction.Location.ProvinceDetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class ProvinceDetailFragment extends Fragment {

    private ProvinceDescriptionFragment provinceDescriptionFragment;
    private ProvinceHotelFragment provinceHotelFragment;
    private ProvinceFoodFragment provinceFoodFragment;
    private ProvinceDestinationFragment provinceDestinationFragment;
    private ProvincePhotoFragment provincePhotoFragment;
    private TabLayout tlMenu;
    private ViewPager vpProvinceDetail;
    private Province province;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_province_detail, container, false);
        vpProvinceDetail =
                (ViewPager) view.findViewById(R.id.fragment_province_detail_vp_province_detail);

        // Get Province from ProvinceDetailActivity
        province = (Province) getArguments().getSerializable("Province");

        setupViewPager(vpProvinceDetail, province);

        tlMenu = (TabLayout) view.findViewById(R.id.fragment_province_detail_tl_menu);
        tlMenu.setupWithViewPager(vpProvinceDetail);
        return view;
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