package com.android.betterway.showscheduleactivity.view;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public interface ShowScheduleView {
    /**
     * 获得日期的key
     * @return key
     */
    long getEditFinishTime();

    /**
     * 获得截图
     * @return 截图
     */
    Bitmap getScreenShot();

    Context returnContext();
}
