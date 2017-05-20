package com.dfa.vinatrip.utils;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.province.detail_province.province_destination.ProvinceDestination;
import com.dfa.vinatrip.domains.main.province.detail_province.province_food.ProvinceFood;
import com.dfa.vinatrip.domains.main.province.detail_province.province_hotel.ProvinceHotel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_map)
public class MapActivity extends AppCompatActivity {

    @ViewById(R.id.my_toolbar)
    Toolbar toolbar;

    @FragmentById(R.id.activity_map_fragment_map)
    MapFragment mapFragment;

    private GoogleMap googleMap;
    private android.support.v7.app.ActionBar actionBar;
    private ProvinceHotel detailHotel;
    private ProvinceFood detailFood;
    private ProvinceDestination detailDestination;
    private String titleActionBar;
    private LatLng latLng;

    @AfterViews
    void onCreate() {
        setupActionBar();

        mapFragment.onCreate(null);
        mapFragment.onResume();
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // This method run after, so detailHotel, detailFood... can be assigned
                if (detailHotel != null) {
                    // For dropping a marker at a point on the Map
                    latLng = new LatLng(detailHotel.getLatitude(), detailHotel.getLongitude());
                    googleMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(detailHotel.getName())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_hotel)))
                            .showInfoWindow();
                }

                if (detailFood != null) {
                    // For dropping a marker at a point on the Map
                    latLng = new LatLng(detailFood.getLatitude(), detailFood.getLongitude());
                    googleMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(detailFood.getName())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_food)))
                            .showInfoWindow();
                }

                if (detailDestination != null) {
                    // For dropping a marker at a point on the Map
                    latLng = new LatLng(detailDestination.getLatitude(),
                            detailDestination.getLongitude());
                    googleMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(detailDestination.getName())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_destination)))
                            .showInfoWindow();
                }

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition =
                        new CameraPosition.Builder().target(latLng).zoom(15).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });


        changeColorStatusBar();
    }

    public void changeColorStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorStatusBar));
        }
    }

    public void setupActionBar() {
        if (getIntent().getParcelableExtra("DetailHotel") != null) {
            // Get ProvinceHotel form EachProvinceHotelFragment
            detailHotel = (ProvinceHotel) getIntent().getParcelableExtra("DetailHotel");
            titleActionBar = detailHotel.getName();
        }
        if (getIntent().getParcelableExtra("DetailFood") != null) {
            // Get ProvinceFood from EachProvinceFoodFragment
            detailFood = (ProvinceFood) getIntent().getParcelableExtra("DetailFood");
            titleActionBar = detailFood.getName();
        }
        if (getIntent().getParcelableExtra("DetailDestination") != null) {
            // Get ProvinceDestination from EachProvinceDestinationFragment
            detailDestination = getIntent().getParcelableExtra("DetailDestination");
            titleActionBar = detailDestination.getName();
        }

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(titleActionBar);

            // Set button back
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
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
