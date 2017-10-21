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
    private SharedPreferences sharedPreferences;
    private MainModel() {
    }
    public void setActivity(Activity activity) {
        sharedPreferences = activity.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
    }
    /**
     * 获得开关的信息
     * @param key 键值
     * @return 开关是否打开的信息
     */
    public boolean getWeatherOn(String key) {
        return sharedPreferences.getBoolean(key, true);
    }

    /**
     * 获得图片地址信息
     * @param key 键值
     * @return 图片的地址信息
     */
    public String getUrl(String key) {
        return sharedPreferences.getString(key, "NONE");
    }

    /**
     * 获得单例
     * @return 单例
     */
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
