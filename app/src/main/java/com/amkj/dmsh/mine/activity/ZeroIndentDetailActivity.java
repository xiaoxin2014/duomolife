package com.amkj.dmsh.mine.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.mine.bean.ZeroGoodsInfoBean;
import com.amkj.dmsh.mine.bean.ZeroIndentEntity;
import com.amkj.dmsh.mine.bean.ZeroIndentEntity.ZeroIndentBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.shopdetails.activity.RefundMoneyActivity;
import com.amkj.dmsh.shopdetails.adapter.IndentDiscountAdapter;
import com.amkj.dmsh.shopdetails.bean.PriceInfoBean;
import com.amkj.dmsh.utils.CountDownTimer;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.views.alertdialog.AlertDialogGoPay;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static android.view.View.GONE;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.dismissLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeLong;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;
import static com.amkj.dmsh.constant.ConstantMethod.showLoadhud;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.utils.TimeUtils.getCoutDownTime;
import static com.amkj.dmsh.utils.TimeUtils.isEndOrStartTimeAddSeconds;

/**
 * Created by xiaoxin on 2020/8/22
 * Version:v4.7.0
 * ClassDescription :0元试用订单详情
 */
public class ZeroIndentDetailActivity extends BaseActivity {


    @BindView(R.id.tv_life_back)
    TextView mTvLifeBack;
    @BindView(R.id.tv_header_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.tv_header_shared)
    TextView mTvHeaderShared;
    @BindView(R.id.tl_normal_bar)
    Toolbar mTlNormalBar;
    @BindView(R.id.tv_indent_detail_status)
    TextView mTvIndentDetailStatus;
    @BindView(R.id.tv_consignee_name)
    TextView mTvConsigneeName;
    @BindView(R.id.tv_consignee_mobile_number)
    TextView mTvConsigneeMobileNumber;
    @BindView(R.id.tv_change_address)
    TextView mTvChangeAddress;
    @BindView(R.id.tv_indent_details_address)
    TextView mTvIndentDetailsAddress;
    @BindView(R.id.tv_indent_user_lea_mes)
    TextView mTvIndentUserLeaMes;
    @BindView(R.id.ll_indent_address_default)
    LinearLayout mLlIndentAddressDefault;
    @BindView(R.id.checkbox_refund)
    CheckBox mCheckboxRefund;
    @BindView(R.id.iv_direct_indent_pro)
    ImageView mIvDirectIndentPro;
    @BindView(R.id.tv_direct_indent_pro_name)
    TextView mTvDirectIndentProName;
    @BindView(R.id.tv_direct_indent_pro_sku)
    TextView mTvDirectIndentProSku;
    @BindView(R.id.tv_predict_time)
    TextView mTvPredictTime;
    @BindView(R.id.tv_direct_indent_pro_price)
    TextView mTvDirectIndentProPrice;
    @BindView(R.id.tv_direct_pro_count)
    TextView mTvDirectProCount;
    @BindView(R.id.tv_product_status)
    TextView mTvProductStatus;
    @BindView(R.id.ll_product)
    LinearLayout mLlProduct;
    @BindView(R.id.tv_service)
    TextView mTvService;
    @BindView(R.id.ll_new_product)
    LinearLayout mLlNewProduct;
    @BindView(R.id.rv_indent_details)
    RecyclerView mRvIndentDetails;
    @BindView(R.id.tv_indent_order_no)
    TextView mTvIndentOrderNo;
    @BindView(R.id.tv_copy_text)
    TextView mTvCopyText;
    @BindView(R.id.ll_indent_order_no)
    LinearLayout mLlIndentOrderNo;
    @BindView(R.id.tv_indent_order_time)
    TextView mTvIndentOrderTime;
    @BindView(R.id.ll_indent_order_time)
    LinearLayout mLlIndentOrderTime;
    @BindView(R.id.tv_indent_pay_way)
    TextView mTvIndentPayWay;
    @BindView(R.id.ll_indent_pay_way)
    LinearLayout mLlIndentPayWay;
    @BindView(R.id.tv_indent_pay_time)
    TextView mTvIndentPayTime;
    @BindView(R.id.ll_indent_pay_time)
    LinearLayout mLlIndentPayTime;
    @BindView(R.id.tv_qy_service)
    TextView mTvQyService;
    @BindView(R.id.ll_qy_service)
    LinearLayout mLlQyService;
    @BindView(R.id.tv_countdownTime)
    TextView mTvCountdownTime;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout mSmartCommunalRefresh;
    @BindView(R.id.ll_button)
    LinearLayout mllButton;
    @BindView(R.id.ll_detail)
    LinearLayout mLlDetail;
    @BindView(R.id.tv_refund_aspect)
    TextView mTvRefundAspect;
    private String mOrderId;
    private ZeroIndentEntity mZeroIndentEntity;
    private ZeroIndentBean mZeroIndentBean;
    private List<PriceInfoBean> mPriceInfoList = new ArrayList<>();
    private IndentDiscountAdapter indentDiscountAdapter;
    private CountDownTimer mCountDownTimer;
    private AlertDialogGoPay mAlertDialogGoPay;
    private boolean isFirst = true;

