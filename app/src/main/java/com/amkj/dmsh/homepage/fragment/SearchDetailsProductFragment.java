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
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.QualityTypeEntity;
import com.amkj.dmsh.dominant.activity.QualityTypeProductActivity;
import com.amkj.dmsh.dominant.activity.ShopTimeScrollDetailsActivity;
import com.amkj.dmsh.homepage.activity.SearchGoodProMoreActivity;
import com.amkj.dmsh.homepage.adapter.ProNoShopCarAdapter;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.shopdetails.integration.IntegralScrollDetailsActivity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.RemoveExistUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tencent.bugly.beta.tinker.TinkerManager;

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
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.RECOMMEND_SEARCH;
import static com.amkj.dmsh.constant.ConstantVariable.RECOMMEND_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TEN;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_0;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_1;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_2;
import static com.amkj.dmsh.constant.Url.H_HOT_SEARCH_PRODUCT;
import static com.amkj.dmsh.constant.Url.H_SEARCH_PRODUCT_RECOMMEND;
import static com.amkj.dmsh.constant.Url.QUALITY_SHOP_TYPE;
import static com.amkj.dmsh.homepage.activity.HomePageSearchActivity.SEARCH_DATA;

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
    private int scrollY = 0;
    private ProNoShopCarAdapter adapterProduct;
    private float screenHeight;
    private QualityTypeEntity.QualityTypeBean qualityTypeBean;
    private UserLikedProductEntity likedProductEntity;
    private RemoveExistUtils removeExistUtils;
    private String searchDate;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_smart_refresh_recycler_float_loading;
    }

    @Override
    protected void initViews() {
        communal_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_gray_f)


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
                if (likedProductEntity != null && !TextUtils.isEmpty(likedProductEntity.getRecommendFlag())) {
                    intent.putExtra("recommendFlag", likedProductEntity.getRecommendFlag());
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
            if (page * TOTAL_COUNT_TEN <= productSearList.size()) {
                page++;
                getDetailsProduct();
            } else {
                getSameTypeProData();
                adapterProduct.setEnableLoadMore(false);
            }
        }, communal_recycler);
        communal_recycler.setAdapter(adapterProduct);

        smart_communal_refresh.setOnRefreshListener(refreshLayout -> loadData());
        download_btn_communal.attachToRecyclerView(communal_recycler, null, new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrollY += dy;
                if (!recyclerView.canScrollVertically(-1)) {
                    scrollY = 0;
                }
                if (screenHeight == 0) {
                    TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
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
        removeExistUtils = new RemoveExistUtils();
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
        if (message.type.equals(SEARCH_DATA)) {
            String resultText = (String) message.result;
            if (!resultText.equals(searchDate)) {
                page = 1;
                searchDate = resultText;
                NetLoadUtils.getNetInstance().showLoadSirLoading(loadService);
                getDetailsProduct();
            }
        }
    }

    @Override
    protected void getReqParams(Bundle bundle) {
        searchDate = (String) bundle.get(SEARCH_DATA);
    }

    private void getDetailsProduct() {
        if (TextUtils.isEmpty(searchDate)) {
            NetLoadUtils.getNetInstance().showLoadSir(loadService, productSearList, likedProductEntity);
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("keyword", searchDate);
        params.put("currentPage", page);
        params.put("searchType", 1);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), H_HOT_SEARCH_PRODUCT,
                params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        adapterProduct.loadMoreComplete();
                        if (page == 1) {
                            productSearList.clear();
                            proRecommendList.clear();
                            removeExistUtils.clearData();
                        }
                        Gson gson = new Gson();
                        likedProductEntity = gson.fromJson(result, UserLikedProductEntity.class);
                        if (likedProductEntity != null) {
                            if (likedProductEntity.getCode().equals(SUCCESS_CODE)) {
                                productSearList.addAll(removeExistUtils.removeExistList(likedProductEntity.getLikedProductBeanList()));
                            } else if (!likedProductEntity.getCode().equals(EMPTY_CODE)) {
                                showToast(getActivity(), likedProductEntity.getMsg());
                            }
                            setEmptyUI();
                            adapterProduct.notifyDataSetChanged();
                        }
                        NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
                    }

                    @Override
                    public void onNotNetOrException() {
                        NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
                        smart_communal_refresh.finishRefresh();
                        adapterProduct.loadMoreEnd(true);
                    }
                });
    }

    private void setEmptyUI() {
        if (productSearList.size() < 1) {
            LikedProductBean likedProductBean = new LikedProductBean();
            likedProductBean.setItemType(TYPE_2);
            likedProductBean.setTitle(searchDate);
            productSearList.add(likedProductBean);
            getProductType();
        }
    }

    /**
     * 获取商品分类
     */
    private void getProductType() {
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), QUALITY_SHOP_TYPE, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                QualityTypeEntity qualityTypeEntity = gson.fromJson(result, QualityTypeEntity.class);
                if (qualityTypeEntity != null && qualityTypeEntity.getCode().equals(SUCCESS_CODE)
                        && qualityTypeEntity.getQualityTypeBeanList() != null && qualityTypeEntity.getQualityTypeBeanList().size() > 0) {
                    qualityTypeBean = qualityTypeEntity.getQualityTypeBeanList().get(0);
                }
            }
        });
    }

    /**
     * 获取相同类目商品
     */
    private void getSameTypeProData() {
        if (likedProductEntity != null && !TextUtils.isEmpty(likedProductEntity.getNoIds())
                && !TextUtils.isEmpty(likedProductEntity.getCategory_id())) {
            Map<String, Object> params = new HashMap<>();
            params.put("id", getStrings(likedProductEntity.getCategory_id()));
            params.put("noIds", getStrings(likedProductEntity.getNoIds()));
            NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(),H_SEARCH_PRODUCT_RECOMMEND,params,new NetLoadListenerHelper(){
                @Override
                public void onSuccess(String result) {
                    proRecommendList.clear();
                    Gson gson = new Gson();
                    likedProductEntity = gson.fromJson(result, UserLikedProductEntity.class);
                    if (likedProductEntity != null) {
                        if (likedProductEntity.getCode().equals(SUCCESS_CODE)) {
                            if (likedProductEntity.getLikedProductBeanList().size() > 0) {
                                LikedProductBean likedProductBean = new LikedProductBean();
                                likedProductBean.setItemType(TYPE_1);
                                proRecommendList.add(likedProductBean);
                            }
                            proRecommendList.addAll(likedProductEntity.getLikedProductBeanList());
                            productSearList.addAll(removeExistUtils.removeExistList(proRecommendList));
                        } else if (!likedProductEntity.getCode().equals(EMPTY_CODE)) {
                            showToast(getActivity(), likedProductEntity.getMsg());
                        }
                        adapterProduct.notifyDataSetChanged();
                    }
                }

                @Override
                public void onNotNetOrException() {
                    smart_communal_refresh.finishRefresh();
                    adapterProduct.loadMoreEnd(true);
                }
            });
        }
    }
}
