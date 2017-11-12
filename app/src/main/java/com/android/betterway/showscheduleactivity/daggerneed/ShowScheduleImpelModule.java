package com.android.betterway.showscheduleactivity.daggerneed;

import com.android.betterway.showscheduleactivity.Impel.ShowScheduleImpel;
import com.android.betterway.showscheduleactivity.view.ShowScheduleView;

import dagger.Module;
import dagger.Provides;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */
@Module
public class ShowScheduleImpelModule {
    private ShowScheduleView mShowScheduleView;
    public ShowScheduleImpelModule(ShowScheduleView showScheduleView) {
        mShowScheduleView = showScheduleView;
    }

    @Provides
    ShowScheduleView getShowScheduleView() {
        return mShowScheduleView;
    }

    @Provides
    ShowScheduleImpel getShowScheduleImpel(ShowScheduleView showScheduleView) {
        return new ShowScheduleImpel(showScheduleView);
    }
}
