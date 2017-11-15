package com.android.betterway.myservice;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public interface HandlerInterface {
    /**
     * 添加新时间
     * @param l 时间
     */
    void addDateI(long l);

    /**
     * 删除时间
     * @param l 时间
     */
    void delateDateI(long l);

    /**
     * 改变间隔
     * @param i 间隔
     */
    void durationChangeI(int i);

    /**
     * 判断是否要开始
     */
    void judgeStartI();
}
