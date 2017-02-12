package com.example.yaoguangyao.coolweather.http;

import android.text.TextUtils;

import com.example.yaoguangyao.coolweather.http.dto.WeatherDto;
import com.example.yaoguangyao.coolweather.util.HttpUtil;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by yaoguangyao on 2017/2/10.
 */

public class WeatherApi {
    private final static String WEATHER_API_KEY = "6c32bf544e6048e7b477a6bce5bc096e";

    /**
     * 获取天气
     * @param cityCode
     * @param apiLinster
     */
    public void getWeather(String cityCode, final ApiLinster apiLinster) {
        String url = "http://guolin.tech/api/weather?cityid=" + cityCode + "&key=" + WEATHER_API_KEY;
        HttpUtil.sendOnHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                apiLinster.onFail(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                if (!TextUtils.isEmpty(responseText)) {
                    apiLinster.onSuccess(responseText);
                }
            }
        });
    }

    /**
     * 获取天气背景图片
     * @param apiLinster
     */
    public void getWeatherPic(final ApiLinster apiLinster) {
        String url = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOnHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                apiLinster.onFail(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                if (!TextUtils.isEmpty(responseText)) {
                    apiLinster.onSuccess(responseText);
                }
            }
        });
    }

    public interface ApiLinster {
        void onSuccess(Object result);
        void onFail(Exception e);
    }
}
