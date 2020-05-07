package com.amkj.dmsh.utils;

import android.content.res.AssetManager;
import android.text.TextUtils;

import com.amkj.dmsh.address.bean.AddressInfo;
import com.amkj.dmsh.address.bean.CityModel;
import com.amkj.dmsh.address.bean.DistrictModel;
import com.amkj.dmsh.address.bean.ProvinceModel;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.createExecutor;

public class AddressUtils {
    private static AddressUtils addressUtils;
    /**
     * 所有省
     */
    public ProvinceModel[] mProvinceData;
    /**
     * key - 省 value - 市
     */
    public Map<Integer, CityModel[]> mCitiesDataMap = new HashMap<>();

    /**
     * key - 市 values - 区
     */
    public Map<Integer, DistrictModel[]> mDistrictDataMap = new HashMap<>();

    /**
     * key - 区 values - 邮编
     */
    public Map<Integer, String> mZipCodeDataMap = new HashMap<>();
    /**
     * 当前省的名称
     */
    public int mCurrentProvinceId;
    /**
     * 当前市的名称
     */
    public int mCurrentCityId;

    /**
     * 当前区的名称
     */
    public int mCurrentDistrictId;
    /**
     * 当前区的邮政编码
     */
    public String mCurrentZipCode = "";

    private AddressUtils() {}

    public static AddressUtils getQyInstance() {
        if (addressUtils == null) {
            synchronized (AddressUtils.class) {
                if (addressUtils == null) {
                    addressUtils = new AddressUtils();
                }
            }
        }
        return addressUtils;
    }

