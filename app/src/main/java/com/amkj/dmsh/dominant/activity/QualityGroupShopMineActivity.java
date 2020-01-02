package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.dao.ShareDao;
import com.amkj.dmsh.dominant.adapter.QualityGroupMineAdapter;
import com.amkj.dmsh.dominant.bean.QualityGroupMineEntity;
import com.amkj.dmsh.dominant.bean.QualityGroupMineEntity.QualityGroupMineBean;
import com.amkj.dmsh.dominant.bean.QualityGroupShareEntity.QualityGroupShareBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.DirectExchangeDetailsActivity;
import com.amkj.dmsh.shopdetails.bean.QualityCreateAliPayIndentBean;
import com.amkj.dmsh.shopdetails.bean.QualityCreateUnionPayIndentEntity;
import com.amkj.dmsh.shopdetails.bean.QualityCreateWeChatPayIndentBean;
import com.amkj.dmsh.shopdetails.payutils.AliPay;
import com.amkj.dmsh.shopdetails.payutils.UnionPay;
import com.amkj.dmsh.shopdetails.payutils.WXPay;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.views.CustomPopWindow;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.PAY_ALI_PAY;
import static com.amkj.dmsh.constant.ConstantVariable.PAY_UNION_PAY;
import static com.amkj.dmsh.constant.ConstantVariable.PAY_WX_PAY;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TEN;
import static com.amkj.dmsh.constant.ConstantVariable.UNION_RESULT_CODE;
import static com.amkj.dmsh.constant.Url.GROUP_MINE_NEW_INDENT;
import static com.amkj.dmsh.constant.Url.Q_PAYMENT_INDENT;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/8
 * class description:我的拼团
 */
public class QualityGroupShopMineActivity extends BaseActivity {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
    private int page = 1;
    private List<QualityGroupMineBean> qualityGroupMineList = new ArrayList<>();
    private QualityGroupMineAdapter qualityGroupMineAdapter;
    private String payWay;
    private CustomPopWindow mCustomPopWindow;
    private QualityGroupMineBean qualityGroupMineBean;
    private QualityCreateWeChatPayIndentBean qualityWeChatIndent;
    private QualityCreateAliPayIndentBean qualityAliPayIndent;
    private QualityGroupMineEntity qualityGroupMineEntity;
    private UnionPay unionPay;
    private QualityCreateUnionPayIndentEntity qualityUnionIndent;
    private boolean firstSet = true;

    @Override
    protected int getContentView() {
        return R.layout.activity_ql_gp_sp_list;
    }

