package com.dfa.vinatrip.domains.main.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by duonghd on 10/9/2017.
 */

public class MainPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> arrayFragment;
    
    public MainPagerAdapter(FragmentManager fm, List<Fragment> arrayFragment) {
        super(fm);
        this.arrayFragment = arrayFragment;
    }
    
    @Override
    public Fragment getItem(int position) {
        return arrayFragment.get(position);
    }
    
    @Override
    public int getCount() {
        return arrayFragment.size();
    }
}
