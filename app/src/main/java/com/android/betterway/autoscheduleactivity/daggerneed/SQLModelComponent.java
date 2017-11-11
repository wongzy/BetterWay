package com.android.betterway.autoscheduleactivity.daggerneed;

import com.android.betterway.autoscheduleactivity.model.SQLModel;
import com.android.betterway.autoscheduleactivity.present.AutoSchedulePresenterImpel;

import dagger.Component;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */
@Component (modules = SQLModelModule.class)
public interface SQLModelComponent {
    /**
     * 注入SQLmodel
     * @param autoSchedulePresenterImpel 注入的对象
     */
    void inject(AutoSchedulePresenterImpel autoSchedulePresenterImpel);

    /**
     * 获得注入的SQLModel
     * @return 注入的SQLModel
     */
    SQLModel getSQLModel();
}
