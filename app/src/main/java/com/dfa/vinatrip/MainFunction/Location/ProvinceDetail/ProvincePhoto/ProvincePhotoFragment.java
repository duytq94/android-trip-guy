package com.dfa.vinatrip.MainFunction.Location.ProvinceDetail.ProvincePhoto;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.dfa.vinatrip.CheckNetwork;
import com.dfa.vinatrip.MainFunction.Location.Province;
import com.dfa.vinatrip.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    @AfterViews
    void onCreateView() {
        province = (Province) getArguments().getSerializable("Province");

        srlReload.setColorSchemeResources(R.color.colorMain);

        provincePhotoList = new ArrayList<>();
        provincePhotoAdapter = new ProvincePhotoAdapter(getActivity(), provincePhotoList, srlReload);
        rvPhotos.setAdapter(provincePhotoAdapter);

        if (CheckNetwork.isNetworkConnected(getActivity())) {
            loadProvincePhoto();
        }

        srlReload.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (CheckNetwork.isNetworkConnected(getActivity())) {
                    provincePhotoList.clear();
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
                });
    }
}
