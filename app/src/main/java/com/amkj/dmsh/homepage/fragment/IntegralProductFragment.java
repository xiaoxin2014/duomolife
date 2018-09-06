package com.amkj.dmsh.homepage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.bean.IntegrationProEntity;
import com.amkj.dmsh.bean.IntegrationProEntity.IntegrationBean;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.homepage.adapter.IntegralProductAdapter;
import com.amkj.dmsh.shopdetails.integration.IntegralScrollDetailsActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
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
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    private int page = 1;
    private String integralType;
    private List<IntegrationBean> integrationBeanList = new ArrayList();
    private IntegralProductAdapter integralProductAdapter;
    private IntegrationProEntity integrationProEntity;

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
                getIntegralData();
            }
        }, communal_recycler);
        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            loadData();
        });
    }

    private void getIntegralData() {
        String url = BASE_URL + Url.H_INTEGRAL_PRODUCT_FILTRATE;
        Map<String, Object> params = new HashMap<>();
        params.put("showCount", TOTAL_COUNT_TWENTY);
        params.put("currentPage", page);
//            积分类型.-1为全部,0为纯积分,1为积分+金钱
        params.put("integralType", integralType);
        NetLoadUtils.getQyInstance().loadNetDataPost(getActivity(), url, params, new NetLoadUtils.NetLoadListener() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                smart_communal_refresh.finishRefresh();
                integralProductAdapter.loadMoreComplete();
                integrationProEntity = gson.fromJson(result, IntegrationProEntity.class);
                if (integrationProEntity != null) {
                    if (integrationProEntity.getCode().equals(SUCCESS_CODE)) {
                        if (page == 1) {
                            integrationBeanList.clear();
                        }
                        integrationBeanList.addAll(integrationProEntity.getIntegrationList());
                    } else if (!integrationProEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(getActivity(), integrationProEntity.getMsg());
                    } else {
                        integralProductAdapter.loadMoreEnd();
                    }
                }
                NetLoadUtils.getQyInstance().showLoadSir(loadService,integrationBeanList, integrationProEntity);
                integralProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void netClose() {
                smart_communal_refresh.finishRefresh();
                integralProductAdapter.loadMoreComplete();
                showToast(getActivity(), R.string.unConnectedNetwork);
                NetLoadUtils.getQyInstance().showLoadSir(loadService,integrationBeanList, integrationProEntity);
            }

            @Override
            public void onError(Throwable throwable) {
                smart_communal_refresh.finishRefresh();
                integralProductAdapter.loadMoreComplete();
                showToast(getActivity(), R.string.invalidData);
                NetLoadUtils.getQyInstance().showLoadSir(loadService,integrationBeanList, integrationProEntity);
            }
        });
    }

    @Override
    protected void loadData() {
        page = 1;
        getIntegralData();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected void getReqParams(Bundle bundle) {
        integralType = (String) bundle.get("integralType");
    }
}
