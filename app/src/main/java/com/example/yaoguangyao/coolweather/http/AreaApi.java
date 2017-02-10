package com.example.yaoguangyao.coolweather.http;

import android.text.TextUtils;
import android.util.Log;

import com.example.yaoguangyao.coolweather.db.entity.Province;
import com.example.yaoguangyao.coolweather.model.CityBo;
import com.example.yaoguangyao.coolweather.model.CountyBo;
import com.example.yaoguangyao.coolweather.model.ProvinceBo;
import com.example.yaoguangyao.coolweather.util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by yaoguangyao on 2017/2/9.
 */

public class AreaApi {

    /**
     * 获取省份数据
     * @param apiLinster
     */
    public void getProvinces(final ApiLinster apiLinster) {
        String url = "http://guolin.tech/api/china";
        HttpUtil.sendOnHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                apiLinster.onFail(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<ProvinceBo> provinceBoList = new ArrayList<ProvinceBo>();
                String responseText = response.body().string();
                if (!TextUtils.isEmpty(responseText)) {
                    try {
                        Log.d("flyzing", "onResponse: " + responseText);
                        JSONArray allProvinces = new JSONArray(responseText);
                        for (int i = 0; i < allProvinces.length(); i ++) {
                            JSONObject provinceObject = allProvinces.getJSONObject(i);
                            ProvinceBo provinceBo = new ProvinceBo();
                            provinceBo.setProvinceName(provinceObject.getString("name"));
                            provinceBo.setProvinceCode(provinceObject.getInt("id"));
                            provinceBoList.add(provinceBo);
                        }
                        apiLinster.onSuccess(provinceBoList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    /**
     * 获取城市数据
     */
    public void getCities(final int provincedId, final ApiLinster apiLinster) {
        String url = "http://guolin.tech/api/china/" + provincedId;
        HttpUtil.sendOnHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                apiLinster.onFail(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<CityBo> cityBoList = new ArrayList<CityBo>();
                String responseText = response.body().string();
                if (!TextUtils.isEmpty(responseText)) {
                    try {
                        JSONArray allCities = new JSONArray(responseText);
                        for (int i = 0; i < allCities.length(); i ++) {
                            JSONObject cityObject = allCities.getJSONObject(i);
                            CityBo cityBo = new CityBo();
                            cityBo.setId(cityObject.getInt("id"));
                            cityBo.setCityName(cityObject.getString("name"));
                            cityBo.setProvinceId(provincedId);
                            cityBoList.add(cityBo);
                        }
                        apiLinster.onSuccess(cityBoList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    /**
     * 获取区、县
     */
    public void getCounties(final int provincedId, int cityId, final ApiLinster apiLinster) {
        String url = "http://guolin.tech/api/china/" + provincedId + "/" + cityId;
        HttpUtil.sendOnHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                apiLinster.onFail(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<CountyBo> countyBoList = new ArrayList<CountyBo>();
                String responseText = response.body().string();
                if (!TextUtils.isEmpty(responseText)) {
                    try {
                        JSONArray allCounties = new JSONArray(responseText);
                        for (int i = 0; i < allCounties.length(); i ++) {
                            JSONObject countyObject = allCounties.getJSONObject(i);
                            CountyBo countyBo = new CountyBo();
                            countyBo.setId(countyObject.getInt("id"));
                            countyBo.setCountyName(countyObject.getString("name"));
                            countyBo.setWeatherId(countyObject.getString("weather_id"));
                            countyBoList.add(countyBo);
                        }
                        apiLinster.onSuccess(countyBoList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }


    public interface ApiLinster {
        void onSuccess(Object result);
        void onFail(Exception e);
    }
}
