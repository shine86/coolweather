package com.example.yaoguangyao.coolweather.http.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yaoguangyao on 2017/2/11.
 */
public class HeWeather {
    public String status;

    public Basic basic;

    public AQI aqi;

    public Now now;

    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<DailyForecast> dailyForecastList;
}
