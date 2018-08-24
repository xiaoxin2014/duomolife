package com.amkj.dmsh.shopdetails.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.dominant.activity.ShopTimeScrollDetailsActivity;
import com.amkj.dmsh.shopdetails.adapter.ShopRecommendHotTopicAdapter;
import com.amkj.dmsh.shopdetails.bean.ShopRecommendHotTopicEntity;
import com.amkj.dmsh.shopdetails.bean.ShopRecommendHotTopicEntity.ShopRecommendHotTopicBean;
import com.amkj.dmsh.shopdetails.integration.IntegralScrollDetailsActivity;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.PRO_COMMENT;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_1;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/1/2
 * version 3.0.6
 * class description:更多推荐 商品 专题
 */

public class ProRecommendActivity extends BaseActivity {
    @BindView(R.id.tv_header_shared)
    TextView header_shared;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tl_normal_bar)
    Toolbar tl_normal_bar;
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
    private String id;
    private String recommendType;
    private List<ShopRecommendHotTopicBean> proRecommendBeans = new ArrayList<>();
    private ShopRecommendHotTopicAdapter shopRecommendHotTopicAdapter;
    private ShopRecommendHotTopicEntity recommendHotTopicEntity;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_refresh_recycle_header;
    }

    @Override
    protected void initViews() {
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        recommendType = intent.getStringExtra("recommendType");
        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(recommendType)) {
            showToast(this, R.string.unConnectedNetwork);
            finish();
            return;
        }
        tl_normal_bar.setSelected(true);
        header_shared.setVisibility(View.INVISIBLE);
        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
           loadData();
        });
        if(PRO_COMMENT.equals(recommendType)){
//            商品
            communal_recycler.setLayoutManager(new GridLayoutManager(ProRecommendActivity.this,2));
            tv_header_titleAll.setText("同类商品推荐");
        }else{
//            专题
            communal_recycler.setLayoutManager(new LinearLayoutManager(ProRecommendActivity.this));
            tv_header_titleAll.setText("相关专题推荐");
        }
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
        shopRecommendHotTopicAdapter = new ShopRecommendHotTopicAdapter(ProRecommendActivity.this, proRecommendBeans);
        communal_recycler.setAdapter(shopRecommendHotTopicAdapter);
        shopRecommendHotTopicAdapter.setEnableLoadMore(false);
        shopRecommendHotTopicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ShopRecommendHotTopicBean shopRecommendHotTopicBean = (ShopRecommendHotTopicBean) view.getTag();
                if (shopRecommendHotTopicBean != null) {
                    switch (shopRecommendHotTopicBean.getItemType()) {
//                        专题
                        case TYPE_1:
                            setSkipPath(ProRecommendActivity.this
                                    , getStrings(shopRecommendHotTopicBean.getAndroidLink()), false);
                            break;
                        default:
                            skipProDetail(shopRecommendHotTopicBean);
                            break;
                    }
                }
            }
        });
    }

    @Override
    protected void loadData() {
        if(PRO_COMMENT.equals(recommendType)){
//            商品
            getDoMoRecommend();
        }else{
//            专题
            getTopicRecommend();
        }
    }
    /**
     * 获取推荐商品
     *
     */
    private void getDoMoRecommend() {
        if (NetWorkUtils.checkNet(ProRecommendActivity.this)) {
            String url = Url.BASE_URL + Url.Q_SP_DETAIL_RECOMMEND;
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    smart_communal_refresh.finishRefresh();
                    communal_load.setVisibility(View.GONE);
                    communal_error.setVisibility(View.GONE);
                    proRecommendBeans.clear();
                    recommendHotTopicEntity = ShopRecommendHotTopicEntity.objectFromData(result);
                    if (recommendHotTopicEntity != null) {
                        if (recommendHotTopicEntity.getCode().equals("01")) {
                            proRecommendBeans.addAll(recommendHotTopicEntity.getShopRecommendHotTopicList());
                        }else if(!recommendHotTopicEntity.getCode().equals("02")){
                            communal_error.setVisibility(View.VISIBLE);
                            showToast(ProRecommendActivity.this, recommendHotTopicEntity.getMsg());
                        }
                    }
                    shopRecommendHotTopicAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    super.onError(ex, isOnCallback);
                    communal_error.setVisibility(View.VISIBLE);
                    smart_communal_refresh.finishRefresh();
                    showToast(ProRecommendActivity.this, R.string.unConnectedNetwork);
                }
            });
        }
    }

    /**
     * 获取推荐主题
     *
     */
    private void getTopicRecommend() {
        if (NetWorkUtils.checkNet(ProRecommendActivity.this)) {
            String url = Url.BASE_URL + Url.Q_SP_DETAIL_TOPIC_RECOMMEND;
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    smart_communal_refresh.finishRefresh();
                    communal_load.setVisibility(View.GONE);
                    communal_error.setVisibility(View.GONE);
                    proRecommendBeans.clear();
                    ShopRecommendHotTopicEntity recommendHotTopicEntity = ShopRecommendHotTopicEntity.objectFromData(result);
                    if (recommendHotTopicEntity != null) {
                        if (recommendHotTopicEntity.getCode().equals("01")) {
                            for (ShopRecommendHotTopicBean shopRecommendHotTopicBean:recommendHotTopicEntity.getShopRecommendHotTopicList()) {
                                shopRecommendHotTopicBean.setItemType(TYPE_1);
                                proRecommendBeans.add(shopRecommendHotTopicBean);
                            }
                        }else if(!recommendHotTopicEntity.getCode().equals("02")){
                            communal_error.setVisibility(View.VISIBLE);
                            showToast(ProRecommendActivity.this, recommendHotTopicEntity.getMsg());
                        }
                    }
                    shopRecommendHotTopicAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    super.onError(ex, isOnCallback);
                    communal_error.setVisibility(View.VISIBLE);
                    smart_communal_refresh.finishRefresh();
                    showToast(ProRecommendActivity.this, R.string.unConnectedNetwork);
                }
            });
        }
    }

    /**
     * 推荐跳转商品详情
     */
    private void skipProDetail(ShopRecommendHotTopicBean shopRecommendHotTopicBean) {
        Intent intent = new Intent();
        switch (shopRecommendHotTopicBean.getType_id()) {
            case 0:
                intent.setClass(ProRecommendActivity.this, ShopTimeScrollDetailsActivity.class);
                break;
            case 2:
                intent.setClass(ProRecommendActivity.this, IntegralScrollDetailsActivity.class);
                break;
            default:
                intent.setClass(ProRecommendActivity.this, ShopScrollDetailsActivity.class);
                break;
        }
        if(recommendHotTopicEntity!=null&&!TextUtils.isEmpty(recommendHotTopicEntity.getRecommendFlag())){
            intent.putExtra("recommendFlag", recommendHotTopicEntity.getRecommendFlag());
        }
        intent.putExtra("productId", String.valueOf(shopRecommendHotTopicBean.getId()));
        startActivity(intent);
    }
    @OnClick({R.id.rel_communal_error, R.id.communal_empty})
    void refreshData() {
        communal_load.setVisibility(View.VISIBLE);
        communal_error.setVisibility(View.GONE);
        loadData();
    }

    @OnClick(R.id.tv_life_back)
    void goBack() {
        finish();
    }

}
