package com.dfa.vinatrip.domains.main.province.detail_province.province_photo;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.province.Province;
import com.dfa.vinatrip.utils.AppUtil;
import com.dfa.vinatrip.utils.RecyclerItemClickListener;
import com.dfa.vinatrip.utils.ShowFullPhotoLocalActivity_;
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
    void init() {
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

        if (AppUtil.isNetworkConnected(getActivity())) {
            srlReload.setRefreshing(true);
            loadProvincePhoto();
        }

        srlReload.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (AppUtil.isNetworkConnected(getActivity())) {
                    provincePhotoList.clear();
                    srlReload.setRefreshing(true);
                    loadProvincePhoto();
                } else {
                    srlReload.setRefreshing(false);
                }
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvPhotos.setLayoutManager(manager);

        rvPhotos.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(), rvPhotos,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        ShowFullPhotoLocalActivity_.intent(getActivity()).listUrlPhotos(
                                (ArrayList<String>) provincePhotoList).position(position).province(province).start();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));
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
