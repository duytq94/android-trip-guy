package com.dfa.vinatrip.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.domains.main.province.Province;
import com.dfa.vinatrip.domains.main.province.detail_province.province_destination.ProvinceDestination;
import com.dfa.vinatrip.widgets.ZoomOutPageTransformer;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EActivity(R.layout.activity_show_full_photo_local)
public class ShowFullPhotoLocalActivity extends AppCompatActivity {

    @ViewById(R.id.activity_full_photo_local_vp_show_full)
    protected ViewPager vpShowFull;
    @ViewById(R.id.my_toolbar)
    protected Toolbar toolbar;

    @Extra
    protected ArrayList<String> listUrlPhotos;
    @Extra
    protected int position;

    // Get intent from detail destination
    @Extra
    protected ProvinceDestination destination;

    // Get intent from detail province
    @Extra
    protected Province province;

    private CustomPagerAdapter customPagerAdapter;
    private ActionBar actionBar;

    @AfterViews
    public void init() {
        setupActionBar();

        customPagerAdapter = new CustomPagerAdapter(ShowFullPhotoLocalActivity.this);
        vpShowFull.setAdapter(customPagerAdapter);
        vpShowFull.setPageTransformer(true, new ZoomOutPageTransformer());

        vpShowFull.setCurrentItem(position);
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
            Picasso.with(ShowFullPhotoLocalActivity.this)
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
                actionBar.setTitle(province.getName());
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
