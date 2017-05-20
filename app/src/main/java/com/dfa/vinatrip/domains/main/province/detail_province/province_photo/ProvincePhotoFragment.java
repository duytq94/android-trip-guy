package com.dfa.vinatrip.MainFunction.Province.ProvinceDetail.ProvincePhoto;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.dfa.vinatrip.MainFunction.Province.Province;
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

@EFragment(R.layout.fragment_province_photo)
public class ProvincePhotoFragment extends Fragment {

    @ViewById(R.id.fragment_province_photo_rv_photos)
    RecyclerView rvPhotos;

    @ViewById(R.id.fragment_province_photo_srl_reload)
    SwipeRefreshLayout srlReload;

    private List<String> provincePhotoList;
    private ProvincePhotoAdapter provincePhotoAdapter;
    private Province province;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private ChildEventListener childEventListener;
    private ValueEventListener valueEventListener;

    @AfterViews
    void onCreateView() {
        province = getArguments().getParcelable("Province");

        srlReload.setColorSchemeResources(R.color.colorMain);

        provincePhotoList = new ArrayList<>();
        provincePhotoAdapter = new ProvincePhotoAdapter(getActivity(), provincePhotoList);
        rvPhotos.setAdapter(provincePhotoAdapter);

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String url;
                url = dataSnapshot.getValue().toString();
                provincePhotoList.add(url);
                provincePhotoAdapter.notifyDataSetChanged();
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
            loadProvincePhoto();
        }

        srlReload.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (TripGuyUtils.isNetworkConnected(getActivity())) {
                    provincePhotoList.clear();
                    srlReload.setRefreshing(true);
                    loadProvincePhoto();
                } else {
                    srlReload.setRefreshing(false);
                }
            }
        });

        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        rvPhotos.setLayoutManager(staggeredGridLayoutManager);
    }

    public void loadProvincePhoto() {
        // if no Internet, this method will not run
        databaseReference.child("ProvincePhoto").child(province.getName())
                .addChildEventListener(childEventListener);

        databaseReference.child("ProvincePhoto").child(province.getName())
                .addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        databaseReference.removeEventListener(childEventListener);
        databaseReference.removeEventListener(valueEventListener);
    }
}
