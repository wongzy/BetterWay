package com.android.betterway.utils;

import android.util.Log;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public final class LogUtil {
    private static final int VERBOSE = 1;
    private static final int DEBUG = 2;
    private static final int INFO = 3;
    private static final int WARN = 4;
    private static final int ERROE = 5;
    private static final int NOTHING = 6;
    private static int state = VERBOSE;

    private LogUtil() { }

    /**
     * 打印Verbose
     * @param tag 标签
     * @param string 说明
     */
    public static void v(String tag, String string) {
        if (state <= VERBOSE) {
            Log.v(tag, string);
        }
    }

    /**
     * 打印Debug
     * @param tag 标签
     * @param string 说明
     */
    public static void d(String tag, String string) {
        if (state <= DEBUG) {
            Log.d(tag, string);
        }
    }

    /**
     * 打印Info信息
     * @param tag 标签
     * @param string 说明
     */
    public static void i(String tag, String string) {
        if (state <= INFO) {
            Log.i(tag, string);
        }
    }

    /**
     * 打印Wain
     * @param tag 标签
     * @param string 说明
     */
    public static void w(String tag, String string) {
        if (state <= WARN) {
            Log.w(tag, string);
        }
    }

    /**
     * 打印Error
     * @param tag 标签
     * @param string 说明
     */
    public static void e(String tag, String string) {
        if (state <= ERROE) {
            Log.e(tag, string);
        }
    }
}
