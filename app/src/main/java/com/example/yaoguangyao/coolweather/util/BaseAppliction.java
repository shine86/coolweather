package com.example.yaoguangyao.coolweather.util;

import android.app.Application;
import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by yaoguangyao on 2017/2/8.
 */

public class BaseAppliction extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        initRealm(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
    }

    /**
     * 获取全局context
     * @return
     */
    public static Context getContext() {
        return context;
    }

    /**
     * 初始化realm
     * @param context
     */
    private void initRealm(Context context) {
        //// Initialize Realm
        Realm.init(context);
    }
}
