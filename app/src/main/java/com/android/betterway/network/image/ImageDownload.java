package com.android.betterway.network.image;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public final class ImageDownload {
    private Retrofit retrofit;
    private ImageDownload() {
        retrofit = new Retrofit.Builder()
                .client(new OkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://cn.bing.com/")
                .build();
    }

    /**
     *
     */
    public static void downloadUrl(){
        
    }
}
