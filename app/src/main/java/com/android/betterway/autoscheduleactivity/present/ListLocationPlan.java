package com.android.betterway.autoscheduleactivity.present;

import android.content.Context;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.Path;
import com.amap.api.services.route.RidePath;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.android.betterway.data.LocationPlan;
import com.android.betterway.data.Plan;
import com.android.betterway.data.RoutePlan;
import com.android.betterway.other.ActivityType;
import com.android.betterway.utils.LatLngUtil;
import com.android.betterway.utils.LogUtil;
import com.android.betterway.utils.TimeUtil;

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
    private static final int WALK = 4;
    private static final int LIMIT = 1200;
    private static final int RIGHTCODE = 1000;
    private static final String TAG = "ListLocationPlan";
    private int number;
    private volatile List<Plan> sortedList = new ArrayList<>();
    private Context mContext;
    @Inject
    public ListLocationPlan() {
    }

    /**
     * 获得List
     * @param locationPlanList 注入的List
     * @param type 出行方式类型
     * @param location 查询城市的名称
     */
    public void getLocationPlanList(final List<LocationPlan> locationPlanList, int type, String location, int i) {
        final int j = i;
        number = locationPlanList.size() * 2 - 1;
        final List<LocationPlan> locationPlans = locationPlanList;
        final int mtype = type;
        final String mlocation = location;
        io.reactivex.Observable.create(new ObservableOnSubscribe<List<LocationPlan>>() {
            @Override
            public void subscribe(ObservableEmitter<List<LocationPlan>> e) throws Exception {
                if (j == 1) {
                    e.onNext(sortLocationPlanList(locationPlans));
                } else {
                    e.onNext(notSortLocationPlanList(locationPlans));
                }
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<LocationPlan>>() {
                    @Override
                    public void accept(List<LocationPlan> locationPlanList) throws Exception {
                        enquiryRoute(locationPlanList, mtype, mlocation);
                    }
                });
    }

    private List<LocationPlan> notSortLocationPlanList(List<LocationPlan> plans) {
        sortedList.addAll(plans);
        return plans;
    }
    /**
     * 查询两个地点之间路程
     * @param locationPlanList 地点List
     * @param type 出行方式
     * @param cityName 查询城市的名称
     */
    private void enquiryRoute(List<LocationPlan> locationPlanList, int type, String cityName) {
        RouteSearch mRouteSearch = new RouteSearch(mContext);
        mRouteSearch.setRouteSearchListener(this);
        for (int i = 0; i < locationPlanList.size() - 1; i++) {
            LocationPlan preLocationPlan, nexLocationPlan;
            preLocationPlan = locationPlanList.get(i);
            nexLocationPlan = locationPlanList.get(i + 1);
            RouteSearch.FromAndTo fromAndTo = new RouteSearch.
                    FromAndTo(LatLngUtil.converLocationPlanToLatLngPoint(preLocationPlan),
                    LatLngUtil.converLocationPlanToLatLngPoint(nexLocationPlan));
            boolean weatherWalk = AMapUtils.calculateLineDistance(
                   LatLngUtil.converLocationPlanToLatLng(preLocationPlan),
                    LatLngUtil.converLocationPlanToLatLng(nexLocationPlan)
            ) <= LIMIT;
            switch (type) {
                case BIKE:
                    RouteSearch.RideRouteQuery rideRouteQuery = new RouteSearch.RideRouteQuery(fromAndTo);
                    mRouteSearch.calculateRideRouteAsyn(rideRouteQuery);
                    break;
                case BUS:
                    if (weatherWalk) {
                        RouteSearch.WalkRouteQuery walkRouteQuery = new RouteSearch.WalkRouteQuery(fromAndTo);
                        mRouteSearch.calculateWalkRouteAsyn(walkRouteQuery);
                    } else {
                        RouteSearch.BusRouteQuery busRouteQuery = new RouteSearch.BusRouteQuery(fromAndTo,
                                RouteSearch.BUS_DEFAULT, cityName, 0);
                        mRouteSearch.calculateBusRouteAsyn(busRouteQuery);
                    }
                    break;
                case CAR:
                    if (weatherWalk) {
                        RouteSearch.WalkRouteQuery walkRouteQuery = new RouteSearch.WalkRouteQuery(fromAndTo);
                        mRouteSearch.calculateWalkRouteAsyn(walkRouteQuery);
                    } else {
                        RouteSearch.DriveRouteQuery driveRouteQuery = new RouteSearch.DriveRouteQuery(fromAndTo,
                                RouteSearch.DRIVING_SINGLE_DEFAULT, null, null, "");
                        mRouteSearch.calculateDriveRouteAsyn(driveRouteQuery);
                    }
                    break;
                default:
                    break;
            }
        }
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
            LatLng mainLatLng = LatLngUtil.converLocationPlanToLatLng(locationPlan);
            float minDis = 0;
            int index = 0;
            for (int j = 0; j < locationPlanList.size(); j++) {
                LocationPlan compareLocationPlan = locationPlanList.get(j);
                LatLng compareLatLng = LatLngUtil.converLocationPlanToLatLng(compareLocationPlan);
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
        sortedList.addAll(locationPlans);
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

    /**
     * 将路径添加进计划
     * @param fromAndTo 起点和终点的LatlonPoint
     * @param plan 需要添加的Plan
     */
    private void addPlan(RouteSearch.FromAndTo fromAndTo, Plan plan) {
        for (int j = 0; j < sortedList.size() - 1; j++) {
            Plan prePlan = sortedList.get(j);
            Plan nexPlan = sortedList.get(j + 1);
            if (prePlan instanceof LocationPlan && nexPlan instanceof LocationPlan) {
                if (LatLngUtil.converLocationPlanToLatLngPoint((LocationPlan) prePlan).equals(fromAndTo.getFrom())
                        && (LatLngUtil.converLocationPlanToLatLngPoint((LocationPlan) nexPlan).equals(fromAndTo.getTo()))) {
                    sortedList.add(j + 1, plan);
                    break;
                }
            }
        }
        judgeIfFill();
    }

    /**
     * 判断是否已经添加完成list
     */
    private void judgeIfFill() {
        if (sortedList.size() == number) {
            sendPlanList(sortedList);
        }
    }

    /**
     * 获得路径设置RoutePlan对象
     * @param type 出行方式
     * @param path 获得的路径
     * @param cost 花费
     * @return 设置好的RoutePlan对象
     */
    private RoutePlan initAndSetRoutePlan(int type, Path path, int cost) {
        RoutePlan routePlan = new RoutePlan();
        routePlan.setType(type);
        routePlan.setStayMinutes(TimeUtil.SecondsToMinutes((int) path.getDuration()));
        routePlan.setMoneySpend(cost);
        return routePlan;
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
        if (i == RIGHTCODE) {
            BusPath busPath = busRouteResult.getPaths().get(0);
            RoutePlan routePlan = initAndSetRoutePlan(BUS, busPath, (int) busPath.getCost());
            addPlan(busRouteResult.getBusQuery().getFromAndTo(), routePlan);
        }
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
        if (i == RIGHTCODE) {
            DrivePath drivePath = driveRouteResult.getPaths().get(0);
            RoutePlan routePlan = initAndSetRoutePlan(CAR, drivePath, (int) drivePath.getTolls());
            addPlan(driveRouteResult.getDriveQuery().getFromAndTo(), routePlan);
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
        if (i == RIGHTCODE) {
            WalkPath walkPath = walkRouteResult.getPaths().get(0);
            RoutePlan routePlan = initAndSetRoutePlan(WALK, walkPath, 0);
            addPlan(walkRouteResult.getWalkQuery().getFromAndTo(), routePlan);
        }
    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {
        if (i == RIGHTCODE) {
            RidePath ridePath = rideRouteResult.getPaths().get(0);
            RoutePlan routePlan = initAndSetRoutePlan(BIKE, ridePath, 0);
            addPlan(rideRouteResult.getRideQuery().getFromAndTo(), routePlan);
        }
    }

    public void setContext(Context context) {
        mContext = context;
    }
}
