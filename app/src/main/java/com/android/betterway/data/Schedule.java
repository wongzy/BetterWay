package com.android.betterway.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public  class Schedule {
    private MyTime editFinishTime;
    private MyTime startTime;
    private String statement;
    private int backgroundID;
    private String location;
    private List<? extends Plan> mPlenlist = new ArrayList<>();

    public MyTime getEditFinishTime() {
        return editFinishTime;
    }

    public void setEditFinishTime(MyTime editFinishTime) {
        this.editFinishTime = editFinishTime;
    }

    public List<? extends Plan> getPlenlist() {
        return mPlenlist;
    }

    public void setPlenlist(List<? extends Plan> plenlist) {
        mPlenlist = plenlist;
    }

    public MyTime getStartTime() {
        return startTime;
    }

    public void setStartTime(MyTime startTime) {
        this.startTime = startTime;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public int getBackgroundID() {
        return backgroundID;
    }

    public void setBackgroundID(int backgroundID) {
        this.backgroundID = backgroundID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
