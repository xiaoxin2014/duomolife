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
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.shopdetails.activity.CouponProductActivity;
import com.amkj.dmsh.shopdetails.adapter.DirectMyCouponAdapter;
import com.amkj.dmsh.shopdetails.bean.DirectCouponEntity;
import com.amkj.dmsh.shopdetails.bean.DirectCouponEntity.DirectCouponBean;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.amkj.dmsh.constant.ConstantMethod.getPersonalInfo;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;

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
    private String couponStatus;
    private List<DirectCouponBean> couponList = new ArrayList();
    private int page = 1;
    private DirectMyCouponAdapter directMyCouponAdapter;
    private int uid;
    private int scrollY;
    private float screenHeight;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_smart_refresh_recycler_float_loading;
    }

    @Override
    protected void initViews() {
        if(TextUtils.isEmpty(couponStatus)){
            return;
        }
        getLoginStatus();
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
        directMyCouponAdapter = new DirectMyCouponAdapter(couponList,"checkCoupon");
        communal_recycler.setAdapter(directMyCouponAdapter);
        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
                //                滚动距离置0
                scrollY = 0;
                page = 1;
                loadData();

        });
        directMyCouponAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (page * DEFAULT_TOTAL_COUNT <= directMyCouponAdapter.getItemCount()) {
                    page++;
                    loadData();
                } else {
                    directMyCouponAdapter.loadMoreEnd();
                }
            }
        }, communal_recycler);
        directMyCouponAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DirectCouponBean directCouponBean = (DirectCouponBean) view.getTag();
                if(directCouponBean!=null&&!TextUtils.isEmpty(directCouponBean.getBeOverdue())
                        && "0".equals(directCouponBean.getBeOverdue())){
                    if(directCouponBean.getUse_range() == 0){
                        setSkipPath(getActivity(),directCouponBean.getAndroid_link(),false);
                    }else{
                        Intent intent = new Intent(getActivity(),CouponProductActivity.class);
                        intent.putExtra("userCouponId",String.valueOf(directCouponBean.getId()));
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
        communal_load.setVisibility(View.VISIBLE);
    }

    private void getLoginStatus() {
        SavePersonalInfoBean personalInfo = getPersonalInfo(getActivity());
        if (personalInfo.isLogin()) {
            uid = personalInfo.getUid();
        }
    }

    private void checkCoupon() {
        String url = Url.BASE_URL + Url.Q_SHOP_DETAILS_COUPON;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", uid);
        params.put("currentPage", page);
        params.put("type",couponStatus);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                directMyCouponAdapter.loadMoreComplete();
                communal_load.setVisibility(View.GONE);
                communal_error.setVisibility(View.GONE);
                if (page == 1) {
                    couponList.clear();
                }
                Gson gson = new Gson();
                DirectCouponEntity directCouponEntity = gson.fromJson(result, DirectCouponEntity.class);
                if (directCouponEntity != null) {
                    if (directCouponEntity.getCode().equals("01")) {
                        couponList.addAll(directCouponEntity.getDirectCouponBeanList());
                        if (page == 1) {
                            directMyCouponAdapter.setNewData(couponList);
                        } else {
                            directMyCouponAdapter.notifyDataSetChanged();
                        }
                    } else if (!directCouponEntity.getCode().equals("02")) {
                        showToast(getActivity(), directCouponEntity.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (page == 1 && directMyCouponAdapter.getItemCount() < 1) {
                    communal_load.setVisibility(View.GONE);
                    communal_error.setVisibility(View.VISIBLE);
                }
                smart_communal_refresh.finishRefresh();
                directMyCouponAdapter.loadMoreComplete();
                super.onError(ex, isOnCallback);
            }
        });
    }

    @Override
    protected void loadData() {
        if (uid > 0) {
            checkCoupon();
        }
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if(message.type.equals("refresh")){
            getLoginStatus();
            loadData();
        }
    }

    @Override
    protected void getReqParams(Bundle bundle) {
        couponStatus = bundle.getString("couponStatus");
    }
}
