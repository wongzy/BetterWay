package com.android.betterway.mainactivity.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.android.betterway.data.MyDate;
import com.android.betterway.data.NewPlan;
import com.android.betterway.data.Schedule;
import com.android.betterway.mainactivity.model.MainModel;
import com.android.betterway.mainactivity.view.MainView;
import com.android.betterway.network.image.ImageDownload;
import com.android.betterway.settingactivity.view.SettingActivity;
import com.android.betterway.showscheduleactivity.view.ShowScheduleActivity;
import com.android.betterway.utils.LogUtil;
import com.android.betterway.utils.TimeUtil;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
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

public class MainPresenterImpel implements MainPresenter {
    private static final String TAG = "MainPresenterImpel";
    private final MainView mMainView;
    private List<Schedule> mScheduleList;
    @Inject
    public MainPresenterImpel(MainView mainView) {
        mMainView = mainView;
    }

    @Override
    public void getSet() {
        Activity activity = mMainView.getActivity();
        Intent intent = new Intent(activity.getApplicationContext(), SettingActivity.class);
        activity.startActivity(intent);
    }

    public List<Schedule> getScheduleList() {
        MainModel mainModel = MainModel.getInstance();
        mScheduleList = mainModel.inquiryAllSchedule(mMainView.getActivity().getApplicationContext());
        return mScheduleList;
    }
    @Override
    public void choseSchedule(int position) {
        final int count = position;
        Observable.create(new ObservableOnSubscribe<Intent>() {
            @Override
            public void subscribe(ObservableEmitter<Intent> e) throws Exception {
                Schedule schedule = mScheduleList.get(count);
                long date = schedule.getStartTime() / 10000L;
                String city = schedule.getCity();
                MainModel mainModel = MainModel.getInstance();
                ArrayList<NewPlan> newPlans = mainModel.inquiryPlans(schedule.getEditFinishTime(),
                        mMainView.getActivity().getApplicationContext());
                Intent intent = new Intent(mMainView.getActivity(), ShowScheduleActivity.class);
                intent.putExtra("list", newPlans);
                intent.putExtra("datelong", date);
                intent.putExtra("city", city);
                intent.putExtra("weatherStore", ShowScheduleActivity.OLD);
                e.onNext(intent);
            }
        }).observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Intent>() {
                    @Override
                    public void accept(Intent intent) throws Exception {
                        mMainView.getActivity().startActivity(intent);
                    }
                });
    }

    @Override
    public String getUrl() {
        Activity activity = mMainView.getActivity();
        MainModel mainModel = MainModel.getInstance();
        mainModel.setActivity(activity);
        if (mainModel.getWeatherOn("use_default_image")) {
            return "DEFAULT";
        }
        if (mainModel.getWeatherOn("use_local_image")) {
            return mainModel.getUrl("Image_path");
        }
        if (mainModel.getWeatherOn("update_image_online")) {
            SharedPreferences sharedPreferences = activity.
                    getSharedPreferences("com.android.betterway_preferences", Context.MODE_PRIVATE);
            int duration = Integer.parseInt(sharedPreferences.getString("update_duration", "1"));
            MyDate myDate = TimeUtil.getDayTime();
            LogUtil.i(TAG, myDate.toString());
            MyDate lastDate = TimeUtil.intToMyDate(sharedPreferences.getInt("downDate", 0));
            LogUtil.i(TAG, lastDate.toString());
            if (TimeUtil.getDayDuration(lastDate, myDate) >= duration) {
                ImageDownload.downloadUrl(activity.getApplicationContext());
            }
            return mainModel.getUrl("OnlineImagePath");
        }
        mainModel = null;
        return "NONE";
    }
}
