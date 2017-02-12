package com.example.yaoguangyao.coolweather.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.example.yaoguangyao.coolweather.R;

/**
 * Created by yaoguangyao on 2017/2/10.
 */

public class MainActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences((this));
        if (prefs.getString("weather", null) != null) {
            Intent intent = new Intent(this, WeatherActiivity.class);
            startActivity(intent);
            finish();
        }
    }
}
