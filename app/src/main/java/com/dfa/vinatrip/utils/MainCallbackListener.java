package com.dfa.vinatrip.utils;

import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by duonghd on 10/9/2017.
 */

public interface MainCallbackListener {
    boolean setup(AppCompatActivity activity, String title);

    boolean setup(AppCompatActivity activity, String title, @DrawableRes int icLeft);

    boolean setup(AppCompatActivity activity, String title, @DrawableRes int icLeft, @DrawableRes int icRight);

    void showAppIcon();

    void hideAppIcon();

    void showLeftIcon();

    void hideLeftIcon();

    void showRightIcon();

    void hideRightIcon();

    void showToolbarColor();

    void hideToolbarColor();

    void setTitle(String title);

    void setOnLeftClickListener(View.OnClickListener onLeftClickListener);

    void setOnRightClickListener(View.OnClickListener onRightClickListener);

    void setOnLlRootListener(View.OnClickListener onLlRootClickListener);
}
