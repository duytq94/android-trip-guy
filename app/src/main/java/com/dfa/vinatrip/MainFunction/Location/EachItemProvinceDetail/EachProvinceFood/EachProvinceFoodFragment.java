package com.dfa.vinatrip.MainFunction.Location.EachItemProvinceDetail.EachProvinceFood;

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
import com.dfa.vinatrip.MainFunction.Location.ProvinceDetail.ProvinceFood.ProvinceFood;
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

public class EachProvinceFoodFragment extends Fragment {

    private TextView tvAddress, tvDescription, tvPhone;
    private ProvinceFood detailFood;
    private LinearLayout llAddress, llPhone;
    private RecyclerView rvProvinceFoodPhotos;
    private SwipeRefreshLayout srlReload;
    private List<String> listUrlPhotos;
    private ProvinceFoodPhotoAdapter provinceFoodPhotoAdapter;
    private ImageView ivMap;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_each_province_food, container, false);

        findViewByIds(view);

        // Get the Food be chosen from EachItemProvinceDetailActivity
        detailFood = (ProvinceFood) getArguments().getSerializable("DetailFood");

        srlReload.setColorSchemeResources(R.color.colorMain);
        if (CheckNetwork.isNetworkConnected(getActivity())) {
            loadProvinceFoodPhotoFromFirebase();
        }
        srlReload.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (CheckNetwork.isNetworkConnected(getActivity())) {
                    listUrlPhotos.clear();
                    loadProvinceFoodPhotoFromFirebase();
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
        rvProvinceFoodPhotos.setLayoutManager(staggeredGridLayoutManager);
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
                    Intent intentCall =
                            new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + detailFood.getPhone()));
                    startActivity(intentCall);
                }
            }
        });

        llAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMap = new Intent(getActivity(), MapActivity.class);

                // Send ProvinceFood to MapActivity
                intentMap.putExtra("DetailFood", detailFood);
                getActivity().startActivity(intentMap);
            }
        });
    }

    public void findViewByIds(View view) {
        tvAddress = (TextView) view.findViewById(R.id.fragment_each_province_food_tv_address);
        tvDescription = (TextView) view.findViewById(R.id.fragment_each_province_food_tv_description);
        tvPhone = (TextView) view.findViewById(R.id.fragment_each_province_food_tv_phone);
        llAddress = (LinearLayout) view.findViewById(R.id.fragment_each_province_food_ll_address);
        llPhone = (LinearLayout) view.findViewById(R.id.fragment_each_province_food_ll_phone);
        srlReload = (SwipeRefreshLayout) view.findViewById(R.id.fragment_each_province_food_srlReload);
        rvProvinceFoodPhotos = (RecyclerView) view.findViewById(R.id.fragment_each_province_food_rv_photos);
        ivMap = (ImageView) view.findViewById(R.id.fragment_each_province_food_iv_map);
    }

    public void setContentViews() {
        tvDescription.setText(detailFood.getDescription());
        tvPhone.setText(detailFood.getPhone());
        tvAddress.setText(detailFood.getAddress());

        listUrlPhotos = new ArrayList<>();
        provinceFoodPhotoAdapter =
                new ProvinceFoodPhotoAdapter(getActivity(), listUrlPhotos, srlReload);
        rvProvinceFoodPhotos.setAdapter(provinceFoodPhotoAdapter);

        // Load static map
        String url = "http://maps.google.com/maps/api/staticmap?center="
                + detailFood.getLatitude()
                + "," + detailFood.getLongitude()
                + "&zoom=15&size=100x120&sensor=false";
        Picasso.with(getActivity()).load(url)
                .into(ivMap, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    public void loadProvinceFoodPhotoFromFirebase() {
        srlReload.setRefreshing(true);

        DatabaseReference databaseReference = firebaseDatabase.getReference();

        // if no Internet, this method will not run
        databaseReference
                .child("ProvinceFoodPhoto")
                .child(detailFood.getProvince())
                .child(detailFood.getName())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String url;
                        url = dataSnapshot.child("url").getValue().toString();

                        listUrlPhotos.add(url);
                        provinceFoodPhotoAdapter.notifyDataSetChanged();
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