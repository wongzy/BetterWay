package com.android.betterway.autoscheduleactivity.present;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.android.betterway.data.LocationPlan;
import com.android.betterway.data.Plan;
import com.android.betterway.utils.LatLngUtil;
import com.android.betterway.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.inject.Inject;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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
        number = locationPlanList.size();
        final List<LocationPlan> locationPlans = locationPlanList;
        final int mtype = type;
        io.reactivex.Observable.create(new ObservableOnSubscribe<List<LocationPlan>>() {
            @Override
            public void subscribe(ObservableEmitter<List<LocationPlan>> e) throws Exception {
                e.onNext(sortLocationPlanList(locationPlans));
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<LocationPlan>>() {
                    @Override
                    public void accept(List<LocationPlan> locationPlanList) throws Exception {
                        sendPlanList(locationPlanList);
                    }
                });
    }

    /**
     * 根据距离重新排序locationPlanList
     * @param locationPlanList 需要排序的List
     * @return 重新排序后的List
     */
    private List<LocationPlan> sortLocationPlanList(List<LocationPlan> locationPlanList) {
        List<LocationPlan> locationPlans = new ArrayList<LocationPlan>();
        LocationPlan firstLocationPlan = locationPlanList.get(0);
        locationPlanList.remove(0);
        locationPlans.add(firstLocationPlan);
        LogUtil.d(TAG, firstLocationPlan.getLocation());
        int count = locationPlanList.size();
        for (int i = 0; i < count; i++) {
            LocationPlan locationPlan = locationPlans.get(locationPlans.size() - 1);
            LatLng mainLatLng = LatLngUtil.convertPointToLatLng(locationPlan.getLatLonPoint());
            float minDis = 0;
            int index = 0;
            for (int j = 0; j < locationPlanList.size(); j++) {
                LocationPlan compareLocationPlan = locationPlanList.get(j);
                LatLng compareLatLng = LatLngUtil.convertPointToLatLng(compareLocationPlan.getLatLonPoint());
                float distance = AMapUtils.calculateLineDistance(mainLatLng, compareLatLng);
                if (minDis == 0 || distance < minDis) {
                    minDis = distance;
                    index = j;
                }
            }
            LocationPlan newLocationPlan = locationPlanList.get(index);
            locationPlans.add(newLocationPlan);
            locationPlanList.remove(index);
        }
        locationPlanList = null;
        return locationPlans;
    }
    /**
     * 发出完成的通知
     * @param planList 发送出的队列
     */
    private void sendPlanList(List<? extends Plan> planList) {
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
