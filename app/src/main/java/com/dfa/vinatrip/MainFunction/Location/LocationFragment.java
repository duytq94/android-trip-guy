package com.dfa.vinatrip.MainFunction.Location;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfa.vinatrip.CheckNetwork;
import com.dfa.vinatrip.MainFunction.Location.ProvinceDetail.ProvinceDetailActivity_;
import com.dfa.vinatrip.MainFunction.RecyclerItemClickListener;
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
import java.util.Timer;
import java.util.TimerTask;

@EFragment(R.layout.fragment_location)
public class LocationFragment extends Fragment {
    @ViewById(R.id.fragment_location_rv_provinces)
    RecyclerView rvProvinces;

    @ViewById(R.id.fragment_location_srlReload)
    SwipeRefreshLayout srlReload;

    @ViewById(R.id.fragment_location_vp_slide_show)
    ViewPager vpSlideShow;

    @ViewById(R.id.fragment_location_ll_dots)
    LinearLayout llDots;

    private ProvinceAdapter provinceAdapter;
    private List<Province> provinceList;

    // 4 photo in slide show
    private int[] mResources = {
            R.drawable.bg_test1,
            R.drawable.bg_test2,
            R.drawable.bg_test3,
            R.drawable.bg_test4
    };
    private CustomPagerAdapter customPagerAdapter;
    private int i = 0;
    private TextView[] tvDots;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    // Catch event when page change, dots color will change
    private ViewPager.OnPageChangeListener onPageChangeListener
            = new ViewPager.OnPageChangeListener() {
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
    };

    @AfterViews
    void onCreateView() {
        customPagerAdapter = new CustomPagerAdapter(getActivity());
        vpSlideShow.setAdapter(customPagerAdapter);
        vpSlideShow.addOnPageChangeListener(onPageChangeListener);

        addBottomDots();

        autoScrollSlideShow();

        srlReload.setColorSchemeResources(R.color.colorMain);

        provinceList = new ArrayList<>();
        provinceAdapter = new ProvinceAdapter(getActivity(), provinceList, srlReload);
        rvProvinces.setAdapter(provinceAdapter);

        srlReload.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (CheckNetwork.isNetworkConnected(getActivity())) {
                    provinceList.clear();
                    loadProvince();
                } else {
                    srlReload.setRefreshing(false);
                }
            }
        });

        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvProvinces.setLayoutManager(staggeredGridLayoutManager);

        // Catch event when click on item RecyclerView
        rvProvinces.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), rvProvinces,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intentToProvinceDetail =
                                new Intent(getActivity(), ProvinceDetailActivity_.class);

                        // Send Province to ProvinceDetailActivity
                        intentToProvinceDetail.putExtra("Province", provinceList.get(position));
                        getActivity().startActivity(intentToProvinceDetail);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));

        if (CheckNetwork.isNetworkConnected(getActivity())) {
            provinceList.clear();
            loadProvince();
        }
    }

    public void autoScrollSlideShow() {
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            public void run() {
                if (i < 4) {
                    vpSlideShow.setCurrentItem(i, true);
                    i++;
                } else i = 0;
            }
        };
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 3000, 2000); //time wait to start scroll, time period
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
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        // If no Internet, this method will not run
        databaseReference
                .child("Province")
                .addChildEventListener(
                        new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                String name, title, avatar, description;
                                name = dataSnapshot.child("name").getValue().toString();
                                title = dataSnapshot.child("title").getValue().toString();
                                avatar = dataSnapshot.child("avatar").getValue().toString();
                                description = dataSnapshot.child("description").getValue().toString();

                                Province province = new Province(name, title, avatar, description);
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
                        });
    }

    public class CustomPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public CustomPagerAdapter(Context context) {
            this.layoutInflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mResources.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = layoutInflater.inflate(R.layout.item_photo_slide_show, container, false);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.ivPhotoSlideShow);
            imageView.setImageResource(mResources[position]);
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }
}
