package com.android.betterway.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.Display;
import android.view.View;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public final class ScreenShotUtil {
    private ScreenShotUtil() {
    }

    /**
     * 获取屏幕截图
     * @param activity 获取当前Activity
     * @return 截图
     */
    public static Bitmap shot(Activity activity) {
        //获取window最底层的view
        View view = activity.getWindow().getDecorView();
        view.buildDrawingCache();
        //状态栏高度
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int stateBarHeight = rect.top;
        Display display = activity.getWindowManager().getDefaultDisplay();
        //获取屏幕宽高
        int widths = display.getWidth();
        int height = display.getHeight();
        //设置允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true);
        //去掉状态栏高度
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, stateBarHeight, widths, height - stateBarHeight);
        view.destroyDrawingCache();
        return bitmap;
    }
}
