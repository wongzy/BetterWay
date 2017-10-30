package com.android.betterway.data;

import com.amap.api.services.core.LatLonPoint;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public class LocationPlan extends Plan {
    private String statement;
    private LatLonPoint mLatLonPoint;

    public LatLonPoint getLatLonPoint() {
        return mLatLonPoint;
    }

    public void setLatLonPoint(LatLonPoint latLonPoint) {
        mLatLonPoint = latLonPoint;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }
}
