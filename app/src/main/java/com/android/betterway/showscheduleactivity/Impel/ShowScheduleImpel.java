package com.android.betterway.showscheduleactivity.Impel;

import com.amap.api.maps2d.model.LatLng;
import com.android.betterway.autoscheduleactivity.daggerneed.DaggerSQLModelComponent;
import com.android.betterway.autoscheduleactivity.daggerneed.SQLModelComponent;
import com.android.betterway.autoscheduleactivity.daggerneed.SQLModelModule;
import com.android.betterway.autoscheduleactivity.model.SQLModel;
import com.android.betterway.data.NewPlan;
import com.android.betterway.data.Schedule;
import com.android.betterway.showscheduleactivity.view.ShowScheduleView;
import com.android.betterway.utils.LogUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public class ShowScheduleImpel {
    private ArrayList<NewPlan> mNewPlanList;
    private ArrayList<LatLng> mLatLngList;
    private SQLModel mSQLModel;
    private final long finishtime;
    private ShowScheduleView mShowScheduleView;
    public ShowScheduleImpel(ShowScheduleView showScheduleView) {
        mShowScheduleView = showScheduleView;
        SQLModelComponent modelComponent = DaggerSQLModelComponent.builder()
                .sQLModelModule(new SQLModelModule(mShowScheduleView.returnContext()))
                .build();
        modelComponent.inject(this);
        mSQLModel = modelComponent.getSQLModel();
        finishtime = new Date().getTime();
    }

    public void initData(ArrayList<NewPlan> newPlanList) {
        mNewPlanList = newPlanList;
        mLatLngList = new ArrayList<LatLng>();
        for (NewPlan newPlan : newPlanList) {
           if (newPlan.getLocation() != null) {
               LatLng latLng = new LatLng(newPlan.getLat(), newPlan.getLon());
               mLatLngList.add(latLng);
           }
        }
        storePlansAndStoreSchedule(newPlanList);
    }

    public ArrayList<NewPlan> getNewPlanList() {
        return mNewPlanList;
    }

    public ArrayList<LatLng> getLatLngList() {
        return mLatLngList;
    }

    /**
     * 储存集合
     * @param list 需要储存的list
     */
    private void storePlansAndStoreSchedule(List<NewPlan> list) {
        final List<NewPlan> newPlans = list;
        Observable.create(new ObservableOnSubscribe<Schedule>() {
            @Override
            public void subscribe(ObservableEmitter<Schedule> e) throws Exception {
                long startTime = 0;
                String location = "";
                int spendTime = 0, spendMoney = 0;
                for (int i = 0; i < newPlans.size(); i++) {
                    NewPlan newPlan = newPlans.get(i);
                    if (i == 0) {
                        startTime = newPlan.getStartTime();
                        location = newPlan.getLocation();
                    }
                    spendMoney += newPlan.getMoneySpend();
                    spendTime += newPlan.getStayMinutes();
                    newPlan.setEditFinishTime(finishtime);
                }
                mSQLModel.insertAllPlan(newPlans);
                Schedule schedule = new Schedule(finishtime, startTime, location, spendTime, spendMoney);
                e.onNext(schedule);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Schedule>() {
                    @Override
                    public void accept(Schedule schedule) throws Exception {
                        mSQLModel.insertSchedule(schedule);
                    }
                });
    }
}
