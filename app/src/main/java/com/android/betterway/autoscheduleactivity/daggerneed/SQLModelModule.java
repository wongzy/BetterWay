package com.android.betterway.autoscheduleactivity.daggerneed;


import android.content.Context;

import com.android.betterway.autoscheduleactivity.model.SQLModel;

import dagger.Module;
import dagger.Provides;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */
@Module
public class SQLModelModule {
    private Context mContext;
    public SQLModelModule(Context context) {
        mContext = context;
    }

    /**
     * 提供构造SQLModel所需的Context
     * @return 所需的Context
     */
    @Provides
    Context returnContext() {
        return mContext;
    }

    /**
     * 提供SQLModel
     * @param context 提供的Context
     * @return 构造完成的SQLModel
     */
    @Provides
    SQLModel returnSQLModel(Context context) {
        return new SQLModel(context);
    }
}
