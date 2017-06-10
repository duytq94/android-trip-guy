package com.dfa.vinatrip.domains.main.province.detail_province.province_destination;

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

@EFragment(R.layout.fragment_province_destination)
public class ProvinceDestinationFragment extends Fragment {

    @ViewById(R.id.fragment_province_destination_rv_destinations)
    RecyclerView rvDestinations;

    @ViewById(R.id.fragment_province_destination_srl_reload)
    SwipeRefreshLayout srlReload;

    private List<ProvinceDestination> provinceDestinationList;
    private ProvinceDestinationAdapter provinceDestinationAdapter;
    private Province province;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private ChildEventListener childEventListener;
    private ValueEventListener valueEventListener;

    @AfterViews
    void init() {
        // Get Province from ProvinceDetailFragment
        province = getArguments().getParcelable("Province");

        srlReload.setColorSchemeResources(R.color.colorMain);

        provinceDestinationList = new ArrayList<>();
        provinceDestinationAdapter = new ProvinceDestinationAdapter(getActivity(), provinceDestinationList);
        rvDestinations.setAdapter(provinceDestinationAdapter);

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ProvinceDestination provinceDestination = dataSnapshot.getValue(ProvinceDestination.class);
                provinceDestinationList.add(provinceDestination);
                provinceDestinationAdapter.notifyDataSetChanged();
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
            loadProvinceDestination();
        }

        srlReload.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (TripGuyUtils.isNetworkConnected(getActivity())) {
                    srlReload.setRefreshing(true);
                    provinceDestinationList.clear();
                    loadProvinceDestination();
                } else {
                    srlReload.setRefreshing(false);
                }
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvDestinations.setLayoutManager(manager);

        // Catch event click on item of RecyclerView
        rvDestinations.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(), rvDestinations, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Send the Destination be chosen to EachItemProvinceDetailActivity
                EachItemProvinceDetailActivity_.intent(getActivity())
                                               .destination(provinceDestinationList.get(position)).start();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    public void loadProvinceDestination() {
        // if no Internet, this method will not run
        databaseReference.child("ProvinceDestination").child(province.getName())
                         .addChildEventListener(childEventListener);

        databaseReference.child("ProvinceDestination").child(province.getName())
                         .addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        databaseReference.removeEventListener(childEventListener);
        databaseReference.removeEventListener(valueEventListener);
    }
}
