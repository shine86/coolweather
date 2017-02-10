package com.example.yaoguangyao.coolweather.model;

import com.example.yaoguangyao.coolweather.db.entity.City;

/**
 * Created by yaoguangyao on 2017/2/9.
 */

public class CityBo {
    private int id;
    private String cityName;
    private int cityCode;
    private int provinceId;

    public CityBo() {
    }

    public CityBo(City city) {
        setId(city.getId());
        setCityName(city.getCityName());
        setProvinceId(city.getProvinceId());
        setCityCode(city.getCityCode());
    }

    public City getEntity() {
        City city = new City();
        city.setId(getId());
        city.setCityCode(getCityCode());
        city.setCityName(getCityName());
        return city;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
