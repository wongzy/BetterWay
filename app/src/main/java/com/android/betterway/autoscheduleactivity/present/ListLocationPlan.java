package com.android.betterway.autoscheduleactivity.present;

import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.android.betterway.data.LocationPlan;
import com.android.betterway.data.Plan;
import com.android.betterway.utils.LogUtil;

import java.util.List;
import java.util.Observable;

import javax.inject.Inject;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public class ListLocationPlan extends Observable implements RouteSearch.OnRouteSearchListener {
    private static final int BIKE = 1;
    private static final int BUS = 2;
    private static final int CAR = 3;
    private static final String TAG = "ListLocationPlan";
    private int number;
    @Inject
    public ListLocationPlan() {
    }

    /**
     * 获得List
     * @param locationPlanList 注入的List
     * @param type 出行方式类型
     */
    public void getLocationPlanList(List<LocationPlan> locationPlanList, int type) {
        number = locationPlanList.size() * 2 - 1;
    }

    /**
     * 发出完成的通知
     * @param planList 发送出的队列
     */
    public void sendPlanList(List<? extends Plan> planList) {
        super.setChanged();
        super.notifyObservers(planList);
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
}
