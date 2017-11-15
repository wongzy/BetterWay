package com.android.betterway;

import android.app.Application;
import android.content.Intent;

import com.android.betterway.R;
import com.android.betterway.data.DaoMaster;
import com.android.betterway.data.DaoSession;
import com.android.betterway.myservice.MyService;

import org.greenrobot.greendao.database.Database;

import butterknife.BindString;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public class MyApplication extends Application {
    private DaoSession planDaoSession;
    private DaoSession scheduleDaosession;
    private final String Plandb = "NEWPLANDB";
    private final String Scheduledb = "SCHEDULEDB";
    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper devOpenHelpernewPlan = new DaoMaster.DevOpenHelper(this,
                Plandb, null);
        planDaoSession = new DaoMaster(devOpenHelpernewPlan.getWritableDb()).newSession();
        DaoMaster.DevOpenHelper devOpenHelperschedule = new DaoMaster.DevOpenHelper(this,
                Scheduledb, null);
        scheduleDaosession = new DaoMaster(devOpenHelperschedule.getWritableDb()).newSession();
    }

    public DaoSession getPlanDaoSession() {
        return planDaoSession;
    }

    public  DaoSession getScheduleDaosession() {
        return scheduleDaosession;
    }

}
