package com.dfa.vinatrip.widgets;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by Truon on 04/21/2017.
 */

public abstract class BaseIndicatorController {
    private View mTarget;

    public void setTarget(View target) {
        this.mTarget = target;
    }

    public View getTarget() {
        return mTarget;
    }

    /**
     * obtainViewWidth
     *
     * @return
     */
    public int getWidth() {
        return mTarget.getWidth();
    }

    /**
     * obtainviewHeight
     *
     * @return
     */
    public int getHeight() {
        return mTarget.getHeight();
    }

    /**
     * Refreshview
     */
    public void postInvalidate() {
        mTarget.postInvalidate();
    }

    /**
     * draw indicator what ever
     * you want to draw
     * Drawindicate
     *
     * @param canvas
     * @param paint
     */
    public abstract void draw(Canvas canvas, Paint paint);

    /**
     * create animation or animations
     * ,and add to your indicator.
     * Create an animation or animation set,add toindcator
     */
    public abstract void createAnimation();
}
