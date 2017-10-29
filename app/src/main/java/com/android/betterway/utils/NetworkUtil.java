package com.android.betterway.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public final class NetworkUtil {
    private NetworkUtil() {
    }
    /**
     * 判断当前是否有网络连接
     * @param context 获取context
     */
    public static void isNetworkConnected(Context context) {
        if (context != null) {
            // 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            // 获取NetworkInfo对象
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            //判断NetworkInfo对象是否为空
            if (networkInfo == null) {
                ToastUtil.show(context, "当前无网络连接");
            }
        }
    }
}
