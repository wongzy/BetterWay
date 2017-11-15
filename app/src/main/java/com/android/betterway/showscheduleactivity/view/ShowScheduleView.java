package com.android.betterway.showscheduleactivity.view;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public interface ShowScheduleView {
    Application returnApplicatin();
    /**
     * 获得截图
     * @return 截图
     */
    Bitmap getScreenShot();

    Context returnApplicationContext();

    Context returnContext();
}
