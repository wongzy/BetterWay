package com.android.betterway.autoscheduleactivity.daggerneed;

import com.android.betterway.autoscheduleactivity.present.AutoSchedulePresenterImpel;
import com.android.betterway.autoscheduleactivity.present.ListLocationPlan;

import dagger.Component;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */
@Component(modules = ListLocationPlanModule.class)
public interface ListLocationPlanComponent {
    /**
     * 注入目标impel
     * @param autoSchedulePresenterImpel 要注入的Impel
     */
    void inject(AutoSchedulePresenterImpel autoSchedulePresenterImpel);

    /**
     * 获得ListLocationPlan实例
     * @return 实例
     */
    ListLocationPlan getListLocationPlan();
}
