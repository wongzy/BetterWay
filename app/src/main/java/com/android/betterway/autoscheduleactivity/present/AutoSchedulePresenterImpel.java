package com.android.betterway.autoscheduleactivity.present;

import com.android.betterway.autoscheduleactivity.view.AutoScheduleView;
import com.android.betterway.data.LocationPlan;
import com.android.betterway.data.Plan;
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
    private List<LocationPlan> mLocationPlanList = new ArrayList<LocationPlan>();
    @Inject
    public AutoSchedulePresenterImpel(AutoScheduleView autoScheduleView) {
        mAutoScheduleView = autoScheduleView;
    }

    @Override
    public void addPlan(LocationPlan locationPlan) {
        mLocationPlanList.add(locationPlan);
        LogUtil.d(TAG, "addPlan " + mLocationPlanList.size());
        mAutoScheduleView.hideButton();
    }

    @Override
    public List<LocationPlan> getList() {
        return mLocationPlanList;
    }

    @Override
    public void deletePlan(int position) {
        mLocationPlanList.remove(position);
    }

    @Override
    public void deleteAllPlan() {
        mLocationPlanList.clear();
    }
}
