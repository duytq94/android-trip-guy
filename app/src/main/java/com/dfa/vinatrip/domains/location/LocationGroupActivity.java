package com.dfa.vinatrip.domains.location;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.location_my_friend.UserFriendMarker;
import com.dfa.vinatrip.domains.main.location_my_friend.UserLocation;
import com.dfa.vinatrip.domains.main.me.UserProfile;
import com.dfa.vinatrip.domains.main.me.detail_me.make_friend.UserFriend;
import com.dfa.vinatrip.domains.main.plan.Plan;
import com.dfa.vinatrip.services.DataService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.client.IO;
import io.socket.client.Socket;

import static com.dfa.vinatrip.ApiUrls.SERVER_SOCKET_LOCATION;

@EActivity(R.layout.activity_location_group)
public class LocationGroupActivity extends AppCompatActivity {

    @Bean
    DataService dataService;

    @Extra
    protected Plan plan;

    @FragmentById(value = R.id.activity_location_group_map_my_friend)
    protected SupportMapFragment smfMyFriend;

    @ViewById(R.id.activity_location_group_iv_turn_location)
    protected ImageView ivTurnLocation;

    private GoogleMap googleMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location location;
    private DatabaseReference databaseReference;
    private UserProfile currentUser;
    private List<UserFriend> userFriendList;
    private List<UserLocation> userLocationList;
    private ImageLoader imageLoader;
    private Marker markerCurrentUser;
    private List<UserFriendMarker> userFriendMarkerList;
    private Boolean statusGPS;
    private BroadcastReceiver broadcastReceiver;
    private IntentFilter filter;
    public static final int REQUEST_TURN_GPS = 1;

    private Socket socket;

