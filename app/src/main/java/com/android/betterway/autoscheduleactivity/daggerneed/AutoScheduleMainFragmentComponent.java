package com.android.betterway.autoscheduleactivity.daggerneed;

import android.support.v4.app.Fragment;

import com.android.betterway.autoscheduleactivity.present.AutoSchedulePresenterImpel;

import dagger.Component;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */
@Component(modules = AutoSchedulePresenterImpelModule.class)
public interface AutoScheduleMainFragmentComponent {
    /**
     * 注入依赖
     * @param fragment 注入依赖的目标fragment
     */
    void inject(Fragment fragment);

    /**
     * 获得AutoSchedulePresenterImpel实例
     * @return 返回实例
     */
    AutoSchedulePresenterImpel getAutoAutoSchedulePresenterImpel();
}
