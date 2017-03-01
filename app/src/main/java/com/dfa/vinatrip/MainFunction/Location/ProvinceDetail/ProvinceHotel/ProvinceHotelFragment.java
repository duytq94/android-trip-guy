package com.dfa.vinatrip.MainFunction.Location.ProvinceDetail.ProvinceHotel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dfa.vinatrip.CheckNetwork;
import com.dfa.vinatrip.MainFunction.Location.EachItemProvinceDetail.EachItemProvinceDetailActivity;
import com.dfa.vinatrip.MainFunction.Location.Province;
import com.dfa.vinatrip.MainFunction.RecyclerItemClickListener;
import com.dfa.vinatrip.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ProvinceHotelFragment extends Fragment {

    private List<ProvinceHotel> provinceHotelList;
    private RecyclerView rvHotels;
    private ProvinceHotelAdapter provinceHotelAdapter;
    private SwipeRefreshLayout srlReload;
    private Province province;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_province_hotel, container, false);

        findViewByIds(view);

        // Get Province from ProvinceDetailFragment
        province = (Province) getArguments().getSerializable("Province");

        srlReload.setColorSchemeResources(R.color.colorMain);

        provinceHotelList = new ArrayList<>();
        provinceHotelAdapter =
                new ProvinceHotelAdapter(getActivity(), provinceHotelList, srlReload);
        rvHotels.setAdapter(provinceHotelAdapter);

        if (CheckNetwork.isNetworkConnected(getActivity())) {
            loadProvinceHotelFromFirebase();
        }

        srlReload.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (CheckNetwork.isNetworkConnected(getActivity())) {
                    provinceHotelList.clear();
                    loadProvinceHotelFromFirebase();
                } else {
                    srlReload.setRefreshing(false);
                }
            }
        });

        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        rvHotels.setLayoutManager(staggeredGridLayoutManager);

        // Catch event click on item of RecyclerView
        rvHotels.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), rvHotels,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intentToEachHotel =
                                new Intent(getActivity(), EachItemProvinceDetailActivity.class);

                        // Send the Hotel be chosen to EachItemProvinceDetailActivity
                        intentToEachHotel.putExtra("DetailHotel", provinceHotelList.get(position));
                        getActivity().startActivity(intentToEachHotel);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));

        return view;
    }

    public void findViewByIds(View view) {
        rvHotels = (RecyclerView) view.findViewById(R.id.fragment_province_hotel_rv_hotels);
        srlReload = (SwipeRefreshLayout) view.findViewById(R.id.fragment_province_hotel_srl_reload);
    }

    public void loadProvinceHotelFromFirebase() {
        srlReload.setRefreshing(true);

        DatabaseReference databaseReference = firebaseDatabase.getReference();

        // if no Internet, this method will not run
        databaseReference
                .child("ProvinceHotel")
                .child(province.getName())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String name, rate, avatar, price, address, description,
                                phone, mail, website, province;
                        float latitude, longitude;

                        name = dataSnapshot.child("name").getValue().toString();
                        rate = dataSnapshot.child("rate").getValue().toString();
                        avatar = dataSnapshot.child("avatar").getValue().toString();
                        price = dataSnapshot.child("price").getValue().toString();
                        address = dataSnapshot.child("address").getValue().toString();
                        description = dataSnapshot.child("description").getValue().toString();
                        phone = dataSnapshot.child("phone").getValue().toString();
                        mail = dataSnapshot.child("mail").getValue().toString();
                        website = dataSnapshot.child("website").getValue().toString();
                        latitude = Float.parseFloat(dataSnapshot.child("latitude").getValue()
                                .toString());
                        longitude = Float.parseFloat(dataSnapshot.child("longitude").getValue()
                                .toString());
                        province = dataSnapshot.child("province").getValue().toString();

                        ProvinceHotel provinceHotel = new
                                ProvinceHotel(name, rate, avatar, price, address, description,
                                phone, mail, website, province, latitude, longitude);

                        provinceHotelList.add(provinceHotel);
                        provinceHotelAdapter.notifyDataSetChanged();
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
