package com.amkj.dmsh.homepage.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.activity.ShopTimeScrollDetailsActivity;
import com.amkj.dmsh.homepage.adapter.ProNoShopCarAdapter;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.shopdetails.integration.IntegralScrollDetailsActivity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

;



/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/1/4
 * version 3.6
 * class description:搜索好物推荐
 */

public class SearchGoodProMoreActivity extends BaseActivity {
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
    //    搜索商品
    private List<LikedProductBean> productSearList = new ArrayList<>();
    private ProNoShopCarAdapter adapterProduct;
    private UserLikedProductEntity likedProduct;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_refresh_recycle_header;
    }

    @Override
    protected void initViews() {
        tl_normal_bar.setSelected(true);
        header_shared.setVisibility(View.INVISIBLE);
        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
           loadData();
        });
        tv_header_titleAll.setText("好物推荐");
        communal_recycler.setLayoutManager(new GridLayoutManager(SearchGoodProMoreActivity.this, 2));
        communal_recycler.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_product)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        adapterProduct = new ProNoShopCarAdapter(SearchGoodProMoreActivity.this, productSearList);
        adapterProduct.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LikedProductBean likedProductBean = (LikedProductBean) view.getTag();
                if (likedProductBean != null) {
                    Intent intent = new Intent();
                    switch (likedProductBean.getType_id()) {
                        case 0:
                            intent.setClass(SearchGoodProMoreActivity.this, ShopTimeScrollDetailsActivity.class);
                            break;
                        case 1:
                            intent.setClass(SearchGoodProMoreActivity.this, ShopScrollDetailsActivity.class);
                            break;
                        case 2:
                            intent.setClass(SearchGoodProMoreActivity.this, IntegralScrollDetailsActivity.class);
                            break;
                    }
                    if(likedProduct!=null&&!TextUtils.isEmpty(likedProduct.getRecommendFlag())){
                        intent.putExtra("recommendFlag", likedProduct.getRecommendFlag());
                    }
                    intent.putExtra("productId", String.valueOf(likedProductBean.getId()));
                    startActivity(intent);
                }
            }
        });
        adapterProduct.setEnableLoadMore(false);
    }

    @Override
    protected void loadData() {
        getSearchGoodPro();
    }

    /**
     * 获取推荐商品
     */
    private void getSearchGoodPro() {
        String url = Url.BASE_URL + Url.H_SEARCH_PRODUCT_GOOD;
        NetLoadUtils.getQyInstance().loadNetDataPost(SearchGoodProMoreActivity.this, url,
                null, new NetLoadUtils.NetLoadListener() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                productSearList.clear();
                Gson gson = new Gson();
                likedProduct = gson.fromJson(result, UserLikedProductEntity.class);
                if (likedProduct != null) {
                    if (likedProduct.getCode().equals(SUCCESS_CODE)) {
                        productSearList.addAll(likedProduct.getLikedProductBeanList());
                    } else if (!likedProduct.getCode().equals(EMPTY_CODE)) {
                        showToast(SearchGoodProMoreActivity.this, likedProduct.getMsg());
                    }
                }
                NetLoadUtils.getQyInstance().showLoadSir(loadService,productSearList,likedProduct);
                adapterProduct.notifyDataSetChanged();
            }

            @Override
            public void netClose() {
                smart_communal_refresh.finishRefresh();
                showToast(SearchGoodProMoreActivity.this, R.string.unConnectedNetwork);
                NetLoadUtils.getQyInstance().showLoadSir(loadService,productSearList,likedProduct);
            }

            @Override
            public void onError(Throwable throwable) {
                smart_communal_refresh.finishRefresh();
                showToast(SearchGoodProMoreActivity.this, R.string.unConnectedNetwork);
                NetLoadUtils.getQyInstance().showLoadSir(loadService,productSearList,likedProduct);
            }
        });
    }

    @OnClick(R.id.tv_life_back)
    void goBack() {
        finish();
    }


}
