package com.amkj.dmsh.homepage.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.dominant.adapter.GoodProductAdapter;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.H_SEARCH_PRODUCT_GOOD;



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
    private GoodProductAdapter adapterProduct;
    private UserLikedProductEntity likedProduct;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_refresh_recycle_header;
    }

    @Override
    protected void initViews() {
        tl_normal_bar.setSelected(true);
        header_shared.setVisibility(View.INVISIBLE);
        smart_communal_refresh.setOnRefreshListener(refreshLayout -> loadData());
        tv_header_titleAll.setText("好物推荐");
        communal_recycler.setLayoutManager(new GridLayoutManager(SearchGoodProMoreActivity.this, 2));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_gray_f)
                .create());
        adapterProduct = new GoodProductAdapter(SearchGoodProMoreActivity.this, productSearList);
        adapterProduct.setEnableLoadMore(false);
        communal_recycler.setAdapter(adapterProduct);
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

                        likedProduct = GsonUtils.fromJson(result, UserLikedProductEntity.class);
                        if (likedProduct != null) {
                            if (likedProduct.getCode().equals(SUCCESS_CODE)) {
                                productSearList.addAll(likedProduct.getGoodsList());
                            } else if (!likedProduct.getCode().equals(EMPTY_CODE)) {
                                showToast( likedProduct.getMsg());
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
