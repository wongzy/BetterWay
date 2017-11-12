package com.android.betterway.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */
@Entity
public class NewPlan implements Parcelable{
    @Id(autoincrement = true)
    private Long id;
    private String location;
    private long startTime;
    private long endTime;
    @NotNull
    private int order;
    @NotNull
    private int moneySpend;
    @NotNull
    private int stayMinutes;
    @NotNull
    private long editFinishTime;
    private int type;
    private String statement;
    private double Lat;
    private double Lon;
    @Generated(hash = 1481491744)
    public NewPlan(Long id, String location, long startTime, long endTime,
            int order, int moneySpend, int stayMinutes, long editFinishTime,
            int type, String statement, double Lat, double Lon) {
        this.id = id;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.order = order;
        this.moneySpend = moneySpend;
        this.stayMinutes = stayMinutes;
        this.editFinishTime = editFinishTime;
        this.type = type;
        this.statement = statement;
        this.Lat = Lat;
        this.Lon = Lon;
    }
    @Generated(hash = 632870933)
    public NewPlan() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getLocation() {
        return this.location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public long getStartTime() {
        return this.startTime;
    }
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    public long getEndTime() {
        return this.endTime;
    }
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
    public int getOrder() {
        return this.order;
    }
    public void setOrder(int order) {
        this.order = order;
    }
    public int getMoneySpend() {
        return this.moneySpend;
    }
    public void setMoneySpend(int moneySpend) {
        this.moneySpend = moneySpend;
    }
    public int getStayMinutes() {
        return this.stayMinutes;
    }
    public void setStayMinutes(int stayMinutes) {
        this.stayMinutes = stayMinutes;
    }
    public long getEditFinishTime() {
        return this.editFinishTime;
    }
    public void setEditFinishTime(long editFinishTime) {
        this.editFinishTime = editFinishTime;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getStatement() {
        return this.statement;
    }
    public void setStatement(String statement) {
        this.statement = statement;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(location);
        dest.writeLong(startTime);
        dest.writeInt(moneySpend);
        dest.writeInt(stayMinutes);
        dest.writeInt(type);
        dest.writeString(statement);
    }
    public static final Parcelable.Creator<NewPlan> CREATOR = new Parcelable.Creator<NewPlan>() {
        @Override
        public NewPlan createFromParcel(Parcel source) {
            NewPlan newPlan = new NewPlan();
            newPlan.location = source.readString();
            newPlan.startTime = source.readLong();
            newPlan.moneySpend = source.readInt();
            newPlan.stayMinutes = source.readInt();
            newPlan.type = source.readInt();
            newPlan.statement = source.readString();
            return newPlan;
        }

        @Override
        public NewPlan[] newArray(int size) {
            return new NewPlan[size];
        }
    };
}
