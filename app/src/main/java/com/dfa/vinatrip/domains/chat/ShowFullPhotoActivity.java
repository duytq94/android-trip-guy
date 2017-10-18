package com.dfa.vinatrip.domains.chat;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.dfa.vinatrip.widgets.RotateLoading;
import com.dfa.vinatrip.widgets.ZoomOutPageTransformer;
import com.github.chrisbanes.photoview.PhotoView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EActivity(R.layout.activity_show_full_photo)
public class ShowFullPhotoActivity extends AppCompatActivity {

    @Extra
    protected String url;
    @Extra
    protected ArrayList<String> listUrl;
    @Extra
    protected int position;

    @ViewById(R.id.my_toolbar)
    protected Toolbar toolbar;
    @ViewById(R.id.activity_show_full_photo_vp)
    protected ViewPager viewPager;

    private ActionBar actionBar;
    private ViewPagerAdapter adapter;

    @AfterViews
    public void init() {
        setupActionBar();
        if (listUrl == null) {
            if (url != null) {
                listUrl = new ArrayList<>();
                listUrl.add(url);
            } else {
                return;
            }
        }
        adapter = new ViewPagerAdapter(this, listUrl);
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(false, new ZoomOutPageTransformer());
        if (position != 0) {
            viewPager.setCurrentItem(position);
        }
    }

    public void setupActionBar() {
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
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

    public class ViewPagerAdapter extends PagerAdapter {
        private ArrayList<String> listUrl;
        private LayoutInflater inflater;
        private DisplayImageOptions imageOptions;
        private ImageLoader imageLoader;

        public ViewPagerAdapter(Context context, ArrayList<String> listUrl) {
            this.listUrl = listUrl;
            inflater = LayoutInflater.from(context);
            imageLoader = ImageLoader.getInstance();
            imageOptions = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.photo_not_available)
                    .showImageOnFail(R.drawable.photo_not_available)
                    .resetViewBeforeLoading(true)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.ARGB_4444)
                    .build();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return listUrl.size();
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            View myImageLayout = inflater.inflate(R.layout.item_photo_slide_show, view, false);
            PhotoView photoView = (PhotoView) myImageLayout.findViewById(R.id.item_photo_slide_show_photoview);
            RotateLoading rotateLoading = (RotateLoading) myImageLayout.findViewById(R.id.item_photo_slide_show_rotate_loading);

            imageLoader.displayImage(listUrl.get(position), photoView, imageOptions, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    rotateLoading.setVisibility(View.VISIBLE);
                    rotateLoading.start();
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    rotateLoading.setVisibility(View.GONE);
                    rotateLoading.stop();
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    rotateLoading.setVisibility(View.GONE);
                    rotateLoading.stop();
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    rotateLoading.setVisibility(View.GONE);
                    rotateLoading.stop();
                }
            });

            view.addView(myImageLayout, 0);
            return myImageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }
    }
}
