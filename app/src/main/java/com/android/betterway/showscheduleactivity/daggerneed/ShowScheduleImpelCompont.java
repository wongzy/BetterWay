package com.android.betterway.showscheduleactivity.daggerneed;

import com.android.betterway.showscheduleactivity.Impel.ShowScheduleImpel;
import com.android.betterway.showscheduleactivity.view.ShowScheduleActivity;

import dagger.Component;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */
@Component (modules = ShowScheduleImpelModule.class)
public interface ShowScheduleImpelCompont {
    /**
     * 注入对象
     * @param showScheduleActivity 需要注入的对象
     */
    void inject(ShowScheduleActivity showScheduleActivity);

    /**
     * 获得Impel
     * @return 返回Impel
     */
    ShowScheduleImpel getShowScheduleImpel();
}
