package com.example.yaoguangyao.coolweather.model;

import com.example.yaoguangyao.coolweather.db.entity.County;

/**
 * Created by yaoguangyao on 2017/2/9.
 */

public class CountyBo {
    private int id;
    private String countyName;
    private String weatherId;
    private int cityId;

    public CountyBo() {
    }

    public CountyBo(County county) {
        setId(county.getId());
        setWeatherId(county.getWeatherId());
        setCountyName(county.getCountyName());
        setCityId(county.getCityId());
    }

    public County getEntity() {
        County county = new County();
        county.setCityId(getCityId());
        county.setId(getId());
        county.setCountyName(getCountyName());
        county.setWeatherId(getWeatherId());
        return county;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
