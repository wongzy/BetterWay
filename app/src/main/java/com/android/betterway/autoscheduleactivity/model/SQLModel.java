package com.android.betterway.autoscheduleactivity.model;

import android.app.Application;
import android.content.Context;

import com.android.betterway.MyApplication;
import com.android.betterway.data.DaoSession;
import com.android.betterway.data.NewPlan;
import com.android.betterway.data.NewPlanDao;
import com.android.betterway.data.Schedule;
import com.android.betterway.data.ScheduleDao;
import com.android.betterway.utils.LogUtil;

import java.util.List;


/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public class SQLModel {
    Context mContext;
    private static final String TAG = "SQLModel";
    private DaoSession newPlanSession, scheduleSesssion;
    public SQLModel(Context context) {
        mContext = context;
    }

    /**
     * 插入planList
     * @param planList 需要插入的List
     */
    public void insertAllPlan(List<NewPlan> planList, Application application) {
        newPlanSession = ((MyApplication)  application).getPlanDaoSession();
        NewPlanDao planDao = newPlanSession.getNewPlanDao();
        planDao.insertOrReplaceInTx(planList);
        LogUtil.d(TAG, "insertAllPlan()");
    }

    /**
     * 插入计划表
     * @param schedule 需要插入的计划表
     */
    public void insertSchedule(Schedule schedule, Application application) {
        scheduleSesssion = ((MyApplication) application) .getScheduleDaosession();
        ScheduleDao scheduleDao = scheduleSesssion.getScheduleDao();
        scheduleDao.insert(schedule);
    }
    /**
     * 删除计划表
     * @param key 需要删除的表的id
     */
    public void deleteSchedule(long key, Application application) {
        scheduleSesssion = ((MyApplication) application) .getScheduleDaosession();
        ScheduleDao scheduleDao = scheduleSesssion.getScheduleDao();
        scheduleDao.deleteByKey(key);
        deletenewPlan(key, application);
    }

    /**
     * 删除计划表
     * @param key 需要删除计划表的id
     */
    private void deletenewPlan(long key, Application application) {
        newPlanSession = ((MyApplication)  application).getPlanDaoSession();
        NewPlanDao planDao = newPlanSession.getNewPlanDao();
        planDao.deleteByKey(key);
    }
}
