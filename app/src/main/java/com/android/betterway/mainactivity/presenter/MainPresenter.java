package com.android.betterway.mainactivity.presenter;

import android.view.View;

import com.android.betterway.utils.TimeUtil;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */
  public interface MainPresenter {
    /**
     * 添加自定义行程
     */
    void addNormalSchedule();

    /**
     * 添加自动规划行程
     */
    void addAutoSchedule();

  /**
   * 跳转到设置界面
   */
  void getSet();

    /**
     * 打开详细行程
     * @param position 列表中项目的位置
     */
    void choseSchedule(int position);

    /**
     * 删除行程
     * @param position 列表中项目的位置
     */
    void deleteSchedule(int position);

    /**
     * 判断数据是否为空
     * @return 是否为空
     */
    boolean isModalEmpty();

  /**
   * 获得图片的地址
   * @return 图片的地址
   */
    String getUrl();
}
