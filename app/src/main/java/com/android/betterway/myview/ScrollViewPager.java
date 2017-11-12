package com.android.betterway.myview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public class ScrollViewPager extends ViewPager {

    public ScrollViewPager(Context context) {
        super(context);
    }

    public ScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (Math.abs(dx) > 50) {
            return super.canScroll(v, checkV, dx, x, y);
        } else {
            return true;
        }
    }
}
