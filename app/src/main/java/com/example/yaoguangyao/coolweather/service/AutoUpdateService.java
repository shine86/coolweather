package com.example.yaoguangyao.coolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.example.yaoguangyao.coolweather.http.WeatherApi;
import com.example.yaoguangyao.coolweather.http.dto.WeatherDto;
import com.google.gson.Gson;

/**
 * Created by yaoguangyao on 2017/2/12.
 */

public class AutoUpdateService extends Service{

    private WeatherApi weatherApi = new WeatherApi();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBingPic();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 8 * 60 * 60 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);

    }

    private void updateBingPic() {
        weatherApi.getWeatherPic(new WeatherApi.ApiLinster() {
            @Override
            public void onSuccess(Object result) {
                if (result != null) {
                    String bingPic = (String) result;
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                    editor.putString("bing_pic", bingPic);
                    editor.apply();
                }
            }

            @Override
            public void onFail(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void updateWeather() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = pref.getString("weather", null);
        if (weatherString != null) {
            WeatherDto weatherDto = new Gson().fromJson(weatherString, WeatherDto.class);
            String weatherId = weatherDto.heWeatherList.get(0).basic.id;
            weatherApi.getWeather(weatherId, new WeatherApi.ApiLinster() {
                @Override
                public void onSuccess(Object result) {
                    if (result != null) {
                        String responseText = (String) result;
                        WeatherDto weather = new Gson().fromJson(responseText, WeatherDto.class);
                        if (weather != null && "ok".equals(weather.heWeatherList.get(0).status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                        }
                    }

                }

                @Override
                public void onFail(Exception e) {
                    e.printStackTrace();
                }
            });

        }
    }
}
