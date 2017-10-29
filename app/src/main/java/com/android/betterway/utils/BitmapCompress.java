package com.android.betterway.utils;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import java.io.ByteArrayOutputStream;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

 public final class BitmapCompress {
    private BitmapCompress() {
    }

    /**
     * 缩放图片像素压缩
     * @param bmp （根据Bitmap图片压缩）
     * @return 压缩后的Bitmap
     */
    public static Bitmap compressScale(Bitmap bmp) {
        // 尺寸压缩倍数,值越大，图片尺寸越小
        int ratio = 5;
        // 压缩Bitmap到对应尺寸
        Bitmap result = Bitmap.createBitmap(bmp.getWidth() / ratio, bmp.getHeight() / ratio, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, bmp.getWidth() / ratio, bmp.getHeight() / ratio);
        canvas.drawBitmap(bmp, null, rect, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 把压缩后的数据存放到baos中
        result.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        return result;
    }
}
