package com.amkj.dmsh.address.activity;

import android.content.Context;
import android.content.Intent;
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
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.AddressUtils;
import com.amkj.dmsh.utils.KeyboardUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.view.WheelView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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


/**
 * Created by xiaoxin on 2020/12/12
 * Version:v4.8.2
 * ClassDescription :新增/编辑地址（重构）
 */
public class AddressNewCreatedActivity extends BaseActivity {
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.tv_address_district)
    TextView mTvAddressDistrict;
    @BindView(R.id.et_address_consignee)
    EditText mEtAddressConsignee;
    @BindView(R.id.et_address_mobile)
    EditText mEtAddressMobile;
    @BindView(R.id.et_address_detail_district)
    EditText mEtAddressDetailDistrict;
    @BindView(R.id.tv_time_click_confirmed)
    TextView mTvTimeClickConfirmed;
    @BindView(R.id.tv_time_click_cancel)
    TextView mTvTimeClickCancel;
    @BindView(R.id.ll_communal_multi_time)
    LinearLayout mLlCommunalMultiTime;
    @BindView(R.id.id_one_wheel)
    WheelView mProvinceWheel;
    @BindView(R.id.id_two_wheel)
    WheelView mCityWheel;
    @BindView(R.id.id_three_wheel)
    WheelView mDistrictWheel;
    @BindView(R.id.cb_address_default)
    CheckBox mCbAddressDefault;
    @BindView(R.id.rel_address_default)
    RelativeLayout mRelAddressDefault;
    @BindView(R.id.ll_address_create)
    LinearLayout mLlAddressCreate;

    private int addressId;
    private ProvinceModel[] mAllProvinces;
    private Map<Integer, List<CityModel>> mCitiesDataMap;
    private Map<Integer, List<DistrictModel>> mDistrictDataMap;
    private int mCurrentProvinceId;
    private int mCurrentCityId;
    private int mCurrentDistrictId;
    private List<String> provinceNameList = new ArrayList<>();
    private List<String> cityNameList = new ArrayList<>();
    private List<String> districtNameList = new ArrayList<>();
    private AddressInfoBean addressInfoBean;
    private AddressInfoEntity mAddressInfoEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_address_new;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        Intent intent = getIntent();
        addressId = getStringChangeIntegers(intent.getStringExtra("addressId"));
        mTvHeaderShared.setText("保存");
        mTvHeaderTitle.setText(addressId > 0 ? "编辑地址" : "新增地址");
        mTvHeaderShared.setCompoundDrawables(null, null, null, null);
        mProvinceWheel.setCyclic(false);
        mCityWheel.setCyclic(false);
        mDistrictWheel.setCyclic(false);
        initAddress();
        setEtFilter(mEtAddressConsignee);
        setWheelListener();
    }

    private void initAddress() {
        AddressUtils addressUtils = AddressUtils.getQyInstance();
        provinceNameList = addressUtils.getAllProvinceName();
        mAllProvinces = addressUtils.getAllProvince();
        mCitiesDataMap = addressUtils.getCitiesDataMap();
        mDistrictDataMap = addressUtils.getCityDistrict();
        mCurrentProvinceId = addressUtils.getCurrentProvince();
        mCurrentCityId = addressUtils.getCurrentCity();
        mCurrentDistrictId = addressUtils.getCurrentDistrict();
        if (mAllProvinces == null || mAllProvinces.length == 0 || mDistrictDataMap == null || mCitiesDataMap == null) {
            addressUtils.initAddress();
            showToast("数据有误，请重试");
            finish();
        }
    }

    @Override
    protected void loadData() {
        if (addressId != 0) {
            getAddressDetails();
        } else {
            mProvinceWheel.setAdapter(new ArrayWheelAdapter<>(provinceNameList));
            mProvinceWheel.setCurrentItem(0);
            updateCities();
        }
    }

    //地址详情内容
    private void getAddressDetails() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", addressId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, ADDRESS_DETAILS, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mAddressInfoEntity = GsonUtils.fromJson(result, AddressInfoEntity.class);
                if (mAddressInfoEntity != null) {
                    if (mAddressInfoEntity.getCode().equals(SUCCESS_CODE)) {
                        addressInfoBean = mAddressInfoEntity.getAddressInfoBean();
                        setAddressInfo(addressInfoBean);
                    } else {
                        showToast(mAddressInfoEntity.getMsg());
                    }
                }
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mAddressInfoEntity);
            }

            @Override
            public void onNotNetOrException() {
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mAddressInfoEntity);
            }
        });
    }

    private void setAddressInfo(AddressInfoBean addressInfoBean) {
        mLlCommunalMultiTime.setFocusable(true);
        mLlCommunalMultiTime.setFocusableInTouchMode(true);
        mEtAddressConsignee.setText(getStringFilter(addressInfoBean.getConsignee()));
        mEtAddressMobile.setText(addressInfoBean.getMobile());
        mTvAddressDistrict.setText(addressInfoBean.getAddress_com());
        mEtAddressDetailDistrict.setText(addressInfoBean.getAddress());
        if (addressInfoBean.getStatus() == 1) {
            mCbAddressDefault.setChecked(true);
            mRelAddressDefault.setVisibility(View.GONE);
        } else {
            mCbAddressDefault.setChecked(false);
        }
        //当前省市县
        mCurrentProvinceId = Integer.parseInt(addressInfoBean.getProvince());
        mCurrentCityId = Integer.parseInt(addressInfoBean.getCity());
        mCurrentDistrictId = Integer.parseInt(!TextUtils.isEmpty(addressInfoBean.getDistrict()) ? addressInfoBean.getDistrict() : "0");
        //切换地址选择器到当前位置
        for (int i = 0; i < mAllProvinces.length; i++) {
            if (mAllProvinces[i].getId() == mCurrentProvinceId) {
                mProvinceWheel.setAdapter(new ArrayWheelAdapter<>(provinceNameList));
                mProvinceWheel.setCurrentItem(i);
                break;
            }
        }

        List<CityModel> cityList = mCitiesDataMap.get(mCurrentProvinceId);
        if (cityList != null) {
            int currentItem = 0;
            for (int i = 0; i < cityList.size(); i++) {
                cityNameList.add(cityList.get(i).getName());
                if (cityList.get(i).getId() == mCurrentCityId) {
                    currentItem = i;
                }
            }
            mCityWheel.setAdapter(new ArrayWheelAdapter<>(cityNameList));
            mCityWheel.setCurrentItem(currentItem);
        }


        List<DistrictModel> districtModels = mDistrictDataMap.get(mCurrentCityId);
        if (districtModels != null) {
            int currentItem = 0;
            for (int i = 0; i < districtModels.size(); i++) {
                districtNameList.add(districtModels.get(i).getName());
                if (districtModels.get(i).getId() == mCurrentDistrictId) {
                    currentItem = i;
                }
            }
            mDistrictWheel.setAdapter(new ArrayWheelAdapter<>(districtNameList));
            mDistrictWheel.setCurrentItem(currentItem);
        }
    }

    private void setWheelListener() {
        // 添加change事件
        mProvinceWheel.setOnItemSelectedListener(index -> {
            //修改省id,刷新城市以及区列表
            mCurrentProvinceId = mAllProvinces[mProvinceWheel.getCurrentItem()].getId();
            updateCities();
        });
        // 添加change事件
        mCityWheel.setOnItemSelectedListener(index -> {
            //修改市id，刷新区列表
            List<CityModel> cityList = mCitiesDataMap.get(mCurrentProvinceId);
            if (cityList != null) {
                mCurrentCityId = cityList.get(mCityWheel.getCurrentItem()).getId();
                updateAreas();
            }
        });
        // 添加change事件
        mDistrictWheel.setOnItemSelectedListener(index -> {
            //修改区id
            List<DistrictModel> districtModels = mDistrictDataMap.get(mCurrentCityId);
            if (districtModels != null) {
                mCurrentDistrictId = districtModels.get(mDistrictWheel.getCurrentItem()).getId();
            }
        });
        KeyboardUtils.registerSoftInputChangedListener(this, new KeyboardUtils.OnSoftInputChangedListener() {
            @Override
            public void onSoftInputChanged(int height) {
                if (height > 0) {
                    if (mLlCommunalMultiTime.getVisibility() == View.VISIBLE) {
                        mLlCommunalMultiTime.setVisibility(View.GONE);
                    }
                } else {
                    mLlAddressCreate.requestFocus();
                }
            }
        });
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        List<CityModel> cityList = mCitiesDataMap.get(mCurrentProvinceId);
        if (cityList != null) {
            if (cityList.size() > 0) {
                mCurrentCityId = cityList.get(0).getId();
            }
            cityNameList.clear();
            for (int i = 0; i < cityList.size(); i++) {
                cityNameList.add(cityList.get(i).getName());
            }
            mCityWheel.setAdapter(new ArrayWheelAdapter<>(cityNameList));
            mCityWheel.setCurrentItem(0);
        }
        updateAreas();
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        List<DistrictModel> districtModels = mDistrictDataMap.get(mCurrentCityId);
        if (districtModels != null) {
            if (districtModels.size() > 0) {
                mCurrentDistrictId = districtModels.get(0).getId();
            }
            districtNameList.clear();
            for (int i = 0; i < districtModels.size(); i++) {
                districtNameList.add(districtModels.get(i).getName());
            }
            mDistrictWheel.setAdapter(new ArrayWheelAdapter<>(districtNameList));
            mDistrictWheel.setCurrentItem(0);
        }
    }

    //添加,编辑地址
    private void editAddress(AddressInfoBean myAddress) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userId);
        if (addressId > 0) {
            params.put("id", addressInfoBean.getId());
        }
        //收件人
        params.put("consignee", myAddress.getConsignee());
        params.put("mobile", myAddress.getMobile());
        //省
        params.put("province", mCurrentProvinceId);
        //市
        params.put("city", mCurrentCityId);
        //区
        if (mCurrentDistrictId > 0 && mCurrentDistrictId >= mCurrentCityId) {
            params.put("district", mCurrentDistrictId);
        }
        params.put("address_com", myAddress.getAddress_com());
        //详细地址
        params.put("address", myAddress.getAddress());
        params.put("isDefault", myAddress.getStatus());
        NetLoadUtils.getNetInstance().loadNetDataPost(this, addressId > 0 ? Url.EDIT_ADDRESS : Url.ADD_ADDRESS, params, new NetLoadListenerHelper() {
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

    @OnClick({R.id.tv_life_back, R.id.tv_time_click_confirmed, R.id.tv_time_click_cancel, R.id.tv_address_district, R.id.tv_header_shared})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_life_back:
                finish();
                break;
            //确认
            case R.id.tv_time_click_confirmed:
                mTvAddressDistrict.setText((
                        provinceNameList.get(mProvinceWheel.getCurrentItem())
                                + cityNameList.get(mCityWheel.getCurrentItem())
                                + (districtNameList.size() > 0 ? districtNameList.get(mDistrictWheel.getCurrentItem()) : "")));
                mLlCommunalMultiTime.setVisibility(View.GONE);
                break;
            //取消
            case R.id.tv_time_click_cancel:
                mLlCommunalMultiTime.setVisibility(View.GONE);
                break;
            //选择地址
            case R.id.tv_address_district:
                if (KeyboardUtils.isSoftInputVisible(this)) {
                    KeyboardUtils.hideSoftInput(this);
                } else {
                    mLlCommunalMultiTime.setVisibility(mLlCommunalMultiTime.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                }
                break;
            //保存地址
            case R.id.tv_header_shared:
                AddressInfoBean myAddress = new AddressInfoBean();
                //收货人
                myAddress.setConsignee(mEtAddressConsignee.getText().toString().trim());
                //手机
                myAddress.setMobile(mEtAddressMobile.getText().toString().trim());
                //省市县
                myAddress.setAddress_com(mTvAddressDistrict.getText().toString().trim());
                //详细街道地址
                myAddress.setAddress(mEtAddressDetailDistrict.getText().toString().trim());
                if (mCbAddressDefault.isChecked()) {
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
                break;
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
        KeyboardUtils.unregisterSoftInputChangedListener(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
        if (v instanceof EditText) {
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

    @Override
    protected boolean isAddLoad() {
        return getStringChangeIntegers(getIntent().getStringExtra("addressId")) > 0;
    }
}
