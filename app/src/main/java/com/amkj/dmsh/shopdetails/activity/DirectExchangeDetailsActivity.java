package com.amkj.dmsh.shopdetails.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.address.activity.SelectedAddressActivity;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.bean.ActivityInfoListBean;
import com.amkj.dmsh.bean.OrderProductNewBean;
import com.amkj.dmsh.bean.TabNameBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.activity.QualityProductActActivity;
import com.amkj.dmsh.dominant.adapter.QualityCustomAdapter;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.qyservice.QyProductIndentInfo;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.shopdetails.adapter.DirectProductListAdapter;
import com.amkj.dmsh.shopdetails.adapter.IndentDiscountAdapter;
import com.amkj.dmsh.shopdetails.bean.ButtonListBean;
import com.amkj.dmsh.shopdetails.bean.IndentDetailEntity;
import com.amkj.dmsh.shopdetails.bean.MainOrderListEntity;
import com.amkj.dmsh.shopdetails.bean.PriceInfoBean;
import com.amkj.dmsh.utils.CountDownTimer;
import com.amkj.dmsh.utils.alertdialog.AlertDialogHelper;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.views.MainButtonView;
import com.amkj.dmsh.views.flycoTablayout.SlidingTabLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static android.view.View.GONE;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.CHANGE_ORDER_ADDRESS;
import static com.amkj.dmsh.constant.ConstantVariable.EDIT_ADDRESS;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_DETAILS_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.UPDATE_INDENT_LIST;
import static com.amkj.dmsh.utils.TimeUtils.getCoutDownTime;
import static com.amkj.dmsh.utils.TimeUtils.isEndOrStartTimeAddSeconds;


/**
 * Created by atd48 on 2016/7/18.
 * 订单详情
 */
public class DirectExchangeDetailsActivity extends BaseActivity {
    @BindView(R.id.tv_indent_title)
    TextView tv_indent_title;
    @BindView(R.id.tb_indent_bar)
    Toolbar tb_indent_bar;
    @BindView(R.id.iv_indent_search)
    ImageView iv_indent_search;
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //订单状态
    @BindView(R.id.tv_indent_detail_status)
    TextView tv_indent_detail_status;
    @BindView(R.id.tv_package_count)
    TextView tv_package_count;
    @BindView(R.id.tv_indent_msg)
    TextView tv_indent_msg;
    //物流相关
    @BindView(R.id.tv_express_info)
    TextView tv_express_info;
    @BindView(R.id.tv_express_time)
    TextView tv_express_time;
    @BindView(R.id.ll_express_info)
    LinearLayout ll_express_info;
    //收件人名字
    @BindView(R.id.ll_indent_address_default)
    LinearLayout ll_indent_address_default;
    @BindView(R.id.tv_consignee_name)
    TextView tv_consignee_name;
    //收件人手机号码
    @BindView(R.id.tv_consignee_mobile_number)
    TextView tv_address_mobile_number;
    //订单地址
    @BindView(R.id.tv_indent_details_address)
    TextView tv_indent_details_address;
    @BindView(R.id.tv_countdownTime)
    TextView tv_countdownTime;
    //具体留言
    @BindView(R.id.tv_indent_user_lea_mes)
    TextView tv_indent_user_lea_mes;
    //修改地址
    @BindView(R.id.tv_change_address)
    TextView tv_change_address;
    //订单价格明细
    @BindView(R.id.rv_indent_details)
    RecyclerView rv_indent_details;
    //订单编号
    @BindView(R.id.tv_indent_order_no)
    TextView tv_indent_order_no;
    @BindView(R.id.ll_indent_order_no)
    LinearLayout ll_indent_order_no;
    //订单创建时间
    @BindView(R.id.tv_indent_order_time)
    TextView tv_indent_order_time;
    @BindView(R.id.ll_indent_order_time)
    LinearLayout ll_indent_order_time;
    //订单支付时间
    @BindView(R.id.tv_indent_pay_time)
    TextView tv_indent_pay_time;
    @BindView(R.id.ll_indent_pay_time)
    LinearLayout ll_indent_pay_time;
    //支付方式
    @BindView(R.id.tv_indent_pay_way)
    TextView tv_indent_pay_way;
    @BindView(R.id.ll_indent_pay_way)
    LinearLayout ll_indent_pay_way;
    @BindView(R.id.sliding_tablayout)
    SlidingTabLayout mSlidingTablayout;
    @BindView(R.id.vp_custom)
    ViewPager mVpCustom;
    @BindView(R.id.main_button_view)
    MainButtonView mMainButtonView;
    //    订单号
    private String orderNo;
    private List<OrderProductNewBean> goodsBeanList = new ArrayList<>();
    //    订单价格优惠列表
    private List<PriceInfoBean> priceInfoList = new ArrayList<>();
    private DirectProductListAdapter directProductListAdapter;
    private IndentDetailEntity infoDetailEntity;
    private IndentDiscountAdapter indentDiscountAdapter;
    private boolean isFirst = true;
    private QualityCustomAdapter qualityCustomAdapter;
    private String[] titles = {"专区1", "专区2", "专区3", "专区4"};
    public final String[] CUSTOM_IDS = new String[]{"405", "406", "407", "408"};
    private AlertDialogHelper mAlertDialogService;
    private int mStatus;
    private CountDownTimer mCountDownTimer;


