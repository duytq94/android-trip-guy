package com.dfa.vinatrip.domains.main.province.each_item_detail_province.each_province_hotel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.province.detail_province.province_hotel.ProvinceHotel;
import com.dfa.vinatrip.utils.AppUtil;
import com.dfa.vinatrip.utils.MapActivity_;
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

@EFragment(R.layout.fragment_each_province_hotel)
public class EachProvinceHotelFragment extends Fragment {

    @ViewById(R.id.fragment_each_province_hotel_tv_address)
    TextView tvAddress;

    @ViewById(R.id.fragment_each_province_hotel_tv_description)
    TextView tvDescription;

    @ViewById(R.id.fragment_each_province_hotel_tv_mail)
    TextView tvMail;

    @ViewById(R.id.fragment_each_province_hotel_tv_phone)
    TextView tvPhone;

    @ViewById(R.id.fragment_each_province_hotel_tv_website)
    TextView tvWebsite;

    @ViewById(R.id.fragment_each_province_hotel_srlReload)
    SwipeRefreshLayout srlReload;

    @ViewById(R.id.fragment_each_province_hotel_rv_photos)
    RecyclerView rvProvinceHotelPhotos;

    @ViewById(R.id.fragment_each_province_hotel_iv_map)
    ImageView ivMap;

    private ProvinceHotel detailHotel;
    private List<String> listUrlPhotos;
    private ProvinceHotelPhotoAdapter provinceHotelPhotoAdapter;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    private ValueEventListener valueEventListener;

    @AfterViews
    void init() {
        // Get the Hotel be chosen from EachItemProvinceDetailActivity
        detailHotel = getArguments().getParcelable("DetailHotel");

        databaseReference = FirebaseDatabase.getInstance().getReference();
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String url;
                url = dataSnapshot.getValue().toString();

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
        };
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (isAdded()) {
                    srlReload.setRefreshing(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        srlReload.setColorSchemeResources(R.color.colorMain);
        if (AppUtil.isNetworkConnected(getActivity())) {
            srlReload.setRefreshing(true);
            loadProvinceHotelPhoto();
        }
        srlReload.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (AppUtil.isNetworkConnected(getActivity())) {
                    listUrlPhotos.clear();
                    srlReload.setRefreshing(true);
                    loadProvinceHotelPhoto();
                } else {
                    srlReload.setRefreshing(false);
                }
            }
        });

        // set content for views
        setContentViews();

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvProvinceHotelPhotos.setLayoutManager(manager);
    }

    @Click(R.id.fragment_each_province_hotel_ll_phone)
    void onLlPhoneClick() {
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

    @Click(R.id.fragment_each_province_hotel_ll_website)
    void onLlWebsiteClick() {
        Intent intentGoWebsite = new Intent(Intent.ACTION_VIEW);
        intentGoWebsite.setData(Uri.parse(detailHotel.getWebsite()));
        startActivity(intentGoWebsite);
    }

    @Click(R.id.fragment_each_province_hotel_ll_address)
    void onLlAddressClick() {
        // Send ProvinceHotel to MapActivity
        MapActivity_.intent(getActivity()).detailHotel(detailHotel).start();
    }

    public void setContentViews() {
        tvDescription.setText(detailHotel.getDescription());
        tvMail.setText(detailHotel.getMail());
        tvPhone.setText(detailHotel.getPhone());
        tvAddress.setText(detailHotel.getAddress());
        tvWebsite.setText(detailHotel.getWebsite());

        listUrlPhotos = new ArrayList<>();
        provinceHotelPhotoAdapter =
                new ProvinceHotelPhotoAdapter(getActivity(), listUrlPhotos);
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

    public void loadProvinceHotelPhoto() {
        // if no Internet, this method will not run
        databaseReference.child("ProvinceHotelPhoto").child(detailHotel.getProvince()).child(detailHotel.getName())
                         .addChildEventListener(childEventListener);

        databaseReference.child("ProvinceHotelPhoto").child(detailHotel.getProvince()).child(detailHotel.getName())
                         .addListenerForSingleValueEvent(valueEventListener);
    }
}
