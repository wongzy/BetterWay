package com.android.betterway.utils;

import android.content.Context;

import com.android.betterway.data.LocationBean;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public final class JsonUtil {
    private JsonUtil() {
    }

    /**
     * 将字符串转化为实体类
     * @param context 上下文
     * @param dataLocation 数据所在位置
     * @return 实体类
     */
    public static ArrayList<LocationBean> getJsonData(Context context, String dataLocation) {
        String jsonData = new GetJsonDataUtil().getJson(context, dataLocation); //获取assets目录下的json文件数据
        ArrayList<LocationBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(jsonData);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                LocationBean entity = gson.fromJson(data.optJSONObject(i).toString(), LocationBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }
}
