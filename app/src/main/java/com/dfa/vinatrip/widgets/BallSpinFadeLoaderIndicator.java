package com.dfa.vinatrip.widgets;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Truon on 04/21/2017.
 */

public class BallSpinFadeLoaderIndicator extends BaseIndicatorController {
    public static final float SCALE = 1.0f;

    public static final int ALPHA = 255;
    /**
     * The proportion of dots
     */
    float[] scaleFloats = new float[]{SCALE,
            SCALE,
            SCALE,
            SCALE,
            SCALE,
            SCALE,
            SCALE,
            SCALE};
    /**
     * Dot set transparency
     */
    int[] alphas = new int[]{ALPHA,
            ALPHA,
            ALPHA,
            ALPHA,
            ALPHA,
            ALPHA,
            ALPHA,
            ALPHA};

    @Override
    public void draw(Canvas canvas, Paint paint) {
        float radius = getWidth() / 10;
        for (int i = 0; i < 8; i++) {
            canvas.save();
            Point point = circleAt(getWidth(), getHeight(), getWidth() / 2 - radius, i * (Math.PI / 4));
            canvas.translate(point.x, point.y);
            canvas.scale(scaleFloats[i], scaleFloats[i]);
            paint.setAlpha(alphas[i]);
            canvas.drawCircle(0, 0, radius, paint);
            canvas.restore();
        }
    }

    /**
     * circularOThe center for(a,b),Radius asR,spotAAnd toXAngle of axisα.
     * PointACoordinate for(a+R*cosα,b+R*sinα)
     *
     * @param width
     * @param height
     * @param radius
     * @param angle
     * @return
     */
    Point circleAt(int width, int height, float radius, double angle) {
        float x = (float) (width / 2 + radius * (Math.cos(angle)));
        float y = (float) (height / 2 + radius * (Math.sin(angle)));
        return new Point(x, y);
    }

    @Override
    public void createAnimation() {
        int[] delays = {0, 120, 240, 360, 480, 600, 720, 780, 840};
        for (int i = 0; i < 8; i++) {
            final int index = i;
            ValueAnimator scaleAnim = ValueAnimator.ofFloat(1, 0.4f, 1);//EstablishValueAnimatorobject
            scaleAnim.setDuration(1000);//Set the duration of the animation
            scaleAnim.setRepeatCount(-1);//Set animation to repeat
            scaleAnim.setStartDelay(delays[i]);//Delayed start animation
            scaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { //ValueAnimatorOnly
                // responsible for the first time,Therefore,
                // it is necessary to monitor the relevant attributes of the object to be updated.
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    scaleFloats[index] = (float) animation.getAnimatedValue();//Gets the value of the current frame
                    postInvalidate();
                }
            });
            scaleAnim.start();//Start attribute animation

            ValueAnimator alphaAnim = ValueAnimator.ofInt(255, 77, 255);//Transparency animation
            alphaAnim.setDuration(1000);//
            alphaAnim.setRepeatCount(-1);
            alphaAnim.setStartDelay(delays[i]);
            alphaAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    alphas[index] = (int) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            alphaAnim.start();
        }
    }

    final class Point {
        public float x;
        public float y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}
