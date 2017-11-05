package com.android.betterway.autoscheduleactivity.present;


import com.android.betterway.autoscheduleactivity.view.AutoScheduleView;
import com.android.betterway.data.LocationPlan;

import javax.inject.Inject;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public class AutoSchedulePresenterImpel implements AutoSchedulePresenter {
    private final AutoScheduleView mAutoScheduleView;
    private static final String TAG = "AutoSchedulePresenterImpel";
    @Inject
    public AutoSchedulePresenterImpel(AutoScheduleView autoScheduleView) {
        mAutoScheduleView = autoScheduleView;
    }

    @Override
    public void addPlan(LocationPlan locationPlan) {
        mAutoScheduleView.getLocationPlanAdapter().addLocationPlan(locationPlan);

    }

    public boolean isFitst() {
        return mAutoScheduleView.getLocationPlanAdapter()
                .getIsFirst();
    }

    @Override
    public void deletePlan(int position) {
        mAutoScheduleView.getLocationPlanAdapter().removeLocationPlan(position);
    }

    @Override
    public void deleteAllPlan() {
        mAutoScheduleView.getLocationPlanAdapter().removeAllLocationPlan();
    }

    public boolean isAdded() {
        return mAutoScheduleView.getLocationPlanAdapter().getItemCount() > 4;
    }
}