    @Override
    protected int getContentView() {
        return R.layout.activity_zero_indent_detail;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        mOrderId = getIntent().getStringExtra("orderId");
        if (TextUtils.isEmpty(mOrderId)) {
            showToast("数据有误，请重试");
            finish();
        }
        mTvHeaderTitle.setText("订单详情");
        mTvHeaderShared.setVisibility(GONE);
        //初始化金额明细
        mRvIndentDetails.setLayoutManager(new LinearLayoutManager(getActivity()));
        indentDiscountAdapter = new IndentDiscountAdapter(this, mPriceInfoList);
        mRvIndentDetails.setAdapter(indentDiscountAdapter);
        mSmartCommunalRefresh.setOnRefreshListener(refreshLayout -> loadData());
    }

    @Override
    protected void loadData() {
        Map<String, String> map = new HashMap<>();
        map.put("orderId", mOrderId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_ZERO_INDENT_DETAIL, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                mSmartCommunalRefresh.finishRefresh();
                mPriceInfoList.clear();
                mZeroIndentEntity = GsonUtils.fromJson(result, ZeroIndentEntity.class);
                if (mZeroIndentEntity != null) {
                    mZeroIndentBean = mZeroIndentEntity.getResult();
                    if (SUCCESS_CODE.equals(mZeroIndentEntity.getCode())) {
                        setDetailInfo();
                        List<PriceInfoBean> priceInfoList = mZeroIndentBean.getPriceInfoList();
                        if (priceInfoList != null && priceInfoList.size() > 0) {
                            mPriceInfoList.addAll(priceInfoList);
                        }
                    } else {
                        showToast(mZeroIndentEntity.getMsg());
                    }
                }
                indentDiscountAdapter.notifyDataSetChanged();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mZeroIndentEntity);
            }

