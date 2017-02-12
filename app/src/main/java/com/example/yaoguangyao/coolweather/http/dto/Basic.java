package com.example.yaoguangyao.coolweather.http.dto;

/**
 * Created by yaoguangyao on 2017/2/12.
 */
public class Basic {
    public String city;
    public String id;
    public Update update;

    public class Update {
        public String loc;
    }
}
