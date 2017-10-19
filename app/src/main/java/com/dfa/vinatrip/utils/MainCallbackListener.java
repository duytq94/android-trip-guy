package com.dfa.vinatrip.utils;

import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dfa.vinatrip.custom_view.NToolbar;

/**
 * Created by duonghd on 10/9/2017.
 */

public interface MainCallbackListener {
    boolean setup(AppCompatActivity activity, String title);

    boolean setup(AppCompatActivity activity, String title, @DrawableRes int icLeft);

    boolean setup(AppCompatActivity activity, String title, @DrawableRes int icLeft, @DrawableRes int icRight);

    NToolbar showAppIcon();

    NToolbar hideAppIcon();

    NToolbar showLeftIcon();

    NToolbar showLeftIcon(@DrawableRes int icLeft);

    NToolbar hideLeftIcon();

    NToolbar showRightIcon();

    NToolbar showRightIcon(@DrawableRes int icRight);

    NToolbar hideRightIcon();

    NToolbar showToolbarColor();

    NToolbar hideToolbarColor();

    NToolbar setTitle(String title);

    NToolbar showShadow();

    NToolbar hideShadow();

    void setOnLeftClickListener(View.OnClickListener onLeftClickListener);

    void setOnRightClickListener(View.OnClickListener onRightClickListener);

    void setOnLlRootListener(View.OnClickListener onLlRootClickListener);
}
