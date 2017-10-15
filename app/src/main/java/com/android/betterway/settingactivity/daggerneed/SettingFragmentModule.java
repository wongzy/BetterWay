package com.android.betterway.settingactivity.daggerneed;

import com.android.betterway.settingactivity.presenter.SettingPresenterImpel;
import com.android.betterway.settingactivity.view.SettingView;

import dagger.Module;
import dagger.Provides;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */
@Module
public class SettingFragmentModule {
    private SettingView mSettingView;
    public SettingFragmentModule(SettingView settingView) {
        mSettingView = settingView;
    }

    @Provides
    SettingPresenterImpel provideSettingPresenterImpel(SettingView settingView) {
        return new SettingPresenterImpel(settingView);
    }
    /**
     *
     * @return
     */
    @Provides
    SettingView provideSettingView() {
        return mSettingView;
    }
}
