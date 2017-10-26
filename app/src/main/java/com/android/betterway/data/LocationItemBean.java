package com.android.betterway.data;

import com.amap.api.services.core.LatLonPoint;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public class LocationItemBean {
    LatLonPoint mLatLonPoint;
    String name;
    String address;

    public LatLonPoint getLatLonPoint() {
        return mLatLonPoint;
    }

    public void setLatLonPoint(LatLonPoint latLonPoint) {
        mLatLonPoint = latLonPoint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
