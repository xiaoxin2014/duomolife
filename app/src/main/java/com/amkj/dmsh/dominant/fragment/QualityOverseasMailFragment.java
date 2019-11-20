package com.amkj.dmsh.dominant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.bean.DMLThemeEntity;
import com.amkj.dmsh.bean.DMLThemeEntity.DMLThemeBean;
import com.amkj.dmsh.bean.DMLThemeEntity.DMLThemeBean.DMLGoodsBean;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.adapter.GoodProductAdapter;
import com.amkj.dmsh.dominant.adapter.QualityOsMailHeaderAdapter;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.RemoveExistUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static com.amkj.dmsh.constant.ConstantMethod.getShowNumber;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_ID;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.constant.Url.QUALITY_OVERSEAS_LIST;
import static com.amkj.dmsh.constant.Url.Q_QUALITY_TYPE_AD;

;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/29
 * class description:海外直邮
 */

public class QualityOverseasMailFragment extends BaseFragment {
    @BindView(R.id.smart_communal_refresh)
    RefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //
    private List<LikedProductBean> typeDetails = new ArrayList();
    private GoodProductAdapter qualityTypeProductAdapter;
    private int themePage = 1;
    private int productPage = 1;
    private List<DMLThemeBean> themeList = new ArrayList();
    private List<CommunalADActivityBean> adBeanList = new ArrayList<>();
    //    头部
    private QualityOsMailHeaderAdapter qualityOsMailHeaderAdapter;
    private OverseasHeaderView overseasHeaderView;
    private boolean isLoadThemeData = true;
    private boolean isLoadProData = true;
    private String categoryId;
    private String categoryType;
    private View headerView;
    private CBViewHolderCreator cbViewHolderCreator;
    private DMLThemeEntity dmlThemeEntity;
    private RemoveExistUtils removeExistUtils;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_smart_refresh_recycler_float_loading;
    }

    @Override
    protected void initViews() {
        communal_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        headerView = LayoutInflater.from(getActivity())
                .inflate(R.layout.layout_overseas_mail_header, (ViewGroup) communal_recycler.getParent(), false);
        overseasHeaderView = new OverseasHeaderView();
        ButterKnife.bind(overseasHeaderView, headerView);
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp).create());

        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData();
            }
        });
        qualityTypeProductAdapter = new GoodProductAdapter(getActivity(), typeDetails);
        communal_recycler.setAdapter(qualityTypeProductAdapter);
        qualityTypeProductAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (isLoadProData || isLoadThemeData) {
                    if (isLoadProData && isLoadThemeData) {
                        themePage++;
                        productPage++;
                        getOverseasThemeData();
                        getOverseasProData();
                    } else if (isLoadThemeData) {
                        themePage++;
                        getOverseasThemeData();
                    } else {
                        productPage++;
                        getOverseasProData();
                    }
                } else {
                    qualityTypeProductAdapter.loadMoreEnd();
                }
            }
        }, communal_recycler);
        overseasHeaderView.communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(getActivity()));
        overseasHeaderView.communal_recycler_wrap.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_gray_f)
                .create());
        qualityOsMailHeaderAdapter = new QualityOsMailHeaderAdapter(getActivity(), themeList, "overseas");
        overseasHeaderView.communal_recycler_wrap.setAdapter(qualityOsMailHeaderAdapter);
        removeExistUtils = new RemoveExistUtils();
    }

    //    海外直邮商品列表
    private void getOverseasProData() {
        Map<String, Object> params = new HashMap<>();
        params.put("showCount", TOTAL_COUNT_TWENTY);
        params.put("currentPage", productPage);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), QUALITY_OVERSEAS_LIST, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                qualityTypeProductAdapter.loadMoreComplete();
                if (productPage == 1) {
                    //重新加载数据
                    typeDetails.clear();
                    removeExistUtils.clearData();
                }
                Gson gson = new Gson();
                UserLikedProductEntity likedProductEntity = gson.fromJson(result, UserLikedProductEntity.class);
                if (likedProductEntity != null) {
                    if (likedProductEntity.getCode().equals(SUCCESS_CODE)) {
                        typeDetails.addAll(removeExistUtils.removeExistList(likedProductEntity.getGoodsList()));
                        qualityTypeProductAdapter.notifyDataSetChanged();
                    } else if (likedProductEntity.getCode().equals(EMPTY_CODE)) {
                        isLoadProData = false;
                        qualityTypeProductAdapter.loadMoreEnd();
                    } else {
                        qualityTypeProductAdapter.loadMoreEnd();
                        showToast(getActivity(), likedProductEntity.getMsg());
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                qualityTypeProductAdapter.loadMoreEnd(true);
            }
        });
    }

    //    海外直邮主题商品列表
    private void getOverseasThemeData() {
        String url =  Url.QUALITY_OVERSEAS_THEME;
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", themePage);
        params.put("showCount", TOTAL_COUNT_TWENTY);
        params.put("goodsCurrentPage", 1);
        params.put("goodsShowCount", 8);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), url
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        qualityTypeProductAdapter.loadMoreComplete();
                        Gson gson = new Gson();
                        dmlThemeEntity = gson.fromJson(result, DMLThemeEntity.class);
                        if (dmlThemeEntity != null) {
                            if (dmlThemeEntity.getCode().equals(SUCCESS_CODE)) {
                                if (themePage == 1) {
                                    themeList.clear();
                                }
                                for (int i = 0; i < dmlThemeEntity.getThemeList().size(); i++) {
                                    DMLThemeBean dmlThemeBean = dmlThemeEntity.getThemeList().get(i);
                                    List<DMLGoodsBean> dmlGoodsBeanList = dmlThemeBean.getGoods();
                                    if (dmlGoodsBeanList != null && dmlGoodsBeanList.size() > 7) {
                                        DMLGoodsBean dmlGoodsBean = new DMLGoodsBean();
                                        dmlGoodsBean.setItemType(ConstantVariable.TYPE_1);
                                        dmlGoodsBean.setId(dmlThemeBean.getId());
                                        dmlGoodsBeanList.add(dmlGoodsBean);
                                        dmlThemeBean.setGoods(dmlGoodsBeanList);
                                    }
                                    themeList.add(dmlThemeBean);
                                }
                                qualityOsMailHeaderAdapter.notifyDataSetChanged();
                            } else if (dmlThemeEntity.getCode().equals(EMPTY_CODE)) {
                                isLoadThemeData = false;
                                if (themePage == 1) {
                                    overseasHeaderView.communal_recycler_wrap.setVisibility(View.GONE);
                                } else {
                                    overseasHeaderView.communal_recycler_wrap.setVisibility(View.VISIBLE);
                                }
                            } else {
                                showToast(getActivity(), dmlThemeEntity.getMsg());
                            }
                            addRemoveHeaderView(themeList);
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, themeList, dmlThemeEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        qualityTypeProductAdapter.loadMoreEnd(true);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, themeList, dmlThemeEntity);
                    }

                    @Override
                    public void netClose() {
                        showToast(getActivity(), R.string.unConnectedNetwork);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        addRemoveHeaderView(themeList);
                        showToast(getActivity(), R.string.invalidData);
                    }
                });
    }

    /**
     * 新增移除头部布局
     */
    private void addRemoveHeaderView(List list) {
        if (list.size() > 0) {
            if (headerView != null) {
                if (headerView.getParent() == null) {
                    qualityTypeProductAdapter.addHeaderView(headerView);
                }
            }
        } else {
            if (headerView != null && headerView.getParent() == null) {
                qualityTypeProductAdapter.removeHeaderView(headerView);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            getOverseasProData();
        }
    }

    @Override
    protected void loadData() {
        themePage = 1;
        productPage = 1;
        isLoadThemeData = true;
        isLoadProData = true;
        getOverseasThemeData();
        getOverseasProData();
        getAdTypeData();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getAdTypeData() {
        Map<String, Object> params = new HashMap<>();
        params.put("categoryType", categoryType);
        params.put("categoryId", categoryId);
        params.put("vidoShow", "1");
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(),Q_QUALITY_TYPE_AD,params,new NetLoadListenerHelper(){
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                Gson gson = new Gson();
                adBeanList.clear();
                CommunalADActivityEntity qualityAdLoop = gson.fromJson(result, CommunalADActivityEntity.class);
                if (qualityAdLoop != null) {
                    if (qualityAdLoop.getCode().equals(SUCCESS_CODE)) {
                        adBeanList.addAll(qualityAdLoop.getCommunalADActivityBeanList());
                        overseasHeaderView.rel_communal_banner.setVisibility(View.VISIBLE);
                        if (cbViewHolderCreator == null) {
                            cbViewHolderCreator = new CBViewHolderCreator() {
                                @Override
                                public Holder createHolder(View itemView) {
                                    return new CommunalAdHolderView(itemView, getActivity(), true);
                                }

                                @Override
                                public int getLayoutId() {
                                    return R.layout.layout_ad_image_video;
                                }
                            };
                        }
                        overseasHeaderView.ad_communal_banner.setPages(getActivity(), cbViewHolderCreator, adBeanList).setCanLoop(true)
                                .setPointViewVisible(true).setPageIndicator(new int[]{R.drawable.unselected_radius, R.drawable.selected_radius})
                                .startTurning(getShowNumber(adBeanList.get(0).getShowTime()) * 1000);
                    } else {
                        if (adBeanList.size() < 1) {
                            overseasHeaderView.rel_communal_banner.setVisibility(View.GONE);
                        }
                    }
                    addRemoveHeaderView(adBeanList);
                }
            }

            @Override
            public void onNotNetOrException() {
                smart_communal_refresh.finishRefresh();
                addRemoveHeaderView(adBeanList);
            }
        });
    }

    @Override
    protected void getReqParams(Bundle bundle) {
        categoryId = bundle.getString(CATEGORY_ID);
        categoryType = bundle.getString(CATEGORY_TYPE);
    }

    class OverseasHeaderView {
        @BindView(R.id.communal_recycler_wrap)
        RecyclerView communal_recycler_wrap;
        @BindView(R.id.rel_communal_banner)
        public RelativeLayout rel_communal_banner;
        @BindView(R.id.ad_communal_banner)
        public ConvenientBanner ad_communal_banner;
    }
}
