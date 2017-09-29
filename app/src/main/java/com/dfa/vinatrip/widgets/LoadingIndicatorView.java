package com.dfa.vinatrip.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.View;

import com.dfa.vinatrip.R;

/**
 * Created by Truon on 04/21/2017.
 */

public class LoadingIndicatorView extends View {

    //indicators indicator
    public static final int BallSpinFadeLoader = 22;

    @IntDef(flag = true,
            value = {
                    BallSpinFadeLoader,
            })
    public @interface Indicator {
    }

    //Sizes (with defaults in DP)
    public static final int DEFAULT_SIZE = 45;

    //attrs
    int mIndicatorId;
    int mIndicatorColor;

    Paint mPaint;

    BaseIndicatorController mIndicatorController;

    private boolean mHasAnimation;

    public LoadingIndicatorView(Context context) {
        super(context);
        init(null, 0);
    }

    public LoadingIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public LoadingIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoadingIndicatorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        /**
         *ObtainTypedArray(Attribute set)
         */
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AVLoadingIndicatorView);
        mIndicatorId = a.getInt(
                R.styleable.AVLoadingIndicatorView_indicator, BallSpinFadeLoader);//Get number properties
        mIndicatorColor = a.getColor(
                R.styleable.AVLoadingIndicatorView_indicator_color, Color.WHITE);//Get color properties
        a.recycle();//Collection of recycling attributes
        mPaint = new Paint();
        mPaint.setColor(mIndicatorColor);//Set the color of the brush
        mPaint.setStyle(Paint.Style.FILL);//Set the style for the brush to fill
        mPaint.setAntiAlias(true);//De aliasing
        applyIndicator();//
    }

    private void applyIndicator() {
        switch (mIndicatorId) {
            case BallSpinFadeLoader:
                mIndicatorController = new BallSpinFadeLoaderIndicator();
                break;
        }
        mIndicatorController.setTarget(this);//Set the control to the currentView
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureDimension(dp2px(DEFAULT_SIZE), widthMeasureSpec);//ObtainViewWidth
        int height = measureDimension(dp2px(DEFAULT_SIZE), heightMeasureSpec);//ObtainViewHeight
        setMeasuredDimension(width, height);//
    }

    /**
     * Measured dimension
     *
     * @param defaultSize Default size
     * @param measureSpec {@see widthMeasureSpec, heightMeasureSpec}
     * @return Returns the result of the measurement
     */
    private int measureDimension(int defaultSize, int measureSpec) {
        int result = defaultSize;
        int specMode = MeasureSpec.getMode(measureSpec);//Measurement specification
        int specSize = MeasureSpec.getSize(measureSpec);//Measure size
        if (specMode == MeasureSpec.EXACTLY) {  //Parent control has set determine the size of the child control,
                                                // Child control will consider the size of the parent control,
                                                //How much you need to set up
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) { //Child controls can set their own desired size
            result = Math.min(defaultSize, specSize);//Minimum value
        } else {
            result = defaultSize;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawIndicator(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!mHasAnimation) {
            mHasAnimation = true;
            applyAnimation();
        }
    }

    void drawIndicator(Canvas canvas) {
        mIndicatorController.draw(canvas, mPaint);
    }

    void applyAnimation() {
        mIndicatorController.createAnimation();
    }

    private int dp2px(int dpValue) {
        return (int) getContext().getResources().getDisplayMetrics().density * dpValue;
    }
}
