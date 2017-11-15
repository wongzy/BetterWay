package com.android.betterway.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;

import java.util.ArrayList;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */
@Entity
public  class Schedule  implements Parcelable{
    @Id
    private Long editFinishTime;
    @NotNull
    private long  startTime;
    private String location;
    @NotNull
    private String city;
    private int spendTime;
    private int spendMoney;
    @Generated(hash = 1678300761)
    public Schedule(Long editFinishTime, long startTime, String location,
            @NotNull String city, int spendTime, int spendMoney) {
        this.editFinishTime = editFinishTime;
        this.startTime = startTime;
        this.location = location;
        this.city = city;
        this.spendTime = spendTime;
        this.spendMoney = spendMoney;
    }
    @Generated(hash = 729319394)
    public Schedule() {
    }
    public Long getEditFinishTime() {
        return this.editFinishTime;
    }
    public void setEditFinishTime(Long editFinishTime) {
        this.editFinishTime = editFinishTime;
    }
    public long getStartTime() {
        return this.startTime;
    }
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    public String getLocation() {
        return this.location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getCity() {
        return this.city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public int getSpendTime() {
        return this.spendTime;
    }
    public void setSpendTime(int spendTime) {
        this.spendTime = spendTime;
    }
    public int getSpendMoney() {
        return this.spendMoney;
    }
    public void setSpendMoney(int spendMoney) {
        this.spendMoney = spendMoney;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(startTime);
    }

    public static final Parcelable.Creator<Schedule> CREATOR = new Creator<Schedule>() {
        @Override
        public Schedule createFromParcel(Parcel source) {
            Schedule schedule = new Schedule();
            schedule.setStartTime(source.readLong());
            return schedule;
        }

        @Override
        public Schedule[] newArray(int size) {
            return new Schedule[size];
        }
    };
}
