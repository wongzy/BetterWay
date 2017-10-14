package com.android.betterway.mainactivity.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.android.betterway.mainactivity.view.MainView;
import com.android.betterway.settingactivity.SettingActivity;
import com.android.betterway.utils.ToastUtil;

import java.lang.ref.WeakReference;

import javax.inject.Inject;


/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public class MainPresenterImpel implements MainPresenter {

    @Inject
    public MainPresenterImpel(MainView mainView) {
    }
    @Override
    public void addNormalSchedule() {
    }

    @Override
    public void addAutoSchedule() {
    }

    @Override
    public void getSet() {
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
