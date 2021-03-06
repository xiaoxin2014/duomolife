package com.amkj.dmsh.mine.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.address.activity.SelectedAddressActivity;
import com.amkj.dmsh.address.bean.AddressInfoEntity;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.bean.QualityCreateAliPayIndentBean;
import com.amkj.dmsh.shopdetails.bean.QualityCreateUnionPayIndentEntity;
import com.amkj.dmsh.shopdetails.bean.QualityCreateWeChatPayIndentBean;
import com.amkj.dmsh.shopdetails.integration.bean.AddressListEntity;
import com.amkj.dmsh.shopdetails.payutils.AliPay;
import com.amkj.dmsh.shopdetails.payutils.UnionPay;
import com.amkj.dmsh.shopdetails.payutils.WXPay;
import com.amkj.dmsh.utils.CountDownTimer;
import com.amkj.dmsh.utils.LifecycleHandler;
import com.amkj.dmsh.utils.SharedPreUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.constant.ConstantMethod.dismissLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;
import static com.amkj.dmsh.constant.ConstantMethod.showLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.NEW_CRE_ADDRESS_REQ;
import static com.amkj.dmsh.constant.ConstantVariable.OPEN_VIP_SUCCESS;
import static com.amkj.dmsh.constant.ConstantVariable.PAY_ALI_PAY;
import static com.amkj.dmsh.constant.ConstantVariable.PAY_UNION_PAY;
import static com.amkj.dmsh.constant.ConstantVariable.PAY_WX_PAY;
import static com.amkj.dmsh.constant.ConstantVariable.SEL_ADDRESS_REQ;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.ADDRESS_LIST;
import static com.amkj.dmsh.utils.TimeUtils.getCoutDownTime;

/**
 * Created by xiaoxin on 2020/8/28
 * Version:v4.7.0
 * ClassDescription :?????????????????????
 */
public class OpenVipWriteActivity extends BaseActivity {
    @BindView(R.id.tv_life_back)
    TextView mTvLifeBack;
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;
    @BindView(R.id.tl_normal_bar)
    Toolbar mTlNormalBar;
    @BindView(R.id.tv_amout)
    TextView mTvAmout;
    @BindView(R.id.tv_lv_top)
    TextView mTvLvTop;
    @BindView(R.id.ll_indent_address_null)
    LinearLayout mLlIndentAddressNull;
    @BindView(R.id.tv_consignee_name)
    TextView mTvConsigneeName;
    @BindView(R.id.tv_consignee_mobile_number)
    TextView mTvConsigneeMobileNumber;
    @BindView(R.id.tv_indent_details_address)
    TextView mTvIndentDetailsAddress;
    @BindView(R.id.img_skip_address)
    ImageView mImgSkipAddress;
    @BindView(R.id.ll_indent_address_default)
    LinearLayout mLlIndentAddressDefault;
    @BindView(R.id.rb_checked_alipay)
    RadioButton mRbCheckedAlipay;
    @BindView(R.id.rb_checked_wechat_pay)
    RadioButton mRbCheckedWechatPay;
    @BindView(R.id.rb_checked_union_pay)
    RadioButton mRbCheckedUnionPay;
    @BindView(R.id.radio_group)
    RadioGroup mRadioGroup;
    @BindView(R.id.img_aliPay)
    ImageView mImgAliPay;
    @BindView(R.id.tv_item_alipay)
    TextView mTvItemAlipay;
    @BindView(R.id.ll_aliPay)
    LinearLayout mLlAliPay;
    @BindView(R.id.img_weChat_way)
    ImageView mImgWeChatWay;
    @BindView(R.id.tv_item_wechat_pay)
    TextView mTvItemWechatPay;
    @BindView(R.id.ll_Layout_weChat)
    LinearLayout mLlLayoutWeChat;
    @BindView(R.id.img_union_way)
    ImageView mImgUnionWay;
    @BindView(R.id.tv_item_union_pay)
    TextView mTvItemUnionPay;
    @BindView(R.id.ll_Layout_union_pay)
    LinearLayout mLlLayoutUnionPay;
    @BindView(R.id.ll_pay_way)
    LinearLayout mLlPayWay;
    @BindView(R.id.tv_open_vip)
    TextView mTvOpenVip;
    @BindView(R.id.ll_open_vip)
    LinearLayout mLlOpenVip;
    @BindView(R.id.rl_address)
    RelativeLayout mRlAddress;
    @BindView(R.id.ll_write)
    LinearLayout mLlWrite;
    @BindView(R.id.tv_countdownTime)
    TextView mTvCountdownTime;
    private String payWay = PAY_ALI_PAY;
    private int addressId;
    private String orderCreateNo;
    private String mCardId;
    private String mPayPrice;
    private String mGiftProductIds;
    private CountDownTimer mCountDownTimer;
    private String mUserCouponId;

