package com.android.betterway.mainactivity.daggerneed;

import com.android.betterway.mainactivity.presenter.MainPresenterImpel;
import com.android.betterway.mainactivity.view.MainView;

import dagger.Module;
import dagger.Provides;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */
@Module
public class MainPresenterImpelModule {
    private MainView mMainView;
    public MainPresenterImpelModule(MainView mainView) {
        mMainView = mainView;
    }
    /**
     * 返回MainPresenterImpelModule
     * @param mainView constructor必需的参数
     * @return MainPresenterImpel实例
     */
    @Provides
    MainPresenterImpel provideImpel(MainView mainView) {
        return new MainPresenterImpel(mainView);
    }

    /**
     * 返回在活动中初始化的本身MainView
     * @return 提供创建MainPresenterImpel的MainView
     */
    @Provides
    MainView provideMainVew() {
        return mMainView;
    }
}
