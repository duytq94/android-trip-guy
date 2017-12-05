package com.dfa.vinatrip.utils;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.dfa.vinatrip.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

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
    private String titleActionBar;
    
    //    @Extra
//    ProvinceHotel detailHotel;
//    @Extra
//    ProvinceFood detailFood;
/*    @Extra
    ProvinceDestinationDetail detailDestination;
    @Extra
    LatLng latLng;*/
    
    @AfterViews
    void init() {
        setupActionBar();

//        mapFragment.onCreate(null);
//        mapFragment.onResume();
//        mapFragment.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(GoogleMap mMap) {
//                googleMap = mMap;
//
//                if (detailHotel != null) {
//                    // For dropping a marker at a point on the Map
//                    latLng = new LatLng(detailHotel.getLatitude(), detailHotel.getLongitude());
//                    googleMap.addMarker(new MarkerOptions()
//                                                .position(latLng)
//                                                .title(detailHotel.getName())
//                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_hotel)))
//                             .showInfoWindow();
//                }
//
//                if (detailFood != null) {
//                    // For dropping a marker at a point on the Map
//                    latLng = new LatLng(detailFood.getLatitude(), detailFood.getLongitude());
//                    googleMap.addMarker(new MarkerOptions()
//                                                .position(latLng)
//                                                .title(detailFood.getName())
//                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_food)))
//                             .showInfoWindow();
//                }
//
//                if (detailDestination != null) {
//                    // For dropping a marker at a point on the Map
//                    latLng = new LatLng(detailDestination.getLatitude(), detailDestination.getLongitude());
//                    googleMap.addMarker(new MarkerOptions()
//                                                .position(latLng)
//                                                .title(detailDestination.getName())
//                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_destination)))
//                             .showInfoWindow();
//                }
//
//                // For zooming automatically to the location of the marker
//                CameraPosition cameraPosition =
//                        new CameraPosition.Builder().target(latLng).zoom(15).build();
//                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//            }
//        });
    }
    
    public void setupActionBar() {
//        if (detailHotel != null) {
//            // Get ProvinceHotel form EachProvinceHotelFragment
//            titleActionBar = detailHotel.getName();
//        }
//        if (detailFood != null) {
//            // Get ProvinceFood from EachProvinceFoodFragment
//            titleActionBar = detailFood.getName();
//        }
//        if (detailDestination != null) {
//            // Get ProvinceDestination from EachProvinceDestinationFragment
//            titleActionBar = detailDestination.getName();
//        }
//
//        setSupportActionBar(toolbar);
//        actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setTitle(titleActionBar);
//
//            // Set button back
//            actionBar.setHomeButtonEnabled(true);
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            super.onBackPressed();
//            return true;
//        }
//        return false;
//    }
}
