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
import com.amkj.dmsh.dominant.activity.ShopTimeScrollDetailsActivity;
import com.amkj.dmsh.homepage.adapter.ProNoShopCarAdapter;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.shopdetails.integration.IntegralScrollDetailsActivity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.H_SEARCH_PRODUCT_GOOD;

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
        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData();
            }
        });
        tv_header_titleAll.setText("好物推荐");
        communal_recycler.setLayoutManager(new GridLayoutManager(SearchGoodProMoreActivity.this, 2));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_gray_f)
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
                    if (likedProduct != null && !TextUtils.isEmpty(likedProduct.getRecommendFlag())) {
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
        NetLoadUtils.getNetInstance().loadNetDataPost(SearchGoodProMoreActivity.this, H_SEARCH_PRODUCT_GOOD,
                null, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        productSearList.clear();
                        Gson gson = new Gson();
                        likedProduct = gson.fromJson(result, UserLikedProductEntity.class);
                        if (likedProduct != null) {
                            if (likedProduct.getCode().equals(SUCCESS_CODE)) {
                                productSearList.addAll(likedProduct.getGoodsList());
                            } else if (!likedProduct.getCode().equals(EMPTY_CODE)) {
                                showToast(SearchGoodProMoreActivity.this, likedProduct.getMsg());
                            }
                        }
                        adapterProduct.notifyDataSetChanged();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, productSearList, likedProduct);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, productSearList, likedProduct);
                    }
                });
    }

    @OnClick(R.id.tv_life_back)
    void goBack() {
        finish();
    }


    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    public View getLoadView() {
        return smart_communal_refresh;
    }
}
