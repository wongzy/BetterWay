package com.android.betterway.settingactivity.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import butterknife.BindString;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public final class SettingModel {
    private static final String PREFERENCE = "com.android.betterway_preferences";
    private SettingModel() {
    }

    /**
     * 获得字符串数据的方法
     * @param activity 传入的activity
     * @param key 数据对应的key
     * @return 获得的字符串数据
     */
    public String getStringData(Activity activity, String key) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "无");
    }

    /**
     * 存入字符串数据的方法
     * @param activity 传入的activity
     * @param key 字符串数据对应的key
     * @param data 需要存入的数据
     */
    public void putStringData(Activity activity, String key, String data) {
        SharedPreferences.Editor editor = activity.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE).edit();
        editor.putString(key, data);
        editor.apply();
    }
    public static SettingModel getInstance() {
        return SettingModelHolder.INSTANCE;
    }

    /**
     * 内部类的单例模式用法
     */
   private static class SettingModelHolder {
       private static final SettingModel INSTANCE = new SettingModel();
   }
}
