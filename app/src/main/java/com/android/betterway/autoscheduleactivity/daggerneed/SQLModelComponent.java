package com.android.betterway.autoscheduleactivity.daggerneed;

import com.android.betterway.autoscheduleactivity.model.SQLModel;
import com.android.betterway.autoscheduleactivity.present.AutoSchedulePresenterImpel;
import com.android.betterway.showscheduleactivity.Impel.ShowScheduleImpel;

import dagger.Component;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */
@Component (modules = SQLModelModule.class)
public interface SQLModelComponent {
    /**
     * 注入ShowScheduleImpel
     * @param showScheduleImpel 需要注入的ShowScheduleImpel
     */
    void inject(ShowScheduleImpel showScheduleImpel);
    /**
     * 获得注入的SQLModel
     * @return 注入的SQLModel
     */
    SQLModel getSQLModel();
}
