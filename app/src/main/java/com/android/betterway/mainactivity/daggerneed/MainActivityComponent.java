package com.android.betterway.mainactivity.daggerneed;

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
     * 需要用到这个连接器的对象，就是这个对象里面有需要注入的属性
     * （被标记为@Inject的属性）
     * 这里inject表示注入的意思，这个方法名可以随意更改，但建议就
     * 用inject即可。
     * @param activity 依赖注入的活动实例
     */
    void inject(MainActivity activity);
}
