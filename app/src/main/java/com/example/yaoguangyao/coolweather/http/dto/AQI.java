package com.example.yaoguangyao.coolweather.http.dto;



/**
 * Created by yaoguangyao on 2017/2/11.
 */
public class AQI {
    public City city;

    public class City {
        public String aqi;
        public String pm25;
    }
}
