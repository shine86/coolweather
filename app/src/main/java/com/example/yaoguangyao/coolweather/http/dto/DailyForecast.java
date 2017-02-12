package com.example.yaoguangyao.coolweather.http.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yaoguangyao on 2017/2/11.
 */
public class DailyForecast {
    public String date;
    public Tmp tmp;
    public Cond cond;

    public class Cond {
        @SerializedName("txt_d")
        public String txtd;
    }

    public class Tmp {
        public String max;
        public String min;
    }
}
