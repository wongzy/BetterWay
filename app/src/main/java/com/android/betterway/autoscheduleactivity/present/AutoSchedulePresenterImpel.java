package com.android.betterway.autoscheduleactivity.present;

import android.util.Log;

import com.android.betterway.autoscheduleactivity.view.AutoScheduleView;
import com.android.betterway.data.LocationPlan;
import com.android.betterway.data.Plan;
import com.android.betterway.recyclerview.LocationPlanAdapter;
import com.android.betterway.utils.LogUtil;

import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

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
        LogUtil.d(TAG, "addPlan()");
        LocationPlanAdapter locationPlanAdapter = mAutoScheduleView.getLocationPlanAdapter();
        locationPlanAdapter.addLocation(locationPlan);
    }



    @Override
    public void deletePlan(int position) {
        LogUtil.d(TAG, "deletePlan()");
        LocationPlanAdapter locationPlanAdapter = mAutoScheduleView.getLocationPlanAdapter();
        locationPlanAdapter.removeLocation(position);
    }

    @Override
    public void deleteAllPlan() {
        LogUtil.d(TAG, "deleteAllPlan()");
        LocationPlanAdapter locationPlanAdapter = mAutoScheduleView.getLocationPlanAdapter();
        locationPlanAdapter.removeAllLocation();
    }
}
