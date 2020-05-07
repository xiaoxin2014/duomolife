package com.amkj.dmsh.shopdetails.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.CouponProductActivity;
import com.amkj.dmsh.shopdetails.adapter.DirectMyCouponAdapter;
import com.amkj.dmsh.shopdetails.bean.DirectCouponEntity;
import com.amkj.dmsh.shopdetails.bean.DirectCouponEntity.DirectCouponBean;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;


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
    private List<DirectCouponBean> couponList = new ArrayList<>();
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
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_twenty_white).create());
        directMyCouponAdapter = new DirectMyCouponAdapter(couponList, "checkCoupon");
        communal_recycler.setAdapter(directMyCouponAdapter);
        smart_communal_refresh.setOnRefreshListener(refreshLayout -> {
            //                滚动距离置0
            scrollY = 0;
            loadData();
        });
        directMyCouponAdapter.setOnLoadMoreListener(() -> {
            page++;
            checkCoupon();
        }, communal_recycler);
        directMyCouponAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DirectCouponBean directCouponBean = (DirectCouponBean) view.getTag();
                if (directCouponBean != null && !TextUtils.isEmpty(directCouponBean.getBeOverdue())
                        && "0".equals(directCouponBean.getBeOverdue())) {
                    //全场券
                    if (directCouponBean.getUse_range() == 0) {
                        setSkipPath(getActivity(), directCouponBean.getAndroid_link(), false);
                    } else {
                        //指定券
                        Intent intent = new Intent(getActivity(), CouponProductActivity.class);
                        intent.putExtra("userCouponId", String.valueOf(directCouponBean.getId()));
                        startActivity(intent);
                    }
                }
            }
        });
        setFloatingButton(download_btn_communal, communal_recycler);
    }

    private void checkCoupon() {
        String url = Url.Q_SHOP_DETAILS_COUPON;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        params.put("currentPage", page);
        params.put("type", couponStatus);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                if (page == 1) {
                    couponList.clear();
                }

                directCouponEntity = GsonUtils.fromJson(result, DirectCouponEntity.class);
                if (directCouponEntity != null) {
                    if (directCouponEntity.getCode().equals(SUCCESS_CODE)) {
                        couponList.addAll(directCouponEntity.getDirectCouponBeanList());
                        directMyCouponAdapter.loadMoreComplete();
                    } else if (!directCouponEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(directCouponEntity.getMsg());
                        directMyCouponAdapter.loadMoreFail();
                    } else {
                        directMyCouponAdapter.loadMoreEnd();
                    }
                }else {
                    directMyCouponAdapter.loadMoreEnd();
                }
                directMyCouponAdapter.notifyDataSetChanged();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, couponList, directCouponEntity);
            }

            @Override
            public void onNotNetOrException() {
                smart_communal_refresh.finishRefresh();
                directMyCouponAdapter.loadMoreEnd(true);
                NetLoadUtils.getNetInstance().showLoadSir(loadService, couponList, directCouponEntity);
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
