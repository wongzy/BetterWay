package com.android.betterway.settingactivity.daggerneed;

import android.app.Fragment;


import com.android.betterway.settingactivity.presenter.SettingPresenterImpel;

import dagger.Component;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */
@Component(modules = SettingFragmentModule.class)
public interface SettingFragmentComponent {
    /**
     * 注入到fragment中去
     * @param fragment 需要注入的fragment
     */
    void inject(Fragment fragment);

    /**
     * 获得SettingPresenterImpel
     * @return 注入的SettingPresenterImpel
     */
    SettingPresenterImpel getSettingPresenterImpel();
}
