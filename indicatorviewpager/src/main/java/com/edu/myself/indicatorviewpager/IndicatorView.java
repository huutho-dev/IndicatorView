package com.edu.myself.indicatorviewpager;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by hnc on 27/02/2017.
 */

public class IndicatorView extends View implements ViewPager.OnPageChangeListener, IndicatorBehavior {

    private ViewPager mViewPager;
    private IndicatorDot[] mDots;

    private long mAnimDuration = IndicatorConstans.DEFAULT_ANIM_DURATION;
    private int mRadiusSelected = IndicatorConstans.DEFAULT_RADIUS_SELECTED;
    private int mRadiusUnSelected = IndicatorConstans.DEFAULT_RADIUS_UNSELECTED;
    private int mDistance = IndicatorConstans.DEFAULT_DISTANCE;

    private int mColorSelected;
    private int mColorUnSelected;

    private ValueAnimator animZoomIn;
    private ValueAnimator animZoomOut;

    private int mCurrentPosition;
    private int mBeforePosition;


    public IndicatorView(Context context) {
        super(context);
        initIndicatorView(context, null);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initIndicatorView(context, attrs);
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initIndicatorView(context, attrs);
    }

    private void initIndicatorView(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndicatorView);
        this.mDistance = typedArray.getInt(R.styleable.IndicatorView_tnh_distance, IndicatorConstans.DEFAULT_DISTANCE);
        this.mRadiusSelected = typedArray.getInt(R.styleable.IndicatorView_tnh_radius_selected, IndicatorConstans.DEFAULT_RADIUS_SELECTED);
        this.mRadiusUnSelected = typedArray.getInt(R.styleable.IndicatorView_tnh_radius_unselected, IndicatorConstans.DEFAULT_RADIUS_UNSELECTED);
        this.mColorSelected = typedArray.getInt(R.styleable.IndicatorView_tnh_color_selected, Color.parseColor("#ffffff"));
        this.mColorUnSelected = typedArray.getInt(R.styleable.IndicatorView_tnh_color_unselected, Color.parseColor("#54ffffff"));
        typedArray.recycle();
    }

    private void initDots(int count) throws PageLessException {
        if (count < 2) throw new PageLessException();

        mDots = new IndicatorDot[count];
        for (int i = 0; i < count; i++) {
            mDots[i] = new IndicatorDot();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int desireHeight = 2 * mRadiusSelected;

        int width, height;

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = widthSize;
        } else {
            width = 0;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desireHeight, heightSize);
        } else {
            height = desireHeight;
        }



       setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        float yCenter = getHeight() / 2;

        // Khoảng cách bao gồm 1 đốt chưa selected + distance = khoảng cách giữa 2 tâm của 2 đốt liên tiếp
        int distanceIncludeDot = 2 * mRadiusUnSelected + mDistance;

        if (mDots != null) {
            float firstXCenter = getWidth() / 2 - ((mDots.length - 1) * distanceIncludeDot / 2);
            for (int i = 0; i < mDots.length; i++) {
                mDots[i].setXY(i == 0 ? firstXCenter : firstXCenter + distanceIncludeDot * i, yCenter);
                mDots[i].setColor(i == mCurrentPosition ? mColorSelected : mColorUnSelected);
                mDots[i].setAlpha(i == mCurrentPosition ? 255 : 255 * mRadiusUnSelected / mRadiusSelected);
                mDots[i].setRadius(i == mCurrentPosition ? mRadiusSelected : mRadiusUnSelected);

            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mDots != null)
            for (IndicatorDot mDot : mDots) {
                mDot.drawDot(canvas);
            }
    }


    /**
     * Viewpager page change
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mBeforePosition = mCurrentPosition;
        mCurrentPosition = position;

        mDots[mCurrentPosition].setColor(mColorSelected);
        mDots[mBeforePosition].setColor(mColorUnSelected);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(mAnimDuration);

        animZoomOut = ValueAnimator.ofInt(mRadiusSelected, mRadiusUnSelected);
        animZoomOut.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            int positionPerform = mBeforePosition;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int newRadius = (int) animZoomOut.getAnimatedValue();
                changeNewRadius(positionPerform, newRadius);

            }
        });

        animZoomIn = ValueAnimator.ofInt(mRadiusUnSelected, mRadiusSelected);
        animZoomIn.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            int positionPerform = mCurrentPosition;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int newRadius = (int) animZoomIn.getAnimatedValue();
                changeNewRadius(positionPerform, newRadius);

            }
        });

        animatorSet.play(animZoomOut).with(animZoomIn);
        animatorSet.start();

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    /**
     * Viewpager behavior
     */

    @Override
    public void setUpWithViewPager(ViewPager viewPager) {
        try {
            mCurrentPosition = viewPager.getCurrentItem();
            this.mViewPager = viewPager;
            this.mViewPager.addOnPageChangeListener(this);
            this.initDots(mViewPager.getAdapter().getCount());
        } catch (PageLessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setRadiusSelected(int radius) {
        this.mRadiusSelected = radius;
    }

    @Override
    public void setRadiusUnSelected(int radius) {
        this.mRadiusUnSelected = radius;
    }

    @Override
    public void setDistanceDot(int distance) {
        this.mDistance = distance;
    }

    /**
     * Private method
     */

    private void changeNewRadius(int positionPerform, int newRadius) {
        if (mDots[positionPerform].mRadius != newRadius) {
            mDots[positionPerform].setRadius(newRadius);
            mDots[positionPerform].setAlpha(newRadius * 255 / mRadiusSelected);
            invalidate();
        }
    }
}
