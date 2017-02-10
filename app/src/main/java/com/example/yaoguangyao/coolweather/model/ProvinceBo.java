package com.example.yaoguangyao.coolweather.model;

import com.example.yaoguangyao.coolweather.db.entity.Province;

import io.realm.annotations.PrimaryKey;

/**
 * Created by yaoguangyao on 2017/2/9.
 */

public class ProvinceBo {
    @PrimaryKey
    private int id;
    private String provinceName;
    private int provinceCode;

    public ProvinceBo() {
    }

    public ProvinceBo(Province province) {
        setId(province.getId());
        setProvinceCode(province.getProvinceCode());
        setProvinceName(province.getProvinceName());
    }

    public Province getEntity() {
        Province province = new Province();
        province.setId(getId());
        province.setProvinceCode(getProvinceCode());
        province.setProvinceName(getProvinceName());
        return province;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
