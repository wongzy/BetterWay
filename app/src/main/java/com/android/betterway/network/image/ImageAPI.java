package com.android.betterway.network.image;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */
interface ImageAPI {
    /**
     * 网络请求
     * @return 实体类
     */
    @GET("HPImageArchive.aspx?format=js&idx=0&n=1")
    Observable<ImageBean> image();
}
