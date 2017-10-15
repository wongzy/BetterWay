package com.android.betterway.mainactivity.view;


import android.app.Activity;
import android.content.Context;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */
public interface MainView {
    /**
     * 获得当前的活动
     * @return 当前活动
     */
    Activity getActivity();
    /**
     * 查看详细行程
     */
    void enterSchedule();

    /**
     * 更新列表
     */
    void notifyRecycler();

    /**
     * 更新壁纸
     */
    void updateImage();
}
