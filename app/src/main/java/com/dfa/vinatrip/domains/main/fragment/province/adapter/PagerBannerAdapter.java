package com.dfa.vinatrip.domains.main.fragment.province.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dfa.vinatrip.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by duonghd on 10/10/2017.
 */

public class PagerBannerAdapter extends PagerAdapter {
    private Context context;
    private List<String> banners;
    
    public PagerBannerAdapter(Context context, List<String> banners) {
        this.context = context;
        this.banners = banners;
    }
    
    @Override
    public int getCount() {
        return banners.size();
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
        Picasso.with(context).load(banners.get(position))
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.photo_not_available)
                .into(ivBanner);
        
        container.addView(view);
        return view;
    }
    
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
