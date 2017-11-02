package com.android.betterway.autoscheduleactivity.view;


import com.android.betterway.recyclerview.LocationPlanAdapter;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public interface AutoScheduleView {

    /**
     * 是否完成安排
     */
    void showFinishedDialog();

    /**
     * 是否清空列表
     */
    void showClearAllDialog();

    /**
     * 获得当前的adapter
     * @return adapter
     */
    LocationPlanAdapter getLocationPlanAdapter();
}
