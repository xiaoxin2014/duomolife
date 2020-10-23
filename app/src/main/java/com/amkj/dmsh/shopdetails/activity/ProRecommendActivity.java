package com.amkj.dmsh.shopdetails.activity;

import android.content.Intent;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.time.activity.ShopTimeScrollDetailsActivity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.adapter.ShopRecommendHotTopicAdapter;
import com.amkj.dmsh.shopdetails.bean.ShopRecommendHotTopicEntity;
import com.amkj.dmsh.shopdetails.bean.ShopRecommendHotTopicEntity.ShopRecommendHotTopicBean;
import com.amkj.dmsh.shopdetails.integration.IntegralScrollDetailsActivity;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.PRO_COMMENT;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_1;
import static com.amkj.dmsh.constant.Url.Q_SP_DETAIL_RECOMMEND;

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
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
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
            showToast( R.string.unConnectedNetwork);
            finish();
            return;
        }
        tl_normal_bar.setSelected(true);
        header_shared.setVisibility(View.INVISIBLE);
        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData();
            }
        });
        if (PRO_COMMENT.equals(recommendType)) {
//            商品
            communal_recycler.setLayoutManager(new GridLayoutManager(ProRecommendActivity.this, 2));
            tv_header_titleAll.setText("同类商品推荐");
        } else {
//            专题
            communal_recycler.setLayoutManager(new LinearLayoutManager(ProRecommendActivity.this));
            tv_header_titleAll.setText("相关专题推荐");
        }
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp)


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
        if (PRO_COMMENT.equals(recommendType)) {
//            商品
            getDoMoRecommend();
        } else {
//            专题
            getTopicRecommend();
        }
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return smart_communal_refresh;
    }

    /**
     * 获取推荐商品
     */
    private void getDoMoRecommend() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_SP_DETAIL_RECOMMEND, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                proRecommendBeans.clear();
                recommendHotTopicEntity = ShopRecommendHotTopicEntity.objectFromData(result);
                if (recommendHotTopicEntity != null) {
                    if (recommendHotTopicEntity.getCode().equals(SUCCESS_CODE)) {
                        proRecommendBeans.addAll(recommendHotTopicEntity.getShopRecommendHotTopicList());
                    } else if (!recommendHotTopicEntity.getCode().equals(EMPTY_CODE)) {
                        showToast( recommendHotTopicEntity.getMsg());
                    }
                }
                shopRecommendHotTopicAdapter.notifyDataSetChanged();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, proRecommendBeans, recommendHotTopicEntity);
            }

            @Override
            public void onNotNetOrException() {
                smart_communal_refresh.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, proRecommendBeans, recommendHotTopicEntity);
            }

            @Override
            public void onError(Throwable throwable) {
                showToast( R.string.invalidData);
            }
        });
    }

    /**
     * 获取推荐主题
     */
    private void getTopicRecommend() {
        String url =  Url.Q_SP_DETAIL_TOPIC_RECOMMEND;
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                proRecommendBeans.clear();
                recommendHotTopicEntity = ShopRecommendHotTopicEntity.objectFromData(result);
                if (recommendHotTopicEntity != null) {
                    if (recommendHotTopicEntity.getCode().equals(SUCCESS_CODE)) {
                        for (ShopRecommendHotTopicBean shopRecommendHotTopicBean : recommendHotTopicEntity.getShopRecommendHotTopicList()) {
                            shopRecommendHotTopicBean.setItemType(TYPE_1);
                            proRecommendBeans.add(shopRecommendHotTopicBean);
                        }
                    } else if (!recommendHotTopicEntity.getCode().equals(EMPTY_CODE)) {
                        showToast( recommendHotTopicEntity.getMsg());
                    }
                }
                shopRecommendHotTopicAdapter.notifyDataSetChanged();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, proRecommendBeans, recommendHotTopicEntity);
            }

            @Override
            public void onNotNetOrException() {
                smart_communal_refresh.finishRefresh();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, proRecommendBeans, recommendHotTopicEntity);
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(R.string.invalidData);
            }
        });
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
        if (recommendHotTopicEntity != null && !TextUtils.isEmpty(recommendHotTopicEntity.getRecommendFlag())) {
            intent.putExtra("recommendFlag", recommendHotTopicEntity.getRecommendFlag());
        }
        intent.putExtra("productId", String.valueOf(shopRecommendHotTopicBean.getId()));
        startActivity(intent);
    }

    @OnClick(R.id.tv_life_back)
    void goBack() {
        finish();
    }
}
