package com.android.betterway.autoscheduleactivity.model;

import android.content.Context;

import com.android.betterway.R;
import com.android.betterway.data.DaoMaster;
import com.android.betterway.data.DaoSession;
import com.android.betterway.data.Plan;
import com.android.betterway.data.Schedule;
import com.android.betterway.data.ScheduleDao;
import com.android.betterway.data.newPlan;
import com.android.betterway.data.newPlanDao;

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
    private DaoSession newPlanSession, scheduleSesssion;
    public SQLModel(Context context) {
        DaoMaster.DevOpenHelper devOpenHelpernewPlan = new DaoMaster.DevOpenHelper(context, newPlandb);
        DaoMaster.DevOpenHelper devOpenHelperschedule = new DaoMaster.DevOpenHelper(context, scheduledb);
        newPlanSession = new DaoMaster(devOpenHelpernewPlan.getWritableDb()).newSession();
        scheduleSesssion = new DaoMaster(devOpenHelperschedule.getWritableDb()).newSession();
    }

    public void insertAllPlan(List<newPlan> planList) {
        newPlanDao planDao = newPlanSession.getNewPlanDao();
        planDao.insertInTx(planList);
    }
    public void insertSchedule(Schedule schedule) {
        ScheduleDao scheduleDao = scheduleSesssion.getScheduleDao();
        scheduleDao.insert(schedule);
    }
    public List<newPlan> inquiryPlans(long key) {
        newPlanDao planDao = newPlanSession.getNewPlanDao();
        List<newPlan> planList
    }
    public void deleteSchedule(Schedule schedule) {
        long key = schedule.getEditFinishTime();
        ScheduleDao scheduleDao = scheduleSesssion.getScheduleDao();
        scheduleDao.deleteByKey(key);
        deletenewPlan(key);
    }
    private void deletenewPlan(long key) {
        newPlanDao planDao = newPlanSession.getNewPlanDao();
        planDao.deleteByKey(key);
    }
}
