package com.amkj.dmsh.homepage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseApplication;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.bean.QualityTypeEntity;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.dominant.activity.QualityTypeProductActivity;
import com.amkj.dmsh.dominant.activity.ShopTimeScrollDetailsActivity;
import com.amkj.dmsh.homepage.activity.SearchGoodProMoreActivity;
import com.amkj.dmsh.homepage.adapter.ProNoShopCarAdapter;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.shopdetails.integration.IntegralScrollDetailsActivity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_CHILD;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_ID;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_NAME;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.RECOMMEND_SEARCH;
import static com.amkj.dmsh.constant.ConstantVariable.RECOMMEND_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_0;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_1;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_2;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/27
 * class description:搜索商品
 */
public class SearchDetailsProductFragment extends BaseFragment {
    @BindView(R.id.smart_communal_refresh)
    RefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    //    搜索商品
    private List<LikedProductBean> productSearList = new ArrayList<>();
    //    商品推荐
    private List<LikedProductBean> proRecommendList = new ArrayList<>();
    int page = 1;
    private String data;
    private int scrollY = 0;
    private ProNoShopCarAdapter adapterProduct;
    private float screenHeight;
    private QualityTypeEntity.QualityTypeBean qualityTypeBean;
    private UserLikedProductEntity likedProduct;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_smart_refresh_recycler_float_loading;
    }

    @Override
    protected void initViews() {
        communal_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
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
        adapterProduct = new ProNoShopCarAdapter(getActivity(), productSearList);
        adapterProduct.setOnItemClickListener((adapter, view, position) -> {
            LikedProductBean likedProductBean = (LikedProductBean) view.getTag();
            if (likedProductBean != null) {
                Intent intent = new Intent();
                switch (likedProductBean.getType_id()) {
                    case 0:
                        intent.setClass(getActivity(), ShopTimeScrollDetailsActivity.class);
                        break;
                    case 1:
                        intent.setClass(getActivity(), ShopScrollDetailsActivity.class);
                        break;
                    case 2:
                        intent.setClass(getActivity(), IntegralScrollDetailsActivity.class);
                        break;
                }
                if (likedProduct != null && !TextUtils.isEmpty(likedProduct.getRecommendFlag())) {
                    intent.putExtra("recommendFlag", likedProduct.getRecommendFlag());
                }
                intent.putExtra(RECOMMEND_TYPE, RECOMMEND_SEARCH);
                intent.putExtra("productId", String.valueOf(likedProductBean.getId()));
                startActivity(intent);
            }
        });
        adapterProduct.setSpanSizeLookup((gridLayoutManager, position) -> {
            if (productSearList.size() > 0) {
                return productSearList.get(position).getItemType() == TYPE_0 ? 1 : 2;
            } else {
                return 0;
            }
        });
        adapterProduct.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.tv_search_empty_more:
                    Intent intent = new Intent();
                    if (qualityTypeBean != null) {
                        intent.setClass(getActivity(), QualityTypeProductActivity.class);
                        intent.putExtra(CATEGORY_ID, String.valueOf(qualityTypeBean.getId()));
                        intent.putExtra(CATEGORY_TYPE, String.valueOf(qualityTypeBean.getType()));
                        intent.putExtra(CATEGORY_NAME, getStrings(qualityTypeBean.getName()));
                        if (qualityTypeBean.getChildCategoryList() != null
                                && qualityTypeBean.getChildCategoryList().size() > 0) {
                            intent.putExtra(CATEGORY_CHILD, String.valueOf(qualityTypeBean.getChildCategoryList().get(0).getId()));
                        }
                    } else {
                        intent.setClass(getActivity(), SearchGoodProMoreActivity.class);
                    }
                    startActivity(intent);
                    break;
            }
        });
        adapterProduct.setOnLoadMoreListener(() -> {
            if (page * DEFAULT_TOTAL_COUNT <= productSearList.size()) {
                page++;
                getDetailsProduct();
            } else {
                adapterProduct.setEnableLoadMore(false);
            }
        }, communal_recycler);
        communal_recycler.setAdapter(adapterProduct);

        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            loadData();
        });
        download_btn_communal.attachToRecyclerView(communal_recycler, null, new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrollY += dy;
                if (!recyclerView.canScrollVertically(-1)) {
                    scrollY = 0;
                }
                if (screenHeight == 0) {
                    BaseApplication app = (BaseApplication) getActivity().getApplication();
                    screenHeight = app.getScreenHeight() * 0.5f;
                }
                if (scrollY >= screenHeight) {
                    download_btn_communal.setVisibility(View.VISIBLE);
                } else {
                    download_btn_communal.setVisibility(View.GONE);
                }
            }
        });
        download_btn_communal.setOnClickListener(v -> {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) communal_recycler.getLayoutManager();
            int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
            int mVisibleCount = linearLayoutManager.findLastVisibleItemPosition()
                    - linearLayoutManager.findFirstVisibleItemPosition() + 1;
            if (firstVisibleItemPosition > mVisibleCount) {
                communal_recycler.scrollToPosition(mVisibleCount);
            }
            communal_recycler.smoothScrollToPosition(0);
        });
    }

    //设置显示

    @Override
    protected void loadData() {
        page = 1;
        getDetailsProduct();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals("search0")) {
            String resultText = (String) message.result;
            if (!resultText.equals(data)) {
                page = 1;
                data = resultText;
                getDetailsProduct();
            }
        }
    }

    @Override
    protected void getReqParams(Bundle bundle) {
        String keyWord = (String) bundle.get("data");
        if (!TextUtils.isEmpty(keyWord)) {
            data = keyWord;
        }
    }

    private void getDetailsProduct() {
        if (!TextUtils.isEmpty(data)) {
            String url = Url.BASE_URL + Url.H_HOT_SEARCH_PRODUCT;
            Map<String, Object> params = new HashMap<>();
            params.put("keyword", data);
            params.put("currentPage", page);
            params.put("searchType", 1);
            NetLoadUtils.getQyInstance().loadNetDataPost(getActivity(), url, params, new NetLoadUtils.NetLoadListener() {
                @Override
                public void onSuccess(String result) {
                    smart_communal_refresh.finishRefresh();
                    adapterProduct.loadMoreComplete();
                    Gson gson = new Gson();
                    likedProduct = gson.fromJson(result, UserLikedProductEntity.class);
                    if (likedProduct != null) {
                        if (likedProduct.getCode().equals(SUCCESS_CODE)) {
                            if (page == 1) {
                                productSearList.clear();
                            }
                            productSearList.addAll(likedProduct.getLikedProductBeanList());
                        } else if (!likedProduct.getCode().equals(EMPTY_CODE)) {
                            showToast(getActivity(), likedProduct.getMsg());
                        }
                        setEmptyUI();
                        adapterProduct.notifyDataSetChanged();
                        /**
                         * 限定条件，推荐只能被调用一次
                         */
                        if (DEFAULT_TOTAL_COUNT * page > productSearList.size()
                                && proRecommendList.size() < 1) {
                            getSameTypeProData();
                        }
                    }
                    NetLoadUtils.getQyInstance().showLoadSirSuccess(loadService);
                }

                @Override
                public void netClose() {
                    NetLoadUtils.getQyInstance().showLoadSirSuccess(loadService);
                    smart_communal_refresh.finishRefresh();
                    adapterProduct.loadMoreComplete();
                }

                @Override
                public void onError(Throwable throwable) {
                    NetLoadUtils.getQyInstance().showLoadSirSuccess(loadService);
                    smart_communal_refresh.finishRefresh();
                    adapterProduct.loadMoreComplete();
                }
            });
        }else{
            NetLoadUtils.getQyInstance().showLoadSirSuccess(loadService);
        }
    }

    private void setEmptyUI() {
        if (productSearList.size() < 1) {
            LikedProductBean likedProductBean = new LikedProductBean();
            likedProductBean.setItemType(TYPE_2);
            likedProductBean.setTitle(data);
            productSearList.add(likedProductBean);
            getProductType();
        }
    }

    /**
     * 获取商品分类
     */
    private void getProductType() {
        if (NetWorkUtils.isConnectedByState(getActivity())) {
            String url = Url.BASE_URL + Url.QUALITY_SHOP_TYPE;
            XUtil.Get(url, null, new MyCallBack<String>() {
                        @Override
                        public void onSuccess(String result) {
                            Gson gson = new Gson();
                            QualityTypeEntity qualityTypeEntity = gson.fromJson(result, QualityTypeEntity.class);
                            if (qualityTypeEntity != null && qualityTypeEntity.getCode().equals("01")
                                    && qualityTypeEntity.getQualityTypeBeanList() != null && qualityTypeEntity.getQualityTypeBeanList().size() > 0) {
                                qualityTypeBean = qualityTypeEntity.getQualityTypeBeanList().get(0);
                            }
                        }
                    }
            );
        }
    }

    /**
     * 获取相同类目商品
     */
    private void getSameTypeProData() {
        if (NetWorkUtils.checkNet(getActivity())
                && likedProduct != null && !TextUtils.isEmpty(likedProduct.getNoIds())
                && !TextUtils.isEmpty(likedProduct.getCategory_id())) {
            String url = Url.BASE_URL + Url.H_SEARCH_PRODUCT_RECOMMEND;
            Map<String, Object> params = new HashMap<>();
            params.put("id", getStrings(likedProduct.getCategory_id()));
            params.put("noIds", getStrings(likedProduct.getNoIds()));
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    proRecommendList.clear();
                    Gson gson = new Gson();
                    likedProduct = gson.fromJson(result, UserLikedProductEntity.class);
                    if (likedProduct != null) {
                        if (likedProduct.getCode().equals("01")) {
                            if (likedProduct.getLikedProductBeanList().size() > 0) {
                                LikedProductBean likedProductBean = new LikedProductBean();
                                likedProductBean.setItemType(TYPE_1);
                                proRecommendList.add(likedProductBean);
                            }
                            proRecommendList.addAll(likedProduct.getLikedProductBeanList());
//                            取并集
                            productSearList.removeAll(proRecommendList);
                            productSearList.addAll(proRecommendList);
                        } else if (!likedProduct.getCode().equals("02")) {
                            showToast(getActivity(), likedProduct.getMsg());
                        }
                        adapterProduct.notifyDataSetChanged();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    smart_communal_refresh.finishRefresh();
                    adapterProduct.loadMoreComplete();
                    super.onError(ex, isOnCallback);
                }
            });
        }
    }
}
