package com.dfa.vinatrip.domains.main.province.detail_province.province_hotel;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.province.Province;
import com.dfa.vinatrip.domains.main.province.each_item_detail_province.EachItemProvinceDetailActivity_;
import com.dfa.vinatrip.utils.RecyclerItemClickListener;
import com.dfa.vinatrip.utils.TripGuyUtils;
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
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private ChildEventListener childEventListener;
    private ValueEventListener valueEventListener;

    @AfterViews
    void init() {
        // Get Province from ProvinceDetailFragment
        province = getArguments().getParcelable("Province");

        srlReload.setColorSchemeResources(R.color.colorMain);

        provinceHotelList = new ArrayList<>();
        provinceHotelAdapter =
                new ProvinceHotelAdapter(getActivity(), provinceHotelList);
        rvHotels.setAdapter(provinceHotelAdapter);

        childEventListener = new ChildEventListener() {
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
                latitude = Float.parseFloat(dataSnapshot.child("latitude").getValue().toString());
                longitude = Float.parseFloat(dataSnapshot.child("longitude").getValue().toString());
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

        if (TripGuyUtils.isNetworkConnected(getActivity())) {
            srlReload.setRefreshing(true);
            loadProvinceHotel();
        }

        srlReload.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (TripGuyUtils.isNetworkConnected(getActivity())) {
                    provinceHotelList.clear();
                    srlReload.setRefreshing(true);
                    loadProvinceHotel();
                } else {
                    srlReload.setRefreshing(false);
                }
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvHotels.setLayoutManager(manager);

        // Catch event click on item of RecyclerView
        rvHotels.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(), rvHotels, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Send the Hotel be chosen to EachItemProvinceDetailActivity
                EachItemProvinceDetailActivity_.intent(getActivity())
                                               .detailHotel(provinceHotelList.get(position)).start();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    public void loadProvinceHotel() {
        // if no Internet, this method will not run
        databaseReference.child("ProvinceHotel").child(province.getName())
                         .addChildEventListener(childEventListener);

        databaseReference.child("ProvinceHotel").child(province.getName())
                         .addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        databaseReference.removeEventListener(childEventListener);
        databaseReference.removeEventListener(valueEventListener);
    }
}
