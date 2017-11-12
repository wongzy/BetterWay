package com.android.betterway.showscheduleactivity.Impel;

import com.amap.api.maps2d.model.LatLng;
import com.android.betterway.autoscheduleactivity.daggerneed.DaggerSQLModelComponent;
import com.android.betterway.autoscheduleactivity.daggerneed.SQLModelComponent;
import com.android.betterway.autoscheduleactivity.daggerneed.SQLModelModule;
import com.android.betterway.autoscheduleactivity.model.SQLModel;
import com.android.betterway.data.NewPlan;
import com.android.betterway.showscheduleactivity.view.ShowScheduleView;
import com.android.betterway.utils.LatLngUtil;
import com.android.betterway.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public class ShowScheduleImpel {
    private ArrayList<NewPlan> mNewPlanList;
    private ArrayList<LatLng> mLatLngList;
    private SQLModel mSQLModel;
    private ShowScheduleView mShowScheduleView;
    public ShowScheduleImpel(ShowScheduleView showScheduleView) {
        mShowScheduleView = showScheduleView;
        SQLModelComponent modelComponent = DaggerSQLModelComponent.builder()
                .sQLModelModule(new SQLModelModule(mShowScheduleView.returnContext()))
                .build();
        modelComponent.inject(this);
        mSQLModel = modelComponent.getSQLModel();
    }

    public void initData(ArrayList<NewPlan> newPlanList) {
        mNewPlanList = newPlanList;
        mLatLngList = new ArrayList<LatLng>();
        for (NewPlan newPlan : mNewPlanList) {
            LatLng latLng = new LatLng(newPlan.getLat(), newPlan.getLon());
            mLatLngList.add(latLng);
        }
    }

    public ArrayList<NewPlan> getNewPlanList() {
        return mNewPlanList;
    }

    public ArrayList<LatLng> getLatLngList() {
        return mLatLngList;
    }
}
