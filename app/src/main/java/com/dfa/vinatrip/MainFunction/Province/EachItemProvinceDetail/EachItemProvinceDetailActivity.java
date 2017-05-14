package com.dfa.vinatrip.MainFunction.Province.EachItemProvinceDetail;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.dfa.vinatrip.MainFunction.Province.EachItemProvinceDetail.EachProvinceDestination.EachProvinceDestinationFragment;
import com.dfa.vinatrip.MainFunction.Province.EachItemProvinceDetail.EachProvinceDestination.EachProvinceDestinationFragment_;
import com.dfa.vinatrip.MainFunction.Province.EachItemProvinceDetail.EachProvinceFood.EachProvinceFoodFragment;
import com.dfa.vinatrip.MainFunction.Province.EachItemProvinceDetail.EachProvinceFood.EachProvinceFoodFragment_;
import com.dfa.vinatrip.MainFunction.Province.EachItemProvinceDetail.EachProvinceHotel.EachProvinceHotelFragment;
import com.dfa.vinatrip.MainFunction.Province.EachItemProvinceDetail.EachProvinceHotel.EachProvinceHotelFragment_;
import com.dfa.vinatrip.MainFunction.Province.ProvinceDetail.ProvinceDestination.ProvinceDestination;
import com.dfa.vinatrip.MainFunction.Province.ProvinceDetail.ProvinceFood.ProvinceFood;
import com.dfa.vinatrip.MainFunction.Province.ProvinceDetail.ProvinceHotel.ProvinceHotel;
import com.dfa.vinatrip.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
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

    @Extra
    ProvinceHotel detailHotel;
    @Extra
    ProvinceDestination detailDestination;
    @Extra
    ProvinceFood detailFood;

    @AfterViews()
    void onCreate() {
        selectFragment();
        changeColorStatusBar();
        setupActionBar();
    }

    public void selectFragment() {
        // Get the Hotel be chosen from ProvinceHotelFragment
        if (detailHotel != null) {
            titleActionBar = detailHotel.getName();
            Bundle bundleHotel = new Bundle();
            bundleHotel.putParcelable("DetailHotel", detailHotel);
            eachProvinceHotelFragment = new EachProvinceHotelFragment_();
            // Send the Hotel be chosen to EachProvinceHotelFragment
            eachProvinceHotelFragment.setArguments(bundleHotel);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_each_item_province_detail_fl_container, eachProvinceHotelFragment)
                    .commit();
            return;
        }

        // Get the Destination be chosen from ProvinceDestinationFragment
        if (detailDestination != null) {
            titleActionBar = detailDestination.getName();

            Bundle bundleDestination = new Bundle();
            bundleDestination.putParcelable("DetailDestination", detailDestination);
            eachProvinceDestinationFragment = new EachProvinceDestinationFragment_();
            // Send the Hotel be chosen to EachProvinceHotelFragment
            eachProvinceDestinationFragment.setArguments(bundleDestination);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_each_item_province_detail_fl_container, eachProvinceDestinationFragment)
                    .commit();
            return;
        }

        if (detailFood != null) {
            // Get the Food be chosen from ProvinceFoodFragment
            titleActionBar = detailFood.getName();

            Bundle bundleFood = new Bundle();
            bundleFood.putParcelable("DetailFood", detailFood);
            eachProvinceFoodFragment = new EachProvinceFoodFragment_();
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
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
