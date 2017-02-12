package com.example.yaoguangyao.coolweather.db;

import android.util.Log;

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
    }

    public List<CountyBo> findAllByCityId(int cityId) {
        realm = Realm.getDefaultInstance();
        List<CountyBo> countyBoList = new ArrayList<>();
        try {
            List<County> countyList = realm.where(County.class).equalTo("cityId", cityId).findAll();
            if (countyList != null) {
                for (County county : countyList) {
                    CountyBo countyBo = new CountyBo(county);
                    countyBoList.add(countyBo);
                }

            }
        } finally {
            realm.close();
        }


        return countyBoList;
    }

    public void save(CountyBo countyBo) {
        Log.d("flyzing", "save: " + countyBo);
        realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            realm.copyToRealm(countyBo.getEntity());
            realm.commitTransaction();
        } finally {
            realm.close();
        }
    }
}
