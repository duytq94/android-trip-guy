package com.dfa.vinatrip.domains.main.province.province_detail.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by duonghd on 10/6/2017.
 */

public class ProvinceDetailPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> arrayFragment;
    private String[] tabTitles;
    
    public ProvinceDetailPagerAdapter(FragmentManager fm, List<Fragment> arrayFragment, String[] tabTitles) {
        super(fm);
        this.arrayFragment = arrayFragment;
        this.tabTitles = tabTitles;
    }
    
    @Override
    public Fragment getItem(int position) {
        return arrayFragment.get(position);
    }
    
    @Override
    public int getCount() {
        return tabTitles.length;
    }
    
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
