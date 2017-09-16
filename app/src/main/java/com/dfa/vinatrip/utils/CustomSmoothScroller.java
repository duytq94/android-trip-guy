package com.dfa.vinatrip.utils;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by User on 9/16/2017.
 */

public class CustomSmoothScroller extends Scroller {

    public CustomSmoothScroller(Context context) {
        super(context);
    }

    public CustomSmoothScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        // set smooth scroll with time (second)
        super.startScroll(startX, startY, dx, dy, 2000);
    }
}
