package com.dfa.vinatrip.MainFunction.Location.ProvinceDetail;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.dfa.vinatrip.MainFunction.Location.Province;
import com.dfa.vinatrip.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

// Control when user click item about hotel, description...
@EActivity(R.layout.activity_province_detail)
public class ProvinceDetailActivity extends AppCompatActivity {

    private Province province;

    @ViewById(R.id.my_toolbar)
    Toolbar toolbar;

    private android.support.v7.app.ActionBar actionBar;

    @AfterViews
    void onCreate() {
        changeColorStatusBar();

        // Get Province from LocationFragment
        province = (Province) getIntent().getSerializableExtra("Province");
        ProvinceDetailFragment provinceDetailFragment = new ProvinceDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Province", province);
        provinceDetailFragment.setArguments(bundle);

        setupActionBar();

        startFragment(provinceDetailFragment);
    }

    public void setupActionBar() {
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(province.getName());
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#228B22")));

            // Set button back
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void startFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        String fragmentTag = backStateName;

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null) {
            //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.activity_province_detail_fl_container, fragment, fragmentTag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        }
    }

    public void changeColorStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorStatusBar));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return false;
    }
}
