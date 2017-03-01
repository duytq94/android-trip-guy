package com.dfa.vinatrip.MainFunction.Location.EachItemProvinceDetail.EachProvinceHotel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfa.vinatrip.CheckNetwork;
import com.dfa.vinatrip.MainFunction.Location.ProvinceDetail.ProvinceHotel.ProvinceHotel;
import com.dfa.vinatrip.MapActivity;
import com.dfa.vinatrip.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EachProvinceHotelFragment extends Fragment {

    private TextView tvAddress, tvDescription, tvMail, tvPhone, tvWebsite;
    private ProvinceHotel detailHotel;
    private LinearLayout llAddress, llPhone, llWebsite;
    private RecyclerView rvProvinceHotelPhotos;
    private SwipeRefreshLayout srlReload;
    private List<String> listUrlPhotos;
    private ProvinceHotelPhotoAdapter provinceHotelPhotoAdapter;
    private ImageView ivMap;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_each_province_hotel, container, false);

        findViewByIds(view);

        // Get the Hotel be chosen from EachItemProvinceDetailActivity
        detailHotel = (ProvinceHotel) getArguments().getSerializable("DetailHotel");

        srlReload.setColorSchemeResources(R.color.colorMain);
        if (CheckNetwork.isNetworkConnected(getActivity())) {
            loadProvinceHotelPhotoFromFirebase();
        }
        srlReload.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (CheckNetwork.isNetworkConnected(getActivity())) {
                    listUrlPhotos.clear();
                    loadProvinceHotelPhotoFromFirebase();
                } else {
                    srlReload.setRefreshing(false);
                }
            }
        });

        // set content for views
        setContentViews();

        setViewsOnClick();
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        rvProvinceHotelPhotos.setLayoutManager(staggeredGridLayoutManager);
        return view;
    }

    public void setViewsOnClick() {
        llPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CALL_PHONE}, 1);
                } else {
                    Intent intentCall = new Intent(Intent.ACTION_CALL,
                            Uri.parse("tel:" + detailHotel.getPhone()));
                    startActivity(intentCall);
                }
            }
        });

        llWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentGoWebsite = new Intent(Intent.ACTION_VIEW);
                intentGoWebsite.setData(Uri.parse(detailHotel.getWebsite()));
                startActivity(intentGoWebsite);
            }
        });

        llAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMap = new Intent(getActivity(), MapActivity.class);

                // Send ProvinceHotel to MapActivity
                intentMap.putExtra("DetailHotel", detailHotel);
                getActivity().startActivity(intentMap);
            }
        });
    }

    public void findViewByIds(View view) {
        tvAddress = (TextView) view.findViewById(R.id.fragment_each_province_hotel_tv_address);
        tvDescription = (TextView) view.findViewById(R.id.fragment_each_province_hotel_tv_description);
        tvMail = (TextView) view.findViewById(R.id.fragment_each_province_hotel_tv_mail);
        tvPhone = (TextView) view.findViewById(R.id.fragment_each_province_hotel_tv_phone);
        tvWebsite = (TextView) view.findViewById(R.id.fragment_each_province_hotel_tv_website);
        llAddress = (LinearLayout) view.findViewById(R.id.fragment_each_province_hotel_ll_address);
        llPhone = (LinearLayout) view.findViewById(R.id.fragment_each_province_hotel_ll_phone);
        llWebsite = (LinearLayout) view.findViewById(R.id.fragment_each_province_hotel_ll_website);
        srlReload = (SwipeRefreshLayout) view.findViewById(R.id.fragment_each_province_hotel_srlReload);
        rvProvinceHotelPhotos = (RecyclerView) view.findViewById(R.id.fragment_each_province_hotel_rv_photos);
        ivMap = (ImageView) view.findViewById(R.id.fragment_each_province_hotel_iv_map);
    }

    public void setContentViews() {
        tvDescription.setText(detailHotel.getDescription());
        tvMail.setText(detailHotel.getMail());
        tvPhone.setText(detailHotel.getPhone());
        tvAddress.setText(detailHotel.getAddress());
        tvWebsite.setText(detailHotel.getWebsite());

        listUrlPhotos = new ArrayList<>();
        provinceHotelPhotoAdapter =
                new ProvinceHotelPhotoAdapter(getActivity(), listUrlPhotos, srlReload);
        rvProvinceHotelPhotos.setAdapter(provinceHotelPhotoAdapter);

        // Load static map
        String url = "http://maps.google.com/maps/api/staticmap?center="
                + detailHotel.getLatitude()
                + "," + detailHotel.getLongitude()
                + "&zoom=15&size=100x120&sensor=false";
        Picasso.with(getActivity())
                .load(url)
                .into(ivMap, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    public void loadProvinceHotelPhotoFromFirebase() {
        srlReload.setRefreshing(true);

        DatabaseReference databaseReference = firebaseDatabase.getReference();

        // if no Internet, this method will not run
        databaseReference
                .child("ProvinceHotelPhoto")
                .child(detailHotel.getProvince())
                .child(detailHotel.getName())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String url;
                        url = dataSnapshot.child("url").getValue().toString();

                        listUrlPhotos.add(url);
                        provinceHotelPhotoAdapter.notifyDataSetChanged();
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
