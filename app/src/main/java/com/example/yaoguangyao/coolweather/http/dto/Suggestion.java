package com.example.yaoguangyao.coolweather.http.dto;

/**
 * Created by yaoguangyao on 2017/2/11.
 */
public class Suggestion {
    public Comf comf;
    public Cw cw;
    public Sport sport;

    public class Comf {
        public String brf;
        public String txt;
    }

    public class Cw {
        public String bref;
        public String txt;
    }

    public class Sport {
        public String bref;
        public String txt;
    }
}
