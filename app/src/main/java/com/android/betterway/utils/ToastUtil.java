package com.android.betterway.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public final class ToastUtil {
    private ToastUtil() {

    }

    /**
     * 显示Toast，默认时间为long
     * @param context 获得显示界面
     * @param string 所要显示的文字
     */
    public static void show(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_LONG).show();
    }
}
