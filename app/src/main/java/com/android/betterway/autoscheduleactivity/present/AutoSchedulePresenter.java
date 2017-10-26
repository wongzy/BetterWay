package com.android.betterway.autoscheduleactivity.present;

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
     * @param plan 要增加的计划
     */
    void addPlan(Plan plan);

    /**
     * 获得所有的计划
     * @return 所有计划
     */
    List<? extends Plan> getList();

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
