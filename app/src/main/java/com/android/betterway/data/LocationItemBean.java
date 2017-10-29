package com.android.betterway.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.services.core.LatLonPoint;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public class LocationItemBean implements Parcelable{
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mLatLonPoint);
        dest.writeString(name);
        dest.writeString(address);
    }
    public static final Parcelable.Creator<LocationItemBean> CREATOR = new Parcelable.Creator<LocationItemBean>() {
        @Override
        public LocationItemBean createFromParcel(Parcel source) {
            LocationItemBean locationItemBean = new LocationItemBean();
            locationItemBean.mLatLonPoint = (LatLonPoint) source.readValue(LatLonPoint.class.getClassLoader());
            locationItemBean.name = source.readString();
            locationItemBean.address = source.readString();
            return locationItemBean;
        }

        @Override
        public LocationItemBean[] newArray(int size) {
            return new LocationItemBean[size];
        }
    };
}
