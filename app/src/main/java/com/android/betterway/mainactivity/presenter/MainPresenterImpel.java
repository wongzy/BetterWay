package com.android.betterway.mainactivity.presenter;

import android.app.Activity;
import android.content.Intent;

import com.android.betterway.mainactivity.view.MainView;
import com.android.betterway.settingactivity.view.SettingActivity;


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
}
