package com.amkj.dmsh.homepage.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.address.activity.SelectedAddressActivity;
import com.amkj.dmsh.address.bean.AddressInfoEntity;
import com.amkj.dmsh.address.bean.AddressInfoEntity.AddressInfoBean;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.homepage.bean.IntegralLotteryAwardGetEntity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.VISIBLE;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.ADDRESS_DETAILS;
import static com.amkj.dmsh.constant.Url.DELIVERY_ADDRESS;
import static com.amkj.dmsh.constant.Url.H_ATTENDANCE_INTEGRAL_LOTTERY_AWARD_GET;
import static com.amkj.dmsh.constant.Url.H_ATTENDANCE_INTEGRAL_LOTTERY_AWARD_INFO;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/7/30
 * version 3.1.5
 * class description:积分夺宝-领奖
 */
public class IntegralLotteryAwardGetActivity extends BaseActivity {
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tl_normal_bar)
    Toolbar tl_normal_bar;
    //    地址为空
    @BindView(R.id.ll_indent_address_null)
    LinearLayout ll_indent_address_null;
    //   有默认地址
    @BindView(R.id.ll_indent_address_default)
    LinearLayout ll_indent_address_default;
    //    收件人名字
    @BindView(R.id.tv_consignee_name)
    TextView tv_consignee_name;
    //    收件人手机号码
    @BindView(R.id.tv_consignee_mobile_number)
    TextView tv_address_mobile_number;
    //    订单地址
    @BindView(R.id.tv_indent_details_address)
    TextView tv_indent_details_address;
    //        海外购实名提示
    @BindView(R.id.tv_oversea_buy_tint)
    TextView tv_oversea_buy_tint;
    //    积分夺宝期数
    @BindView(R.id.tv_integral_lottery_award_time)
    TextView tv_integral_lottery_award_time;
    @BindView(R.id.iv_integral_lottery_award_image)
    ImageView iv_integral_lottery_award_image;
    @BindView(R.id.tv_integral_lottery_award_name)
    TextView tv_integral_lottery_award_name;
    //    确认领取
    @BindView(R.id.tv_integral_lottery_award_confirm_get)
    TextView tv_integral_lottery_award_confirm_get;

    private final int NEW_CRE_ADDRESS_REQ = 101;
    private final int SEL_ADDRESS_REQ = 102;
    private int addressId;
    private boolean isFirst;
    private String activityId;
    private IntegralLotteryAwardGetEntity integralLotteryAwardEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_integral_lottery_award_get;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        Intent intent = getIntent();
        activityId = intent.getStringExtra("activityId");
        if (TextUtils.isEmpty(activityId)) {
            showToast("数据缺失");
            finish();
            return;
        }
        tv_integral_lottery_award_time.setVisibility(View.GONE);
        tv_oversea_buy_tint.setVisibility(View.GONE);
        header_shared.setVisibility(View.GONE);
        tv_header_titleAll.setText("领取奖品");
        tl_normal_bar.setSelected(true);
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected void loadData() {
        if (userId > 0) {
            getIntegralLotteryAward();
            if (isFirst || addressId < 1) {
                getDefaultAddress();
            } else {
                getAddressDetails();
            }
        }
    }

    /**
     * 积分夺宝领取
     */
    private void getIntegralLotteryAward() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("activityId", activityId);
        NetLoadUtils.getNetInstance().loadNetDataPost(IntegralLotteryAwardGetActivity.this,
                H_ATTENDANCE_INTEGRAL_LOTTERY_AWARD_INFO
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {

                        integralLotteryAwardEntity = GsonUtils.fromJson(result, IntegralLotteryAwardGetEntity.class);
                        if (integralLotteryAwardEntity != null) {
                            if (SUCCESS_CODE.equals(integralLotteryAwardEntity.getCode())) {
                                setLotteryAwardData(integralLotteryAwardEntity);
                            } else {
                                showToast( integralLotteryAwardEntity.getMsg());
                            }
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, integralLotteryAwardEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, integralLotteryAwardEntity);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showToast( R.string.unConnectedNetwork);
                    }
                });
    }

    /**
     * 设置积分夺宝奖励
     *
     * @param integralLotteryAwardEntity
     */
    private void setLotteryAwardData(IntegralLotteryAwardGetEntity integralLotteryAwardEntity) {
        GlideImageLoaderUtil.loadCenterCrop(this, iv_integral_lottery_award_image, getStrings(integralLotteryAwardEntity.getImage()));
        tv_integral_lottery_award_name.setText(getStrings(integralLotteryAwardEntity.getPrizeName()));
        tv_integral_lottery_award_confirm_get.setVisibility(View.VISIBLE);
        if (integralLotteryAwardEntity.getStatus() == 2) {
            tv_integral_lottery_award_confirm_get.setEnabled(true);
            tv_integral_lottery_award_confirm_get.setText("确认");
        } else if (integralLotteryAwardEntity.getStatus() == 3) {
            tv_integral_lottery_award_confirm_get.setEnabled(false);
            tv_integral_lottery_award_confirm_get.setText("已领取");
        } else if (integralLotteryAwardEntity.getStatus() == 4) {
            tv_integral_lottery_award_confirm_get.setEnabled(false);
            tv_integral_lottery_award_confirm_get.setText("已过期");
        } else {
            tv_integral_lottery_award_confirm_get.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                finish();
            } else {
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == NEW_CRE_ADDRESS_REQ || requestCode == SEL_ADDRESS_REQ) {
                //            获取地址成功
                addressId = data.getIntExtra("addressId", 0);
                isFirst = false;
                getAddressDetails();
            } else if (requestCode == IS_LOGIN_CODE) {
                loadData();
            }
        }
    }

    private void getDefaultAddress() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, DELIVERY_ADDRESS, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                AddressInfoEntity addressInfoEntity = GsonUtils.fromJson(result, AddressInfoEntity.class);
                if (addressInfoEntity != null) {
                    if (addressInfoEntity.getCode().equals(SUCCESS_CODE)) {
                        setAddressData(addressInfoEntity.getAddressInfoBean());
                    } else if (addressInfoEntity.getCode().equals(EMPTY_CODE)) {
                        setAddressData(null);
                    } else {
                        showToast( addressInfoEntity.getMsg());
                    }
                }
            }
        });
    }

    private void getAddressDetails() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", addressId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, ADDRESS_DETAILS, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                AddressInfoEntity addressInfoEntity = GsonUtils.fromJson(result, AddressInfoEntity.class);
                if (addressInfoEntity != null) {
                    if (addressInfoEntity.getCode().equals(SUCCESS_CODE)) {
                        setAddressData(addressInfoEntity.getAddressInfoBean());
                    } else if (addressInfoEntity.getCode().equals(EMPTY_CODE)) {
                        setAddressData(null);
                    } else {
                        showToast(addressInfoEntity.getMsg());
                    }
                }
            }
        });
    }

    private void setAddressData(AddressInfoBean addressInfoBean) {
        if (addressInfoBean != null) {
            addressId = addressInfoBean.getId();
            ll_indent_address_default.setVisibility(VISIBLE);
            ll_indent_address_null.setVisibility(View.GONE);
            tv_consignee_name.setText(addressInfoBean.getConsignee());
            tv_address_mobile_number.setText(addressInfoBean.getMobile());
            tv_indent_details_address.setText((addressInfoBean.getAddress_com() + addressInfoBean.getAddress() + " "));
        } else {
            ll_indent_address_default.setVisibility(View.GONE);
            ll_indent_address_null.setVisibility(VISIBLE);
        }
    }

    private void setLotteryAward() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("activityId", activityId);
        params.put("addressId", addressId);
        params.put("recordId", integralLotteryAwardEntity.getId());
        NetLoadUtils.getNetInstance().loadNetDataPost(this, H_ATTENDANCE_INTEGRAL_LOTTERY_AWARD_GET,
                params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        loadHud.dismiss();

                        RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                        if (requestStatus != null) {
                            if (SUCCESS_CODE.equals(requestStatus.getCode())) {
                                showToast("领取成功");
                                finish();
                            } else {
                                showToastRequestMsg( requestStatus);
                            }
                        }
                    }

                    @Override
                    public void onNotNetOrException() {
                        loadHud.dismiss();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showToast( R.string.invalidData);
                    }
                });
    }

    //    地址列表为空 跳转新建地址
    @OnClick(R.id.tv_lv_top)
    void skipNewAddress(View view) {
        Intent intent = new Intent(IntegralLotteryAwardGetActivity.this, SelectedAddressActivity.class);
        startActivityForResult(intent, NEW_CRE_ADDRESS_REQ);
    }

    //  更换地址  跳转地址列表
    @OnClick({R.id.ll_indent_address_default, R.id.img_skip_address})
    void skipAddressList(View view) {
        Intent intent = new Intent(IntegralLotteryAwardGetActivity.this, SelectedAddressActivity.class);
        intent.putExtra("addressId", String.valueOf(addressId));
        startActivityForResult(intent, SEL_ADDRESS_REQ);
    }

    @OnClick(R.id.tv_integral_lottery_award_confirm_get)
    void awardGet(View view) {
        if (addressId > 0) {
            loadHud.show();
            setLotteryAward();
        } else {
            showToast(R.string.address_not_null);
        }
    }


    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }
}
