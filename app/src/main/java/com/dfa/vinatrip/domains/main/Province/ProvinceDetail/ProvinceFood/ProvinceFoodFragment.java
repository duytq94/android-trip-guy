package com.dfa.vinatrip.MainFunction.Province.ProvinceDetail.ProvinceFood;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dfa.vinatrip.MainFunction.Province.EachItemProvinceDetail.EachItemProvinceDetailActivity_;
import com.dfa.vinatrip.MainFunction.Province.Province;
import com.dfa.vinatrip.utils.RecyclerItemClickListener;
import com.dfa.vinatrip.R;
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

@EFragment(R.layout.fragment_province_food)
public class ProvinceFoodFragment extends Fragment {

    @ViewById(R.id.fragment_province_food_rv_foods)
    RecyclerView rvFoods;

    @ViewById(R.id.fragment_province_food_srl_reload)
    SwipeRefreshLayout srlReload;

    private List<ProvinceFood> provinceFoodList;
    private ProvinceFoodAdapter provinceFoodAdapter;
    private Province province;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private ChildEventListener childEventListener;
    private ValueEventListener valueEventListener;

    @AfterViews
    void onCreateView() {
        // Get Province from ProvinceDetailFragment
        province = getArguments().getParcelable("Province");

        srlReload.setColorSchemeResources(R.color.colorMain);

        provinceFoodList = new ArrayList<>();
        provinceFoodAdapter = new ProvinceFoodAdapter(getActivity(), provinceFoodList);
        rvFoods.setAdapter(provinceFoodAdapter);

        childEventListener = new ChildEventListener() {
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
                                                             description, timeOpen, phone, province, latitude,
                                                             longitude);

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
            loadProvinceFood();
        }

        srlReload.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (TripGuyUtils.isNetworkConnected(getActivity())) {
                    provinceFoodList.clear();
                    srlReload.setRefreshing(true);
                    loadProvinceFood();
                } else {
                    srlReload.setRefreshing(false);
                }
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvFoods.setLayoutManager(manager);

        // Catch event click on item of RecyclerView
        rvFoods.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(), rvFoods, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Send the Food be chosen to EachItemProvinceDetailActivity
                EachItemProvinceDetailActivity_.intent(getActivity())
                                               .detailFood(provinceFoodList.get(position)).start();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    public void loadProvinceFood() {
        // if no Internet, this method will not run
        databaseReference.child("ProvinceFood").child(province.getName())
                         .addChildEventListener(childEventListener);

        databaseReference.child("ProvinceFood").child(province.getName())
                         .addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        databaseReference.removeEventListener(childEventListener);
        databaseReference.removeEventListener(valueEventListener);
    }
}
