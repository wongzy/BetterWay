package com.android.betterway.autoscheduleactivity.view;


import android.content.Context;

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

    /**
     * 显示progressdialog
     */
    void showProgressDialog();

    /**
     * 关闭proogressdialog
     */
    void dismissProgressDialog();

    /**
     * 关闭bottomsheet
     */
    void dismissBottomSheet();

    /**
     * 返回上下文
     * @return 上下文对象
     */
    Context returnContext();

    /**
     * 获得所查询的城市
     * @return 查询城市的名称
     */
    String returnSearchLocation();
}
