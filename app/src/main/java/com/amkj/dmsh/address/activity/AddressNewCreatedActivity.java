package com.amkj.dmsh.address.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.address.bean.AddressInfoEntity;
import com.amkj.dmsh.address.bean.AddressInfoEntity.AddressInfoBean;
import com.amkj.dmsh.address.bean.CityModel;
import com.amkj.dmsh.address.bean.DistrictModel;
import com.amkj.dmsh.address.bean.ProvinceModel;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.AddressUtils;
import com.amkj.dmsh.utils.KeyboardUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeIntegers;
import static com.amkj.dmsh.constant.ConstantMethod.getStringFilter;
import static com.amkj.dmsh.constant.ConstantMethod.setEtFilter;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.ADDRESS_DETAILS;
import static com.amkj.dmsh.constant.Url.ADD_ADDRESS;
import static com.amkj.dmsh.constant.Url.EDIT_ADDRESS;


/**
 * Created by atd48 on 2016/7/15.
 */
public class AddressNewCreatedActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_address_district)
    TextView tv_address_district;
    @BindView(R.id.et_address_consignee)
    EditText et_address_consignee;
    @BindView(R.id.et_address_mobile)
    EditText et_address_mobile;
    @BindView(R.id.et_address_detail_district)
    EditText et_address_detail_district;

    @BindView(R.id.tv_time_click_confirmed)
    TextView tv_time_click_confirmed;
    @BindView(R.id.tv_time_click_cancel)
    TextView tv_time_click_cancel;
    //是否展开显示
    @BindView(R.id.ll_communal_multi_time)
    LinearLayout ll_communal_multi_time;
    //    是否展示默认地址选择
    @BindView(R.id.id_one_wheel)
    WheelView id_province;
    @BindView(R.id.id_two_wheel)
    WheelView id_city;
    @BindView(R.id.id_three_wheel)
    WheelView id_district;
    @BindView(R.id.cb_address_default)
    CheckBox cb_address_default;
    @BindView(R.id.rel_address_default)
    RelativeLayout rel_address_default;
    @BindView(R.id.ll_address_create)
    LinearLayout ll_address_create;
    boolean isSelected = true;
    Handler handler = new Handler();
    private ProvinceModel[] mProvinceData;
    private Map<Integer, DistrictModel[]> mDistrictDataMap;
    private int mCurrentCityId;
    private int mCurrentDistrictId;
    private Map<Integer, String> mZipDataMap;
    private Map<Integer, CityModel[]> mCitiesDataMap;
    private int mCurrentProvinceId;
    private String[] provinceName;
    private String[] cityName;
    private String[] districtName;

    private int addressId;
    private AddressInfoBean addressInfoBean;

    @Override
    protected void initViews() {
        getLoginStatus(this);
        initAddress();
        tv_header_shared.setCompoundDrawables(null, null, null, null);
        tv_header_shared.setText("保存");
        tv_header_titleAll.setText("新增地址");
        Intent intent = getIntent();
        addressId = getStringChangeIntegers(intent.getStringExtra("addressId"));
        setEtFilter(et_address_consignee);
        //手机号匹配
        setUpListener();
        setUpData();
        KeyboardUtils.registerSoftInputChangedListener(this, new KeyboardUtils.OnSoftInputChangedListener() {
            @Override
            public void onSoftInputChanged(int height) {
                if (height > 0) {
                    if (ll_communal_multi_time.getVisibility() == View.VISIBLE) {
                        ll_communal_multi_time.setVisibility(View.GONE);
                        isSelected = true;
                    }
                } else {
                    ll_address_create.requestFocus();
                }
            }
        });
    }

    private void initAddress() {
        AddressUtils addressUtils = AddressUtils.getQyInstance();
        mProvinceData = addressUtils.getAllProvince();
        mDistrictDataMap = addressUtils.getCityDistrict();
        mCurrentCityId = addressUtils.getCurrentCity();
        mCurrentDistrictId = addressUtils.getCurrentDistrict();
        mZipDataMap = addressUtils.getZipCodeDataMap();
        mCitiesDataMap = addressUtils.getCitiesDataMap();
        mCurrentProvinceId = addressUtils.getCurrentProvince();
        if (mProvinceData == null || mDistrictDataMap == null || mZipDataMap == null || mCitiesDataMap == null) {
            finish();
        }
    }

    @Override
    protected void loadData() {
        if (addressId != 0) {
            getAddressDetails();
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_address_new;
    }

    private void getAddressDetails() {
        //地址详情内容
        Map<String, Object> params = new HashMap<>();
        params.put("id", addressId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, ADDRESS_DETAILS, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                AddressInfoEntity addressInfoEntity = GsonUtils.fromJson(result, AddressInfoEntity.class);
                if (addressInfoEntity != null) {
                    if (addressInfoEntity.getCode().equals(SUCCESS_CODE)) {
                        addressInfoBean = addressInfoEntity.getAddressInfoBean();
                        setData(addressInfoBean);
                    } else {
                        showToast(addressInfoEntity.getMsg());
                    }
                }
            }
        });
    }

    private void setData(AddressInfoBean addressInfoBean) {
        ll_communal_multi_time.setFocusable(true);
        ll_communal_multi_time.setFocusableInTouchMode(true);
        et_address_consignee.setText(getStringFilter(addressInfoBean.getConsignee()));
        et_address_mobile.setText(addressInfoBean.getMobile());
//        省市县
        tv_address_district.setText(addressInfoBean.getAddress_com());
//        详细街道地址
        et_address_detail_district.setText(addressInfoBean.getAddress());
        if (addressInfoBean.getStatus() == 1) {
            cb_address_default.setChecked(true);
            rel_address_default.setVisibility(View.GONE);
        } else {
            cb_address_default.setChecked(false);
        }
//        当前省市县
        mCurrentProvinceId = Integer.parseInt(addressInfoBean.getProvince());
        mCurrentCityId = Integer.parseInt(addressInfoBean.getCity());
        mCurrentDistrictId = Integer.parseInt(!TextUtils.isEmpty(addressInfoBean.getDistrict()) ? addressInfoBean.getDistrict() : "0");
    }

    @OnClick(R.id.tv_header_shared)
    void addSaved(View view) {
        AddressInfoBean myAddress = new AddressInfoBean();
//        收货人
        myAddress.setConsignee(et_address_consignee.getText().toString().trim());
//        手机
        myAddress.setMobile(et_address_mobile.getText().toString().trim());
//        省市县
        myAddress.setAddress_com(tv_address_district.getText().toString().trim());
//        详细街道地址
        myAddress.setAddress(et_address_detail_district.getText().toString().trim());
        if (cb_address_default.isChecked()) {
            myAddress.setStatus(1);
        } else {
            myAddress.setStatus(0);
        }
        if (myAddress.getAddress_com().length() < 1 || myAddress.getAddress().length() < 1
                || myAddress.getConsignee().length() < 1 || myAddress.getMobile().length() < 1) {
            showToast("请完整填写收货人资料");
        } else {
            String mobilePhone = myAddress.getMobile();
            if (mobilePhone.startsWith("1")) {            //手机号
                editAddress(myAddress);
            } else if (!mobilePhone.startsWith("-")) {                //固话
                editAddress(myAddress);
            } else {
                showToast("联系方式有误，请重新输入");
            }
        }
    }

    private void editAddress(AddressInfoBean myAddress) {
        if (addressId != 0) {
//            修改地址
            alterAddress(myAddress);
        } else {
//            添加地址
            addAddress(myAddress);
        }
    }

    //  添加地址
    private void addAddress(AddressInfoBean myAddress) {
        //地址详情内容
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userId);
//        收货人
        params.put("consignee", myAddress.getConsignee());
        params.put("mobile", myAddress.getMobile());
        params.put("province", mCurrentProvinceId);
        params.put("city", mCurrentCityId);
        if (mCurrentDistrictId > 0 && mCurrentDistrictId >= mCurrentCityId) {
            params.put("district", mCurrentDistrictId);
        }
