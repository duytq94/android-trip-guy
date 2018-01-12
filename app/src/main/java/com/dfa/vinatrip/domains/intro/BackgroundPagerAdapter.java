package com.dfa.vinatrip.domains.intro;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.models.IntroObj;

import java.util.ArrayList;

/**
 * Created by duonghd on 1/12/2018.
 * duonghd1307@gmail.com
 */

public class BackgroundPagerAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<IntroObj> introObjs;
    
    public BackgroundPagerAdapter(Context context, ArrayList<IntroObj> introObjs) {
        this.context = context;
        this.introObjs = introObjs;
    }
    
    @Override
    public int getCount() {
        return introObjs.size();
    }
    
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }
    
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.welcome_background_pager, container, false);
        LinearLayout llMain = (LinearLayout) view.findViewById(R.id.background_pager_ll_main);
        ImageView ivIcon = (ImageView) view.findViewById(R.id.background_pager_iv_icon);
        TextView tvTitle = (TextView) view.findViewById(R.id.background_pager_tv_title);
        TextView tvContent = (TextView) view.findViewById(R.id.background_pager_tv_content);
        
        IntroObj introObj = introObjs.get(position);
        llMain.setBackground(introObj.getBgDrawable());
        ivIcon.setImageResource(introObj.getIcDrawable());
        tvTitle.setText(introObj.getIoTitle());
        tvContent.setText(introObj.getIoContent());
        
        container.addView(view);
        return view;
    }
    
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
