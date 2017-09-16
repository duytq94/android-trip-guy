package com.dfa.vinatrip.domains.main.province;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.province.detail_province.ProvinceDetailActivity_;
import com.dfa.vinatrip.domains.search.SearchActivity_;
import com.dfa.vinatrip.services.DataService;
import com.dfa.vinatrip.utils.AutoScrollViewPager;
import com.dfa.vinatrip.utils.RecyclerItemClickListener;
import com.dfa.vinatrip.utils.TripGuyUtils;
import com.dfa.vinatrip.utils.ViewPagerAdapter;
import com.dfa.vinatrip.utils.ZoomOutPageTransformer;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

@EFragment(R.layout.fragment_province)
public class ProvinceFragment extends Fragment {
    @Bean
    DataService dataService;

    @ViewById(R.id.fragment_province_rv_provinces)
    RecyclerView rvProvinces;

    @ViewById(R.id.fragment_province_srlReload)
    SwipeRefreshLayout srlReload;

    @ViewById(R.id.fragment_province_vp_slide_show)
    AutoScrollViewPager vpSlideShow;

    @ViewById(R.id.fragment_province_indicator)
    CircleIndicator indicator;

    private ProvinceAdapter provinceAdapter;
    private List<Province> provinceList;

    // 4 photo in slide show
    private List<Integer> photoList;
    private ViewPagerAdapter viewPagerAdapter;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    private ValueEventListener valueEventListener;

    @AfterViews
    void init() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        photoList = new ArrayList<>();
        photoList.add(R.drawable.bg_test1);
        photoList.add(R.drawable.bg_test2);
        photoList.add(R.drawable.bg_test3);
        photoList.add(R.drawable.bg_test4);

        viewPagerAdapter = new ViewPagerAdapter(getActivity(), photoList);

        vpSlideShow.setAdapter(viewPagerAdapter);
        vpSlideShow.setInterval(4000);
        vpSlideShow.setPageTransformer(true, new ZoomOutPageTransformer());

        indicator.setViewPager(vpSlideShow);

        srlReload.setColorSchemeResources(R.color.colorMain);
        srlReload.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (TripGuyUtils.isNetworkConnected(getActivity())) {
                    provinceList.clear();
                    loadProvince();
                } else {
                    srlReload.setRefreshing(false);
                }
            }
        });

        provinceList = new ArrayList<>();
        provinceList.addAll(dataService.getProvinceList());
        provinceAdapter = new ProvinceAdapter(getActivity(), provinceList);
        rvProvinces.setAdapter(provinceAdapter);

        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvProvinces.setLayoutManager(layoutManager);

        // Catch event when click on item RecyclerView
        rvProvinces.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(), rvProvinces, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // Send Province to ProvinceDetailActivity
                        ProvinceDetailActivity_
                                .intent(getActivity())
                                .province(provinceList.get(position))
                                .start();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Province province = dataSnapshot.getValue(Province.class);
                provinceList.add(province);
                provinceAdapter.notifyDataSetChanged();
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
                // update provinceList again
                dataService.setProvinceList(provinceList);
                srlReload.setRefreshing(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    @Click(R.id.fragment_province_rl_search)
    void onRlSearchClick() {
        SearchActivity_.intent(this).fromView("ProvinceFragment").start();
    }

    public void loadProvince() {
        srlReload.setRefreshing(true);

        databaseReference.child("Province").addChildEventListener(childEventListener);
        // This method to be called after all the onChildAdded() calls have happened
        databaseReference.child("Province").addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        vpSlideShow.startAutoScroll();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        vpSlideShow.stopAutoScroll();
        databaseReference.removeEventListener(childEventListener);
        databaseReference.removeEventListener(valueEventListener);
    }
}
