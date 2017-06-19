package com.dfa.vinatrip.utils;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dfa.vinatrip.R;

import java.util.List;

/**
 * Created by duytq on 6/19/2017.
 */

public class ViewPagerAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater;
    private List<Integer> photoList;

    public ViewPagerAdapter(Context context, List<Integer> photoList) {
        this.photoList = photoList;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return photoList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = (ImageView) layoutInflater.inflate(R.layout.item_photo_slide_show, container, false);
        imageView.setImageResource(photoList.get(position));
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }
}