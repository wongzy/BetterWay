package com.android.betterway.autoscheduleactivity.model;

import android.content.Context;

import com.android.betterway.R;
import com.android.betterway.data.DaoMaster;
import com.android.betterway.data.DaoSession;
import com.android.betterway.data.NewPlan;
import com.android.betterway.data.NewPlanDao;
import com.android.betterway.data.Schedule;
import com.android.betterway.data.ScheduleDao;
import com.android.betterway.utils.LogUtil;

import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindString;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public class SQLModel {
    @BindString(R.string.newplan_db)
    String newPlandb;
    @BindString(R.string.schedule_db)
    String scheduledb;
    private static final String TAG = "SQLModel";
    private DaoSession newPlanSession, scheduleSesssion;
    public SQLModel(Context context) {
        DaoMaster.DevOpenHelper devOpenHelpernewPlan = new DaoMaster.DevOpenHelper(context, newPlandb);
        DaoMaster.DevOpenHelper devOpenHelperschedule = new DaoMaster.DevOpenHelper(context, scheduledb);
        newPlanSession = new DaoMaster(devOpenHelpernewPlan.getWritableDb()).newSession();
        scheduleSesssion = new DaoMaster(devOpenHelperschedule.getWritableDb()).newSession();
    }

    /**
     * 插入planList
     * @param planList 需要插入的List
     */
    public void insertAllPlan(List<NewPlan> planList) {
        NewPlanDao planDao = newPlanSession.getNewPlanDao();
        planDao.insertOrReplaceInTx(planList);
        LogUtil.d(TAG, "insertAllPlan()");
    }

    /**
     * 插入计划表
     * @param schedule 需要插入的计划表
     */
    public void insertSchedule(Schedule schedule) {
        ScheduleDao scheduleDao = scheduleSesssion.getScheduleDao();
        scheduleDao.insert(schedule);
    }

    /**
     * 查询计划
     * @param key 计划对应的关键字
     * @return 查询到的计划
     */
    public List<NewPlan> inquiryPlans(long key) {
        NewPlanDao planDao = newPlanSession.getNewPlanDao();
        List<NewPlan> planList = planDao.queryBuilder().where(NewPlanDao.Properties.EditFinishTime.eq(key))
                .orderAsc(NewPlanDao.Properties.Order)
                .list();
        LogUtil.e(TAG, planList.size() + "条");
        return planList;
    }

    /**
     * 删除计划表
     * @param schedule 需要删除的计划表
     */
    public void deleteSchedule(Schedule schedule) {
        long key = schedule.getEditFinishTime();
        ScheduleDao scheduleDao = scheduleSesssion.getScheduleDao();
        scheduleDao.deleteByKey(key);
        deletenewPlan(key);
    }

    /**
     * 删除计划表
     * @param key 需要删除计划表的id
     */
    private void deletenewPlan(long key) {
        NewPlanDao planDao = newPlanSession.getNewPlanDao();
        planDao.deleteByKey(key);
    }
}
