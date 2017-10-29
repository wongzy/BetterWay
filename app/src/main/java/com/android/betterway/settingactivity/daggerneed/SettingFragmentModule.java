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

    /**
     * 提供SettingPresenterImpel的方法
     * @param settingView 构造必需的接口参数
     * @return Impel对象
     */
    @Provides
    SettingPresenterImpel provideSettingPresenterImpel(SettingView settingView) {
        return new SettingPresenterImpel(settingView);
    }
    /**
     * 提供接口
     * @return 接口
     */
    @Provides
    SettingView provideSettingView() {
        return mSettingView;
    }
}
