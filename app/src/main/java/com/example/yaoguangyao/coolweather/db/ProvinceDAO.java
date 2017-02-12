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

    public ProvinceDao() {}

    public List<ProvinceBo> findAll() {
        realm = Realm.getDefaultInstance();
        List<ProvinceBo> provinceBoList = new ArrayList<>();
        try {
            List<Province> provinceList = realm.where(Province.class).findAll();
            if (provinceList.size() > 0) {
                for (Province province : provinceList) {
                    ProvinceBo provinceBo = new ProvinceBo(province);
                    provinceBoList.add(provinceBo);
                }
            }
        } finally {
            realm.close();
        }

        return provinceBoList;
    }

    public void save(ProvinceBo provinceBo) {
        realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            realm.copyToRealm(provinceBo.getEntity());
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }

    public void deleteAll() {
        realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            realm.where(Province.class).findAll().deleteAllFromRealm();
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }


}
