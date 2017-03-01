package com.dfa.vinatrip.MainFunction.Location.ProvinceDetail.ProvinceFood;

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

public class ProvinceFoodFragment extends Fragment {

    private List<ProvinceFood> provinceFoodList;
    private RecyclerView rvFoods;
    private ProvinceFoodAdapter provinceFoodAdapter;
    private SwipeRefreshLayout srlReload;
    private ProvinceFood provinceFoodResult;
    private Province province;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_province_food, container, false);

        findViewByIds(view);

        // Get Province from ProvinceDetailFragment
        province = (Province) getArguments().getSerializable("Province");

        srlReload.setColorSchemeResources(R.color.colorMain);

        provinceFoodList = new ArrayList<>();
        provinceFoodAdapter = new ProvinceFoodAdapter(getActivity(), provinceFoodList, srlReload);
        rvFoods.setAdapter(provinceFoodAdapter);

        if (CheckNetwork.isNetworkConnected(getActivity())) {
            loadProvinceFood();
        }

        srlReload.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (CheckNetwork.isNetworkConnected(getActivity())) {
                    provinceFoodList.clear();
                    loadProvinceFood();
                } else {
                    srlReload.setRefreshing(false);
                }
            }
        });

        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        rvFoods.setLayoutManager(staggeredGridLayoutManager);

        // Catch event click on item of RecyclerView
        rvFoods.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), rvFoods,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intentToEachFood =
                                new Intent(getActivity(), EachItemProvinceDetailActivity.class);

                        // Send the Food be chosen to EachItemProvinceDetailActivity
                        intentToEachFood.putExtra("DetailFood", provinceFoodList.get(position));
                        getActivity().startActivity(intentToEachFood);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));

        return view;
    }

    public void findViewByIds(View view) {
        rvFoods = (RecyclerView) view.findViewById(R.id.fragment_province_food_rv_foods);
        srlReload = (SwipeRefreshLayout) view.findViewById(R.id.fragment_province_food_srl_reload);
    }

    public void loadProvinceFood() {
        srlReload.setRefreshing(true);

        DatabaseReference databaseReference = firebaseDatabase.getReference();

        // if no Internet, this method will not run
        databaseReference
                .child("ProvinceFood")
                .child(province.getName())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String name, avatar, price, address, description, timeOpen, phone, province;
                        float latitude, longitude;

                        name = dataSnapshot.child("name").getValue().toString();
                        avatar = dataSnapshot.child("avatar").getValue().toString();
                        price = dataSnapshot.child("price").getValue().toString();
                        address = dataSnapshot.child("address").getValue().toString();
                        description = dataSnapshot.child("description").getValue().toString();
                        timeOpen = dataSnapshot.child("timeOpen").getValue().toString();
                        phone = dataSnapshot.child("phone").getValue().toString();
                        latitude = Float.parseFloat(dataSnapshot.child("latitude").getValue()
                                .toString());
                        longitude = Float.parseFloat(dataSnapshot.child("longitude").getValue()
                                .toString());
                        province = dataSnapshot.child("province").getValue().toString();

                        ProvinceFood provinceFood = new ProvinceFood(name, avatar, price, address,
                                description, timeOpen, phone, province, latitude, longitude);

                        provinceFoodList.add(provinceFood);
                        provinceFoodAdapter.notifyDataSetChanged();
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
