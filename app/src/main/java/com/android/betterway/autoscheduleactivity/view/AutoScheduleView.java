package com.android.betterway.autoscheduleactivity.view;


/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public interface AutoScheduleView {
    /**
     * 显示recyclerview
     */
    void showRecyclerView();

    /**
     * 隐藏recyclerview
     */
    void hideRecyclerView();

    /**
     * 更新recyclerview
     */
    void notifyRecyclerView();

    /**
     * 显示添加按钮
     */
    void showButton();

    /**
     * 隐藏添加按钮
     */
    void hideButton();
}
