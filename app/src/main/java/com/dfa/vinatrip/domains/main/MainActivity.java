package com.dfa.vinatrip.domains.main;

import android.app.SearchManager;
import android.content.Context;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.custom_view.NToolbar;
import com.dfa.vinatrip.custom_view.NonSwipeViewPager;
import com.dfa.vinatrip.domains.main.adapter.MainPagerAdapter;
import com.dfa.vinatrip.domains.main.fragment.deal.DealFragment_;
import com.dfa.vinatrip.domains.main.fragment.me.MeFragment_;
import com.dfa.vinatrip.domains.main.fragment.plan.PlanFragment_;
import com.dfa.vinatrip.domains.main.fragment.province.ProvinceFragment_;
import com.dfa.vinatrip.domains.main.fragment.trend.TrendFragment_;
import com.dfa.vinatrip.utils.StopShiftModeBottomNavView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;

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

    private SearchView searchView;

    @AfterViews
    public void init() {
        nToolbar.setup(this, "TripGuy");
        nToolbar.showAppIcon();
        nToolbar.showToolbarColor();

        List<Fragment> arrayFragment = new ArrayList<>();
        arrayFragment.add(ProvinceFragment_.builder().build());
        arrayFragment.add(TrendFragment_.builder().build());
        arrayFragment.add(PlanFragment_.builder().build());
        arrayFragment.add(DealFragment_.builder().build());
        arrayFragment.add(MeFragment_.builder().build());

        vpFragment.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), arrayFragment));
        vpFragment.setOffscreenPageLimit(arrayFragment.size());

        StopShiftModeBottomNavView.disableShiftMode(bnvMenu);
        bnvMenu.setOnNavigationItemSelectedListener
                (item -> {
                    switch (item.getItemId()) {
                        case R.id.iconLocation:
                            vpFragment.setCurrentItem(0, false);
                            searchView.setVisibility(View.GONE);
                            break;
                        case R.id.iconTrend:
                            vpFragment.setCurrentItem(1, false);
                            searchView.setVisibility(View.GONE);
                            break;
                        case R.id.iconPlan:
                            vpFragment.setCurrentItem(2, false);
                            searchView.setVisibility(View.GONE);
                            break;
                        case R.id.iconDeal:
                            vpFragment.setCurrentItem(3, false);
                            searchView.setVisibility(View.VISIBLE);
                            searchView.setQueryHint("Tìm deal");
                            break;
                        case R.id.iconMe:
                            vpFragment.setCurrentItem(4, false);
                            searchView.setVisibility(View.GONE);
                            break;
                    }
                    // Must true so item in bottom bar can transform
                    return true;
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search_menu_menuSearch).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setVisibility(View.GONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                EventBus.getDefault().post(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }
}

