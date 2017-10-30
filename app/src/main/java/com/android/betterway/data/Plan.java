package com.android.betterway.data;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public abstract class Plan {
    private String location;
    private MyTime editFinishTime;
    private MyTime startTime;
    private MyTime endTime;
    private int moneySpend;
    private int stayMinutes;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public MyTime getEditFinishTime() {
        return editFinishTime;
    }

    public void setEditFinishTime(MyTime editFinishTime) {
        this.editFinishTime = editFinishTime;
    }

    public MyTime getStartTime() {
        return startTime;
    }

    public void setStartTime(MyTime startTime) {
        this.startTime = startTime;
    }

    public MyTime getEndTime() {
        return endTime;
    }

    public void setEndTime(MyTime endTime) {
        this.endTime = endTime;
    }

    public int getMoneySpend() {
        return moneySpend;
    }

    public void setMoneySpend(int moneySpend) {
        this.moneySpend = moneySpend;
    }

    public int getStayMinutes() {
        return stayMinutes;
    }

    public void setStayMinutes(int stayMinutes) {
        this.stayMinutes = stayMinutes;
    }
}
