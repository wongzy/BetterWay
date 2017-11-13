package com.android.betterway.mainactivity.presenter;


/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */
  public interface MainPresenter {
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
   * 获得图片的地址
   * @return 图片的地址
   */
    String getUrl();
}
