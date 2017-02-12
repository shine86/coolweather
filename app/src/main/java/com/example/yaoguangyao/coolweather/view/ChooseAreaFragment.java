package com.example.yaoguangyao.coolweather.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yaoguangyao.coolweather.R;
import com.example.yaoguangyao.coolweather.db.CityDao;
import com.example.yaoguangyao.coolweather.db.CountyDao;
import com.example.yaoguangyao.coolweather.db.ProvinceDao;
import com.example.yaoguangyao.coolweather.db.entity.County;
import com.example.yaoguangyao.coolweather.http.AreaApi;
import com.example.yaoguangyao.coolweather.model.CityBo;
import com.example.yaoguangyao.coolweather.model.CountyBo;
import com.example.yaoguangyao.coolweather.model.ProvinceBo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yaoguangyao on 2017/2/9.
 */

public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private ProvinceDao provinceDao;
    private CityDao cityDao;
    private CountyDao countyDao;
    private AreaApi areaApi;

    private ProgressDialog progressDialog;
    private TextView titleTxt;
    private Button backBtn;
    private ListView viewList;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();

    private List<ProvinceBo> provinceBoList;
    private List<CityBo> cityBoList;
    private List<CountyBo> countyBoList;

    private ProvinceBo selectedProvinceBo;
    private CityBo selectedCityBo;

    private int currentLevel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        provinceDao = new ProvinceDao();
        cityDao = new CityDao();
        countyDao = new CountyDao();
        areaApi = new AreaApi();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_area, container, false);
        titleTxt = (TextView) view.findViewById(R.id.txt_title);
        backBtn = (Button) view.findViewById(R.id.btn_back);
        viewList = (ListView) view.findViewById(R.id.list_view);

        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, dataList);
        viewList.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvinceBo = provinceBoList.get(position);
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    selectedCityBo = cityBoList.get(position);
                    queryCounties();
                } else if (currentLevel == LEVEL_COUNTY) {
                    String weatherId = countyBoList.get(position).getWeatherId();
                    Intent intent = new Intent(getActivity(), WeatherActiivity.class);
                    intent.putExtra("weather_id", weatherId);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == LEVEL_COUNTY) {
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    queryProvinces();
                }
            }
        });

        queryProvinces();
    }

    /**
     * 查询省份
     */
    private void queryProvinces() {
        titleTxt.setText("中国");
        backBtn.setVisibility(View.GONE);

        //从entity拷贝到model
        provinceBoList = provinceDao.findAll();

        //如果有省份列表则显示在列表中，否则调用天气接口获取数据
        if (provinceBoList.size() > 0) {
            dataList.clear();
            for (ProvinceBo provinceBo : provinceBoList) {
                dataList.add(provinceBo.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            viewList.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        } else {
            queryProvinceFromApi();
        }
    }

    /**
     * 调用接口获取省份
     */
    private void queryProvinceFromApi() {
        showProgressDialog(); //打开loading进度条
        areaApi.getProvinces(new AreaApi.ApiLinster() {
            @Override
            public void onSuccess(Object result) {
                if (result != null) {
                    List<ProvinceBo> provinceBoList = (List<ProvinceBo>) result;
                    for (ProvinceBo provinceBo : provinceBoList) {
                        provinceDao.save(provinceBo);
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            //保存完后，从数据库查出渲染列表
                            queryProvinces();
                        }
                    });
                }
            }

            @Override
            public void onFail(Exception e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(), "加载省份列表失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 获取城市
     */
    private void queryCities() {
        titleTxt.setText(selectedProvinceBo.getProvinceName());
        backBtn.setVisibility(View.VISIBLE);
        cityBoList = cityDao.findAllByProvinceId(selectedProvinceBo.getId());
        if (cityBoList.size() > 0) {
            dataList.clear();
            for (CityBo cityBo : cityBoList) {
                dataList.add(cityBo.getCityName());
            }
            adapter.notifyDataSetChanged();
            viewList.setSelection(0);
            currentLevel = LEVEL_CITY;
        } else {
            int provinceId = selectedProvinceBo.getId();
            queryCitiesFromApi(provinceId);
        }
    }

    /**
     * 调用接口返回城市
     * @param provinceId
     */
    private void queryCitiesFromApi(int provinceId) {
        showProgressDialog();
        areaApi.getCities(provinceId, new AreaApi.ApiLinster() {
            @Override
            public void onSuccess(Object result) {
                if (result != null) {
                    List<CityBo> cityBoList = (List<CityBo>) result;
                    for (CityBo cityBo : cityBoList) {
                        cityDao.save(cityBo);
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            queryCities();
                        }
                    });
                }
            }

            @Override
            public void onFail(Exception e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(), "加载城市列表失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    /**
     * 查询区、县
     */
    private void queryCounties() {
        titleTxt.setText(selectedCityBo.getCityName());
        backBtn.setVisibility(View.VISIBLE);
        countyBoList = countyDao.findAllByCityId(selectedCityBo.getId());
        if (countyBoList.size() > 0) {
            dataList.clear();
            for (CountyBo countyBo : countyBoList) {
                dataList.add(countyBo.getCountyName());
            }
            adapter.notifyDataSetChanged();
            viewList.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        } else {
            queryCountiesFromApi(selectedProvinceBo.getId(), selectedCityBo.getId());
        }
    }

    /**
     * 调用接口查询区、县
     */
    private void queryCountiesFromApi(int provinceId, int cityId) {
        showProgressDialog();

        areaApi.getCounties(provinceId, cityId, new AreaApi.ApiLinster() {
            @Override
            public void onSuccess(Object result) {
                if (result != null) {
                    List<CountyBo> countyBoList = (List<CountyBo>) result;
                    for (CountyBo countyBo : countyBoList) {
                        countyDao.save(countyBo);
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            queryCounties();
                        }
                    });
                }
            }

            @Override
            public void onFail(Exception e) {
                closeProgressDialog();
                Toast.makeText(getContext(), "加载区列表失败", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * 打开进度条
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(true);
        }
    }

    /**
     * 关闭进度条
     */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

}