    @Override
    protected void initViews() {
        getLoginStatus(QualityGroupShopMineActivity.this);
        tv_header_shared.setVisibility(View.GONE);
        tv_header_titleAll.setText("我的拼团");

        smart_communal_refresh.setOnRefreshListener(refreshLayout -> loadData());
        communal_recycler.setLayoutManager(new LinearLayoutManager(QualityGroupShopMineActivity.this));
        qualityGroupMineAdapter = new QualityGroupMineAdapter(QualityGroupShopMineActivity.this, qualityGroupMineList);
        communal_recycler.setAdapter(qualityGroupMineAdapter);
        qualityGroupMineAdapter.setOnLoadMoreListener(() -> {
            page++;
            getData();
        }, communal_recycler);
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp).create());
        setFloatingButton(download_btn_communal, communal_recycler);
        qualityGroupMineAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                qualityGroupMineBean = (QualityGroupMineBean) view.getTag();
                if (qualityGroupMineBean != null) {
                    switch (view.getId()) {
                        case R.id.tv_ql_gp_mine_inv_join:
//                            弹框
                            switch (qualityGroupMineBean.getGpStatus()) {
                                case 3:
                                case 4:
                                    if (userId > 0) {
                                        View indentPopWindow = getLayoutInflater().inflate(R.layout.layout_indent_pay_pop, null, false);
                                        PopupWindowView popupWindowView = new PopupWindowView();
                                        ButterKnife.bind(popupWindowView, indentPopWindow);
                                        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(QualityGroupShopMineActivity.this)
                                                .setView(indentPopWindow)
                                                .enableBackgroundDark(true) //弹出popWindow时，背景是否变暗
                                                .setBgDarkAlpha(0.7f) // 控制亮度
                                                .create().showAtLocation((View) communal_recycler.getParent(), Gravity.CENTER, 0, 0);
                                    } else {
                                        getLoginStatus(QualityGroupShopMineActivity.this);
                                    }
                                    break;
                                case 1:
                                    if (qualityGroupMineBean != null) {
                                        QualityGroupShareBean qualityGroupShareBean = new QualityGroupShareBean();
                                        qualityGroupShareBean.setGpPicUrl(qualityGroupMineBean.getCoverImage());
                                        qualityGroupShareBean.setName(qualityGroupMineBean.getProductName());
                                        qualityGroupShareBean.setGpInfoId(qualityGroupMineBean.getGpInfoId());
                                        qualityGroupShareBean.setGpRecordId(qualityGroupMineBean.getGpRecordId());
                                        ShareDao.invitePartnerGroup(getActivity(), qualityGroupShareBean, qualityGroupMineBean.getOrderNo());
                                    }
                                    break;
                            }
                            break;
                        case R.id.tv_ql_gp_mine_details:
                            Intent intent = new Intent(QualityGroupShopMineActivity.this, DirectExchangeDetailsActivity.class);
                            intent.putExtra("orderNo", qualityGroupMineBean.getOrderNo());
                            startActivity(intent);
                            break;
                    }
                }
            }
        });

        qualityGroupMineAdapter.setOnItemClickListener((adapter, view, position) -> {
            QualityGroupMineBean qualityGroupMineBean = (QualityGroupMineBean) view.getTag();
            if (qualityGroupMineBean != null) {
                Intent intent = new Intent(QualityGroupShopMineActivity.this, QualityGroupShopDetailActivity.class);
                intent.putExtra("gpInfoId", String.valueOf(qualityGroupMineBean.getGpInfoId()));
                intent.putExtra("productId", String.valueOf(qualityGroupMineBean.getProductId()));
                intent.putExtra("gpRecordId", String.valueOf(qualityGroupMineBean.getGpRecordId()));
                intent.putExtra("orderNo", qualityGroupMineBean.getOrderNo());
                intent.putExtra("gpStatus", String.valueOf(qualityGroupMineBean.getGpStatus()));
                startActivity(intent);
            }
        });
    }


    private void paymentIndent() {
        if (qualityGroupMineBean == null) {
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("no", qualityGroupMineBean.getOrderNo());
        params.put("userId", userId);
        //        2019.1.16 新增银联支付
        params.put("buyType", payWay);
        if (payWay.equals(PAY_UNION_PAY)) {
            params.put("paymentLinkType", 2);
            params.put("isApp", true);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_PAYMENT_INDENT, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                if (payWay.equals(PAY_WX_PAY)) {
                    qualityWeChatIndent = gson.fromJson(result, QualityCreateWeChatPayIndentBean.class);
                    if (qualityWeChatIndent != null) {
                        if (qualityWeChatIndent.getCode().equals(SUCCESS_CODE)) {
                            //返回成功，调起微信支付接口
                            doWXPay(qualityWeChatIndent.getResult().getPayKey());
                        } else {
                            showToast(QualityGroupShopMineActivity.this, qualityWeChatIndent.getResult() == null
                                    ? qualityWeChatIndent.getMsg() : qualityWeChatIndent.getResult().getMsg());
                        }
                    }
                } else if (payWay.equals(PAY_ALI_PAY)) {
                    qualityAliPayIndent = gson.fromJson(result, QualityCreateAliPayIndentBean.class);
                    if (qualityAliPayIndent != null) {
                        if (qualityAliPayIndent.getCode().equals(SUCCESS_CODE)) {
                            //返回成功，调起支付宝支付接口
                            doAliPay(qualityAliPayIndent.getResult().getPayKey());
                        } else {
                            showToast(QualityGroupShopMineActivity.this, qualityWeChatIndent.getResult() == null
                                    ? qualityWeChatIndent.getMsg() : qualityWeChatIndent.getResult().getMsg());
                        }
                    }
                } else if (payWay.equals(PAY_UNION_PAY)) {
                    qualityUnionIndent = gson.fromJson(result, QualityCreateUnionPayIndentEntity.class);
                    if (qualityUnionIndent != null) {
                        if (qualityUnionIndent.getCode().equals(SUCCESS_CODE)) {
                            //返回成功，调起银联支付接口
                            unionPay(qualityUnionIndent);
                        } else {
                            showToast(QualityGroupShopMineActivity.this, qualityUnionIndent.getQualityCreateUnionPayIndent() != null &&
                                    qualityUnionIndent.getQualityCreateUnionPayIndent() != null &&
                                    !TextUtils.isEmpty(qualityUnionIndent.getQualityCreateUnionPayIndent().getMsg()) ?
                                    getStrings(qualityUnionIndent.getQualityCreateUnionPayIndent().getMsg()) :
                                    getStrings(qualityUnionIndent.getMsg()));
                        }
                    }
                }
            }

            @Override
            public void netClose() {
                showToast(QualityGroupShopMineActivity.this, R.string.unConnectedNetwork);
            }
        });
    }

    private void doWXPay(QualityCreateWeChatPayIndentBean.ResultBean.PayKeyBean pay_param) {
        WXPay.init(getApplicationContext());//要在支付前调用
        WXPay.getInstance().doPayDateObject(pay_param, new WXPay.WXPayResultCallBack() {
            @Override
            public void onSuccess() {
//                recordPaySuc();
                showToast(getApplication(), "支付成功");
//                刷新当前页
                loadData();
            }

            @Override
            public void onError(int error_code) {
                switch (error_code) {
                    case WXPay.NO_OR_LOW_WX:
                        showToast(getApplication(), "未安装微信或微信版本过低");
                        break;
                    case WXPay.ERROR_PAY_PARAM:
                        showToast(getApplication(), "参数错误");
                        break;
                    case WXPay.ERROR_PAY:
                        showToast(getApplication(), "支付失败");
                        break;
                }
            }

            @Override
            public void onCancel() {
                showToast(getApplication(), "支付取消");
            }
        });
    }

    private void doAliPay(String pay_param) {
        new AliPay(this, pay_param, new AliPay.AliPayResultCallBack() {
            @Override
            public void onSuccess() {
//                recordPaySuc();
                showToast(getApplication(), "支付成功");
//                刷新当前页
                loadData();
            }

            @Override
            public void onDealing() {
                showToast(getApplication(), "支付处理中...");
            }

            @Override
            public void onError(int error_code) {
                switch (error_code) {
                    case AliPay.ERROR_RESULT:
                        showToast(getApplication(), "支付失败:支付结果解析错误");
                        break;

                    case AliPay.ERROR_NETWORK:
                        showToast(getApplication(), "支付失败:网络连接错误");
                        break;

                    case AliPay.ERROR_PAY:
                        showToast(getApplication(), "支付错误:支付码支付失败");
                        break;

                    default:
                        showToast(getApplication(), "支付错误");
                        break;
                }

            }

            @Override
            public void onCancel() {
                showToast(getApplication(), "支付取消");
            }
        }).doPay();
    }

    /**
     * 银联支付
     *
     * @param qualityUnionIndent
     */
    private void unionPay(@NonNull QualityCreateUnionPayIndentEntity qualityUnionIndent) {
        if (qualityUnionIndent.getQualityCreateUnionPayIndent().getPayKeyBean() != null &&
                !TextUtils.isEmpty(qualityUnionIndent.getQualityCreateUnionPayIndent().getPayKeyBean().getPaymentUrl())) {
            if (loadHud != null) {
                loadHud.show();
            }
            unionPay = new UnionPay(QualityGroupShopMineActivity.this,
                    qualityUnionIndent.getQualityCreateUnionPayIndent().getPayKeyBean().getPaymentUrl(),
                    new UnionPay.UnionPayResultCallBack() {
                        @Override
                        public void onUnionPaySuccess(String webResultValue) {
                            if (loadHud != null) {
                                loadHud.dismiss();
                            }
                            //                recordPaySuc();
                            showToast(getApplication(), "支付成功");
//                刷新当前页
                            loadData();
                        }

                        @Override
                        public void onUnionPayError(String errorMes) {
                            if (loadHud != null) {
                                loadHud.dismiss();
                            }
                            showToast(getApplication(), "支付取消");
                        }
                    });
        } else {
            showToast(QualityGroupShopMineActivity.this, "缺少重要参数，请选择其它支付渠道！");
        }
    }

    /**
     * 七鱼客服记录轨迹
     */
