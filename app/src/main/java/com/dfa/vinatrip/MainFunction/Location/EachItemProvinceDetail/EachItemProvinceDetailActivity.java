package com.dfa.vinatrip.MainFunction.Location.EachItemProvinceDetail;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.dfa.vinatrip.MainFunction.Location.EachItemProvinceDetail.EachProvinceDestination.EachProvinceDestinationFragment;
import com.dfa.vinatrip.MainFunction.Location.EachItemProvinceDetail.EachProvinceFood.EachProvinceFoodFragment;
import com.dfa.vinatrip.MainFunction.Location.EachItemProvinceDetail.EachProvinceHotel.EachProvinceHotelFragment;
import com.dfa.vinatrip.MainFunction.Location.ProvinceDetail.ProvinceDestination.ProvinceDestination;
import com.dfa.vinatrip.MainFunction.Location.ProvinceDetail.ProvinceFood.ProvinceFood;
import com.dfa.vinatrip.MainFunction.Location.ProvinceDetail.ProvinceHotel.ProvinceHotel;
import com.dfa.vinatrip.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_each_item_province_detail)
public class EachItemProvinceDetailActivity extends AppCompatActivity {


    @ViewById(R.id.my_toolbar)
    Toolbar toolbar;

    private ActionBar actionBar;


    private EachProvinceHotelFragment eachProvinceHotelFragment;
    private EachProvinceFoodFragment eachProvinceFoodFragment;
    private EachProvinceDestinationFragment eachProvinceDestinationFragment;
    private String titleActionBar;

    @AfterViews()
    void onCreate() {
        selectFragment();
        changeColorStatusBar();
        setupActionBar();
    }

    public void selectFragment() {
        // Get the Hotel be chosen from ProvinceHotelFragment
        if (getIntent().getSerializableExtra("DetailHotel") != null) {
            ProvinceHotel detailHotel = (ProvinceHotel) getIntent().getSerializableExtra("DetailHotel");
            titleActionBar = detailHotel.getName();
            Bundle bundleHotel = new Bundle();
            bundleHotel.putSerializable("DetailHotel", detailHotel);
            eachProvinceHotelFragment = new EachProvinceHotelFragment();
            // Send the Hotel be chosen to EachProvinceHotelFragment
            eachProvinceHotelFragment.setArguments(bundleHotel);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_each_item_province_detail_fl_container, eachProvinceHotelFragment)
                    .commit();
            return;
        }

        // Get the Destination be chosen from ProvinceDestinationFragment
        if (getIntent().getSerializableExtra("DetailDestination") != null) {
            ProvinceDestination detailDestination = (ProvinceDestination) getIntent().getSerializableExtra("DetailDestination");
            titleActionBar = detailDestination.getName();


            Bundle bundleDestination = new Bundle();
            bundleDestination.putSerializable("DetailDestination", detailDestination);
            eachProvinceDestinationFragment = new EachProvinceDestinationFragment();
            // Send the Hotel be chosen to EachProvinceHotelFragment
            eachProvinceDestinationFragment.setArguments(bundleDestination);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_each_item_province_detail_fl_container, eachProvinceDestinationFragment)
                    .commit();
            return;
        }

        if (getIntent().getSerializableExtra("DetailFood") != null) {
            // Get the Food be chosen from ProvinceFoodFragment
            ProvinceFood detailFood = (ProvinceFood) getIntent().getSerializableExtra("DetailFood");
            titleActionBar = detailFood.getName();
            Bundle bundleFood = new Bundle();
            bundleFood.putSerializable("DetailFood", detailFood);
            eachProvinceFoodFragment = new EachProvinceFoodFragment();
            // Send the Food be chosen to EachProvinceHotelFragment
            eachProvinceFoodFragment.setArguments(bundleFood);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_each_item_province_detail_fl_container, eachProvinceFoodFragment)
                    .commit();
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

    public void changeColorStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorStatusBar));
        }
    }

    public void setupActionBar() {
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(titleActionBar);

            // Set button back
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
