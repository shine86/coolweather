package com.example.yaoguangyao.coolweather.db;

import com.example.yaoguangyao.coolweather.db.entity.County;
import com.example.yaoguangyao.coolweather.model.CountyBo;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by yaoguangyao on 2017/2/9.
 */

public class CountyDao {
    private Realm realm;

    public CountyDao() {
        realm = Realm.getDefaultInstance();
    }

    public List<CountyBo> findAllByCityId(int cityId) {
        List<County> countyList = null;
        try {
            countyList = realm.where(County.class).equalTo("cityId", cityId).findAll();
        } finally {
            realm.close();
        }

        List<CountyBo> countyBoList = new ArrayList<>();
        if (countyList != null) {
            for (County county : countyList) {
                CountyBo countyBo = new CountyBo(county);
                countyBoList.add(countyBo);
            }

        }
        return countyBoList;
    }

    public void save(CountyBo countyBo) {
        try {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(countyBo.getEntity());
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }
}
