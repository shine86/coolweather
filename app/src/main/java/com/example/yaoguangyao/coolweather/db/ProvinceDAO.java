package com.example.yaoguangyao.coolweather.db;

import com.example.yaoguangyao.coolweather.db.entity.Province;
import com.example.yaoguangyao.coolweather.model.ProvinceBo;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by yaoguangyao on 2017/2/9.
 */

public class ProvinceDao {
    private Realm realm;

    public ProvinceDao() {
        realm = Realm.getDefaultInstance();
    }

    public List<ProvinceBo> findAll() {
        List<Province> provinceList = null;
        try {
            provinceList = realm.where(Province.class).findAll();
        } finally {
            realm.close();
        }

        List<ProvinceBo> provinceBoList = new ArrayList<>();
        if (provinceList.size() > 0) {
            for (Province province : provinceList) {
                ProvinceBo provinceBo = new ProvinceBo(province);
                provinceBoList.add(provinceBo);
            }
        }

        return provinceBoList;
    }

    public void save(ProvinceBo provinceBo) {
        try {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(provinceBo.getEntity());
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }


}
