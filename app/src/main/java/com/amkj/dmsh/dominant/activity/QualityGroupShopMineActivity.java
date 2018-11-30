package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.dominant.adapter.QualityGroupMineAdapter;
import com.amkj.dmsh.dominant.bean.QualityGroupMineEntity;
import com.amkj.dmsh.dominant.bean.QualityGroupMineEntity.QualityGroupMineBean;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.DirectExchangeDetailsActivity;
import com.amkj.dmsh.shopdetails.alipay.AliPay;
import com.amkj.dmsh.shopdetails.bean.QualityCreateAliPayIndentBean;
import com.amkj.dmsh.shopdetails.bean.QualityCreateWeChatPayIndentBean;
import com.amkj.dmsh.shopdetails.weixin.WXPay;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.views.CustomPopWindow;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.bugly.beta.tinker.TinkerManager;
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
import static com.amkj.dmsh.constant.ConstantVariable.PAY_WX_PAY;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;

;

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
    private int scrollY;
    private float screenHeight;
    private List<QualityGroupMineBean> qualityGroupMineList = new ArrayList<>();
    private QualityGroupMineAdapter qualityGroupMineAdapter;
    private String payWay;
    private CustomPopWindow mCustomPopWindow;
    private QualityGroupMineBean qualityGroupMineBean;
    private QualityCreateWeChatPayIndentBean qualityWeChatIndent;
    private QualityCreateAliPayIndentBean qualityAliPayIndent;
    private QualityGroupMineEntity qualityGroupMineEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_ql_gp_sp_list;
    }

    @Override
    protected void initViews() {
        getLoginStatus(QualityGroupShopMineActivity.this);
        tv_header_shared.setVisibility(View.GONE);
        tv_header_titleAll.setText("我的拼团");

        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
            loadData();
        }});
        communal_recycler.setLayoutManager(new LinearLayoutManager(QualityGroupShopMineActivity.this));
        qualityGroupMineAdapter = new QualityGroupMineAdapter(QualityGroupShopMineActivity.this, qualityGroupMineList);
        communal_recycler.setAdapter(qualityGroupMineAdapter);
        qualityGroupMineAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (page * TOTAL_COUNT_TWENTY <= qualityGroupMineList.size()) {
                    page++;
                    getData();
                } else {
                    qualityGroupMineAdapter.loadMoreEnd();
                    qualityGroupMineAdapter.setEnableLoadMore(false);
                }
            }
        }, communal_recycler);
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp)






                .create());
        download_btn_communal.attachToRecyclerView(communal_recycler, null, new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrollY += dy;
                if (!recyclerView.canScrollVertically(-1)) {
                    scrollY = 0;
                }
                if (screenHeight == 0) {
                    TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
                    screenHeight = app.getScreenHeight();
                }
                if (scrollY >= screenHeight) {
                    download_btn_communal.setVisibility(View.VISIBLE);
                } else {
                    download_btn_communal.setVisibility(View.GONE);
                }
            }
        });
        download_btn_communal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) communal_recycler.getLayoutManager();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                int mVisibleCount = linearLayoutManager.findLastVisibleItemPosition()
                        - linearLayoutManager.findFirstVisibleItemPosition() + 1;
                if (firstVisibleItemPosition > mVisibleCount) {
                    communal_recycler.scrollToPosition(mVisibleCount);
                }
                communal_recycler.smoothScrollToPosition(0);
            }
        });
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
                                    inviteFriendJoinGroup(qualityGroupMineBean);
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

        qualityGroupMineAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                QualityGroupMineBean qualityGroupMineBean = (QualityGroupMineBean) view.getTag();
                if (qualityGroupMineBean != null && (qualityGroupMineBean.getGpStatus() == 1 || qualityGroupMineBean.getGpStatus() == 2)) {
                    Intent intent = new Intent(QualityGroupShopMineActivity.this, QualityGroupShopDetailActivity.class);
                    intent.putExtra("gpInfoId", String.valueOf(qualityGroupMineBean.getGpInfoId()));
                    intent.putExtra("gpRecordId", String.valueOf(qualityGroupMineBean.getGpRecordId()));
                    intent.putExtra("orderNo", qualityGroupMineBean.getOrderNo());
                    intent.putExtra("invitePartnerJoin", true);
                    startActivity(intent);
                } else {
                    showToast(QualityGroupShopMineActivity.this, "已结束拼团");
                }
            }
        });
    }

    private void inviteFriendJoinGroup(QualityGroupMineBean qualityGroupMineBean) {
        if (qualityGroupMineBean != null) {
            new UMShareAction(QualityGroupShopMineActivity.this
                    , qualityGroupMineBean.getGpPicUrl()
                    , qualityGroupMineBean.getName()
                    , getStrings(qualityGroupMineBean.getSubtitle())
                    , Url.BASE_SHARE_PAGE_TWO + "m/template/share_template/groupShare.html?id=" + qualityGroupMineBean.getGpInfoId()
                    + "&record=" + qualityGroupMineBean.getGpRecordId(), "pages/groupshare/groupshare?id=" + qualityGroupMineBean.getGpInfoId()
                    + (TextUtils.isEmpty(qualityGroupMineBean.getOrderNo()) ? "&gpRecordId=" + qualityGroupMineBean.getGpRecordId() : "&order=" + qualityGroupMineBean.getOrderNo()));
        }
    }

    private void paymentIndent() {
        String url = Url.BASE_URL + Url.Q_PAYMENT_INDENT;
        if (qualityGroupMineBean != null) {
            Map<String, Object> params = new HashMap<>();
            params.put("no", qualityGroupMineBean.getOrderNo());
            params.put("userId", userId);
            params.put("buyType", payWay);
            XUtil.Post(url, params, new MyCallBack<String>() {
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
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    showToast(QualityGroupShopMineActivity.this, R.string.unConnectedNetwork);
                    super.onError(ex, isOnCallback);
                }
            });
        }
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
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            NetLoadUtils.getQyInstance().showLoadSirLoading(loadService);
            loadData();
        }
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void loadData() {
        page = 1;
        getData();
    }

    @Override
    protected View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected void getData() {
        if (userId < 1) {
            return;
        }
        String url = Url.BASE_URL + Url.GROUP_MINE_INDENT;
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("showCount", TOTAL_COUNT_TWENTY);
        params.put("uid", userId);
        NetLoadUtils.getQyInstance().loadNetDataPost(this, url
                , params, new NetLoadUtils.NetLoadListener() {
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
                            } else if (!qualityGroupMineEntity.getCode().equals(EMPTY_CODE)) {
                                showToast(QualityGroupShopMineActivity.this, qualityGroupMineEntity.getMsg());
                            }
                            qualityGroupMineAdapter.notifyDataSetChanged();
                        }
                        NetLoadUtils.getQyInstance().showLoadSir(loadService, qualityGroupMineList, qualityGroupMineEntity);
                    }

                    @Override
                    public void netClose() {
                        qualityGroupMineAdapter.loadMoreComplete();
                        smart_communal_refresh.finishRefresh();
                        showToast(QualityGroupShopMineActivity.this, R.string.unConnectedNetwork);
                        NetLoadUtils.getQyInstance().showLoadSir(loadService, qualityGroupMineList, qualityGroupMineEntity);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        smart_communal_refresh.finishRefresh();
                        qualityGroupMineAdapter.loadMoreComplete();
                        showToast(QualityGroupShopMineActivity.this, R.string.connectedFaile);
                        NetLoadUtils.getQyInstance().showLoadSir(loadService, qualityGroupMineList, qualityGroupMineEntity);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    class PopupWindowView {
        @OnClick({R.id.ll_pay_ali, R.id.iv_pay_icon_ali, R.id.tv_pay_text_ali
                , R.id.ll_pay_we_chat, R.id.iv_pay_icon_we_chat, R.id.tv_pay_text_we_chat
                , R.id.tv_pay_cancel})
        void clickPayView(View view) {
            mCustomPopWindow.dissmiss();
            switch (view.getId()) {
//                            支付宝
                case R.id.ll_pay_ali:
                case R.id.iv_pay_icon_ali:
                case R.id.tv_pay_text_ali:
                    //                调起支付宝支付
                    payWay = PAY_ALI_PAY;
                    paymentIndent();
                    break;
//                            微信
                case R.id.ll_pay_we_chat:
                case R.id.iv_pay_icon_we_chat:
                case R.id.tv_pay_text_we_chat:
                    payWay = PAY_WX_PAY;
//                调起微信支付
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
