package com.dfa.vinatrip.MainFunction.Location.ProvinceDetail.ProvincePhoto;

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
import com.dfa.vinatrip.MainFunction.Location.Province;
import com.dfa.vinatrip.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ProvincePhotoFragment extends Fragment {

    private List<ProvincePhoto> provincePhotoList;
    private RecyclerView rvPhotos;
    private ProvincePhotoAdapter provincePhotoAdapter;
    private SwipeRefreshLayout srlReload;
    private Province province;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_province_photo, container, false);

        findViewByIds(view);

        province = (Province) getArguments().getSerializable("Province");

        srlReload.setColorSchemeResources(R.color.colorMain);

        provincePhotoList = new ArrayList<>();
        provincePhotoAdapter = new ProvincePhotoAdapter(getActivity(), provincePhotoList, srlReload);
        rvPhotos.setAdapter(provincePhotoAdapter);

        if (CheckNetwork.isNetworkConnected(getActivity())) {
            loadProvincePhotoFromFirebase();
        }

        srlReload.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (CheckNetwork.isNetworkConnected(getActivity())) {
                    provincePhotoList.clear();
                    loadProvincePhotoFromFirebase();
                } else {
                    srlReload.setRefreshing(false);
                }
            }
        });

        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        rvPhotos.setLayoutManager(staggeredGridLayoutManager);

        return view;
    }

    public void findViewByIds(View view) {
        rvPhotos = (RecyclerView) view.findViewById(R.id.fragment_province_photo_rv_photos);
        srlReload = (SwipeRefreshLayout) view.findViewById(R.id.fragment_province_photo_srl_reload);
    }

    public void loadProvincePhotoFromFirebase() {
        srlReload.setRefreshing(true);
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        // if no Internet, this method will not run
        databaseReference
                .child("ProvincePhoto")
                .child(province.getName())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String url;
                        url = dataSnapshot.child("url").getValue().toString();
                        ProvincePhoto provincePhoto = new ProvincePhoto(url);
                        provincePhotoList.add(provincePhoto);
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
                });
    }
}
