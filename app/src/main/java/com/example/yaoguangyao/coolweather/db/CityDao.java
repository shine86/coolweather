package com.example.yaoguangyao.coolweather.db;

import com.example.yaoguangyao.coolweather.db.entity.City;
import com.example.yaoguangyao.coolweather.model.CityBo;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by yaoguangyao on 2017/2/8.
 */

public class CityDao {
    private Realm realm;

    public CityDao() {
        realm = Realm.getDefaultInstance();
    }

    public List<CityBo> findAllByProvinceId(int provinceId) {
        List<City> cityList = null;
        try {
            cityList = realm.where(City.class).equalTo("provinceId", provinceId).findAll();
        } finally {
            realm.close();
        }

        List<CityBo> cityBoList = new ArrayList<>();
        if (cityList.size() > 0) {
            for (City city : cityList) {
                CityBo cityBo = new CityBo(city);
                cityBoList.add(cityBo);
            }
        }

        return cityBoList;
    }



    public void save(CityBo cityBo) {
        try {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(cityBo.getEntity());
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }

}
