package com.example.yaoguangyao.coolweather.db;

import android.util.Log;

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

    }

    public List<CityBo> findAllByProvinceId(int provinceId) {
        realm = Realm.getDefaultInstance();
        List<CityBo> cityBoList = new ArrayList<>();
        try {
            List<City> cityList = realm.where(City.class).equalTo("provinceId", provinceId).findAll();
            Log.d("flyzing", "findAllByProvinceId cityList: " + cityList.size());
            if (cityList.size() > 0) {
                for (City city : cityList) {
                    CityBo cityBo = new CityBo(city);
                    cityBoList.add(cityBo);
                }
            }
        } finally {
            realm.close();
        }
        return cityBoList;
    }



    public void save(CityBo cityBo) {
        realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            realm.copyToRealm(cityBo.getEntity());
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }

}
