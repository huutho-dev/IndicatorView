package com.edu.myself.indicatorviewpager;

import android.support.v4.view.ViewPager;

/**
 * Created by hnc on 27/02/2017.
 */

public interface IndicatorBehavior {

    void setUpWithViewPager(ViewPager viewPager) throws PageLessException;

    void setRadiusSelected(int radius);

    void setRadiusUnSelected(int radius);

    void setDistanceDot(int distance);

    class PageLessException extends Exception {

        @Override
        public String getMessage() {
            return "Pages must equal or larger than 2";
        }
    }
}