//    private void recordPaySuc() {
//        if (qualityGroupMineBean != null) {
//            Ntalker.getBaseInstance().startAction_paySuccess("支付成功订单", "拼团订单", enterpriseId
//                    , null, getStrings(qualityGroupMineBean.getOrderNo()), getStrings(qualityGroupMineBean.getGpPrice()));
//        }
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            if (requestCode == UNION_RESULT_CODE) {
                return;
            } else {
                finish();
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            NetLoadUtils.getNetInstance().showLoadSirLoading(loadService);
            loadData();
        } else if (requestCode == UNION_RESULT_CODE) {
            if (unionPay != null && qualityUnionIndent.getQualityCreateUnionPayIndent() != null) {
                String webManualFinish = data.getStringExtra("webManualFinish");
                unionPay.unionPayResult(this, qualityUnionIndent.getQualityCreateUnionPayIndent().getNo(), webManualFinish);
            } else {
                showToast("支付取消！");
            }
        }
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void loadData() {
        page = 1;
        getData();
    }

    @Override
    public View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected void getData() {
        if (userId < 1) {
            NetLoadUtils.getNetInstance().showLoadSirLoadFailed(loadService);
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("showCount", TOTAL_COUNT_TEN);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, GROUP_MINE_NEW_INDENT
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        qualityGroupMineAdapter.loadMoreComplete();
                        if (page == 1) {
                            qualityGroupMineList.clear();
                        }
                        Gson gson = new Gson();
                        qualityGroupMineEntity = gson.fromJson(result, QualityGroupMineEntity.class);
                        if (qualityGroupMineEntity != null) {
                            if (qualityGroupMineEntity.getCode().equals(SUCCESS_CODE)) {
                                for (int i = 0; i < qualityGroupMineEntity.getQualityGroupMineBeanList().size(); i++) {
                                    QualityGroupMineBean qualityGroupMineBean = qualityGroupMineEntity.getQualityGroupMineBeanList().get(i);
                                    qualityGroupMineBean.setGpStatusMsg(qualityGroupMineEntity.getStatus()
                                            .get(String.valueOf(qualityGroupMineBean.getGpStatus())));
                                    qualityGroupMineList.add(qualityGroupMineBean);
                                }
                            } else if (qualityGroupMineEntity.getCode().equals(EMPTY_CODE)) {
                                qualityGroupMineAdapter.loadMoreEnd();
                            } else {
                                showToast(QualityGroupShopMineActivity.this, qualityGroupMineEntity.getMsg());
                            }
                            qualityGroupMineAdapter.notifyDataSetChanged();
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, qualityGroupMineList, qualityGroupMineEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        qualityGroupMineAdapter.loadMoreEnd(true);
                        smart_communal_refresh.finishRefresh();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, qualityGroupMineList, qualityGroupMineEntity);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!firstSet) {
            loadData();
            firstSet = false;
        }
    }

    class PopupWindowView {
        @OnClick({R.id.ll_pay_ali
                , R.id.ll_pay_we_chat
                , R.id.ll_pay_union
                , R.id.tv_pay_cancel})
        void clickPayView(View view) {
            mCustomPopWindow.dissmiss();
            switch (view.getId()) {
//                            支付宝
                case R.id.ll_pay_ali:
                    //                调起支付宝支付
                    payWay = PAY_ALI_PAY;
                    paymentIndent();
                    break;
//                            微信
                case R.id.ll_pay_we_chat:
                    payWay = PAY_WX_PAY;
//                调起微信支付
                    paymentIndent();
                    break;
                case R.id.ll_pay_union:
//                    银联支付
                    payWay = PAY_UNION_PAY;
                    paymentIndent();
                    break;
//                            取消
                case R.id.tv_pay_cancel:
                    break;
            }
        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack() {
        finish();
    }
}
