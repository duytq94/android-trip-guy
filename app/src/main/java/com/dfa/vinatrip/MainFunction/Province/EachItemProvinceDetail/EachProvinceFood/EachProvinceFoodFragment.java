package com.dfa.vinatrip.MainFunction.Province.EachItemProvinceDetail.EachProvinceFood;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfa.vinatrip.MainFunction.Province.ProvinceDetail.ProvinceFood.ProvinceFood;
import com.dfa.vinatrip.MapActivity_;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.TripGuyUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_each_province_food)
public class EachProvinceFoodFragment extends Fragment {

    @ViewById(R.id.fragment_each_province_food_tv_address)
    TextView tvAddress;

    @ViewById(R.id.fragment_each_province_food_tv_description)
    TextView tvDescription;

    @ViewById(R.id.fragment_each_province_food_tv_phone)
    TextView tvPhone;

    @ViewById(R.id.fragment_each_province_food_srlReload)
    SwipeRefreshLayout srlReload;

    @ViewById(R.id.fragment_each_province_food_rv_photos)
    RecyclerView rvProvinceFoodPhotos;

    @ViewById(R.id.fragment_each_province_food_iv_map)
    ImageView ivMap;

    private ProvinceFood detailFood;
    private List<String> listUrlPhotos;
    private ProvinceFoodPhotoAdapter provinceFoodPhotoAdapter;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    @AfterViews
    void onCreateView() {
        // Get the Food be chosen from EachItemProvinceDetailActivity
        detailFood = getArguments().getParcelable("DetailFood");

        srlReload.setColorSchemeResources(R.color.colorMain);
        if (TripGuyUtils.isNetworkConnected(getActivity())) {
            srlReload.setRefreshing(true);
            loadProvinceFoodPhoto();
        }
        srlReload.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (TripGuyUtils.isNetworkConnected(getActivity())) {
                    listUrlPhotos.clear();
                    srlReload.setRefreshing(true);
                    loadProvinceFoodPhoto();
                } else {
                    srlReload.setRefreshing(false);
                }
            }
        });

        // set content for views
        setContentViews();

        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        rvProvinceFoodPhotos.setLayoutManager(staggeredGridLayoutManager);
    }

    @Click(R.id.fragment_each_province_food_ll_phone)
    void onLlPhoneClick() {
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

    @Click(R.id.fragment_each_province_food_ll_address)
    void onLlAddressClick() {
        Intent intentMap = new Intent(getActivity(), MapActivity_.class);

        // Send ProvinceFood to MapActivity
        intentMap.putExtra("DetailFood", detailFood);
        getActivity().startActivity(intentMap);
    }

    public void setContentViews() {
        tvDescription.setText(detailFood.getDescription());
        tvPhone.setText(detailFood.getPhone());
        tvAddress.setText(detailFood.getAddress());

        listUrlPhotos = new ArrayList<>();
        provinceFoodPhotoAdapter =
                new ProvinceFoodPhotoAdapter(getActivity(), listUrlPhotos);
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

    public void loadProvinceFoodPhoto() {
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        // if no Internet, this method will not run
        databaseReference.child("ProvinceFoodPhoto").child(detailFood.getProvince()).child(detailFood.getName())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String url;
                        url = dataSnapshot.getValue().toString();

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

        databaseReference.child("ProvinceFoodPhoto").child(detailFood.getProvince()).child(detailFood.getName())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (isAdded()) {
                            srlReload.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}