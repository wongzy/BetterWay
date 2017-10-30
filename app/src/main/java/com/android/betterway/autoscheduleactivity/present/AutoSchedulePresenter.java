package com.android.betterway.autoscheduleactivity.present;

import com.android.betterway.data.LocationPlan;
import com.android.betterway.data.Plan;

import java.util.List;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public interface AutoSchedulePresenter {
    /**
     * 增加计划
     * @param locationPlan 要增加的计划
     */
    void addPlan(LocationPlan locationPlan);

    /**
     * 获得所有的计划
     * @return 所有计划
     */
    List<LocationPlan> getList();

    /**
     * 删除计划
     * @param position 要删除的计划的位置
     */
    void deletePlan(int position);

    /**
     * 删除所有计划
     */
    void deleteAllPlan();
}
