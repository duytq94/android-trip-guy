package com.dfa.vinatrip.custom_view;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.utils.MainCallbackListener;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by duonghd on 10/9/2017.
 * duonghd1307@gmail.com
 */

@EViewGroup(R.layout.view_ntoolbar)
public class NToolbar extends RelativeLayout implements MainCallbackListener {
    @ViewById(R.id.view_ntoolbar)
    protected Toolbar toolbar;
    @ViewById(R.id.view_ntoolbar_iv_left_icon)
    protected ImageView ivLeftIcon;
    @ViewById(R.id.view_ntoolbar_iv_right_icon)
    protected ImageView ivRightIcon;
    @ViewById(R.id.view_ntoolbar_tv_title)
    protected TextView tvTitle;
    @ViewById(R.id.view_ntoolbar_civ_app_icon)
    protected CircleImageView civAppIcon;
    @ViewById(R.id.view_ntoolbar_ll_root)
    protected LinearLayout llRoot;
    @ViewById(R.id.view_ntoolbar_view_shadow)
    protected View viewShadow;
    
    private AppCompatActivity activity;
    private boolean showLeftIcon = false;
    private boolean showRightIcon = false;
    private boolean showAppIcon = false;
    private boolean showToolbarColor = false;
    private boolean checkSetup = true;
    
    public NToolbar(Context context) {
        super(context);
    }
    
    public NToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    private void defaultSetup(AppCompatActivity activity) {
        this.activity = activity;
        ivLeftIcon.setVisibility(showLeftIcon ? VISIBLE : GONE);
        ivRightIcon.setVisibility(showRightIcon ? VISIBLE : GONE);
        civAppIcon.setVisibility(showAppIcon ? VISIBLE : GONE);
        toolbar.setBackgroundColor(
                showToolbarColor ? activity.getResources().getColor(R.color.colorMain) :
                        activity.getResources().getColor(R.color.transparent));
    }
    
    @Override
    public void setup(AppCompatActivity activity, String title) {
        defaultSetup(activity);
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
    }
    
    @Override
    public void setup(AppCompatActivity activity, String title, @DrawableRes int icLeft) {
        setup(activity, title);
        if (ivLeftIcon != null) {
            if (icLeft != 0) {
                ivLeftIcon.setVisibility(View.VISIBLE);
                ivLeftIcon.setImageResource(icLeft);
            } else {
                ivLeftIcon.setVisibility(View.GONE);
            }
        }
    }
    
    @Override
    public void setup(AppCompatActivity activity, String title, @DrawableRes int icLeft, @DrawableRes int icRight) {
        setup(activity, title, icLeft);
        if (ivRightIcon != null) {
            if (icRight != 0) {
                ivRightIcon.setVisibility(View.VISIBLE);
                ivRightIcon.setImageResource(icRight);
            } else {
                ivRightIcon.setVisibility(View.GONE);
            }
        }
    }
    
    @Override
    public NToolbar showAppIcon() {
        showAppIcon = true;
        civAppIcon.setVisibility(VISIBLE);
        return this;
    }
    
    @Override
    public NToolbar hideAppIcon() {
        showAppIcon = false;
        civAppIcon.setVisibility(GONE);
        return this;
    }
    
    @Override
    public NToolbar showLeftIcon() {
        showLeftIcon = true;
        ivLeftIcon.setVisibility(VISIBLE);
        return this;
    }
    
    @Override
    public NToolbar showLeftIcon(@DrawableRes int icLeft) {
        showLeftIcon = true;
        ivLeftIcon.setVisibility(VISIBLE);
        
        if (icLeft != 0) {
            ivLeftIcon.setImageResource(icLeft);
        }
        return this;
    }
    
    @Override
    public NToolbar hideLeftIcon() {
        showLeftIcon = false;
        ivLeftIcon.setVisibility(GONE);
        return this;
    }
    
    @Override
    public NToolbar showRightIcon() {
        showRightIcon = true;
        ivRightIcon.setVisibility(VISIBLE);
        return this;
    }
    
    @Override
    public NToolbar showRightIcon(@DrawableRes int icRight) {
        showRightIcon = true;
        ivRightIcon.setVisibility(VISIBLE);
        
        if (icRight != 0) {
            ivLeftIcon.setImageResource(icRight);
        }
        return this;
    }
    
    @Override
    public NToolbar hideRightIcon() {
        showRightIcon = false;
        ivRightIcon.setVisibility(GONE);
        return this;
    }
    
    @Override
    public NToolbar showToolbarColor() {
        showToolbarColor = true;
        toolbar.setBackgroundColor(activity.getResources().getColor(R.color.colorMain));
        return this;
    }
    
    @Override
    public NToolbar hideToolbarColor() {
        showToolbarColor = false;
        toolbar.setBackgroundColor(activity.getResources().getColor(R.color.transparent));
        return this;
    }
    
    @Override
    public NToolbar setTitle(String title) {
        tvTitle.setText(title);
        return this;
    }
    
    @Override
    public NToolbar showShadow() {
        viewShadow.setVisibility(VISIBLE);
        return this;
    }
    
    @Override
    public NToolbar hideShadow() {
        viewShadow.setVisibility(GONE);
        return this;
    }
    
    @Override
    public void setOnLeftClickListener(View.OnClickListener onLeftClickListener) {
        ivLeftIcon.setOnClickListener(onLeftClickListener);
    }
    
    @Override
    public void setOnRightClickListener(View.OnClickListener onRightClickListener) {
        ivRightIcon.setOnClickListener(onRightClickListener);
    }
    
    @Override
    public void setOnLlRootListener(View.OnClickListener onLlRootClickListener) {
        llRoot.setOnClickListener(onLlRootClickListener);
    }
}
