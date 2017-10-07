package com.android.betterway.mainactivity.view;


import android.content.Context;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */
public interface MainView {
    /**
     * 获得当前类中的Context传递给MainPresenterImpel
     * @return 实例所在的Context
     */
    Context getContext();
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
