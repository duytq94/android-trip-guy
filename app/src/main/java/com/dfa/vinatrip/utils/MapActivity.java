package com.dfa.vinatrip.utils;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.custom_view.NToolbar;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.ViewById;

@SuppressLint("Registered")
@EActivity(R.layout.activity_map)
public class MapActivity extends AppCompatActivity {
    @ViewById(R.id.activity_map_tb_ntoolbar)
    protected NToolbar nToolbar;
    @FragmentById(R.id.activity_map_fragment_map)
    protected MapFragment mapFragment;

    /*    @Extra
        protected int type;*/
    @Extra
    protected String title;
    @Extra
    protected double latitude;
    @Extra
    protected double longitude;

    private GoogleMap googleMap;

    @AfterViews
    void init() {
        nToolbar.setup(this, title);
        nToolbar.showLeftIcon();
        nToolbar.showToolbarColor();
        nToolbar.setOnLeftClickListener(v -> onBackPressed());

        mapFragment.onCreate(null);
        mapFragment.onResume();
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                LatLng latLng = new LatLng(latitude, longitude);
                googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(title)
                        /*.icon(BitmapDescriptorFactory.fromResource(type == TYPE_EVENT ? R.drawable.ic_hotel :
                                type == TYPE_HOTEL ? R.drawable.ic_hotel :
                                        type == TYPE_FOOD ? R.drawable.ic_food : R.drawable.ic_destination))*/
                ).showInfoWindow();

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition =
                        new CameraPosition.Builder().target(latLng).zoom(15).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
