package com.example.yaoguangyao.coolweather.view;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yaoguangyao.coolweather.R;
import com.example.yaoguangyao.coolweather.http.WeatherApi;
import com.example.yaoguangyao.coolweather.http.dto.DailyForecast;
import com.example.yaoguangyao.coolweather.http.dto.WeatherDto;
import com.google.gson.Gson;

/**
 * Created by yaoguangyao on 2017/2/10.
 */

public class WeatherActiivity extends AppCompatActivity{

    public SwipeRefreshLayout swipeRefresh;

    private WeatherApi weatherApi;

    private ScrollView weatherLayout;

    private TextView titleTxt;

    private TextView titleUpdateTimeTxt;

    private TextView degreeTxt;

    private TextView weatherInfoTxt;

    private LinearLayout forecastLayout;

    private TextView aqiTxt;

    private TextView pm25Txt;

    private TextView comfortTxt;

    private TextView carWashTxt;

    private TextView sportTxt;

    private ImageView bingPicImg;

    public DrawerLayout drawerLayout;

    private Button navBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        weatherApi = new WeatherApi();
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navBtn = (Button) findViewById(R.id.btn_nav);

        navBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //初始化组件
        weatherLayout = (ScrollView) findViewById(R.id.layout_weather);
        titleTxt = (TextView) findViewById(R.id.txt_title);
        titleUpdateTimeTxt = (TextView) findViewById(R.id.txt_title_update_time);
        degreeTxt = (TextView) findViewById(R.id.txt_degree);
        weatherInfoTxt = (TextView) findViewById(R.id.txt_weather_info);
        forecastLayout = (LinearLayout) findViewById(R.id.layout_forecast);
        aqiTxt = (TextView) findViewById(R.id.txt_aqi);
        pm25Txt = (TextView) findViewById(R.id.txt_pm25);
        comfortTxt = (TextView) findViewById(R.id.txt_comfort);
        carWashTxt = (TextView) findViewById(R.id.txt_car_wash);
        sportTxt = (TextView) findViewById(R.id.txt_sport);
        bingPicImg = (ImageView) findViewById(R.id.img_bing_pic);

        queryWeather();      
    }

    public void queryWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);



        String bingPic = prefs.getString("bing_pic", null);
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(bingPicImg);
        } else {
            loadBingPic();
        }

        final String weatherId;

        String weatherString = prefs.getString("weather", null);
        if (weatherString != null) {
            WeatherDto weatherDto = new Gson().fromJson(weatherString, WeatherDto.class);
            weatherId = weatherDto.heWeatherList.get(0).basic.id;
            showWeatherInfo(weatherDto);
        } else {
            weatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(weatherId);
            }
        });
        loadBingPic();
    }

    public void requestWeather(String weatherId) {
        weatherApi.getWeather(weatherId, new WeatherApi.ApiLinster() {
            @Override
            public void onSuccess(final Object result) {
                if (result != null) {
                    Log.d("flyzing", "onSuccess: " + result.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            WeatherDto weatherDto = new Gson().fromJson(result.toString(), WeatherDto.class);
                            if (weatherDto != null && weatherDto.heWeatherList.get(0).status.equals("ok")) {
                                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActiivity.this).edit();
                                editor.putString("weather", result.toString());
                                editor.apply();
                                showWeatherInfo(weatherDto);
                            } else {
                                Toast.makeText(WeatherActiivity.this, "获取天气失败", Toast.LENGTH_SHORT).show();
                            }
                            swipeRefresh.setRefreshing(false);
                        }
                    });
                }
            }

            @Override
            public void onFail(Exception e) {
                Toast.makeText(WeatherActiivity.this, "获取天气失败", Toast.LENGTH_SHORT).show();
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    private void loadBingPic() {
        weatherApi.getWeatherPic(new WeatherApi.ApiLinster() {
            @Override
            public void onSuccess(Object result) {
                if (result != null) {
                    final String bingPic = (String) result;
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActiivity.this).edit();
                    editor.putString("bing_pic", bingPic);
                    editor.apply();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(WeatherActiivity.this).load(bingPic).into(bingPicImg);
                        }
                    });
                }
            }

            @Override
            public void onFail(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void showWeatherInfo(WeatherDto weatherDto) {
        String cityName = weatherDto.heWeatherList.get(0).basic.city;
        String updateTime = weatherDto.heWeatherList.get(0).basic.update.loc.split(" ")[1];
        String degree = weatherDto.heWeatherList.get(0).now.tmp + "℃";
        String weatherInfo = weatherDto.heWeatherList.get(0).now.cond.txt;
        titleTxt.setText(cityName);
        titleUpdateTimeTxt.setText(updateTime);
        degreeTxt.setText(degree);
        weatherInfoTxt.setText(weatherInfo);

        forecastLayout.removeAllViews();
        for (DailyForecast forecast : weatherDto.heWeatherList.get(0).dailyForecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateTxt = (TextView) view.findViewById(R.id.txt_date);
            TextView infoTxt = (TextView) view.findViewById(R.id.txt_info);
            TextView maxTxt = (TextView) view.findViewById(R.id.txt_max);
            TextView minTxt = (TextView) view.findViewById(R.id.txt_min);
            dateTxt.setText(forecast.date);
            infoTxt.setText(forecast.cond.txtd);
            maxTxt.setText(forecast.tmp.max);
            minTxt.setText(forecast.tmp.min);
            forecastLayout.addView(view);
        }

        if (weatherDto.heWeatherList.get(0).aqi != null) {
            aqiTxt.setText(weatherDto.heWeatherList.get(0).aqi.city.aqi);
            pm25Txt.setText(weatherDto.heWeatherList.get(0).aqi.city.pm25);
        }

        String comfort = "舒适度：" + weatherDto.heWeatherList.get(0).suggestion.comf.txt;
        String carWash = "洗车指数：" + weatherDto.heWeatherList.get(0).suggestion.cw.txt;
        String sport = "运动建议：" + weatherDto.heWeatherList.get(0).suggestion.sport.txt;
        comfortTxt.setText(comfort);
        carWashTxt.setText(carWash);
        sportTxt.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
    }
}
