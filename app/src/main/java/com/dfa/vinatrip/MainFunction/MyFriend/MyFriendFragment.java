package com.dfa.vinatrip.MainFunction.MyFriend;

import android.Manifest;
import android.content.Context;
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
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dfa.vinatrip.CheckNetwork;
import com.dfa.vinatrip.MainFunction.Me.UserDetail.MakeFriend.UserFriend;
import com.dfa.vinatrip.MainFunction.Me.UserProfile;
import com.dfa.vinatrip.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentById;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

@EFragment(R.layout.fragment_my_friend)
public class MyFriendFragment extends Fragment {

    @FragmentById(value = R.id.fragment_my_friend_map_my_friend, childFragment = true)
    SupportMapFragment smfMyFriend;

    private GoogleMap googleMap;
    private FirebaseUser firebaseUser;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location location;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private UserProfile currentUser;
    private List<UserFriend> listUserFriends;

    @AfterViews
    void onCreateView() {
        if (CheckNetwork.isNetworkConnected(getActivity())) {
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if (firebaseUser != null) {
                askPermission();
                initViews();
            }
        }
    }

    public void askPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
                ActivityCompat.requestPermissions(getActivity(), permissions, 10);
            }
        }
    }

    public boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public void initViews() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        smfMyFriend.onCreate(null);
        smfMyFriend.onResume();
        smfMyFriend.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                if (checkPermission() && firebaseUser != null) {
                    googleMap = mMap;
                    loadUserProfile();
                    loadUserFriend();
                    locationListener();
                }
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
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        List<String> listProviders = locationManager.getAllProviders();
        for (String provider : listProviders) {
            location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                Picasso.with(getActivity())
                        .load(currentUser.getAvatar())
                        .into(currentUserTarget);
            }
        }
    }

    public void getUserFriendLocation(UserFriend userFriend) {
        View viewMaker = LayoutInflater.from(getActivity()).inflate(R.layout.maker_avatar, null);
        CircleImageView civAvatar = (CircleImageView) viewMaker.findViewById(R.id.maker_avatar_civ_avatar);

        LatLng latLng = new LatLng(10.799420, 106.641175);
        civAvatar.setImageResource(R.drawable.ic_avatar);

        Bitmap bmAvatar = createBitmapFromView(viewMaker);
        googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("aaa")
                .icon(BitmapDescriptorFactory.fromBitmap(bmAvatar)))
                .showInfoWindow();

    }

    private Target currentUserTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            View viewMaker = LayoutInflater.from(getActivity()).inflate(R.layout.maker_avatar, null);
            CircleImageView civAvatar = (CircleImageView) viewMaker.findViewById(R.id.maker_avatar_civ_avatar);

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            civAvatar.setImageBitmap(bitmap);

            Bitmap bmAvatar = createBitmapFromView(viewMaker);
            googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(currentUser.getNickname())
                    .icon(BitmapDescriptorFactory.fromBitmap(bmAvatar)))
                    .showInfoWindow();

            // For zooming automatically to the location of the marker
            CameraPosition cameraPosition =
                    new CameraPosition.Builder().target(latLng).zoom(15).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    public Bitmap createBitmapFromView(View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    public void loadUserProfile() {
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        // If no Internet, this method will not run
        databaseReference.child("UserProfile").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UserProfile result = dataSnapshot.getValue(UserProfile.class);
                if (result.getUid().equals(firebaseUser.getUid())) {
                    currentUser = result;
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
        databaseReference.child("UserProfile")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        getCurrentUserLocation();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void loadUserFriend() {
        final DatabaseReference referenceFriend = firebaseDatabase.getReference();

        // If no Internet, this method will not run
        referenceFriend.child("UserFriend").child(firebaseUser.getUid())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        UserFriend userFriend = dataSnapshot.getValue(UserFriend.class);

                        // Don't add the friend not agree yet to list
                        if (!userFriend.getFriendId().equals(firebaseUser.getUid()) &&
                                userFriend.getState().equals("friend")) {
                            listUserFriends.add(userFriend);
                            getUserFriendLocation(userFriend);
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
    }
}
