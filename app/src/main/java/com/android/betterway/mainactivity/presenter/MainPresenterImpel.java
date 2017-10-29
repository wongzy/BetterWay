package com.android.betterway.mainactivity.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.android.betterway.data.MyDate;
import com.android.betterway.mainactivity.model.MainModel;
import com.android.betterway.mainactivity.view.MainView;
import com.android.betterway.network.image.ImageDownload;
import com.android.betterway.settingactivity.view.SettingActivity;
import com.android.betterway.utils.TimeUtil;


import javax.inject.Inject;


/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public class MainPresenterImpel implements MainPresenter {
    private static final String TAG = "MainPresenterImpel";
    private final MainView mMainView;

    @Inject
    public MainPresenterImpel(MainView mainView) {
        mMainView = mainView;
    }
    @Override
    public void addNormalSchedule() {
    }

    @Override
    public void addAutoSchedule() {
    }

    @Override
    public void getSet() {
        Activity activity = mMainView.getActivity();
        Intent intent = new Intent(activity.getApplicationContext(), SettingActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void choseSchedule(int position) {
    }

    @Override
    public void deleteSchedule(int position) {

    }

    @Override
    public boolean isModalEmpty() {
        return false;
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
            MyDate lastDate = TimeUtil.IntToMyDate(sharedPreferences.getInt("downDate", 0));
            if (TimeUtil.getDayDuration(lastDate, myDate) >= duration) {
                ImageDownload.downloadUrl(activity.getApplicationContext());
            }
            return mainModel.getUrl("OnlineImagePath");
        }
        mainModel = null;
        return "NONE";
    }
}
