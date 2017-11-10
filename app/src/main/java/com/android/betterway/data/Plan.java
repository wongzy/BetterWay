package com.android.betterway.data;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */
public abstract class Plan {
    private String location;
    private long startTime;
    private long endTime;
    private int order;
    private int moneySpend;
    private int stayMinutes;
    public Plan() {
        moneySpend = 0;
        stayMinutes = 20;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
