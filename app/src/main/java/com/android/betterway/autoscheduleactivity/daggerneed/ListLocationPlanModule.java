package com.android.betterway.autoscheduleactivity.daggerneed;

import com.android.betterway.autoscheduleactivity.present.ListLocationPlan;

import dagger.Module;
import dagger.Provides;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */
@Module
public class ListLocationPlanModule {
    /**
     * 获得ListLocationPlan对象
     * @return ListLocationPlan对象
     */
    @Provides
    ListLocationPlan provideListLocationPlan() {
        return new ListLocationPlan();
    }
}