//        邮编
//        选择地址
        params.put("address_com", myAddress.getAddress_com());
//        纤细街道地址
        params.put("address", myAddress.getAddress());
        params.put("isDefault", myAddress.getStatus());
        NetLoadUtils.getNetInstance().loadNetDataPost(this, ADD_ADDRESS, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                AddressInfoEntity addressInfoEntity = GsonUtils.fromJson(result, AddressInfoEntity.class);
                if (addressInfoEntity != null) {
                    if (addressInfoEntity.getCode().equals(SUCCESS_CODE)) {
                        showToast("添加成功");
                        Intent data = new Intent();
                        data.putExtra("addressInfoBean", addressInfoEntity.getAddressInfoBean());
                        setResult(RESULT_OK, data);
                        finish();
                    } else {
                        showToast(addressInfoEntity.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(R.string.do_failed);
            }
        });
    }

    //  修改地址
    private void alterAddress(AddressInfoBean myAddress) {
        //地址详情内容
        Map<String, Object> params = new HashMap<>();
        params.put("id", addressInfoBean.getId());
        params.put("user_id", userId);
//        收货人
        params.put("consignee", myAddress.getConsignee());
        params.put("mobile", myAddress.getMobile());
        params.put("province", mCurrentProvinceId);
        params.put("city", mCurrentCityId);
        if (mCurrentDistrictId > 0 && mCurrentDistrictId >= mCurrentCityId) {
            params.put("district", mCurrentDistrictId);
        }
//        选择地址
        params.put("address_com", myAddress.getAddress_com());
//        纤细街道地址
        params.put("address", myAddress.getAddress());
        params.put("isDefault", myAddress.getStatus());
        NetLoadUtils.getNetInstance().loadNetDataPost(this, EDIT_ADDRESS, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                AddressInfoEntity addressInfoEntity = GsonUtils.fromJson(result, AddressInfoEntity.class);
                if (addressInfoEntity != null) {
                    if (addressInfoEntity.getCode().equals(SUCCESS_CODE)) {
                        Intent data = new Intent();
                        data.putExtra("addressInfoBean", addressInfoEntity.getAddressInfoBean());
                        setResult(RESULT_OK, data);
                        finish();
                    } else {
                        showToast(addressInfoEntity.getMsg());
                    }
                }
            }
        });
    }

    private void setUpListener() {
        // 添加change事件
        id_province.setOnItemSelectedListener(index -> updateCities());
        // 添加change事件
        id_city.setOnItemSelectedListener(index -> updateAreas());
        // 添加change事件
        id_district.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                DistrictModel[] districtModels = mDistrictDataMap.get(mCurrentCityId);
                if (districtModels != null && districtModels.length > 0 && districtModels.length > index) {
                    mCurrentDistrictId = districtModels[index].getId();
                } else {
                    mCurrentDistrictId = 0;
                }
            }
        });
        // 添加onclick事件
        tv_time_click_cancel.setOnClickListener(this);
        tv_time_click_confirmed.setOnClickListener(this);
    }

    private void setUpData() {
        if (mProvinceData != null) {
            provinceName = new String[mProvinceData.length];
            for (int i = 0; i < mProvinceData.length; i++) {
                ProvinceModel mProvinceDatum = mProvinceData[i];
                if (mProvinceDatum != null) {
                    if (!TextUtils.isEmpty(mProvinceData[i].getName())) {
                        provinceName[i] = mProvinceData[i].getName();
                    }
                }
            }
            id_province.setAdapter(new ArrayWheelAdapter<>(Arrays.asList(provinceName)));
            id_province.setCyclic(false);
            id_province.setCurrentItem(0);
            updateCities();
            updateAreas();
        }
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = id_city.getCurrentItem();
        mCurrentCityId = mCitiesDataMap.get(mCurrentProvinceId)[pCurrent].getId();
        DistrictModel[] districtModels = mDistrictDataMap.get(mCurrentCityId);
        districtName = new String[districtModels.length];
        for (int i = 0; i < districtModels.length; i++) {
            districtName[i] = districtModels[i].getName();
        }
        id_district.setAdapter(new ArrayWheelAdapter<>(Arrays.asList(districtName)));
        id_district.setCyclic(false);
        id_district.setCurrentItem(0);
        if (districtModels.length > 0) {
            DistrictModel districtModel = districtModels[id_district.getCurrentItem()];
            if (districtModel != null) {
                mCurrentDistrictId = districtModel.getId();
            }
        }
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = id_province.getCurrentItem();
        mCurrentProvinceId = mProvinceData[pCurrent].getId();
        CityModel[] cityModels = mCitiesDataMap.get(mCurrentProvinceId);
        cityName = new String[cityModels.length];
        for (int i = 0; i < cityModels.length; i++) {
            cityName[i] = cityModels[i].getName();
        }
        id_city.setAdapter(new ArrayWheelAdapter<>(Arrays.asList(cityName)));
        id_city.setCyclic(false);
        id_city.setCurrentItem(0);
        updateAreas();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_time_click_confirmed:
                showSelectedResult();
                ll_communal_multi_time.setVisibility(View.GONE);
                isSelected = true;
                break;
            case R.id.tv_time_click_cancel:
                ll_communal_multi_time.setVisibility(View.GONE);
                isSelected = true;
                break;
        }
    }

    private void showSelectedResult() {
//        当前省市区ID
        tv_address_district.setText((
                provinceName[id_province.getCurrentItem()]
                        + cityName[id_city.getCurrentItem()]
                        + (districtName.length > 0 ? districtName[id_district.getCurrentItem()] : "")));
    }

    @OnClick(R.id.tv_address_district)
    void selectedAddress() {
        if (KeyboardUtils.isSoftInputVisible(this)) {
            KeyboardUtils.hideSoftInput(this);
        } else {
            setAddressDialog();
        }
    }

    /**
     * 设置地址选择隐藏显示
     */
    private void setAddressDialog() {
        if (isSelected) {
            ll_communal_multi_time.setVisibility(View.VISIBLE);
            isSelected = false;
        } else {
            ll_communal_multi_time.setVisibility(View.GONE);
            isSelected = true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if (requestCode == ConstantVariable.IS_LOGIN_CODE) {
                finish();
            } else {
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            loadData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        KeyboardUtils.unregisterSoftInputChangedListener(this);
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                InputMethodManager imm =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Return whether touch the view.
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }
}
