package com.dfa.vinatrip.MainFunction.Province.ProvinceDetail.ProvinceHotel;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.dfa.vinatrip.CheckNetwork;
import com.dfa.vinatrip.MainFunction.Province.EachItemProvinceDetail.EachItemProvinceDetailActivity_;
import com.dfa.vinatrip.MainFunction.Province.Province;
import com.dfa.vinatrip.MainFunction.RecyclerItemClickListener;
import com.dfa.vinatrip.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_province_hotel)
public class ProvinceHotelFragment extends Fragment {

    @ViewById(R.id.fragment_province_hotel_rv_hotels)
    RecyclerView rvHotels;

    @ViewById(R.id.fragment_province_hotel_srl_reload)
    SwipeRefreshLayout srlReload;

    private List<ProvinceHotel> provinceHotelList;
    private ProvinceHotelAdapter provinceHotelAdapter;
    private Province province;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    @AfterViews
    void onCreateView() {
        // Get Province from ProvinceDetailFragment
        province = (Province) getArguments().getSerializable("Province");

        srlReload.setColorSchemeResources(R.color.colorMain);

        provinceHotelList = new ArrayList<>();
        provinceHotelAdapter =
                new ProvinceHotelAdapter(getActivity(), provinceHotelList);
        rvHotels.setAdapter(provinceHotelAdapter);

        if (CheckNetwork.isNetworkConnected(getActivity())) {
            srlReload.setRefreshing(true);
            loadProvinceHotel();
        }

        srlReload.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (CheckNetwork.isNetworkConnected(getActivity())) {
                    provinceHotelList.clear();
                    srlReload.setRefreshing(true);
                    loadProvinceHotel();
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
                                new Intent(getActivity(), EachItemProvinceDetailActivity_.class);

                        // Send the Hotel be chosen to EachItemProvinceDetailActivity
                        intentToEachHotel.putExtra("DetailHotel", provinceHotelList.get(position));
                        getActivity().startActivity(intentToEachHotel);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));
    }

    public void loadProvinceHotel() {
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        // if no Internet, this method will not run
        databaseReference.child("ProvinceHotel").child(province.getName())
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

        databaseReference.child("ProvinceHotel").child(province.getName())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        srlReload.setRefreshing(false);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
