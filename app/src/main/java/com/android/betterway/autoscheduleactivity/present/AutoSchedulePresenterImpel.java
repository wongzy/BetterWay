package com.android.betterway.autoscheduleactivity.present;

import com.android.betterway.autoscheduleactivity.view.AutoScheduleView;
import com.android.betterway.data.Plan;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public class AutoSchedulePresenterImpel implements AutoSchedulePresenter {
    private final AutoScheduleView mAutoScheduleView;
    @Inject
    public AutoSchedulePresenterImpel(AutoScheduleView autoScheduleView) {
        mAutoScheduleView = autoScheduleView;
    }
    @Override
    public void addPlan(Plan plan) {

    }

    @Override
    public List<? extends Plan> getList() {
        return null;
    }

    @Override
    public void deletePlan(int position) {

    }

    @Override
    public void deleteAllPlan() {

    }
}
