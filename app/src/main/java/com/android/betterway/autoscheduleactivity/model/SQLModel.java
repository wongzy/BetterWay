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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public class SQLModel {
    Context mContext;
    @BindString(R.string.newplan_db)
    String newPlandb;
    @BindString(R.string.schedule_db)
    String scheduledb;
    private static final String TAG = "SQLModel";
    private DaoSession newPlanSession, scheduleSesssion;
    public SQLModel(Context context) {
        mContext = context;
    }

    /**
     * 插入planList
     * @param planList 需要插入的List
     */
    public void insertAllPlan(List<NewPlan> planList) {
        DaoMaster.DevOpenHelper devOpenHelpernewPlan = new DaoMaster.DevOpenHelper(mContext, newPlandb);
        newPlanSession = new DaoMaster(devOpenHelpernewPlan.getWritableDb()).newSession();
        NewPlanDao planDao = newPlanSession.getNewPlanDao();
        planDao.insertOrReplaceInTx(planList);
        LogUtil.d(TAG, "insertAllPlan()");
    }

    /**
     * 插入计划表
     * @param schedule 需要插入的计划表
     */
    public void insertSchedule(Schedule schedule) {
        DaoMaster.DevOpenHelper devOpenHelperschedule = new DaoMaster.DevOpenHelper(mContext, scheduledb);
        scheduleSesssion = new DaoMaster(devOpenHelperschedule.getWritableDb()).newSession();
        ScheduleDao scheduleDao = scheduleSesssion.getScheduleDao();
        scheduleDao.insert(schedule);
    }

    /**
     * 查询计划
     * @param key 计划对应的关键字
     * @return 查询到的计划
     */
    public ArrayList<NewPlan> inquiryPlans(long key) {
        DaoMaster.DevOpenHelper devOpenHelpernewPlan = new DaoMaster.DevOpenHelper(mContext, newPlandb);
        newPlanSession = new DaoMaster(devOpenHelpernewPlan.getWritableDb()).newSession();
        NewPlanDao planDao = newPlanSession.getNewPlanDao();
        ArrayList<NewPlan> planList = new ArrayList<>();
        planList.addAll(planDao.queryBuilder().where(NewPlanDao.Properties.EditFinishTime.eq(key))
                .orderAsc(NewPlanDao.Properties.Order)
                .list());
        LogUtil.d(TAG, planList.size() + "条");
        return planList;
    }

    /**
     * 删除计划表
     * @param schedule 需要删除的计划表
     */
    public void deleteSchedule(Schedule schedule) {
        DaoMaster.DevOpenHelper devOpenHelperschedule = new DaoMaster.DevOpenHelper(mContext, scheduledb);
        scheduleSesssion = new DaoMaster(devOpenHelperschedule.getWritableDb()).newSession();
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
        DaoMaster.DevOpenHelper devOpenHelpernewPlan = new DaoMaster.DevOpenHelper(mContext, newPlandb);
        newPlanSession = new DaoMaster(devOpenHelpernewPlan.getWritableDb()).newSession();
        NewPlanDao planDao = newPlanSession.getNewPlanDao();
        planDao.deleteByKey(key);
    }
}
