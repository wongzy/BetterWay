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
    private String statement;
    private String location;

    @Generated(hash = 1322649335)
    public Schedule(long editFinishTime, long startTime, String statement,
            String location) {
        this.editFinishTime = editFinishTime;
        this.startTime = startTime;
        this.statement = statement;
        this.location = location;
    }

    @Generated(hash = 729319394)
    public Schedule() {
    }

    public long getEditFinishTime() {
        return editFinishTime;
    }

    public void setEditFinishTime(long editFinishTime) {
        this.editFinishTime = editFinishTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
