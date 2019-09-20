package com.amkj.dmsh.homepage.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.AllSearchEntity;
import com.amkj.dmsh.homepage.adapter.ProNoShopCarAdapter;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.skipProductUrl;
import static com.amkj.dmsh.constant.ConstantVariable.ALL_SEARCH_KEY;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TEN;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_0;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_1;
import static com.amkj.dmsh.constant.Url.H_HOT_SEARCH_ALL;
import static com.amkj.dmsh.constant.Url.H_SEARCH_PRODUCT_RECOMMEND;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/27
 * class description:搜索商品
 */
public class SearchDetailsProductNewFragment extends BaseSearchDetailFragment {
    //    搜索商品
    private List<LikedProductBean> productSearList = new ArrayList<>();
    //    商品推荐
    private List<LikedProductBean> proRecommendList = new ArrayList<>();
    private ProNoShopCarAdapter adapterProduct;

    @Override
    protected void initRv() {
        communal_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_gray_f)
                .create());
        adapterProduct = new ProNoShopCarAdapter(getActivity(), productSearList);
        adapterProduct.setOnItemClickListener((adapter, view, position) -> {
            LikedProductBean likedProductBean = (LikedProductBean) view.getTag();
            if (likedProductBean != null) {
                skipProductUrl(getActivity(), likedProductBean.getType_id(), likedProductBean.getId());
            }
        });
        adapterProduct.setSpanSizeLookup((gridLayoutManager, position) -> {
            if (productSearList.size() > 0) {
                return productSearList.get(position).getItemType() == TYPE_0 ? 1 : 2;
            } else {
                return 0;
            }
        });

        adapterProduct.setOnLoadMoreListener(() -> {
            if (page * TOTAL_COUNT_TEN <= productSearList.size()) {
                page++;
                getSearchDetail();
            } else {
                getSameTypeProData();
            }
        }, communal_recycler);
        communal_recycler.setAdapter(adapterProduct);
    }

    //获取商品搜索结果
    protected void getSearchDetail() {
        if (TextUtils.isEmpty(getKeywords())) return;
        Map<String, Object> params = new HashMap<>();
        params.put("keyword", getKeywords());
        params.put("searchType", getSearchType());
        params.put("currentPage", page);
        params.put("showCount", TOTAL_COUNT_TEN);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), H_HOT_SEARCH_ALL,
                params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        searchSucess = true;
                        smart_communal_refresh.setVisibility(View.VISIBLE);
                        mNestedScrollview.setVisibility(View.GONE);
                        smart_communal_refresh.finishRefresh();
                        adapterProduct.loadMoreComplete();
                        if (page == 1) {
                            productSearList.clear();
                            proRecommendList.clear();
                            removeExistUtils.clearData();
                        }
                        Gson gson = new Gson();
                        allSearchEntity = gson.fromJson(result, AllSearchEntity.class);
                        if (allSearchEntity != null && allSearchEntity.getSearchBean() != null) {
                            searchBean = allSearchEntity.getSearchBean();
                            if (allSearchEntity.getCode().equals(SUCCESS_CODE)) {
                                productSearList.addAll(removeExistUtils.removeExistList(searchBean.getGoodsList()));
                            } else if (!allSearchEntity.getCode().equals(EMPTY_CODE)) {
                                showToast(getActivity(), allSearchEntity.getMsg());
                            }
                        }
                        adapterProduct.notifyDataSetChanged();
                        setEmptyCallback();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, productSearList, allSearchEntity);
                        updateSearchNum();
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.setVisibility(View.VISIBLE);
                        mNestedScrollview.setVisibility(View.GONE);
                        smart_communal_refresh.finishRefresh();
                        adapterProduct.loadMoreEnd();
                        setEmptyCallback();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, productSearList, allSearchEntity);
                    }
                });
    }

    @Override
    protected String getSearchKey() {
        return ALL_SEARCH_KEY;
    }

    /**
     * 获取相同类目商品
     */
    private void getSameTypeProData() {
        if (searchBean != null && !TextUtils.isEmpty(searchBean.getNoIds())
                && !TextUtils.isEmpty(searchBean.getCategoryId())) {
            Map<String, Object> params = new HashMap<>();
            params.put("id", getStrings(searchBean.getCategoryId()));
            params.put("noIds", getStrings(searchBean.getNoIds()));
            NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), H_SEARCH_PRODUCT_RECOMMEND, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    adapterProduct.loadMoreEnd();
                    proRecommendList.clear();
                    Gson gson = new Gson();
                    UserLikedProductEntity likedProductEntity = gson.fromJson(result, UserLikedProductEntity.class);
                    if (likedProductEntity != null) {
                        if (likedProductEntity.getCode().equals(SUCCESS_CODE)) {
                            if (likedProductEntity.getGoodsList().size() > 0) {
                                LikedProductBean likedProductBean = new LikedProductBean();
                                likedProductBean.setItemType(TYPE_1);
                                proRecommendList.add(likedProductBean);
                            }
                            proRecommendList.addAll(likedProductEntity.getGoodsList());
                            productSearList.addAll(removeExistUtils.removeExistList(proRecommendList));
                        } else if (!likedProductEntity.getCode().equals(EMPTY_CODE)) {
                            showToast(getActivity(), likedProductEntity.getMsg());
                        }
                        adapterProduct.notifyDataSetChanged();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, productSearList, allSearchEntity);
                    }
                }

                @Override
                public void onNotNetOrException() {
                    smart_communal_refresh.finishRefresh();
                    adapterProduct.loadMoreEnd();
                    NetLoadUtils.getNetInstance().showLoadSir(loadService, productSearList, allSearchEntity);
                }
            });
        }
    }
}
