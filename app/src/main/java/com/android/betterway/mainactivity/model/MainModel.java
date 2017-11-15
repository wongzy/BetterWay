package com.android.betterway.mainactivity.model;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.android.betterway.MyApplication;
import com.android.betterway.R;
import com.android.betterway.data.DaoMaster;
import com.android.betterway.data.DaoSession;
import com.android.betterway.data.NewPlan;
import com.android.betterway.data.NewPlanDao;
import com.android.betterway.data.Schedule;
import com.android.betterway.data.ScheduleDao;
import com.android.betterway.utils.LogUtil;

import java.util.ArrayList;

import butterknife.BindString;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public final class MainModel {
    @BindString(R.string.newplan_db)
    String newPlandb;
    @BindString(R.string.schedule_db)
    String scheduledb;
    private static final String TAG = "MianModel";
    private static final String PREFERENCE = "com.android.betterway_preferences";
    private SharedPreferences sharedPreferences;
    private MainModel() {
    }
    public void setActivity(Activity activity) {
        sharedPreferences = activity.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
    }
    /**
     * 获得开关的信息
     * @param key 键值
     * @return 开关是否打开的信息
     */
    public boolean getWeatherOn(String key) {
        return sharedPreferences.getBoolean(key, true);
    }

    /**
     * 获得图片地址信息
     * @param key 键值
     * @return 图片的地址信息
     */
    public String getUrl(String key) {
        return sharedPreferences.getString(key, "NONE");
    }

    /**
     * 获得单例
     * @return 单例
     */
    public static MainModel getInstance() {
        return MainModelHolder.INSTANCE;
    }

    /**
     * 单例模式内部类
     */
    private static class MainModelHolder {
        private static final MainModel INSTANCE = new MainModel();
    }
    /**
     * 查询计划
     * @param key 计划对应的关键字
     * @return 查询到的计划
     */
    public ArrayList<NewPlan> inquiryPlans(long key, Application application) {
        DaoSession newPlanSession = ((MyApplication) application).getPlanDaoSession();
        NewPlanDao planDao = newPlanSession.getNewPlanDao();
        ArrayList<NewPlan> planList = new ArrayList<>();
        planList.addAll(planDao.queryBuilder().where(NewPlanDao.Properties.EditFinishTime.eq(key))
                .orderAsc(NewPlanDao.Properties.Order)
                .list());
        LogUtil.d(TAG, planList.size() + "条");
        return planList;
    }

    /**
     * 查询所有的路书
     * @return 路书集合
     */
    public ArrayList<Schedule> inquiryAllSchedule(Application application) {
        ArrayList<Schedule> schedules = new ArrayList<Schedule>();
        DaoSession scheduleSesssion = ((MyApplication) application).getScheduleDaosession();
        ScheduleDao scheduleDao = scheduleSesssion.getScheduleDao();
        schedules.addAll(scheduleDao.loadAll());
        return schedules;
    }
}
