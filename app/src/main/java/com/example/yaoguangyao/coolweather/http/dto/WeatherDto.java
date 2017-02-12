package com.example.yaoguangyao.coolweather.http.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yaoguangyao on 2017/2/11.
 */

public class WeatherDto {

    @SerializedName("HeWeather")
    public List<HeWeather> heWeatherList;
}
