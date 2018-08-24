package com.amkj.dmsh.homepage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.bean.IntegrationProEntity;
import com.amkj.dmsh.bean.IntegrationProEntity.IntegrationBean;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.homepage.adapter.IntegralProductAdapter;
import com.amkj.dmsh.shopdetails.integration.IntegralScrollDetailsActivity;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.constant.Url.BASE_URL;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/8/4
 * version 3.1.5
 * class description:积分商品
 */
public class IntegralProductFragment extends BaseFragment {
    @BindView(R.id.smart_communal_refresh)
    RefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.communal_load)
    View communal_load;
    @BindView(R.id.communal_error)
    View communal_error;
    @BindView(R.id.communal_empty)
    View communal_empty;
    private int page = 1;
    private String integralType;
    private List<IntegrationBean> integrationBeanList = new ArrayList();
    private IntegralProductAdapter integralProductAdapter;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_smart_refresh_recycler_float_loading;
    }

    @Override
    protected void initViews() {
        if (TextUtils.isEmpty(integralType)) {
            return;
        }
        communal_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
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
        integralProductAdapter = new IntegralProductAdapter(getActivity(), integrationBeanList);
        communal_recycler.setAdapter(integralProductAdapter);
        integralProductAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                IntegrationBean integrationBean = (IntegrationBean) view.getTag();
                if (integrationBean != null) {
                    Intent intent = new Intent(getActivity(), IntegralScrollDetailsActivity.class);
                    intent.putExtra("productId", String.valueOf(integrationBean.getId()));
                    startActivity(intent);
                }
            }
        });
        integralProductAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                loadData();
            }
        }, communal_recycler);
        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            page = 1;
            loadData();
        });
    }

    @Override
    protected void loadData() {
        if (NetWorkUtils.checkNet(getActivity())) {
            String url = BASE_URL + Url.H_INTEGRAL_PRODUCT_FILTRATE;
            Map<String, Object> params = new HashMap<>();
            params.put("showCount", TOTAL_COUNT_TWENTY);
            params.put("currentPage", page);
//            积分类型.-1为全部,0为纯积分,1为积分+金钱
            params.put("integralType", integralType);
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    if (page == 1) {
                        integrationBeanList.clear();
                    }
                    smart_communal_refresh.finishRefresh();
                    integralProductAdapter.loadMoreComplete();
                    IntegrationProEntity integrationProEntity = gson.fromJson(result, IntegrationProEntity.class);
                    if (integrationProEntity != null) {
                        if (integrationProEntity.getCode().equals("01")) {
                            integrationBeanList.addAll(integrationProEntity.getIntegrationList());
                        } else if (!integrationProEntity.getCode().equals("02")) {
                            showToast(getActivity(), integrationProEntity.getMsg());
                        } else {
                            integralProductAdapter.loadMoreEnd();
                        }
                    }
                    integralProductAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    integralProductAdapter.loadMoreComplete();
                    showToast(getActivity(), R.string.invalidData);
                    super.onError(ex, isOnCallback);
                }
            });
        } else {
            smart_communal_refresh.finishRefresh();
            integralProductAdapter.loadMoreComplete();
            if (page == 1) {
                communal_load.setVisibility(View.GONE);
                communal_error.setVisibility(View.VISIBLE);
            }
            showToast(getActivity(), R.string.unConnectedNetwork);
        }
    }

    @Override
    protected void getReqParams(Bundle bundle) {
        integralType = (String) bundle.get("integralType");
    }
}
