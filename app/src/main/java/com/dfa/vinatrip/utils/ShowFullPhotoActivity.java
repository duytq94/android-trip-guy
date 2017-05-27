package com.dfa.vinatrip.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.province.Province;
import com.dfa.vinatrip.domains.main.province.detail_province.province_destination.ProvinceDestination;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EActivity(R.layout.activity_show_full_photo)
public class ShowFullPhotoActivity extends AppCompatActivity {

    @ViewById(R.id.activity_full_photo_vp_show_full)
    ViewPager vpShowFull;

    @ViewById(R.id.my_toolbar)
    Toolbar toolbar;

    @Extra
    ArrayList<String> listUrlPhotos;
    @Extra
    int position;
    @Extra
    ProvinceDestination destination;

    private CustomPagerAdapter customPagerAdapter;
    private android.support.v7.app.ActionBar actionBar;
    private Province detailProvince;

    @AfterViews
    void onCreate() {
        detailProvince = getIntent().getParcelableExtra("DetailProvince");

        changeColorStatusBar();
        setupActionBar();

        customPagerAdapter = new CustomPagerAdapter(ShowFullPhotoActivity.this);
        vpShowFull.setAdapter(customPagerAdapter);

        vpShowFull.setCurrentItem(position);
    }

    public void changeColorStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.black));
        }
    }

    public class CustomPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public CustomPagerAdapter(Context context) {
            this.layoutInflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return listUrlPhotos.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = (PhotoView) layoutInflater.inflate(R.layout.item_full_photo, container, false);
            Picasso.with(ShowFullPhotoActivity.this)
                   .load(listUrlPhotos.get(position))
                   .placeholder(R.drawable.ic_loading)
                   .into(photoView);
            container.addView(photoView);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((PhotoView) object);
        }
    }

    public void setupActionBar() {
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (destination != null) {
                actionBar.setTitle(destination.getName());
            } else {
                actionBar.setTitle(detailProvince.getName());
            }
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));

            // Set button back
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return false;
    }
}