    public void initAddress(){
        if(mProvinceData==null){
            createExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    initProvinceData();
                }
            });
        }
    }
    public void initAddress(String address){
        if(mProvinceData==null&& !TextUtils.isEmpty(address)){
            createExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    initProvinceData(address);
                }
            });
        }
    }

    //    地址初始化
    private void initProvinceData() {
        initProvinceData(null);
    }
    private void initProvinceData(String addressData) {
        String adsPath = mAppContext.getFilesDir().getAbsolutePath() + "/adr_s/asr_s.txt";
        if(!TextUtils.isEmpty(addressData)){
            setAddressData(getAddressInfoBean(addressData));
        }else if (new File(adsPath).exists()) {
            try {
                setAddressData(getAddressInfoBean(FileStreamUtils.readFile2String(adsPath)));
            } catch (JsonSyntaxException e) {
                try {
                    getAssetAdsData();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        } else {
            try {
                getAssetAdsData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据数据获取addressinfo对象
     * @param addressData
     * @return
     */
    private AddressInfo getAddressInfoBean(String addressData) {
        if(!TextUtils.isEmpty(addressData)){

            return GsonUtils.fromJson(addressData, AddressInfo.class);
        }
        return null;
    }

    /**
     * @throws IOException
     */
    private void getAssetAdsData() throws IOException {
        AddressInfo addressInfo = null;
        InputStream input = null;
        try {
            AssetManager asset = mAppContext.getAssets();
            input = asset.open("area.json");

            addressInfo = GsonUtils.fromJson(new InputStreamReader(input), AddressInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (JsonIOException e) {
            e.printStackTrace();
        } finally {
            if(input!=null){
                input.close();
            }
        }
        setAddressData(addressInfo);
    }

    private void setAddressData(AddressInfo addressInfo) {
        try {
            Comparator<AddressInfo.AddressBean> comparator = (lhs, rhs) -> {
                if (lhs.getId() < rhs.getId()) {
                    return -1;
                }
                return 1;
            };
            List<AddressInfo.AddressBean> addressList = addressInfo.getArea();
            Collections.sort(addressList, comparator);
//            全部省市县的集合
            List<AddressInfo.AddressBean> provinceAddressList = new ArrayList<>();
            List<AddressInfo.AddressBean> cityAddressList = new ArrayList<>();
            List<AddressInfo.AddressBean> districtAddressList = new ArrayList<>();
            for (AddressInfo.AddressBean address : addressList) {
                if (address.getType() == 2 && address.getParent_id() == 1) {
//                获取省
                    provinceAddressList.add(address);
//                    省级排序
//                    Collections.sort(provinceTestList, comparator);
                } else if (address.getType() == 3) {
//                    获取市
                    cityAddressList.add(address);
//                    市级排序
//                    Collections.sort(cityTestList, comparator);
                } else if (address.getType() == 4) {
//                    获取县
                    districtAddressList.add(address);
//                    县级排序
//                    Collections.sort(districtTestList, comparator);
                }
            }
            mProvinceData = new ProvinceModel[provinceAddressList.size()];
            ProvinceModel provinceModel;
            CityModel cityModel;
            DistrictModel districtModel;
//            省集合 包含每个市集合
            List<ProvinceModel> provinceModelList = new ArrayList<>();
//            市集合 包含每个县
            List<CityModel> cityModelList;
//          县
            List<DistrictModel> districtModelList;
            for (int i = 0; i < provinceAddressList.size(); i++) {
                cityModelList = new ArrayList<>();
                provinceModel = new ProvinceModel();
//                获取当前省的市
                for (int j = 0; j < cityAddressList.size(); j++) {
                    cityModel = new CityModel();
                    // 遍历省下面的所有市的数据
                    if (cityAddressList.get(j).getParent_id() == provinceAddressList.get(i).getId()) {
//                        设置cityModel数据
                        cityModel.setId(cityAddressList.get(j).getId());
                        cityModel.setName(cityAddressList.get(j).getName());
                        cityModel.setZip(cityAddressList.get(j).getZip());
                        cityModelList.add(cityModel);
                    }
                }
                provinceModel.setName(provinceAddressList.get(i).getName());
                provinceModel.setId(provinceAddressList.get(i).getId());
                provinceModel.setCityList(cityModelList);
                provinceModelList.add(provinceModel);
// 遍历所有省的数据
                mProvinceData[i] = provinceModelList.get(i);
                CityModel[] cities = new CityModel[cityModelList.size()];
                for (int j = 0; j < cityModelList.size(); j++) {
                    districtModelList = new ArrayList<>();
                    cities[j] = cityModelList.get(j);
//                    获取市下面的县集合
                    for (int k = 0; k < districtAddressList.size(); k++) {
                        districtModel = new DistrictModel();
                        if (districtAddressList.get(k).getParent_id() == cityModelList.get(j).getId()) {
//                        设置districtModel数据
                            districtModel.setName(districtAddressList.get(k).getName());
                            districtModel.setId(districtAddressList.get(k).getId());
                            districtModel.setZip(districtAddressList.get(k).getZip());
                            districtModelList.add(districtModel);
                        }
                    }
                    cityModelList.get(j).setDistrictList(districtModelList);
                    DistrictModel[] districtArray = new DistrictModel[districtModelList.size()];
                    for (int k = 0; k < districtModelList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        districtModel = districtModelList.get(k);
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        mZipCodeDataMap.put(districtModelList.get(k).getId(), districtModelList.get(k).getZip());
                        districtArray[k] = districtModel;
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDataMap.put(cities[j].getId(), districtArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitiesDataMap.put(provinceModelList.get(i).getId(), cities);
            }
            // 获取解析出来的数据
            //*/ 初始化默认选中的省、市、区
            if (!provinceModelList.isEmpty()) {
                mCurrentProvinceId = provinceModelList.get(0).getId();
                List<CityModel> cityList = provinceModelList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityId = cityList.get(0).getId();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictId = districtList.get(0).getId();
                    mCurrentZipCode = districtList.get(0).getZip();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    //      所有省
    public ProvinceModel[] getAllProvince() {
        return mProvinceData;
    }

    //    当前市的名称
    public int getCurrentCity() {
        return mCurrentCityId;
    }

    //    当前县的名称
    public int getCurrentDistrict() {
        return mCurrentDistrictId;
    }

    //    当前邮政编码
    public String getCurrentZipCode() {
        return mCurrentZipCode;
    }

    //    邮政Map
    public Map<Integer, String> getZipCodeDataMap() {
        return mZipCodeDataMap;
    }

    //    市-县
    public Map<Integer, DistrictModel[]> getCityDistrict() {
        return mDistrictDataMap;
    }

    //   省 -市
    public Map<Integer, CityModel[]> getCitiesDataMap() {
        return mCitiesDataMap;
    }

    //   当前省名字
    public int getCurrentProvince() {
        return mCurrentProvinceId;
    }
}
