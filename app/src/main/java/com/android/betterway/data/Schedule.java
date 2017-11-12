package com.android.betterway.data;

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
public  class Schedule {
    @Id
    private long editFinishTime;
    @NotNull
    private long  startTime;
    private String location;
    private int spendTime;
    private int spendMoney;
    @Generated(hash = 1710414451)
    public Schedule(long editFinishTime, long startTime, String location,
            int spendTime, int spendMoney) {
        this.editFinishTime = editFinishTime;
        this.startTime = startTime;
        this.location = location;
        this.spendTime = spendTime;
        this.spendMoney = spendMoney;
    }
    @Generated(hash = 729319394)
    public Schedule() {
    }
    public long getEditFinishTime() {
        return this.editFinishTime;
    }
    public void setEditFinishTime(long editFinishTime) {
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

    
}
