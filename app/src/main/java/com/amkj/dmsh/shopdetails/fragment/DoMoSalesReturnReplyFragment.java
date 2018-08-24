package com.amkj.dmsh.shopdetails.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseApplication;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.shopdetails.activity.DoMoRefundDetailActivity;
import com.amkj.dmsh.shopdetails.adapter.DirectSalesReturnRecordAdapter;
import com.amkj.dmsh.shopdetails.bean.DirectReturnRecordEntity;
import com.amkj.dmsh.shopdetails.bean.DirectReturnRecordEntity.DirectReturnRecordBean.OrderListBean;
import com.amkj.dmsh.utils.inteface.MyCallBack;
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
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PRO_STATUS;
import static com.amkj.dmsh.constant.ConstantVariable.REFUND_REPAIR;
import static com.amkj.dmsh.constant.ConstantVariable.REFUND_TYPE;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/2/23
 * class description:退货申诉
 */

public class DoMoSalesReturnReplyFragment extends BaseFragment {
    @BindView(R.id.smart_communal_refresh)
    RefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    @BindView(R.id.communal_load)
    View communal_load;
    @BindView(R.id.communal_error)
    View communal_error;
    @BindView(R.id.communal_empty)
    View communal_empty;
    private List<OrderListBean> orderListBeanList = new ArrayList();
    private int page = 1;
    private DirectSalesReturnRecordAdapter directSalesReturnListAdapter;
    //    reply 退货申诉 record 申诉记录
    private boolean isOnPause;
    private int scrollY = 0;
    private float screenHeight;
    private int uid;
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
        directSalesReturnListAdapter = new DirectSalesReturnRecordAdapter(getActivity(), orderListBeanList);
        communal_recycler.setAdapter(directSalesReturnListAdapter);

        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
                //                滚动距离置0
                scrollY = 0;
                page = 1;
                loadData();
        });
        directSalesReturnListAdapter.setOnLoadMoreListener(() -> {
            if (page * DEFAULT_TOTAL_COUNT <= directSalesReturnListAdapter.getItemCount()) {
                page++;
                loadData();
            } else {
                directSalesReturnListAdapter.loadMoreEnd();
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

        directSalesReturnListAdapter.setOnItemClickListener((adapter, view, position) -> {
            OrderListBean orderListBean = (OrderListBean) view.getTag();
            if (orderListBean != null) {
                Intent intent = new Intent(getActivity(), DoMoRefundDetailActivity.class);
                if(50 <= orderListBean.getStatus() && orderListBean.getStatus() <= 58){
                    intent.putExtra(REFUND_TYPE, REFUND_REPAIR);
                }else{
                    intent.putExtra(REFUND_TYPE, REFUND_TYPE);
                }
                intent.putExtra("no", orderListBean.getNo());
                intent.putExtra("orderProductId", String.valueOf(orderListBean.getOrderProductId()));
                intent.putExtra("orderRefundProductId", String.valueOf(orderListBean.getOrderRefundProductId()));
                startActivity(intent);
            }
        });
        directSalesReturnListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            OrderListBean orderListBean = (OrderListBean) view.getTag();
            if (orderListBean != null) {
                Intent intent = new Intent(getActivity(), DoMoRefundDetailActivity.class);
                if(50 <= orderListBean.getStatus() && orderListBean.getStatus() <= 58){
                    intent.putExtra(REFUND_TYPE, REFUND_REPAIR);
                }else{
                    intent.putExtra(REFUND_TYPE, REFUND_TYPE);
                }
                intent.putExtra("no", orderListBean.getNo());
                intent.putExtra("orderProductId", String.valueOf(orderListBean.getOrderProductId()));
                intent.putExtra("orderRefundProductId", String.valueOf(orderListBean.getOrderRefundProductId()));
                startActivity(intent);
            }
        });
        communal_load.setVisibility(View.VISIBLE);
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

    //设置显示
    //第一个设置
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setUserVisibleHint(true);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void loadData() {
        String url = Url.BASE_URL + Url.Q_APPLY_AFTER_SALE_REPLY_RECORD;
        Map<String, Object> params = new HashMap<>();
        params.put("userId", uid);
        params.put("showCount", DEFAULT_TOTAL_COUNT);
        params.put("currentPage", page);
        XUtil.Post(url, params, new MyCallBack<String>() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        communal_load.setVisibility(View.GONE);
                        communal_error.setVisibility(View.GONE);
                        directSalesReturnListAdapter.loadMoreComplete();
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
                            DirectReturnRecordEntity inquiryOrderEntry = gson.fromJson(result, DirectReturnRecordEntity.class);
                            INDENT_PRO_STATUS = inquiryOrderEntry.getDirectReturnRecordBean().getStatus();
                            orderListBeanList.addAll(inquiryOrderEntry.getDirectReturnRecordBean().getOrderList());
                        } else if (!code.equals("02")) {
                            showToast(getActivity(), msg);
                        }
                        if (page == 1) {
                            directSalesReturnListAdapter.setNewData(orderListBeanList);
                        } else {
                            directSalesReturnListAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        smart_communal_refresh.finishRefresh();
                        communal_load.setVisibility(View.GONE);
                        communal_error.setVisibility(View.VISIBLE);
                        directSalesReturnListAdapter.loadMoreComplete();
                        super.onError(ex, isOnCallback);
                    }
                }

        );
    }

    public void onPause() {
        super.onPause();
        isOnPause = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isOnPause) {
            loadData();
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
