package com.dfa.vinatrip.domains.chat;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.widgets.RotateLoading;
import com.github.chrisbanes.photoview.PhotoView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_show_full_photo)
public class ShowFullPhotoActivity extends AppCompatActivity {

    @ViewById(R.id.my_toolbar)
    protected Toolbar toolbar;
    @ViewById(R.id.activity_show_full_photo_photoview)
    protected PhotoView photoView;
    @ViewById(R.id.activity_show_full_photo_rotate_loading_right)
    protected RotateLoading rotateLoading;

    @Extra
    protected String url;

    private DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.drawable.photo_not_available) // resource or drawable
            .showImageOnFail(R.drawable.photo_not_available)
            .resetViewBeforeLoading(true)  // default
            .cacheInMemory(true) // default
            .cacheOnDisk(true) // default
            .bitmapConfig(Bitmap.Config.ARGB_4444) // default
            .build();
    private ImageLoader imageLoader;
    private ActionBar actionBar;

    @AfterViews
    public void init() {
        setupActionBar();

        imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(url, photoView, displayImageOptions,
                new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {
                        rotateLoading.setVisibility(View.VISIBLE);
                        rotateLoading.start();
                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {
                        rotateLoading.setVisibility(View.GONE);
                        rotateLoading.stop();
                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        rotateLoading.setVisibility(View.GONE);
                        rotateLoading.stop();
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {
                        rotateLoading.setVisibility(View.GONE);
                        rotateLoading.stop();
                    }
                });
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
}
