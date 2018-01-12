package com.dfa.vinatrip.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.custom_view.NToolbar;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import static com.dfa.vinatrip.utils.Constants.REQUEST_PERMISSION_LOCATION;

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
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location location;
    private GoogleMap googleMap;
    private Polyline currentPolyline;

    @AfterViews
    void init() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener();
        requestPermission();

        nToolbar.setup(this, title);
        nToolbar.showLeftIcon();
        nToolbar.showToolbarColor();
        nToolbar.setOnLeftClickListener(v -> onBackPressed());

        mapFragment.onCreate(null);
        mapFragment.onResume();
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                if (isPermissionGranted()) {
                    googleMap.setMyLocationEnabled(true);
                } else {
                    googleMap.setMyLocationEnabled(false);
                }

                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        if (isPermissionGranted()) {
                            if (location != null) {
                                getDirection(new LatLng(location.getLatitude(),
                                        location.getLongitude()), marker.getPosition());
                            }
                        } else {
                            Toast.makeText(MapActivity.this, "Bạn chưa cấp quyền truy cập vị trí", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                LatLng latLng = new LatLng(latitude, longitude);
                googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(title)
                        .snippet("Chỉ đường tới đây")
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

    public void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION_LOCATION);
            }
        }
    }

    public boolean isPermissionGranted() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public void locationListener() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                getCurrentUserLocation();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
    }

    @SuppressLint("MissingPermission")
    public void getCurrentUserLocation() {
        List<String> listProviders = locationManager.getAllProviders();
        for (String provider : listProviders) {
            if (locationManager.getLastKnownLocation(provider) != null) {
                location = locationManager.getLastKnownLocation(provider);
            }
        }
    }

    public void getDirection(LatLng from, LatLng to) {
        GoogleDirection.withServerKey(getString(R.string.google_api_key))
                .from(new LatLng(from.latitude, from.longitude))
                .to(new LatLng(to.latitude, to.longitude))
                .avoid(AvoidType.FERRIES)
                .avoid(AvoidType.HIGHWAYS)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if (direction.isOK()) {
                            if (currentPolyline != null) {
                                currentPolyline.remove();
                            }

                            Route route = direction.getRouteList().get(0);
                            ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
                            currentPolyline = googleMap.addPolyline(DirectionConverter.createPolyline(MapActivity.this,
                                    directionPositionList, 5, Color.RED));
                        } else {
                            Toast.makeText(MapActivity.this, direction.getStatus(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        Toast.makeText(MapActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onResume() {
        super.onResume();
        if (locationManager != null && isPermissionGranted()) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 5, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, locationListener);
            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 1000, 5, locationListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (locationManager != null && isPermissionGranted()) {
            locationManager.removeUpdates(locationListener);
        }
    }
}
