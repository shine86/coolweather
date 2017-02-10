package com.example.yaoguangyao.coolweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by yaoguangyao on 2017/2/8.
 */

public class HttpUtil {

    public static void sendOnHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }

}
