package com.android.betterway.data;



/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */
public class LocationPlan extends Plan {
    private long editFinishTime;
    private String statement;
    private double Lat;
    private double Lon;

    public LocationPlan() {
        super();
    }
    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public long getEditFinishTime() {
        return this.editFinishTime;
    }

    public void setEditFinishTime(long editFinishTime) {
        this.editFinishTime = editFinishTime;
    }

    public double getLat() {
        return this.Lat;
    }

    public void setLat(double Lat) {
        this.Lat = Lat;
    }

    public double getLon() {
        return this.Lon;
    }

    public void setLon(double Lon) {
        this.Lon = Lon;
    }
    public NewPlan convertToNewPlan() {
        NewPlan newPlan = new NewPlan();
        newPlan.setLocation(getLocation());
        newPlan.setStartTime(getStartTime());
        newPlan.setLat(Lat);
        newPlan.setLon(Lon);
        newPlan.setStayMinutes(getStayMinutes());
        newPlan.setStatement(statement);
        newPlan.setEndTime(getEndTime());
        newPlan.setOrder(getOrder());
        newPlan.setMoneySpend(getMoneySpend());
        return newPlan;
    }
}