    @AfterViews
    public void init() {
        try {
            socket = IO.socket(SERVER_SOCKET_LOCATION);
            socket.connect();

            databaseReference = FirebaseDatabase.getInstance().getReference();
            currentUser = dataService.getCurrentUser();

            socket.emit("join_room", dataService.getCurrentUser().getEmail(), plan.getId());

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            changeIconLocation();
            initBroadcastReceiver();
            locationListener();
            askPermission();
            setup();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Click(R.id.activity_location_group_iv_info)
    public void onIvInfoClick() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Xem vị trí bạn bè");
        alertDialog.setMessage(getString(R.string.message_my_friend));
        alertDialog.setIcon(R.drawable.ic_symbol);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "XONG", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
    }

    @Click(R.id.activity_location_group_iv_turn_location)
    public void onIvTurnLocationClick() {
        if (ivTurnLocation.getTag().equals("gps_on")) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("GPS đang mở!");
            alertDialog.setMessage("Bạn bè sẽ nhìn thấy vị trí của bạn trên bản đồ.");
            alertDialog.setIcon(R.drawable.ic_symbol);
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "XONG", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alertDialog.show();
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("GPS đang tắt");
            alertDialog.setMessage("Bạn bè sẽ không nhìn thấy bạn, bạn có muốn mở?");
            alertDialog.setIcon(R.drawable.ic_symbol);
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "MỞ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intentTurnGPS = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intentTurnGPS, REQUEST_TURN_GPS);
                }
            });
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "HỦY", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alertDialog.show();
        }
    }

    public void askPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION};
                ActivityCompat.requestPermissions(this, permissions, 10);
            }
        }
    }

    public boolean checkPermission() {
        return ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED;
    }

    public void setup() {
        if (checkPermission()) {
            smfMyFriend.onCreate(null);
            smfMyFriend.onResume();
            smfMyFriend.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap mMap) {
                    ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(LocationGroupActivity.this).build();
                    ImageLoader.getInstance().init(config);
                    imageLoader = ImageLoader.getInstance();

                    googleMap = mMap;

                    userFriendMarkerList = new ArrayList<>();

                    userFriendList = dataService.getUserFriendList();

                    userLocationList = new ArrayList<>();
                    loadUserLocation();

                    getCurrentUserLocation();
                }
            });
        }
    }

    public void loadUserLocation() {
        // If no Internet, this method will not run
        databaseReference.child("UserLocation")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        UserLocation userLocation = dataSnapshot.getValue(UserLocation.class);
                        userLocationList.add(userLocation);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        UserLocation userLocation = dataSnapshot.getValue(
                                UserLocation.class);
                        for (UserFriend userFriend : userFriendList) {
                            if (userLocation.getUid().equals(userFriend.getFriendId())) {
                                getUserFriendLocation(userLocation);
                            }
                        }
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        // This method to be called after all the onChildAdded() calls have happened
        databaseReference.child("UserLocation")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (UserFriend userFriend : userFriendList) {
                            for (UserLocation userLocation : userLocationList) {
                                if (userFriend.getFriendId().equals(userLocation.getUid())) {
                                    getUserFriendLocation(userLocation);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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

    public void getCurrentUserLocation() {
        List<String> listProviders = locationManager.getAllProviders();
        for (String provider : listProviders) {
            if (locationManager.getLastKnownLocation(provider) != null) {
                location = locationManager.getLastKnownLocation(provider);
                uploadCurrentUserLocation(location);
                if (markerCurrentUser != null) {
                    markerCurrentUser.remove();
                }
                Picasso.with(this)
                        .load(currentUser.getAvatar())
                        .into(currentUserTarget);
            }
        }
    }

    public void getUserFriendLocation(final UserLocation userLocation) {
        for (int i = 0; i < userFriendMarkerList.size(); i++) {
            if (userLocation.getUid().equals(userFriendMarkerList.get(i).getFriendId())) {
                userFriendMarkerList.get(i).getMarker().remove();
            }
        }
        imageLoader.loadImage(userLocation.getAvatar(), new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                View viewMaker = LayoutInflater.from(LocationGroupActivity.this).inflate(R.layout.maker_avatar, null);
                CircleImageView civAvatar = viewMaker.findViewById(R.id.maker_avatar_civ_avatar);
                civAvatar.setImageBitmap(loadedImage);
                Bitmap bmAvatar = createBitmapFromView(viewMaker);

                LatLng latLng = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());

                Marker marker = googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(userLocation.getNickname())
                        .icon(BitmapDescriptorFactory.fromBitmap(bmAvatar)));
                marker.showInfoWindow();

                UserFriendMarker userFriendMarker = new UserFriendMarker(marker, userLocation.getUid());
                userFriendMarkerList.add(userFriendMarker);
            }
        });
    }

    private Target currentUserTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            View viewMaker = LayoutInflater.from(LocationGroupActivity.this).inflate(R.layout.maker_avatar, null);
            CircleImageView civAvatar = viewMaker.findViewById(R.id.maker_avatar_civ_avatar);
            civAvatar.setImageBitmap(bitmap);
            Bitmap bmAvatar = createBitmapFromView(viewMaker);

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            try {
                markerCurrentUser = googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Tôi")
                        .icon(BitmapDescriptorFactory.fromBitmap(bmAvatar)));
                markerCurrentUser.showInfoWindow();

                // For zooming automatically to the location of the markerCurrentUser
                CameraPosition cameraPosition =
                        new CameraPosition.Builder().target(latLng).zoom(17).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    public void uploadCurrentUserLocation(Location location) {
        UserLocation userLocation = new UserLocation(currentUser.getUid(),
                currentUser.getAvatar(),
                currentUser.getNickname(),
                location.getLatitude(),
                location.getLongitude());
        databaseReference.child("UserLocation").child(currentUser.getUid()).setValue(userLocation);
    }

    public Bitmap createBitmapFromView(View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    public void initBroadcastReceiver() {
        filter = new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                changeIconLocation();
            }
        };
    }

    public void changeIconLocation() {
        statusGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (statusGPS) {
            ivTurnLocation.setImageResource(R.drawable.ic_location);
            ivTurnLocation.setTag("gps_on");
        } else {
            ivTurnLocation.setImageResource(R.drawable.ic_location_off);
            ivTurnLocation.setTag("gps_off");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (locationManager != null && checkPermission()) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 5, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, locationListener);
            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 1000, 5, locationListener);
            if (broadcastReceiver == null) initBroadcastReceiver();
            registerReceiver(broadcastReceiver, filter);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (locationManager != null && checkPermission()) {
            locationManager.removeUpdates(locationListener);
            unregisterReceiver(broadcastReceiver);
        }
    }

    @OnActivityResult(REQUEST_TURN_GPS)
    public void onResult(int resultCode, Intent data) {
        // Don't check the result code
        changeIconLocation();
    }
}
