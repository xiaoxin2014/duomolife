package com.amkj.dmsh.shopdetails.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseApplication;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.dominant.bean.QualityGroupShareEntity;
import com.amkj.dmsh.dominant.bean.QualityGroupShareEntity.QualityGroupShareBean;
import com.amkj.dmsh.shopdetails.activity.DirectExchangeDetailsActivity;
import com.amkj.dmsh.shopdetails.activity.DirectLogisticsDetailsActivity;
import com.amkj.dmsh.shopdetails.adapter.DoMoIndentListAdapter;
import com.amkj.dmsh.shopdetails.bean.InquiryOrderEntry;
import com.amkj.dmsh.shopdetails.bean.InquiryOrderEntry.OrderInquiryDateEntry.OrderListBean;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.amkj.dmsh.base.BaseApplication.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.CANCEL_PAY_ORDER;
import static com.amkj.dmsh.constant.ConstantVariable.CHECK_LOG;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PRO_STATUS;
import static com.amkj.dmsh.constant.ConstantVariable.INVITE_GROUP;
import static com.amkj.dmsh.constant.ConstantVariable.LITTER_CONSIGN;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

;

/**
 * Created by atd48 on 2016/8/23.
 * 待发货
 */
public class DoMoIndentWaitSendFragment extends BaseFragment {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    List<OrderListBean> orderListBeanList = new ArrayList();
    //根据type类型分类DuomoIndentPayFragment
    private int page = 1;
    private DoMoIndentListAdapter doMoIndentListAdapter;
    private boolean isOnPause;
    private int scrollY = 0;
    private float screenHeight;
    private InquiryOrderEntry inquiryOrderEntry;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_smart_refresh_recycler_float_loading;
    }

    @Override
    protected void initViews() {
        getLoginStatus(this);
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
        doMoIndentListAdapter.setOnClickViewListener(new DoMoIndentListAdapter.OnClickViewListener() {
            @Override
            public void click(String type, OrderListBean orderListBean) {
                Intent intent = new Intent();
                switch (type) {
                    case LITTER_CONSIGN:
                    case CHECK_LOG:
//                        部分发货，查看物流
                        intent.setClass(getActivity(), DirectLogisticsDetailsActivity.class);
                        intent.putExtra("orderNo", orderListBean.getNo());
                        startActivity(intent);
                        break;
                    case CANCEL_PAY_ORDER:
                        intent.setClass(getActivity(), DirectExchangeDetailsActivity.class);
                        intent.putExtra("orderNo", orderListBean.getNo());
                        startActivity(intent);
                        break;
                    case INVITE_GROUP:
                        getInviteGroupInfo(orderListBean.getNo());
                        break;
                }
            }
        });
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

    @Override
    protected void loadData() {
        page = 1;
        getWaitSendData();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getWaitSendData() {
        String url = Url.BASE_URL + Url.Q_INQUIRY_WAIT_SEND;
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("showCount", DEFAULT_TOTAL_COUNT);
        params.put("currentPage", page);
        params.put("orderType", "currency");
        //        版本号控制 3 组合商品赠品
        params.put("version", 3);
        NetLoadUtils.getQyInstance().loadNetDataPost(mAppContext, url, params, new NetLoadUtils.NetLoadListener() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                doMoIndentListAdapter.loadMoreComplete();
                String code = "";
                String msg = "";
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    code = (String) jsonObject.get("code");
                    msg = (String) jsonObject.get("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (code.equals(SUCCESS_CODE)) {
                    if (page == 1) {
                        orderListBeanList.clear();
                    }
                    Gson gson = new Gson();
                    inquiryOrderEntry = gson.fromJson(result, InquiryOrderEntry.class);
                    INDENT_PRO_STATUS = inquiryOrderEntry.getOrderInquiryDateEntry().getStatus();
                    orderListBeanList.addAll(inquiryOrderEntry.getOrderInquiryDateEntry().getOrderList());
                } else if (!code.equals(EMPTY_CODE)) {
                    showToast(getActivity(), msg);
                }
                doMoIndentListAdapter.notifyDataSetChanged();
                NetLoadUtils.getQyInstance().showLoadSir(loadService,code);
            }

            @Override
            public void netClose() {
                smart_communal_refresh.finishRefresh();
                doMoIndentListAdapter.loadMoreComplete();
                NetLoadUtils.getQyInstance().showLoadSir(loadService,inquiryOrderEntry);
            }

            @Override
            public void onError(Throwable throwable) {
                smart_communal_refresh.finishRefresh();
                doMoIndentListAdapter.loadMoreComplete();
                NetLoadUtils.getQyInstance().showLoadSir(loadService,inquiryOrderEntry);
            }
        });
    }

    /**
     * 邀请参团获取信息
     *
     * @param no
     */
    private void getInviteGroupInfo(String no) {
        String url = Url.BASE_URL + Url.GROUP_MINE_SHARE;
        if (NetWorkUtils.checkNet(getActivity())) {
            if (loadHud != null) {
                loadHud.show();
            }
            Map<String, Object> params = new HashMap<>();
            params.put("orderNo", no);
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    if (loadHud != null) {
                        loadHud.dismiss();
                    }
                    Gson gson = new Gson();
                    QualityGroupShareEntity qualityGroupShareEntity = gson.fromJson(result, QualityGroupShareEntity.class);
                    if (qualityGroupShareEntity != null) {
                        if (qualityGroupShareEntity.getCode().equals("01")) {
                            QualityGroupShareBean qualityGroupShareBean = qualityGroupShareEntity.getQualityGroupShareBean();
                            invitePartnerGroup(qualityGroupShareBean);
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    if (loadHud != null) {
                        loadHud.dismiss();
                    }
                    showToast(getActivity(), R.string.do_failed);
                }
            });
        } else {
            showToast(getActivity(), R.string.unConnectedNetwork);
        }
    }

    /**
     * 邀请参团
     *
     * @param qualityGroupShareBean 参团信息
     */
    private void invitePartnerGroup(@NonNull QualityGroupShareBean qualityGroupShareBean) {
        new UMShareAction(getActivity()
                , qualityGroupShareBean.getGpPicUrl()
                , qualityGroupShareBean.getName()
                , getStrings(qualityGroupShareBean.getSubtitle())
                , Url.BASE_SHARE_PAGE_TWO + "m/template/share_template/groupShare.html?id=" + qualityGroupShareBean.getGpInfoId()
                + "&record=" + qualityGroupShareBean.getGpRecordId());
    }
}
