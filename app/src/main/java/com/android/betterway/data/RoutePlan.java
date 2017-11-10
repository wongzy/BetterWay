package com.android.betterway.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */
public class RoutePlan extends Plan {
    private long editFinishTime;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getEditFinishTime() {
        return this.editFinishTime;
    }

    public void setEditFinishTime(long editFinishTime) {
        this.editFinishTime = editFinishTime;
    }
}