    @Override
    protected int getContentView() {
        return R.layout.activity_vip_direct_write;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        if (getIntent().getExtras() != null) {
            mGiftProductIds = getIntent().getStringExtra("giftProductIds");
            mCardId = getIntent().getStringExtra("cardId");
            mPayPrice = getIntent().getStringExtra("payPrice");
            mUserCouponId = getIntent().getStringExtra("userCouponId");
        } else {
            showToast("????????????????????????");
        }
        mTvHeaderTitle.setText("????????????");
        mTvHeaderShared.setVisibility(GONE);
        mTvAmout.setText(getStringsChNPrice(this, mPayPrice));
        //??????????????????
        mRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_checked_alipay) {
                payWay = PAY_ALI_PAY;
            } else if (checkedId == R.id.rb_checked_wechat_pay) {
                payWay = PAY_WX_PAY;
            } else if (checkedId == R.id.rb_checked_union_pay) {
                payWay = PAY_UNION_PAY;
            }
        });
    }

    @Override
    protected void loadData() {
        if (!TextUtils.isEmpty(mGiftProductIds)) {
            mRlAddress.setVisibility(VISIBLE);
            getAddress();
        }
        getPayWay();
        showCountDownTime();
    }

    private void showCountDownTime() {
        if (mCountDownTimer == null) {
            mCountDownTimer = new CountDownTimer(getActivity()) {
                @Override
                public void onTick(long millisUntilFinished) {
                    String coutDownTime = getCoutDownTime(millisUntilFinished, false);
                    String coutDownText = "??????????????????????????????" + coutDownTime;
                    mTvCountdownTime.setText(ConstantMethod.getSpannableString(coutDownText, 10, coutDownText.length(), -1, "#3274d9", true));
                }

                @Override
                public void onFinish() {
                    mTvCountdownTime.setText("?????????");
                }
            };
        }

        mCountDownTimer.setMillisInFuture(2 * 60 * 60 * 1000);
        mCountDownTimer.start();
    }

    //???????????????????????????????????????????????????
    private void getAddress() {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, ADDRESS_LIST, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                AddressListEntity addressListEntity = GsonUtils.fromJson(result, AddressListEntity.class);
                if (addressListEntity != null) {
                    if (addressListEntity.getCode().equals(SUCCESS_CODE)) {
                        List<AddressInfoEntity.AddressInfoBean> addressAllBeanList = addressListEntity.getAddressAllBeanList();
                        if (addressAllBeanList != null && addressAllBeanList.size() > 0) {
                            setAddressData(addressAllBeanList.get(0));
                        }
                    } else if (addressListEntity.getCode().equals(EMPTY_CODE)) {
                        setAddressData(null);
                    } else {
                        showToast(addressListEntity.getMsg());
                    }
                }
            }
        });
    }

    private void getPayWay() {
        Map<String, String> map = new HashMap<>();
        map.put("flag", "vip");
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Url.GET_PAYTYPE_LIST, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Map map = GsonUtils.fromJson(result, Map.class);
                if (map != null) {
                    Object payways = map.get("result");
                    if (payways instanceof List && ((List) payways).size() > 0) {
                        //??????
                        if (!((List) payways).contains("1")) {
                            mRadioGroup.getChildAt(1).setVisibility(GONE);
                            mLlPayWay.getChildAt(1).setVisibility(GONE);
                        }
                        //?????????
                        if (!((List) payways).contains("2")) {
                            mRadioGroup.getChildAt(0).setVisibility(GONE);
                            mLlPayWay.getChildAt(0).setVisibility(GONE);
                        }
                        //??????
                        if (!((List) payways).contains("3")) {
                            mRadioGroup.getChildAt(2).setVisibility(GONE);
                            mLlPayWay.getChildAt(2).setVisibility(GONE);
                        }

                        String payType = (String) ((List) payways).get(0);
                        if ("1".equals(payType)) {
                            payWay = PAY_WX_PAY;
                            ((RadioButton) mRadioGroup.getChildAt(1)).setChecked(true);
                        } else if ("2".equals(payType)) {
                            payWay = PAY_ALI_PAY;
                            ((RadioButton) mRadioGroup.getChildAt(0)).setChecked(true);
                        } else if ("3".equals(payType)) {
                            payWay = PAY_UNION_PAY;
                            ((RadioButton) mRadioGroup.getChildAt(2)).setChecked(true);
                        }
                    }
                }
                NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
            }

            @Override
            public void onNotNetOrException() {
                NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
            }
        });
    }

    //??????????????????
    private void setAddressData(AddressInfoEntity.AddressInfoBean addressInfoBean) {
        if (addressInfoBean != null) {
            addressId = addressInfoBean.getId();
            mLlIndentAddressDefault.setVisibility(VISIBLE);
            mLlIndentAddressNull.setVisibility(GONE);
            mTvConsigneeName.setText(addressInfoBean.getConsignee());
            mTvConsigneeMobileNumber.setText(addressInfoBean.getMobile());
            mTvIndentDetailsAddress.setText((addressInfoBean.getAddress_com() + addressInfoBean.getAddress() + " "));
        } else {
            mLlIndentAddressDefault.setVisibility(GONE);
            mLlIndentAddressNull.setVisibility(VISIBLE);
        }
    }


    @OnClick({R.id.tv_life_back, R.id.ll_open_vip, R.id.tv_lv_top, R.id.ll_indent_address_default})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_life_back:
                finish();
                break;
            case R.id.ll_open_vip:
                if (TextUtils.isEmpty(payWay)) {
                    showToast("?????????????????????");
                } else if (TextUtils.isEmpty(mCardId)) {
                    showToast("?????????????????????");
                } else if (!TextUtils.isEmpty(mGiftProductIds) && addressId == 0) {
                    showToast("?????????????????????");
                } else {
                    creatIndent();
                }
                break;
            case R.id.tv_lv_top:
                intent = new Intent(getActivity(), SelectedAddressActivity.class);
                intent.putExtra("hasDefaultAddress", false);
                startActivityForResult(intent, NEW_CRE_ADDRESS_REQ);
                break;
            case R.id.ll_indent_address_default:
                intent = new Intent(getActivity(), SelectedAddressActivity.class);
                intent.putExtra("addressId", String.valueOf(addressId));
                startActivityForResult(intent, SEL_ADDRESS_REQ);
                break;
        }
    }

    //????????????
    private void creatIndent() {
        showLoadhud(this);
        Map<String, Object> map = new HashMap<>();
        map.put("buyType", payWay);
        map.put("cardId", mCardId);
        if (addressId > 0) {
            map.put("addressId", addressId);
        }
        if (!TextUtils.isEmpty(mGiftProductIds)) {
            map.put("giftProductIds", mGiftProductIds);
        }
        if (!TextUtils.isEmpty(mUserCouponId)) {
            map.put("userCouponId", mUserCouponId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.SUBMIT_VIP_USER, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                dealingIndentPayResult(result);
            }

            @Override
            public void onNotNetOrException() {
                dismissLoadhud(getActivity());
                showToast(R.string.do_failed);
            }
        });
    }

    /**
     * ????????????????????????
     */
    private void dealingIndentPayResult(String result) {
        dismissLoadhud(this);
        if (payWay.equals(PAY_WX_PAY)) {
            QualityCreateWeChatPayIndentBean qualityWeChatIndent = GsonUtils.fromJson(result, QualityCreateWeChatPayIndentBean.class);
            if (qualityWeChatIndent != null) {
                String msg = qualityWeChatIndent.getResult() != null &&
                        !TextUtils.isEmpty(qualityWeChatIndent.getResult().getMsg()) ?
                        getStrings(qualityWeChatIndent.getResult().getMsg()) :
                        getStrings(qualityWeChatIndent.getMsg());
                if (qualityWeChatIndent.getCode().equals(SUCCESS_CODE)) {
                    //???????????????????????????????????????
                    orderCreateNo = qualityWeChatIndent.getResult().getNo();
                    doWXPay(qualityWeChatIndent.getResult());
                } else {
                    showToast(msg);
                }
            }
        } else if (payWay.equals(PAY_ALI_PAY)) {
            QualityCreateAliPayIndentBean qualityAliPayIndent = GsonUtils.fromJson(result, QualityCreateAliPayIndentBean.class);
            if (qualityAliPayIndent != null) {
                String msg = qualityAliPayIndent.getResult() != null &&
                        !TextUtils.isEmpty(qualityAliPayIndent.getResult().getMsg()) ?
                        getStrings(qualityAliPayIndent.getResult().getMsg()) :
                        getStrings(qualityAliPayIndent.getMsg());
                if (qualityAliPayIndent.getCode().equals(SUCCESS_CODE)) {
                    //??????????????????????????????????????????
                    orderCreateNo = qualityAliPayIndent.getResult().getNo();
                    doAliPay(qualityAliPayIndent.getResult());
                } else {
                    showToast(msg);
                }
            }
        } else if (payWay.equals(PAY_UNION_PAY)) {
            QualityCreateUnionPayIndentEntity qualityUnionIndent = GsonUtils.fromJson(result, QualityCreateUnionPayIndentEntity.class);
            if (qualityUnionIndent != null) {
                String msg = qualityUnionIndent.getQualityCreateUnionPayIndent() != null &&
                        !TextUtils.isEmpty(qualityUnionIndent.getQualityCreateUnionPayIndent().getMsg()) ?
                        getStrings(qualityUnionIndent.getQualityCreateUnionPayIndent().getMsg()) :
                        getStrings(qualityUnionIndent.getMsg());
                if (SUCCESS_CODE.equals(qualityUnionIndent.getCode())) {
                    //???????????????????????????????????????
                    orderCreateNo = qualityUnionIndent.getQualityCreateUnionPayIndent().getNo();
                    unionPay(qualityUnionIndent);
                } else {
                    showToast(msg);
                }
            }
        }
    }

    //????????????
    private void doAliPay(QualityCreateAliPayIndentBean.ResultBean resultBean) {
        new AliPay(this, resultBean.getNo(), resultBean.getPayKey(), new AliPay.AliPayResultCallBack() {
            @Override
            public void onSuccess() {
                showToast("????????????");
                paySucess();
            }

            @Override
            public void onDealing() {
                showToast("???????????????...");
            }

            @Override
            public void onError(int error_code) {
                switch (error_code) {
                    case AliPay.ERROR_RESULT:
                        showToast("????????????:????????????????????????");
                        break;
                    case AliPay.ERROR_NETWORK:
                        showToast("????????????:??????????????????");
                        break;
                    case AliPay.ERROR_PAY:
                        showToast("????????????:?????????????????????");
                        break;
                    default:
                        showToast("????????????");
                        break;
                }
            }

            @Override
            public void onCancel() {
                showToast("????????????");
            }
        }).doPay();
    }

    //????????????
    private void doWXPay(QualityCreateWeChatPayIndentBean.ResultBean resultBean) {
        WXPay.init(this);//?????????????????????
        WXPay.getInstance().doPayDateObject(resultBean.getNo(), resultBean.getPayKey(), new WXPay.WXPayResultCallBack() {
            @Override
            public void onSuccess() {
                showToast("????????????");
                paySucess();
            }

            @Override
            public void onError(int error_code) {
                switch (error_code) {
                    case WXPay.NO_OR_LOW_WX:
                        showToast("????????????????????????????????????");
                        break;
                    case WXPay.ERROR_PAY_PARAM:
                        showToast("????????????");
                        break;
                    case WXPay.ERROR_PAY:
                        showToast("????????????");
                        break;
                }
            }

            @Override
            public void onCancel() {
                showToast("????????????");
            }
        });
    }

    //????????????
    private void unionPay(@NonNull QualityCreateUnionPayIndentEntity qualityUnionIndent) {
        if (qualityUnionIndent.getQualityCreateUnionPayIndent().getPayKeyBean() != null &&
                !TextUtils.isEmpty(qualityUnionIndent.getQualityCreateUnionPayIndent().getPayKeyBean().getPaymentUrl())) {
            if (loadHud != null) {
                loadHud.show();
            }
            UnionPay unionPay = new UnionPay(getActivity(), qualityUnionIndent.getQualityCreateUnionPayIndent().getNo(),
                    qualityUnionIndent.getQualityCreateUnionPayIndent().getPayKeyBean().getPaymentUrl(),
                    new UnionPay.UnionPayResultCallBack() {
                        @Override
                        public void onUnionPaySuccess(String webResultValue) {
                            if (loadHud != null) {
                                loadHud.dismiss();
                            }
                            if (!TextUtils.isEmpty(webResultValue) && "1".equals(webResultValue)) {
                                finish();
                            } else {
                                paySucess();
                            }
                        }

                        @Override
                        public void onUnionPayError(String errorMes) {
                            if (loadHud != null) {
                                loadHud.dismiss();
                            }
                            showToast(errorMes);
                        }
                    });
        } else {
            showToast("???????????????????????????????????????????????????");
        }
    }


    //????????????
    private void paySucess() {
        //??????????????????
        ConstantMethod.setIsVip(true);
        SharedPreUtils.setParam("isVip", true);
        //??????????????????
        EventBus.getDefault().post(new EventMessage(OPEN_VIP_SUCCESS));
        //?????????????????????????????????????????????????????????????????????????????????
        Intent intent = new Intent(getActivity(), DomolifeVipActivity.class);
        intent.putExtra("vipPaySuccess", "1");
        new LifecycleHandler(this).postDelayed(() -> {
            startActivity(intent);
            finish();
        }, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if (requestCode == IS_LOGIN_CODE) {
                finish();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case NEW_CRE_ADDRESS_REQ:
            case SEL_ADDRESS_REQ:
                //??????????????????
                AddressInfoEntity.AddressInfoBean addressInfoBean = data.getParcelableExtra("addressInfoBean");
                setAddressData(addressInfoBean);
                break;
            case IS_LOGIN_CODE:
                loadData();
                break;
        }
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return mLlWrite;
    }
}
