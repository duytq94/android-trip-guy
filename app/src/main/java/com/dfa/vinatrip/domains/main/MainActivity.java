package com.dfa.vinatrip.domains.main;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.custom_view.NToolbar;
import com.dfa.vinatrip.custom_view.NonSwipeViewPager;
import com.dfa.vinatrip.domains.main.adapter.MainPagerAdapter;
import com.dfa.vinatrip.domains.main.fragment.me.MeFragment_;
import com.dfa.vinatrip.domains.main.fragment.plan.PlanFragment_;
import com.dfa.vinatrip.domains.main.fragment.province.ProvinceFragment_;
import com.dfa.vinatrip.domains.main.fragment.share.ShareFragment_;
import com.dfa.vinatrip.domains.main.fragment.trend.TrendFragment_;
import com.dfa.vinatrip.utils.StopShiftModeBottomNavView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewById(R.id.activity_main_toolbar)
    protected NToolbar nToolbar;
    @ViewById(R.id.activity_main_nsvp_viewpager)
    protected NonSwipeViewPager vpFragment;
    @ViewById(R.id.activity_main_bnv_menu)
    protected BottomNavigationView bnvMenu;

    @AfterViews
    public void init() {
        nToolbar.setup(this, "TripGuy");
        nToolbar.showAppIcon();
        nToolbar.showToolbarColor();

        List<Fragment> arrayFragment = new ArrayList<>();
        arrayFragment.add(ProvinceFragment_.builder().build());
        arrayFragment.add(TrendFragment_.builder().build());
        arrayFragment.add(PlanFragment_.builder().build());
        arrayFragment.add(ShareFragment_.builder().build());
        arrayFragment.add(MeFragment_.builder().build());

        vpFragment.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), arrayFragment));
        vpFragment.setOffscreenPageLimit(arrayFragment.size());

        StopShiftModeBottomNavView.disableShiftMode(bnvMenu);
        bnvMenu.setOnNavigationItemSelectedListener
                (item -> {
                    switch (item.getItemId()) {
                        case R.id.iconLocation:
                            vpFragment.setCurrentItem(0, false);
                            break;
                        case R.id.iconTrend:
                            vpFragment.setCurrentItem(1, false);
                            break;
                        case R.id.iconPlan:
                            vpFragment.setCurrentItem(2, false);
                            break;
                        case R.id.iconShare:
                            vpFragment.setCurrentItem(3, false);
                            break;
                        case R.id.iconMe:
                            vpFragment.setCurrentItem(4, false);
                            break;
                    }
                    // Must true so item in bottom bar can transform
                    return true;
                });
    }
}

