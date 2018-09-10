package com.amkj.dmsh.shopdetails.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseApplication;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.shopdetails.activity.CouponProductActivity;
import com.amkj.dmsh.shopdetails.adapter.DirectMyCouponAdapter;
import com.amkj.dmsh.shopdetails.bean.DirectCouponEntity;
import com.amkj.dmsh.shopdetails.bean.DirectCouponEntity.DirectCouponBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.amkj.dmsh.base.BaseApplication.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/1/23
 * version 3.7
 * class description:优惠券
 */

public class DirectMyCouponFragment extends BaseFragment {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    private String couponStatus;
    private List<DirectCouponBean> couponList = new ArrayList();
    private int page = 1;
    private DirectMyCouponAdapter directMyCouponAdapter;
    private int scrollY;
    private float screenHeight;
    private DirectCouponEntity directCouponEntity;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_smart_refresh_recycler_float_loading;
    }

    @Override
    protected void initViews() {
        if (TextUtils.isEmpty(couponStatus)) {
            return;
        }
        getLoginStatus(this);
        communal_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        communal_recycler.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_twenty_white)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        directMyCouponAdapter = new DirectMyCouponAdapter(couponList, "checkCoupon");
        communal_recycler.setAdapter(directMyCouponAdapter);
        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            //                滚动距离置0
            scrollY = 0;
            loadData();

        });
        directMyCouponAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (page * DEFAULT_TOTAL_COUNT <= directMyCouponAdapter.getItemCount()) {
                    page++;
                    checkCoupon();
                } else {
                    directMyCouponAdapter.loadMoreEnd();
                }
            }
        }, communal_recycler);
        directMyCouponAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DirectCouponBean directCouponBean = (DirectCouponBean) view.getTag();
                if (directCouponBean != null && !TextUtils.isEmpty(directCouponBean.getBeOverdue())
                        && "0".equals(directCouponBean.getBeOverdue())) {
                    if (directCouponBean.getUse_range() == 0) {
                        setSkipPath(getActivity(), directCouponBean.getAndroid_link(), false);
                    } else {
                        Intent intent = new Intent(getActivity(), CouponProductActivity.class);
                        intent.putExtra("userCouponId", String.valueOf(directCouponBean.getId()));
                        startActivity(intent);
                    }
                }
            }
        });
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
    }

    private void checkCoupon() {
        String url = Url.BASE_URL + Url.Q_SHOP_DETAILS_COUPON;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("currentPage", page);
        params.put("type", couponStatus);
        NetLoadUtils.getQyInstance().loadNetDataPost(mAppContext, url, params, new NetLoadUtils.NetLoadListener() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                directMyCouponAdapter.loadMoreComplete();
                if (page == 1) {
                    couponList.clear();
                }
                Gson gson = new Gson();
                directCouponEntity = gson.fromJson(result, DirectCouponEntity.class);
                if (directCouponEntity != null) {
                    if (directCouponEntity.getCode().equals(SUCCESS_CODE)) {
                        couponList.addAll(directCouponEntity.getDirectCouponBeanList());
                        directMyCouponAdapter.notifyDataSetChanged();
                    } else if (!directCouponEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(getActivity(), directCouponEntity.getMsg());
                    }
                }
                NetLoadUtils.getQyInstance().showLoadSir(loadService,couponList, directCouponEntity);
            }

            @Override
            public void netClose() {
                smart_communal_refresh.finishRefresh();
                directMyCouponAdapter.loadMoreComplete();
                NetLoadUtils.getQyInstance().showLoadSir(loadService,couponList, directCouponEntity);
            }

            @Override
            public void onError(Throwable throwable) {
                smart_communal_refresh.finishRefresh();
                directMyCouponAdapter.loadMoreComplete();
                NetLoadUtils.getQyInstance().showLoadSir(loadService,couponList, directCouponEntity);
            }
        });
    }

    @Override
    protected void loadData() {
        page = 1;
        checkCoupon();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals("refresh")) {
            getLoginStatus(this);
            loadData();
        }
    }

    @Override
    protected void getReqParams(Bundle bundle) {
        couponStatus = bundle.getString("couponStatus");
    }
}
