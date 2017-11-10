package com.android.betterway.utils;

import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.android.betterway.data.LocationPlan;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public final class LatLngUtil {
    private LatLngUtil() {
    }

    /**
     * 将LatLonPoint 转化为LatLon
     * @param latLonPoint 需要转化的LatLonPoint
     * @return 转化好的LatLng
     */
    public static LatLng convertPointToLatLng(LatLonPoint latLonPoint) {
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }

    public static LatLonPoint converLocationPlanToLatLngPoint(LocationPlan locationPlan) {
        return new LatLonPoint(locationPlan.getLat(), locationPlan.getLon());
    }

    public static LatLng converLocationPlanToLatLng(LocationPlan locationPlan) {
        return new LatLng(locationPlan.getLat(), locationPlan.getLon());
    }
}
