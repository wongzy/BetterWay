package com.android.betterway.autoscheduleactivity.daggerneed;

import com.android.betterway.autoscheduleactivity.present.AutoSchedulePresenterImpel;
import com.android.betterway.autoscheduleactivity.view.AutoScheduleView;

import dagger.Module;
import dagger.Provides;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */
@Module
public class AutoSchedulePresenterImpelModule {
    private static final String TAG = "AutoSchedulePresneterImpelMOdule";
    private AutoScheduleView mAutoScheduleView;
    public AutoSchedulePresenterImpelModule(AutoScheduleView autoScheduleView) {
        mAutoScheduleView = autoScheduleView;
    }

    /**
     * 返回AutoSchedulePresenterImpel实例
     * @param autoScheduleView 构造AutoSchedulePresenter所必须的参数
     * @return AutoSchedulePresenterImpel实例
     */
    @Provides
    AutoSchedulePresenterImpel getAutoSchedulePresenterImpel(AutoScheduleView autoScheduleView) {
        return new AutoSchedulePresenterImpel(autoScheduleView);
    }

    /**
     * 提供构造AutoSchedulePresenterImpel所需的参数
     * @return 构造所需参数
     */
    @Provides
    AutoScheduleView provideAutoScheduleView() {
        return mAutoScheduleView;
    }
}
