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
    private String city;
    public ShowScheduleImpelModule(ShowScheduleView showScheduleView, String city) {
        mShowScheduleView = showScheduleView;
        this.city = city;
    }

    @Provides
    ShowScheduleView getShowScheduleView() {
        return mShowScheduleView;
    }

    @Provides
    String getCity() {
        return city;
    }

    @Provides
    ShowScheduleImpel getShowScheduleImpel(ShowScheduleView showScheduleView, String city) {
        return new ShowScheduleImpel(showScheduleView, city);
    }
}
