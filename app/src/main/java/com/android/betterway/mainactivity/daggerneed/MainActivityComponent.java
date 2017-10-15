package com.android.betterway.mainactivity.daggerneed;

import android.app.Activity;

import com.android.betterway.mainactivity.presenter.MainPresenterImpel;
import com.android.betterway.mainactivity.view.MainActivity;

import dagger.Component;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */
@Component(modules = MainPresenterImpelModule.class)
public interface MainActivityComponent {
    /**
     * 这里inject表示注入的意思，这个方法名可以随意更改，但建议就
     * 用inject即可。
     * @param activity 依赖注入的活动实例
     */
    void inject(Activity activity);
    MainPresenterImpel getMainPresenterImpel();
}
