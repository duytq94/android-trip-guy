package com.dfa.vinatrip.domains.main.province;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.province.detail_province.ProvinceDetailActivity_;
import com.dfa.vinatrip.domains.search.SearchActivity_;
import com.dfa.vinatrip.services.DataService;
import com.dfa.vinatrip.utils.RecyclerItemClickListener;
import com.dfa.vinatrip.utils.TripGuyUtils;
import com.dfa.vinatrip.utils.ViewPagerAdapter;
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_province)
public class ProvinceFragment extends Fragment {
    @Bean
    DataService dataService;

    @ViewById(R.id.fragment_province_rv_provinces)
    RecyclerView rvProvinces;

    @ViewById(R.id.fragment_province_srlReload)
    SwipeRefreshLayout srlReload;

    @ViewById(R.id.fragment_province_vp_slide_show)
    ViewPager vpSlideShow;

    @ViewById(R.id.fragment_province_ll_dots)
    LinearLayout llDots;

    private ProvinceAdapter provinceAdapter;
    private List<Province> provinceList;

    // 4 photo in slide show
    private List<Integer> photoList;
    private ViewPagerAdapter viewPagerAdapter;
    private int i;
    private TextView[] tvDots;
    private Handler handler;
    private Runnable update;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    private ValueEventListener valueEventListener;

    @AfterViews
    void init() {
        i = 0;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        photoList = new ArrayList<>();
        photoList.add(R.drawable.bg_test1);
        photoList.add(R.drawable.bg_test2);
        photoList.add(R.drawable.bg_test3);
        photoList.add(R.drawable.bg_test4);

        viewPagerAdapter = new ViewPagerAdapter(getActivity(), photoList);

        vpSlideShow.setAdapter(viewPagerAdapter);

        // Catch event when page change, dots color will change
        vpSlideShow.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                makeColorDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        addBottomDots();

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

    public void autoScrollSlideShow() {
        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(vpSlideShow.getContext(),
                                                                 new AccelerateInterpolator());
            mScroller.set(vpSlideShow, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }

        handler = new Handler();
        update = new Runnable() {
            public void run() {
                if (i < 4) {
                    vpSlideShow.setCurrentItem(i, true);
                    i++;
                    handler.postDelayed(this, 3000);
                } else {
                    i = 0;
                    handler.postDelayed(this, 3000);
                }
            }
        };
        handler.post(update);
    }

    public void addBottomDots() {
        tvDots = new TextView[4];
        for (int i = 0; i < 4; i++) {
            tvDots[i] = new TextView(getActivity());
            // bullet char
            tvDots[i].setText("\u2022");
            tvDots[i].setTextSize(50);
            tvDots[i].setTextColor(Color.GRAY);
            llDots.addView(tvDots[i]);
        }
        tvDots[0].setTextColor(Color.WHITE);
    }

    public void makeColorDot(int position) {
        for (int i = 0; i < 4; i++) {
            tvDots[i].setTextColor(Color.GRAY);
        }
        tvDots[position].setTextColor(Color.WHITE);
    }

    public void loadProvince() {
        srlReload.setRefreshing(true);

        databaseReference.child("Province").addChildEventListener(childEventListener);
        // This method to be called after all the onChildAdded() calls have happened
        databaseReference.child("Province").addListenerForSingleValueEvent(valueEventListener);
    }

    public class FixedSpeedScroller extends Scroller {

        private int mDuration = 2000;

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }


        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            // Ignore received duration, use fixed one instead
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        autoScrollSlideShow();
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(update);
        databaseReference.removeEventListener(childEventListener);
        databaseReference.removeEventListener(valueEventListener);
    }
}
