package com.dfa.vinatrip.MainFunction.Memory;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dfa.vinatrip.MainFunction.ViewPagerSwipeFragmentAdapter;
import com.dfa.vinatrip.R;

public class MemoryFragment extends Fragment {

    private TabLayout tlMenu;
    private ViewPager vpMenu;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memory, container, false);

        vpMenu = (ViewPager) view.findViewById(R.id.fragment_memory_vp_menu);
        tlMenu = (TabLayout) view.findViewById(R.id.fragment_memory_tl_menu);
        tlMenu.setupWithViewPager(vpMenu);
        setupViewPager(vpMenu);
        return view;
    }

    public void setupViewPager(ViewPager vpMenu) {
        ViewPagerSwipeFragmentAdapter adapter = new ViewPagerSwipeFragmentAdapter(getChildFragmentManager());
    }
}
