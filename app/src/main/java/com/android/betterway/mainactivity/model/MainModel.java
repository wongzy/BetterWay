package com.android.betterway.mainactivity.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public final class MainModel {
    private static final String PREFERENCE = "com.android.betterway_preferences";
    private MainModel() {
    }
    /**
     * 获得开关的信息
     * @param activity 传入的活动引用
     * @param key 键值
     * @return 开关是否打开的信息
     */
    public boolean getWeatherOn(Activity activity, String key) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, true);
    }

    /**
     * 获得图片地址信息
     * @param activity 传入的活动引用
     * @param key 键值
     * @return 图片的地址信息
     */
    public String getUrl(Activity activity, String key) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "NONE");
    }
    public static MainModel getInstance() {
        return MainModelHolder.INSTANCE;
    }

    /**
     * 单例模式内部类
     */
    private static class MainModelHolder {
        private static final MainModel INSTANCE = new MainModel();
    }
}
