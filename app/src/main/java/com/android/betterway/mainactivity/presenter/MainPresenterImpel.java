package com.android.betterway.mainactivity.presenter;

import android.content.Context;

import com.android.betterway.mainactivity.view.MainView;
import com.android.betterway.utils.ToastUtil;

import javax.inject.Inject;


/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public class MainPresenterImpel implements MainPresenter {
    private final Context mContext;

    @Inject
    public MainPresenterImpel(MainView mainView) {
        mContext = mainView.getContext();
    }
    @Override
    public void addNormalSchedule() {
        ToastUtil.show(mContext, "addNormalSchedule");
    }

    @Override
    public void addAutoSchedule() {
        ToastUtil.show(mContext, "addAutoSchedule");
    }

    @Override
    public void getSet() {
        ToastUtil.show(mContext, "getSet");
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
