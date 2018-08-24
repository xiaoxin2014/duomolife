package com.amkj.dmsh.shopdetails.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseApplication;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.release.dialogutils.AlertSettingBean;
import com.amkj.dmsh.release.dialogutils.AlertView;
import com.amkj.dmsh.release.dialogutils.OnAlertItemClickListener;
import com.amkj.dmsh.shopdetails.activity.DirectExchangeDetailsActivity;
import com.amkj.dmsh.shopdetails.adapter.DoMoIndentListAdapter;
import com.amkj.dmsh.shopdetails.bean.InquiryOrderEntry;
import com.amkj.dmsh.shopdetails.bean.InquiryOrderEntry.OrderInquiryDateEntry.OrderListBean;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.amkj.dmsh.constant.ConstantMethod.getPersonalInfo;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.CANCEL_ORDER;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PRO_STATUS;
import static com.amkj.dmsh.constant.ConstantVariable.PAY;

;

/**
 * Created by atd48 on 2016/8/23.
 * 待支付
 */
public class DoMoIndentWaitPayFragment extends BaseFragment implements OnAlertItemClickListener {
    @BindView(R.id.smart_communal_refresh)
    RefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    @BindView(R.id.communal_load)
    View communal_load;
    @BindView(R.id.communal_error)
    View communal_error;
    @BindView(R.id.communal_empty)
    View communal_empty;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    List<OrderListBean> orderListBeanList = new ArrayList();
    //根据type类型分类DuomoIndentPayFragment
    private int page = 1;
    private int uid;
    private AlertView cancelOrderDialog;
    private OrderListBean orderBean;
    private boolean isOnPause;
    private DoMoIndentListAdapter doMoIndentListAdapter;
    private int scrollY = 0;
    private float screenHeight;
    @Override
    protected int getContentView() {
        return R.layout.layout_communal_smart_refresh_recycler_float_loading;
    }
    @Override
    protected void initViews() {
        getLoginStatus();
        communal_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        communal_recycler.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        doMoIndentListAdapter = new DoMoIndentListAdapter(getActivity(), orderListBeanList);
        communal_recycler.setAdapter(doMoIndentListAdapter);

        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
                //                滚动距离置0
                scrollY = 0;
                page = 1;
                loadData();
        });
        doMoIndentListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (page * DEFAULT_TOTAL_COUNT <= doMoIndentListAdapter.getItemCount()) {
                    page++;
                    loadData();
                } else {
                    doMoIndentListAdapter.loadMoreEnd();
                }
            }
        }, communal_recycler);
        download_btn_communal.attachToRecyclerView(communal_recycler, null, new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(-1)) {
                    scrollY = 0;
                }
                scrollY += dy;
                if (screenHeight == 0) {
                    BaseApplication app = (BaseApplication) getActivity().getApplication();
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
        doMoIndentListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                InquiryOrderEntry.OrderInquiryDateEntry.OrderListBean orderListBean = (InquiryOrderEntry.OrderInquiryDateEntry.OrderListBean) view.getTag();
                if (orderListBean != null) {
                    Intent intent = new Intent(getActivity(), DirectExchangeDetailsActivity.class);
                    intent.putExtra("orderNo", orderListBean.getNo());
                    intent.putExtra("uid", orderListBean.getUserId());
                    startActivity(intent);
                }
            }
        });
        doMoIndentListAdapter.setOnClickViewListener(new DoMoIndentListAdapter.OnClickViewListener() {
            @Override
            public void click(String type, InquiryOrderEntry.OrderInquiryDateEntry.OrderListBean orderListBean) {
                orderBean = orderListBean;
                Intent intent = new Intent();
                switch (type) {
                    case CANCEL_ORDER:
//                        取消订单
                        AlertSettingBean alertSettingBean = new AlertSettingBean();
                        AlertSettingBean.AlertData alertData = new AlertSettingBean.AlertData();
                        alertData.setCancelStr("取消");
                        alertData.setDetermineStr("确定");
                        alertData.setFirstDet(true);
                        alertData.setMsg("确定要取消当前订单？");
                        alertSettingBean.setStyle(AlertView.Style.Alert);
                        alertSettingBean.setAlertData(alertData);
                        cancelOrderDialog = new AlertView(alertSettingBean, getActivity(), DoMoIndentWaitPayFragment.this);
                        cancelOrderDialog.setCancelable(true);
                        cancelOrderDialog.show();
                        break;
                    case PAY:
                        intent.setClass(getActivity(), DirectExchangeDetailsActivity.class);
                        intent.putExtra("orderNo", orderListBean.getNo());
                        intent.putExtra("uid", orderListBean.getUserId());
                        startActivity(intent);
                        break;
                }
            }
        });
        communal_load.setVisibility(View.VISIBLE);
    }


    @Override
    protected void loadData() {
        String url = Url.BASE_URL + Url.Q_INQUIRY_WAIT_PAY;
        Map<String, Object> params = new HashMap<>();
        params.put("userId", uid);
        params.put("showCount", 10);
        params.put("currentPage", page);
        //        版本号控制 3 组合商品赠品
        params.put("version", 3);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                communal_load.setVisibility(View.GONE);
                communal_error.setVisibility(View.GONE);
                doMoIndentListAdapter.loadMoreComplete();
                if (page == 1) {
                    orderListBeanList.clear();
                }
                String code = "";
                String msg = "";
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    code = (String) jsonObject.get("code");
                    msg = (String) jsonObject.get("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (code.equals("01")) {
                    Gson gson = new Gson();
                    InquiryOrderEntry inquiryOrderEntry = gson.fromJson(result, InquiryOrderEntry.class);
                    InquiryOrderEntry.OrderInquiryDateEntry orderInquiryDateEntry = inquiryOrderEntry.getOrderInquiryDateEntry();
                    if (!TextUtils.isEmpty(orderInquiryDateEntry.getCurrentTime())) {
                        for (int i = 0; i < orderInquiryDateEntry.getOrderList().size(); i++) {
                            OrderListBean orderListBean = inquiryOrderEntry.getOrderInquiryDateEntry().getOrderList().get(i);
                            orderListBean.setCurrentTime(orderInquiryDateEntry.getCurrentTime());
                        }
                    }
                    INDENT_PRO_STATUS = inquiryOrderEntry.getOrderInquiryDateEntry().getStatus();
                    orderListBeanList.addAll(inquiryOrderEntry.getOrderInquiryDateEntry().getOrderList());
                } else if (!code.equals("02")) {
                    showToast(getActivity(), msg);
                }
                if (page == 1) {
                    doMoIndentListAdapter.setNewData(orderListBeanList);
                } else {
                    doMoIndentListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                smart_communal_refresh.finishRefresh();
                communal_load.setVisibility(View.GONE);
                communal_error.setVisibility(View.VISIBLE);
                doMoIndentListAdapter.loadMoreComplete();
                super.onError(ex, isOnCallback);
            }
        });
    }

    private void getLoginStatus() {
        SavePersonalInfoBean personalInfo = getPersonalInfo(getActivity());
        if (personalInfo.isLogin()) {
            uid = personalInfo.getUid();
        } else {
            //未登录跳转登录页
            Intent intent = new Intent(getActivity(), MineLoginActivity.class);
            startActivityForResult(intent, ConstantVariable.IS_LOGIN_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantVariable.IS_LOGIN_CODE) {
            getLoginStatus();
        }
    }

    //  取消订单
    private void cancelOrder() {
        String url = Url.BASE_URL + Url.Q_INDENT_CANCEL;
        Map<String, Object> params = new HashMap<>();
        params.put("no", orderBean.getNo());
        params.put("userId", uid);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals("01")) {
                        showToast(getActivity(), requestStatus.getMsg());
                        loadData();
                    } else {
                        showToast(getActivity(), requestStatus.getResult() != null ?
                                requestStatus.getResult().getMsg() : requestStatus.getMsg());
                    }
                }
            }
        });
    }

    public void onPause() {
        isOnPause = true;
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isOnPause) {
            loadData();
        }
    }

    @Override
    public void onAlertItemClick(Object o, int position) {
        if (o == cancelOrderDialog && position != AlertView.CANCELPOSITION) {
            cancelOrder();
        }
    }
    @OnClick({R.id.rel_communal_error, R.id.communal_empty})
    void refreshData(View view) {
        communal_load.setVisibility(View.VISIBLE);
        communal_empty.setVisibility(View.GONE);
        communal_error.setVisibility(View.GONE);
        page=1;
        loadData();
    }
}