    @Override
    protected int getContentView() {
        return R.layout.activity_direct_indent_detail;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
        if (loadHud != null) {
            loadHud.setCancellable(false);
        }
        iv_indent_search.setVisibility(GONE);
        tb_indent_bar.setSelected(true);
        tv_indent_title.setText("订单详情");
        Intent intent = getIntent();
        orderNo = intent.getStringExtra("orderNo");
        communal_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        communal_recycler.setNestedScrollingEnabled(false);
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_one_px).create());
        directProductListAdapter = new DirectProductListAdapter(getActivity(), goodsBeanList, INDENT_DETAILS_TYPE);
        communal_recycler.setAdapter(directProductListAdapter);
        smart_communal_refresh.setOnRefreshListener(refreshLayout -> {
            //                滚动距离置0
            loadData();
            directProductListAdapter.setEnableLoadMore(false);
        });
        directProductListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            OrderProductNewBean orderProductInfoBean = (OrderProductNewBean) view.getTag();
            Intent dataIndent = new Intent();
            if (orderProductInfoBean != null) {
                int status = orderProductInfoBean.getStatus();
                switch (view.getId()) {
                    //单件商品申请退款
                    case R.id.tv_refund:
                        if (status == 13) {
                            showServiceDialog("该商品已被发货方锁定，可能正在发货尚未上传物流单号，建议您再耐心等待，如仍需退款，请联系人工客服解锁。", "再等等");
                            return;
                        } else if (status == 10) {
                            dataIndent.setClass(getActivity(), DirectApplyRefundActivity.class);
                            dataIndent.putExtra("refundType", ConstantVariable.NOGOODS_REFUND);
                        } else {
                            dataIndent.setClass(getActivity(), SelectRefundTypeActivity.class);
                        }
                        dataIndent.putExtra("orderNo", orderNo);
                        List<OrderProductNewBean> goods = new ArrayList<>();
                        goods.add(orderProductInfoBean);
                        dataIndent.putExtra("goods", GsonUtils.toJson(goods));
                        startActivity(dataIndent);
                        break;
                    //跳转售后详情
                    case R.id.tv_service:
                        ConstantMethod.skipRefundDetail(getActivity(), orderProductInfoBean.getRefundNo());
                        break;
                    //跳转活动专场
                    case R.id.ll_communal_activity_topic_tag:
                        if (!TextUtils.isEmpty(orderProductInfoBean.getActivityCode())) {
                            dataIndent.setClass(getActivity(), QualityProductActActivity.class);
                            dataIndent.putExtra("activityCode", getStrings(orderProductInfoBean.getActivityCode()));
                            startActivity(dataIndent);
                        }
                        break;
                    //跳转商品详情
                    case R.id.ll_product:
                        dataIndent.setClass(getActivity(), ShopScrollDetailsActivity.class);
                        dataIndent.putExtra("productId", String.valueOf(orderProductInfoBean.getProductId()));
                        startActivity(dataIndent);
                        break;
                }
            }
        });

        rv_indent_details.setLayoutManager(new LinearLayoutManager(DirectExchangeDetailsActivity.this));
        rv_indent_details.setNestedScrollingEnabled(false);
        indentDiscountAdapter = new IndentDiscountAdapter(priceInfoList);
        rv_indent_details.setAdapter(indentDiscountAdapter);

        //初始化自定义专区
        qualityCustomAdapter = new QualityCustomAdapter(getSupportFragmentManager(), Arrays.asList(CUSTOM_IDS), getSimpleName());
        mVpCustom.setAdapter(qualityCustomAdapter);
        mVpCustom.setOffscreenPageLimit(titles.length - 1);
        mSlidingTablayout.setViewPager(mVpCustom, titles);
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
        if (requestCode == CHANGE_ORDER_ADDRESS) {
            showToast("地址修改成功");
        } else if (requestCode == IS_LOGIN_CODE) {
            loadData();
        }
    }

    @Override
    protected void loadData() {
        Map<String, Object> params = new HashMap<>();
        params.put("orderNo", orderNo);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Url.Q_INDENT_NEW_DETAILS
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        goodsBeanList.clear();
                        priceInfoList.clear();
                        String code = "";
                        String msg = "";
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            code = (String) jsonObject.get("code");
                            msg = (String) jsonObject.get("msg");
                            if (code.equals(SUCCESS_CODE)) {

                                infoDetailEntity = GsonUtils.fromJson(result, IndentDetailEntity.class);
                                if (infoDetailEntity != null && infoDetailEntity.getResult() != null) {
                                    setIndentData(infoDetailEntity.getResult());
                                }
                            } else {
                                showToast(msg);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        directProductListAdapter.notifyDataSetChanged();
                        indentDiscountAdapter.notifyDataSetChanged();
                        communal_recycler.smoothScrollToPosition(0);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, code);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, infoDetailEntity);
                    }
                });
    }

    @Override
    public View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void setIndentData(MainOrderListEntity.MainOrderBean mainOrderBean) {
        /**
         * 头部
         */
        Map<String, String> logisticsInfo = mainOrderBean.getLogisticsInfo();
        String expressInfo = "";
        if (logisticsInfo != null) {
            expressInfo = logisticsInfo.get("content");
            String expressTime = logisticsInfo.get("time");
            tv_express_info.setText(getStrings(expressInfo));//物流动态
            tv_express_time.setText(getStrings(expressTime));//物流更新时间
        }
        ll_express_info.setVisibility(logisticsInfo != null && !TextUtils.isEmpty(expressInfo) ? View.VISIBLE : GONE);
        int packageCount = mainOrderBean.getPackageCount();
        tv_indent_detail_status.setText(getStrings(mainOrderBean.getStatusText()));//订单主状态
        tv_package_count.setText(packageCount > 1 ? ConstantMethod.getIntegralFormat(this, R.string.packag_count, packageCount) : "");//包裹数量
        tv_indent_msg.setText(getStrings(mainOrderBean.getMsg()));//订单子状态
        tv_indent_msg.setVisibility(!TextUtils.isEmpty(mainOrderBean.getMsg()) ? View.VISIBLE : GONE);
        ll_indent_address_default.setVisibility(!TextUtils.isEmpty(mainOrderBean.getAddress()) ? View.VISIBLE : GONE);
        tv_consignee_name.setText(mainOrderBean.getConsignee());//收件人名字
        tv_address_mobile_number.setText(mainOrderBean.getMobile());//收件人手机号码
        tv_indent_details_address.setText(mainOrderBean.getAddress());//收货地址
        tv_indent_user_lea_mes.setVisibility(!TextUtils.isEmpty(mainOrderBean.getUserRemark()) ? View.VISIBLE : GONE);//买家留言
        tv_indent_user_lea_mes.setText(!TextUtils.isEmpty(mainOrderBean.getUserRemark()) ? getStringsFormat(this, R.string.buy_message, mainOrderBean.getUserRemark()) : "");
        //修改收货地址
        boolean isChangeAddress = false;
        List<ButtonListBean> buttonList = mainOrderBean.getButtonList();
        if (buttonList != null && buttonList.size() > 0) {
            for (int i = 0; i < buttonList.size(); i++) {
                ButtonListBean buttonListBean = buttonList.get(i);
                if (buttonListBean.getId() == EDIT_ADDRESS) {
                    isChangeAddress = true;
                    tv_change_address.setSelected(!buttonListBean.isClickable());
                    break;
                }
            }
        }
        tv_change_address.setVisibility(isChangeAddress ? View.VISIBLE : GONE);

        //待支付倒计时
        mStatus = mainOrderBean.getStatus();
        if (mStatus >= 0 && mStatus < 10) {
            tv_countdownTime.setVisibility(View.VISIBLE);
            setCountTime(mainOrderBean);
        } else {
            tv_countdownTime.setVisibility(GONE);
        }
        //商品列表数据组装
        List<OrderProductNewBean> orderProductList = mainOrderBean.getOrderProductList();
        List<ActivityInfoListBean> activityInfoList = mainOrderBean.getActivityInfoList();
        if (activityInfoList != null && activityInfoList.size() > 0) {
            for (int i = 0; i < activityInfoList.size(); i++) {
                ActivityInfoListBean activityInfoListBean = activityInfoList.get(i);
                if (activityInfoListBean != null) {
                    List<OrderProductNewBean> productList = activityInfoListBean.getOrderProductList();
                    for (int j = 0; j < productList.size(); j++) {
                        OrderProductNewBean orderProductBean = productList.get(j);
                        orderProductBean.setActivityCode(activityInfoListBean.getActivityCode());
                        orderProductBean.setActivityTag(activityInfoListBean.getActivityTag());
                        //每个活动组用分割线隔开
                        orderProductBean.setShowLine(j == productList.size() - 1);
                        goodsBeanList.add(orderProductBean);
                    }
                }
            }
        }

        if (orderProductList != null && orderProductList.size() > 0) {
            goodsBeanList.addAll(orderProductList);
        }

        /**
         * 尾部
         */
        if (TextUtils.isEmpty(mainOrderBean.getOrderNo())) {        //订单编号
            ll_indent_order_no.setVisibility(GONE);
        } else {
            ll_indent_order_no.setVisibility(View.VISIBLE);
            tv_indent_order_no.setTag(mainOrderBean.getOrderNo());
            tv_indent_order_no.setText(mainOrderBean.getOrderNo());
        }

        if (TextUtils.isEmpty(mainOrderBean.getCreateTime())) {        //订单时间
            ll_indent_order_time.setVisibility(GONE);
        } else {
            ll_indent_order_time.setVisibility(View.VISIBLE);
            tv_indent_order_time.setText(mainOrderBean.getCreateTime());
        }

        if (TextUtils.isEmpty(mainOrderBean.getPayTime())) {        //支付时间
            ll_indent_pay_time.setVisibility(GONE);
        } else {
            ll_indent_pay_time.setVisibility(View.VISIBLE);
            tv_indent_pay_time.setText(mainOrderBean.getPayTime());
        }

        if (TextUtils.isEmpty(mainOrderBean.getPayType())) {        //支付方式
            ll_indent_pay_way.setVisibility(GONE);
        } else {
            ll_indent_pay_way.setVisibility(View.VISIBLE);
            tv_indent_pay_way.setText(mainOrderBean.getPayType());
        }
        //订单价格明细
        if (mainOrderBean.getPriceInfoList() != null && mainOrderBean.getPriceInfoList().size() > 0) {
            priceInfoList.addAll(mainOrderBean.getPriceInfoList());
        }

        //主订单按钮
        mMainButtonView.updateView(this, mainOrderBean, getSimpleName());
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mVpCustom.getLayoutParams();
        layoutParams.bottomMargin = mMainButtonView.getVisibility() == View.VISIBLE ? AutoSizeUtils.mm2px(mAppContext, 98) : 0;
        mVpCustom.setLayoutParams(layoutParams);
    }

    private void setCountTime(MainOrderListEntity.MainOrderBean indentDetailBean) {
        String currentTime = infoDetailEntity.getCurrentTime();
        String createTime = indentDetailBean.getCreateTime();
        try {
            //格式化开始时间
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            Date dateCurrent = formatter.parse(currentTime);
            Date dateCreat = formatter.parse(createTime);
            long second = indentDetailBean.getSecond();
            if (isEndOrStartTimeAddSeconds(createTime, currentTime, second)) {
                tv_countdownTime.setVisibility(View.VISIBLE);
                long millisInFuture = dateCreat.getTime() + second * 1000 - dateCurrent.getTime();
                if (mCountDownTimer == null) {
                    mCountDownTimer = new CountDownTimer(getActivity(), 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            String coutDownTime = getCoutDownTime(millisUntilFinished, false);
                            String coutDownText = "剩 " + coutDownTime + " 自动关闭";
                            tv_countdownTime.setText(ConstantMethod.getSpannableString(coutDownText, 2, 2 + coutDownTime.length() + 1, -1, "#3274d9", true));
                        }

                        @Override
                        public void onFinish() {
                            cancel();
                            tv_countdownTime.setText("已结束");
                        }
                    };
                }

                mCountDownTimer.setMillisInFuture(millisInFuture);
                mCountDownTimer.start();
            } else {
                tv_countdownTime.setVisibility(GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            tv_countdownTime.setVisibility(GONE);
        }
    }

    @OnClick({R.id.tv_indent_back, R.id.iv_indent_service, R.id.ll_qy_service, R.id.ll_express_info, R.id.tv_copy_text, R.id.tv_change_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_indent_back:
                finish();
                break;
            //联系客服
            case R.id.iv_indent_service:
            case R.id.ll_qy_service:
                openService();
                break;
            //跳转物流详情
            case R.id.ll_express_info:
                Intent intent = new Intent(getActivity(), DirectLogisticsDetailsActivity.class);
                intent.putExtra("orderNo", orderNo);
                getActivity().startActivity(intent);
                break;
            //复制订单号
            case R.id.tv_copy_text:
                String content = (String) tv_indent_order_no.getTag();
                ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("Label", getStrings(content));
                cmb.setPrimaryClip(mClipData);
                showToast("已复制");
                break;
            //修改收货地址
            case R.id.tv_change_address:
                if (!tv_change_address.isSelected()) {
                    intent = new Intent(this, SelectedAddressActivity.class);
                    intent.putExtra("orderNo", orderNo);
                    startActivityForResult(intent, CHANGE_ORDER_ADDRESS);
                } else {
                    showServiceDialog("已超过自主修改时间，如需修改收货信息请联系人工客服", "不改了");
                }

                break;
        }
    }


    private void openService() {
        if (infoDetailEntity != null) {
            MainOrderListEntity.MainOrderBean indentDetailBean = infoDetailEntity.getResult();
            QyProductIndentInfo qyProductIndentInfo = null;
            if (indentDetailBean != null) {
                qyProductIndentInfo = new QyProductIndentInfo();
                if (goodsBeanList.size() > 0) {
                    qyProductIndentInfo.setTitle(getStrings(goodsBeanList.get(0).getProductName()));
                    qyProductIndentInfo.setPicUrl(getStrings(goodsBeanList.get(0).getPicUrl()));
                }
                qyProductIndentInfo.setDesc(indentDetailBean.getStatusText());
                qyProductIndentInfo.setNote(String.format(getResources().getString(R.string.money_price_chn), getStrings(indentDetailBean.getPayAmount())));
                qyProductIndentInfo.setUrl(Url.BASE_SHARE_PAGE_TWO + "m/template/order_template/order.html?noid=" + orderNo);
            }
            QyServiceUtils.getQyInstance().openQyServiceChat(this, "订单详情", Url.BASE_SHARE_PAGE_TWO + "m/template/order_template/order.html?noid=" + orderNo, qyProductIndentInfo);
        } else {
            QyServiceUtils.getQyInstance().openQyServiceChat(this, "订单详情", Url.BASE_SHARE_PAGE_TWO + "m/template/order_template/order.html?noid=" + orderNo, null);
        }
    }


    //显示联系客服弹窗
    private void showServiceDialog(String msg, String confirmText) {
        if (mAlertDialogService == null) {
            mAlertDialogService = new AlertDialogHelper(this, R.layout.layout_alert_dialog_new);
            mAlertDialogService.setCancelText("联系客服")
                    .setAlertListener(new AlertDialogHelper.AlertConfirmCancelListener() {
                        @Override
                        public void confirm() {

                        }

                        @Override
                        public void cancel() {
                            QyServiceUtils.getQyInstance().openQyServiceChat(getActivity());
                        }
                    });
        }
        mAlertDialogService.setMsg(msg).setConfirmText(confirmText).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirst) {
            loadData();
        }
        isFirst = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAlertDialogService != null) {
            mAlertDialogService.dismiss();
        }
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (UPDATE_INDENT_LIST.equals(message.type)) {
            if (getSimpleName().equals(message.result)) {
                loadData();
            }
        } else if (message.type.equals(ConstantVariable.UPDATE_CUSTOM_NAME)) {
            try {
                if (mSlidingTablayout != null) {
                    TabNameBean tabNameBean = (TabNameBean) message.result;
                    if (getSimpleName().equals(tabNameBean.getSimpleName())) {
                        TextView titleView = mSlidingTablayout.getTitleView(tabNameBean.getPosition());
                        titleView.setText(tabNameBean.getTabName());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