            @Override
            public void onNotNetOrException() {
                mSmartCommunalRefresh.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, mZeroIndentEntity);
            }
        });
    }

    private void setDetailInfo() {
        mTvIndentDetailStatus.setText(getStrings(mZeroIndentBean.getStatusText()));//订单主状态
        mLlIndentAddressDefault.setVisibility(!TextUtils.isEmpty(mZeroIndentBean.getAddress()) ? View.VISIBLE : GONE);
        mTvConsigneeName.setText(mZeroIndentBean.getConsignee());//收件人名字
        mTvConsigneeMobileNumber.setText(mZeroIndentBean.getMobile());//收件人手机号码
        mTvIndentDetailsAddress.setText(mZeroIndentBean.getAddress());//收货地址
        mTvIndentUserLeaMes.setVisibility(!TextUtils.isEmpty(mZeroIndentBean.getRemark()) ? View.VISIBLE : GONE);//买家留言
        mTvIndentUserLeaMes.setText(!TextUtils.isEmpty(mZeroIndentBean.getRemark()) ? getStringsFormat(getActivity(), R.string.buy_message, mZeroIndentBean.getRemark()) : "");
        //商品信息
        if (mZeroIndentBean.getZeroGoodsInfo() != null) {
            ZeroGoodsInfoBean zeroGoodsInfo = mZeroIndentBean.getZeroGoodsInfo();
            GlideImageLoaderUtil.loadCenterCrop(getActivity(), mIvDirectIndentPro, zeroGoodsInfo.getProductImg());
            mTvDirectIndentProName.setText(getStrings(zeroGoodsInfo.getProductName()));
            mTvDirectIndentProPrice.setText(getStringsChNPrice(getActivity(), zeroGoodsInfo.getMarketPrice()));
            mTvDirectProCount.setText("x" + getStrings(zeroGoodsInfo.getCount()));
        }

        //待支付倒计时
        String second = mZeroIndentBean.getSecond();
        if (!TextUtils.isEmpty(second)) {
            mTvCountdownTime.setVisibility(View.VISIBLE);
            setCountTime(mZeroIndentBean);
            //去支付按钮
            mllButton.setVisibility(View.VISIBLE);
        } else {
            mTvCountdownTime.setVisibility(GONE);
            mllButton.setVisibility(GONE);
        }
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mLlDetail.getLayoutParams();
        layoutParams.bottomMargin = AutoSizeUtils.mm2px(mAppContext, mllButton.getVisibility() == View.VISIBLE ? 100 : 0);
        mLlDetail.setLayoutParams(layoutParams);

        //退款去向
        mTvRefundAspect.setVisibility(!TextUtils.isEmpty(mZeroIndentBean.getRefundNo()) ? View.VISIBLE : GONE);

        if (TextUtils.isEmpty(mZeroIndentBean.getOrderNo())) {        //订单编号
            mLlIndentOrderNo.setVisibility(GONE);
        } else {
            mLlIndentOrderNo.setVisibility(View.VISIBLE);
            mTvIndentOrderNo.setText(mZeroIndentBean.getOrderNo());
            mTvIndentOrderNo.setTag(mZeroIndentBean.getOrderNo());
        }

        if (TextUtils.isEmpty(mZeroIndentBean.getCreateTime())) {        //订单时间
            mLlIndentOrderTime.setVisibility(GONE);
        } else {
            mLlIndentOrderTime.setVisibility(View.VISIBLE);
            mTvIndentOrderTime.setText(mZeroIndentBean.getCreateTime());
        }

        if (TextUtils.isEmpty(mZeroIndentBean.getPayTime())) {        //支付时间
            mLlIndentPayTime.setVisibility(GONE);
        } else {
            mLlIndentPayTime.setVisibility(View.VISIBLE);
            mTvIndentPayTime.setText(mZeroIndentBean.getPayTime());
        }

        if (TextUtils.isEmpty(mZeroIndentBean.getPayType())) {        //支付方式
            mLlIndentPayWay.setVisibility(GONE);
        } else {
            mLlIndentPayWay.setVisibility(View.VISIBLE);
            mTvIndentPayWay.setText(mZeroIndentBean.getPayType());
        }
    }

    private void setCountTime(ZeroIndentBean zeroIndentBean) {
        String currentTime = mZeroIndentEntity.getCurrentTime();
        String createTime = zeroIndentBean.getCreateTime();
        try {
            //格式化开始时间
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            Date dateCurrent = formatter.parse(currentTime);
            Date dateCreat = formatter.parse(createTime);
            long second = getStringChangeLong(zeroIndentBean.getSecond());
            if (isEndOrStartTimeAddSeconds(createTime, currentTime, second)) {
                mTvCountdownTime.setVisibility(View.VISIBLE);
                long millisInFuture = dateCreat.getTime() + second * 1000 - dateCurrent.getTime();
                if (mCountDownTimer == null) {
                    mCountDownTimer = new CountDownTimer(getActivity()) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            String coutDownTime = getCoutDownTime(millisUntilFinished, false);
                            String coutDownText = "剩 " + coutDownTime + " 自动关闭";
                            mTvCountdownTime.setText(ConstantMethod.getSpannableString(coutDownText, 2, 2 + coutDownTime.length() + 1, -1, "#3274d9", true));
                        }

                        @Override
                        public void onFinish() {
                            mTvCountdownTime.setText("已结束");
                        }
                    };
                }

                mCountDownTimer.setMillisInFuture(millisInFuture);
                mCountDownTimer.start();
            } else {
                mTvCountdownTime.setVisibility(GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            mTvCountdownTime.setVisibility(GONE);
        }
    }


    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return mSmartCommunalRefresh;
    }


    @OnClick({R.id.tv_life_back, R.id.ll_qy_service, R.id.tv_copy_text, R.id.tv_refund_aspect, R.id.tv_go_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_life_back:
                finish();
                break;
            //退款去向
            case R.id.tv_refund_aspect:
                if (mZeroIndentBean != null && !TextUtils.isEmpty(mZeroIndentBean.getRefundNo())) {
                    Intent intent = new Intent(this, RefundMoneyActivity.class);
                    intent.putExtra("refundNo", mZeroIndentBean.getRefundNo());
                    intent.putExtra("isZero", "1");
                    startActivity(intent);
                }
                break;
            //联系客服
            case R.id.ll_qy_service:
                QyServiceUtils.getQyInstance().openQyServiceChat(this, "0元试用订单详情");
                break;
            //复制订单号
            case R.id.tv_copy_text:
                String content = (String) mTvIndentOrderNo.getTag();
                ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("Label", getStrings(content));
                cmb.setPrimaryClip(mClipData);
                showToast("已复制");
                break;
            //去支付
            case R.id.tv_go_pay:
                if (mZeroIndentBean != null) {
                    goPay(mZeroIndentBean.getOrderNo());
                }
                break;
        }
    }

    private void goPay(String orderNo) {
        showLoadhud(this);
        Map<String, Object> map = new HashMap<>();
        map.put("orderNo", orderNo);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.GET_PAYTYPE_LIST, map, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                dismissLoadhud(getActivity());
                Map map = GsonUtils.fromJson(result, Map.class);
                if (mAlertDialogGoPay == null) {
                    mAlertDialogGoPay = new AlertDialogGoPay(getActivity(), map.get("result"), getSimpleName());
                }
                mAlertDialogGoPay.show(orderNo);
            }

            @Override
            public void onNotNetOrException() {
                dismissLoadhud(getActivity());
            }
        });
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
        if (requestCode == IS_LOGIN_CODE) {
            loadData();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirst) {
            loadData();
        }
        isFirst = false;
    }
}
