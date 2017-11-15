package com.android.betterway.showscheduleactivity.Impel;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.amap.api.maps2d.model.LatLng;
import com.android.betterway.autoscheduleactivity.daggerneed.DaggerSQLModelComponent;
import com.android.betterway.autoscheduleactivity.daggerneed.SQLModelComponent;
import com.android.betterway.autoscheduleactivity.daggerneed.SQLModelModule;
import com.android.betterway.autoscheduleactivity.model.SQLModel;
import com.android.betterway.data.NewPlan;
import com.android.betterway.data.Schedule;
import com.android.betterway.myservice.MyService;
import com.android.betterway.showscheduleactivity.view.ShowScheduleActivity;
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
    private static final String TAG = "ShowScheduleImpel";
    private String city;
    private ShowScheduleView mShowScheduleView;
    public ShowScheduleImpel(ShowScheduleView showScheduleView, String city) {
        mShowScheduleView = showScheduleView;
        SQLModelComponent modelComponent = DaggerSQLModelComponent.builder()
                .sQLModelModule(new SQLModelModule(mShowScheduleView.returnApplicationContext()))
                .build();
        modelComponent.inject(this);
        mSQLModel = modelComponent.getSQLModel();
        finishtime = new Date().getTime();
        this.city = city;
    }
    public void initData(ArrayList<NewPlan> newPlanList, int weatherSrore) {
        mNewPlanList = newPlanList;
        mLatLngList = new ArrayList<LatLng>();
        for (NewPlan newPlan : newPlanList) {
           if (newPlan.getLocation() != null) {
               LatLng latLng = new LatLng(newPlan.getLat(), newPlan.getLon());
               mLatLngList.add(latLng);
           }
        }
        if (weatherSrore == ShowScheduleActivity.NEW) {
            storePlansAndStoreSchedule(newPlanList);
        }
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
                StringBuffer location = new StringBuffer("");
                int spendTime = 0, spendMoney = 0;
                for (int i = 0; i < newPlans.size(); i ++) {
                    NewPlan newPlan = newPlans.get(i);
                    if (i == 0) {
                        startTime = newPlan.getStartTime();
                        LogUtil.i(TAG, "startTime" + startTime);
                        location.append(newPlan.getLocation());
                    } else if(i % 2 == 0){
                            String templocation = "-" + newPlan.getLocation();
                            location.append(templocation);
                    }
                    spendMoney += newPlan.getMoneySpend();
                    LogUtil.i(TAG, "spend menty" + newPlan.getMoneySpend());
                    spendTime += newPlan.getStayMinutes();
                    LogUtil.i(TAG, "spend miute" + newPlan.getStayMinutes());
                    newPlan.setEditFinishTime(finishtime);
                }
                mSQLModel.insertAllPlan(newPlans, mShowScheduleView.returnApplicatin());
                Schedule schedule = new Schedule(finishtime, startTime, location.toString(), city, spendTime, spendMoney);
                e.onNext(schedule);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Schedule>() {
                    @Override
                    public void accept(Schedule schedule) throws Exception {
                        LogUtil.i(TAG, "schedule "+ schedule.getCity()+ schedule.getEditFinishTime() + schedule.getLocation());
                        mSQLModel.insertSchedule(schedule, mShowScheduleView.returnApplicatin());
                        LogUtil.i(TAG, "schedule insert");
                    }
                });
        postMessage(MyService.SCHEDULE_ADD_KEY, MyService.SCHEDULE_ADD, mNewPlanList.get(0).getStartTime());
    }

    public void deleteSchedule() {
        mSQLModel.deleteSchedule(finishtime, mShowScheduleView.returnApplicatin());
        postMessage(MyService.SCHEDULE_DELETE_KEY, MyService.SCHEDULE_DELELTE, mNewPlanList.get(0).getStartTime());
    }

    public void delteScheduleByKey(long key) {
        mSQLModel.deleteSchedule(key, mShowScheduleView.returnApplicatin());
        postMessage(MyService.SCHEDULE_DELETE_KEY, MyService.SCHEDULE_DELELTE, mNewPlanList.get(0).getStartTime());
    }

    private void postMessage(final String key , final int what, final long value) {
        Intent intent = new Intent(mShowScheduleView.returnContext(), MyService.class);
        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Messenger messenger = new Messenger(service);
                Message message = new Message();
                message.what = what;
                Bundle bundle = new Bundle();
                bundle.putLong(key, value);
                message.setData(bundle);
                try{
                    messenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        mShowScheduleView.returnContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }
}
