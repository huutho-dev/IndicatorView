package com.edu.myself.indicatorviewpager;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

/**
 * Created by hnc on 27/02/2017.
 */

public class IndicatorDot {

    public Paint mPaint;
    public PointF mCenter;
    public int mRadius;

    public IndicatorDot() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mCenter = new PointF();
    }

    public void setColor(int color) {
        mPaint.setColor(color);
    }

    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    public void setRadius(int radius) {
        this.mRadius = radius;
    }

    public void setXY(float x, float y) {
        this.mCenter.set(x, y);
    }

    public void drawDot(Canvas canvas) {
        canvas.drawCircle(mCenter.x, mCenter.y, mRadius, mPaint);
    }


    @Override
    public String toString() {
        return "IndicatorDot{" +
                "mpaintColor " + mPaint.getColor()+
                "mPaint=" + mPaint +
                ", mCenter=" + mCenter +
                ", mRadius=" + mRadius +
                '}';
    }
}
