package com.android.betterway.autoscheduleactivity.present;


import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.android.betterway.autoscheduleactivity.daggerneed.DaggerListLocationPlanComponent;
import com.android.betterway.autoscheduleactivity.daggerneed.ListLocationPlanComponent;
import com.android.betterway.autoscheduleactivity.view.AutoScheduleView;
import com.android.betterway.data.LocationPlan;
import com.android.betterway.data.Plan;

import java.util.Observable;
import java.util.Observer;

import javax.inject.Inject;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public class AutoSchedulePresenterImpel implements AutoSchedulePresenter, Observer{
    private static final int BIKE = 1;
    private static final int BUS = 2;
    private static final int CAR = 3;
    private final AutoScheduleView mAutoScheduleView;
    private ListLocationPlan mListLocationPlan;
    private static final String TAG = "AutoSchedulePresenterImpel";
    @Inject
    public AutoSchedulePresenterImpel(AutoScheduleView autoScheduleView) {
        mAutoScheduleView = autoScheduleView;
        ListLocationPlanComponent listLocationPlanComponent =
                DaggerListLocationPlanComponent.builder().build();
        listLocationPlanComponent.inject(this);
        mListLocationPlan = listLocationPlanComponent.getListLocationPlan();
        mListLocationPlan.addObserver(this);
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
        return mAutoScheduleView.getLocationPlanAdapter().getItemCount() > 3;
    }

    public void finishAddedLocationPlan(int type) {
        mAutoScheduleView.dismissBottomSheet();
        switch (type) {
            case BIKE:
                mAutoScheduleView.showProgressDialog();
                break;
            case BUS:
                mAutoScheduleView.showProgressDialog();
                break;
            case CAR:
                mAutoScheduleView.showProgressDialog();
                break;
            default:
                break;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        mAutoScheduleView.dismissProgressDialog();
    }
}
