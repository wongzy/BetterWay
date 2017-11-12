package com.android.betterway.autoscheduleactivity.present;


import android.content.Intent;
import android.util.Log;

import com.android.betterway.autoscheduleactivity.daggerneed.DaggerListLocationPlanComponent;
import com.android.betterway.autoscheduleactivity.daggerneed.DaggerSQLModelComponent;
import com.android.betterway.autoscheduleactivity.daggerneed.ListLocationPlanComponent;
import com.android.betterway.autoscheduleactivity.daggerneed.SQLModelComponent;
import com.android.betterway.autoscheduleactivity.daggerneed.SQLModelModule;
import com.android.betterway.autoscheduleactivity.model.SQLModel;
import com.android.betterway.autoscheduleactivity.view.AutoScheduleView;
import com.android.betterway.data.DaoMaster;
import com.android.betterway.data.DaoSession;
import com.android.betterway.data.LocationPlan;
import com.android.betterway.data.MyTime;
import com.android.betterway.data.NewPlan;
import com.android.betterway.data.Plan;
import com.android.betterway.data.RoutePlan;
import com.android.betterway.other.DeadMessage;
import com.android.betterway.showscheduleactivity.view.ShowScheduleActivity;
import com.android.betterway.utils.LogUtil;
import com.android.betterway.utils.TimeUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.inject.Inject;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public class AutoSchedulePresenterImpel implements AutoSchedulePresenter, Observer {
    private static final int BIKE = 1;
    private static final int BUS = 2;
    private static final int CAR = 3;
    private final AutoScheduleView mAutoScheduleView;
    private ListLocationPlan mListLocationPlan;
    //private SQLModel mSQLModel;
    private static final String TAG = "AutoSchedulePresenterImpel";
    @Inject
    public AutoSchedulePresenterImpel(AutoScheduleView autoScheduleView) {
        mAutoScheduleView = autoScheduleView;
        ListLocationPlanComponent listLocationPlanComponent =
                DaggerListLocationPlanComponent.builder().build();
        listLocationPlanComponent.inject(this);
        mListLocationPlan = listLocationPlanComponent.getListLocationPlan();
        mListLocationPlan.addObserver(this);;
    }

    @Override
    public void addPlan(LocationPlan locationPlan) {
        mAutoScheduleView.getLocationPlanAdapter().addLocationPlan(locationPlan);
    }

    public boolean isFitst() {
        return mAutoScheduleView.getLocationPlanAdapter()
                .getIsFirst();
    }

    @Override
    public void deletePlan(int position) {
        mAutoScheduleView.getLocationPlanAdapter().removeLocationPlan(position);
    }

    @Override
    public void deleteAllPlan() {
        mAutoScheduleView.getLocationPlanAdapter().removeAllLocationPlan();
    }

    public boolean isAdded() {
        return mAutoScheduleView.getLocationPlanAdapter().getItemCount() > 3;
    }

    /**
     * 处理点击完成后的逻辑
     * @param type 出行方式
     */
    public void finishAddedLocationPlan(int type) {
        mAutoScheduleView.dismissBottomSheet();
        mListLocationPlan.setContext(mAutoScheduleView.returnContext());
        switch (type) {
            case BIKE:
                mAutoScheduleView.showProgressDialog();
                mListLocationPlan.getLocationPlanList(mAutoScheduleView.getLocationPlanAdapter()
                        .getLocationPlanList(), BIKE, mAutoScheduleView.returnSearchLocation());
                break;
            case BUS:
                mAutoScheduleView.showProgressDialog();
                mListLocationPlan.getLocationPlanList(mAutoScheduleView.getLocationPlanAdapter()
                .getLocationPlanList(), BUS, mAutoScheduleView.returnSearchLocation());
                break;
            case CAR:
                mAutoScheduleView.showProgressDialog();
                mListLocationPlan.getLocationPlanList(mAutoScheduleView.getLocationPlanAdapter()
                .getLocationPlanList(), CAR, mAutoScheduleView.returnSearchLocation());
                break;
            default:
                break;
        }
    }
    @SuppressWarnings("unchecked")
    @Override
    public void update(Observable o, Object arg) {
        final Object marg = arg;
        io.reactivex.Observable.create(new ObservableOnSubscribe<ArrayList<NewPlan>>() {
            @Override
            public void subscribe(ObservableEmitter<ArrayList<NewPlan>> e) throws Exception {
                long datelong = mAutoScheduleView.getDateLong();
                List<Plan> planList = (List<Plan>) marg;
                ArrayList<NewPlan> newPlanList = new ArrayList<NewPlan>();
                MyTime nowMyTime =new MyTime();
                for (int i = 0; i < planList.size(); i++) {
                    Plan plan = planList.get(i);
                    if (i == 0 && plan instanceof LocationPlan) {
                        datelong = datelong * 10000L + (long) plan.getStartTime();
                        LogUtil.i(TAG, "datelong = " + datelong);
                        MyTime tempMyTime = TimeUtil.longToTotalMyTime(datelong);
                        LogUtil.i(TAG, tempMyTime.toString());
                        nowMyTime = TimeUtil.myTimeAddDuration(tempMyTime, plan.getStayMinutes());
                        plan.setEndTime(nowMyTime.getTotalLong());
                        plan.setOrder(i);
                        newPlanList.add(((LocationPlan) plan).convertToNewPlan());
                        continue;
                    }
                    if (plan instanceof LocationPlan) {
                        plan.setStartTime(nowMyTime.getTotalLong());
                        nowMyTime = TimeUtil.myTimeAddDuration(nowMyTime, plan.getStayMinutes());
                        plan.setEndTime(nowMyTime.getTotalLong());
                        plan.setOrder(i);
                        newPlanList.add(((LocationPlan) plan).convertToNewPlan());
                        continue;
                    }
                    if (plan instanceof RoutePlan) {
                        plan.setStartTime(nowMyTime.getTotalLong());
                        nowMyTime = TimeUtil.myTimeAddDuration(nowMyTime, plan.getStayMinutes());
                        plan.setEndTime(nowMyTime.getTotalLong());
                        plan.setOrder(i);
                        newPlanList.add(((RoutePlan) plan).convertToNewPlan());
                    }
                }
                e.onNext(newPlanList);
            }
        }).observeOn(Schedulers.newThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArrayList<NewPlan>>() {
                    @Override
                    public void accept(ArrayList<NewPlan> list) throws Exception {
                        Intent intent = new Intent(mAutoScheduleView.returnContext(), ShowScheduleActivity.class);
                        intent.putParcelableArrayListExtra("list", list);
                        intent.putExtra("datelong", mAutoScheduleView.getDateLong());
                        intent.putExtra("city", mAutoScheduleView.returnSearchLocation());
                        mAutoScheduleView.dismissProgressDialog();
                        mAutoScheduleView.returnContext().startActivity(intent);
                        postDeadMessage(DeadMessage.FINISH);
                    }
                });
    }
    private void postDeadMessage(DeadMessage deadMessage) {
        EventBus.getDefault().post(deadMessage);
    }
}
