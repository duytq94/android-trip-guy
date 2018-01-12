package com.dfa.vinatrip.domains.main.fragment.province.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.widgets.RotateLoading;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by duonghd on 10/10/2017.
 * duonghd1307@gmail.com
 */

public class PagerBannerAdapter extends PagerAdapter {
    private Context context;
    private List<String> bannerURLs;
    private ImageLoader imageLoader;
    private DisplayImageOptions imageOptions;

    public PagerBannerAdapter(Context context, List<String> bannerURLs) {
        this.context = context;
        this.bannerURLs = bannerURLs;
        this.imageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.bg_green)
                .showImageForEmptyUri(R.drawable.photo_not_available)
                .showImageOnFail(R.drawable.photo_not_available)
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.ARGB_4444)
                .build();
        this.imageLoader = ImageLoader.getInstance();
    }

    @Override
    public int getCount() {
        return bannerURLs.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.item_pager_banner, container, false);
        ImageView ivBanner = (ImageView) view.findViewById(R.id.item_pager_banner_iv_banner);
        RotateLoading rotateLoading = (RotateLoading) view.findViewById((R.id.item_pager_banner_rotate_loading));

        imageLoader.displayImage(bannerURLs.get(position), ivBanner, imageOptions,
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
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
