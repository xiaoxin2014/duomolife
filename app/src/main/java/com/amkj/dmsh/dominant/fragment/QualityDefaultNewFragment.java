package com.amkj.dmsh.dominant.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.BaseAddCarProInfoBean;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.dominant.adapter.QualityGoodNewProAdapter;
import com.amkj.dmsh.dominant.adapter.QualityTypeAreaNewAdapter;
import com.amkj.dmsh.dominant.bean.QualityGoodProductEntity;
import com.amkj.dmsh.dominant.bean.QualityGoodProductEntity.Attribute;
import com.amkj.dmsh.dominant.bean.QualityHomeTypeEntity;
import com.amkj.dmsh.dominant.bean.QualityHomeTypeEntity.QualityHomeTypeBean;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.network.NetCacheLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.RemoveExistUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.utils.multitypejson.MultiTypeJsonParser;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.amkj.dmsh.constant.ConstantMethod.adClickTotal;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getShowNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.insertFragmentNewTotalData;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.START_AUTO_PAGE_TURN;
import static com.amkj.dmsh.constant.ConstantVariable.STOP_AUTO_PAGE_TURN;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.BASE_URL;
import static com.amkj.dmsh.constant.Url.QUALITY_SHOP_GOODS_PRO;
import static com.amkj.dmsh.constant.Url.Q_HOME_AD_LOOP;
import static com.amkj.dmsh.constant.Url.Q_HOME_CENTER_TYPE;
import static com.amkj.dmsh.constant.Url.Q_HOME_CLASS_TYPE;
import static com.amkj.dmsh.dominant.fragment.QualityFragment.updateCarNum;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/5
 * class description:良品默认页
 */
public class QualityDefaultNewFragment extends BaseFragment {
    @BindView(R.id.smart_communal_refresh)
    RefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    private int page = 1;
    private List<CommunalADActivityBean> adBeanList = new ArrayList<>();
    private List<QualityHomeTypeBean> typeBeanArrayList = new ArrayList<>();
    private List<QualityHomeTypeBean> typeBeanCenterList = new ArrayList<>();
    private List<Attribute> goodsProList = new ArrayList<>();
    private QualityTypeAreaNewAdapter qualityTypeAreaAdapter;
    private QualityTypeAreaNewAdapter qualityTypeCenterAdapter;
    private QualityGoodNewProAdapter qualityGoodNewProAdapter;
    private QualityTypeView qualityTypeView;
    private CBViewHolderCreator cbViewHolderCreator;
    private RemoveExistUtils removeExistUtils;
    private boolean isUpdateCache;

    @Override
    protected int getContentView() {
        return R.layout.layout_communal_smart_refresh_recycler_float_loading;
    }

    @Override
    protected void initViews() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        communal_recycler.setLayoutManager(gridLayoutManager);

        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                isUpdateCache = true;
                loadData();
            }
        });
//        好物
        qualityGoodNewProAdapter = new QualityGoodNewProAdapter(getActivity(), goodsProList);
        View typeHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_qt_pro_type, null, false);
        qualityTypeView = new QualityTypeView();
        ButterKnife.bind(qualityTypeView, typeHeaderView);
        qualityTypeView.initView();
        qualityGoodNewProAdapter.addHeaderView(typeHeaderView);
        View goodProView = LayoutInflater.from(getActivity()).inflate(R.layout.adapter_ql_goods_pro_header, null, false);
        qualityGoodNewProAdapter.addHeaderView(goodProView);
        communal_recycler.setAdapter(qualityGoodNewProAdapter);
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp)
                .create());

        qualityGoodNewProAdapter.setOnLoadMoreListener(() -> {
            page++;
            getGoodsPro();
        }, communal_recycler);
        qualityTypeAreaAdapter.setOnItemClickListener((adapter, view, position) -> {
            QualityHomeTypeBean qualityHomeTypeBean = (QualityHomeTypeBean) view.getTag();
            if (qualityHomeTypeBean != null) {
                setSkipPath(getActivity(), qualityHomeTypeBean.getAndroidLink(), false);
            }
        });
        qualityGoodNewProAdapter.setOnItemClickListener((adapter, view, position) -> {
            Attribute attribute = (Attribute) view.getTag();
            if (attribute != null) {
                switch (attribute.getObjectType()) {
                    case "product":
                        LikedProductBean likedProductBean = (LikedProductBean) attribute;
                        Intent intent = new Intent(getActivity(), ShopScrollDetailsActivity.class);
                        intent.putExtra("productId", String.valueOf(likedProductBean.getId()));
                        startActivity(intent);
                        break;
                    case "ad":
                        CommunalADActivityBean communalADActivityBean = (CommunalADActivityBean) attribute;
                        /**
                         * 3.1.9 加入好物广告统计
                         */
                        adClickTotal(getActivity(), communalADActivityBean.getId());
                        setSkipPath(getActivity(), getStrings(communalADActivityBean.getAndroidLink()), false);
                        break;
                }

            }
        });
        qualityGoodNewProAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            loadHud.show();
            Attribute attribute = (Attribute) view.getTag();
            if (attribute != null) {
                if (userId > 0) {
                    switch (view.getId()) {
                        case R.id.iv_pro_add_car:
                            LikedProductBean likedProductBean = (LikedProductBean) attribute;
                            BaseAddCarProInfoBean baseAddCarProInfoBean = new BaseAddCarProInfoBean();
                            baseAddCarProInfoBean.setProductId(likedProductBean.getId());
                            baseAddCarProInfoBean.setActivityCode(getStrings(likedProductBean.getActivityCode()));
                            baseAddCarProInfoBean.setProName(getStrings(likedProductBean.getName()));
                            baseAddCarProInfoBean.setProPic(getStrings(likedProductBean.getPicUrl()));
                            ConstantMethod constantMethod = new ConstantMethod();
                            constantMethod.addShopCarGetSku(getActivity(), baseAddCarProInfoBean, loadHud);
                            constantMethod.setAddOnCarListener(() -> EventBus.getDefault().post(new EventMessage(updateCarNum, updateCarNum)));
                            break;
                    }
                } else {
                    loadHud.dismiss();
                    getLoginStatus(QualityDefaultNewFragment.this);
                }
            }
        });
        totalPersonalTrajectory = insertFragmentNewTotalData(getActivity(), this.getClass().getSimpleName());
        removeExistUtils = new RemoveExistUtils();
    }

    @Override
    protected void loadData() {
        page = 1;
        getAdLoop();
        getHomeIndexType();
        getCenterType();
        getGoodsPro();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getAdLoop() {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("vidoShow", "1");
        NetLoadUtils.getNetInstance().loadNetDataGetCache(getActivity(),BASE_URL + Q_HOME_AD_LOOP, params, isUpdateCache, new NetCacheLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                getADJsonData(result);
            }
        });
    }

    private void getADJsonData(String result) {
        adBeanList.clear();
        Gson gson = new Gson();
        CommunalADActivityEntity qualityAdLoop = gson.fromJson(result, CommunalADActivityEntity.class);
        if (qualityAdLoop != null) {
            if (qualityAdLoop.getCode().equals(SUCCESS_CODE)) {
                adBeanList.addAll(qualityAdLoop.getCommunalADActivityBeanList());
                qualityTypeView.rel_communal_banner.setVisibility(View.VISIBLE);
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
                qualityTypeView.ad_communal_banner.setPages(getActivity(), cbViewHolderCreator, adBeanList).setCanLoop(true)
                        .setPointViewVisible(true).setCanScroll(true)
                        .setPageIndicator(new int[]{R.drawable.unselected_radius, R.drawable.selected_radius})
                        .startTurning(getShowNumber(adBeanList.get(0).getShowTime()) * 1000);
            } else {
                if (adBeanList.size() < 1) {
                    qualityTypeView.rel_communal_banner.setVisibility(View.GONE);
                }
            }
        }
    }

    private void getGoodsPro() {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        /**
         * version 1 区分是否带入广告页
         */
        params.put("version", 1);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), QUALITY_SHOP_GOODS_PRO
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        qualityGoodNewProAdapter.loadMoreComplete();
                        if (page == 1) {
                            goodsProList.clear();
                            removeExistUtils.clearData();
                        }
                        MultiTypeJsonParser<Attribute> multiTypeJsonParser = new MultiTypeJsonParser.Builder<Attribute>()
                                .registerTypeElementName("objectType")
                                .registerTargetClass(Attribute.class)
                                .registerTypeElementValueWithClassType("product", LikedProductBean.class)
                                .registerTypeElementValueWithClassType("ad", CommunalADActivityBean.class)
                                .build();
                        QualityGoodProductEntity qualityGoodProductEntity = multiTypeJsonParser.fromJson(result, QualityGoodProductEntity.class);
                        if (qualityGoodProductEntity != null) {
                            if (qualityGoodProductEntity.getCode().equals(SUCCESS_CODE)) {
                                goodsProList.addAll(removeExistUtils.removeExistList(qualityGoodProductEntity.getGoodProductList()));
                            } else if (qualityGoodProductEntity.getCode().equals(EMPTY_CODE)) {
                                qualityGoodNewProAdapter.loadMoreEnd();
                            } else {
                                qualityGoodNewProAdapter.loadMoreEnd();
                                showToast(getActivity(), qualityGoodProductEntity.getMsg());
                            }
                        }
                        NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
                        qualityGoodNewProAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        qualityGoodNewProAdapter.loadMoreEnd(true);
                        NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
                    }
                });
    }

    private void getCenterType() {
        NetLoadUtils.getNetInstance().loadNetDataGetCache(getActivity(), BASE_URL + Q_HOME_CENTER_TYPE, isUpdateCache, new NetCacheLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                Gson gson = new Gson();
                QualityHomeTypeEntity homeTypeEntity = gson.fromJson(result, QualityHomeTypeEntity.class);
                if (homeTypeEntity != null) {
                    if (homeTypeEntity.getCode().equals(SUCCESS_CODE)) {
                        typeBeanCenterList.clear();
                        for (int i = 0; i < homeTypeEntity.getQualityHomeTypeList().size(); i++) {
                            QualityHomeTypeBean qualityHomeTypeBean = homeTypeEntity.getQualityHomeTypeList().get(i);
                            if (homeTypeEntity.getQualityHomeTypeList().size() == 3) {
                                qualityHomeTypeBean.setItemType(ConstantVariable.TYPE_2);
                            } else {
                                qualityHomeTypeBean.setItemType(ConstantVariable.TYPE_1);
                            }
                            typeBeanCenterList.add(qualityHomeTypeBean);
                        }
                        if (typeBeanCenterList.size() == 3) {
                            //        中间栏。。布局
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL, false);
                            qualityTypeView.rv_ql_center_pro.setLayoutManager(gridLayoutManager);
                            qualityTypeCenterAdapter = new QualityTypeAreaNewAdapter(getActivity(), typeBeanCenterList);
                            gridLayoutManager.setSpanCount(2);
                            qualityTypeCenterAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
                                @Override
                                public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                                    return position == 0 ? 2 : 1;
                                }
                            });
                        } else if (typeBeanCenterList.size() == 4) {
                            //        中间栏。。布局
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
                            qualityTypeView.rv_ql_center_pro.setLayoutManager(gridLayoutManager);
                            qualityTypeCenterAdapter = new QualityTypeAreaNewAdapter(getActivity(), typeBeanCenterList);
                        } else if (typeBeanCenterList.size() == 5) {
                            //        中间栏。。布局
                            qualityTypeView.rv_ql_center_pro.setLayoutManager(new GridLayoutManager(getActivity(), 7));
                            qualityTypeCenterAdapter = new QualityTypeAreaNewAdapter(getActivity(), typeBeanCenterList);
                            qualityTypeCenterAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
                                @Override
                                public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                                    switch (position) {
                                        case 0:
                                            return 4;
                                        case 1:
                                        case 4:
                                            return 3;
                                        default:
                                            return 2;
                                    }
                                }
                            });
                        }
                        qualityTypeView.rv_ql_center_pro.setAdapter(qualityTypeCenterAdapter);
                        qualityTypeCenterAdapter.setNewData(typeBeanCenterList);
                        qualityTypeCenterAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                QualityHomeTypeBean qualityHomeTypeBean = (QualityHomeTypeBean) view.getTag();
                                if (qualityHomeTypeBean != null) {
                                    ConstantMethod.setSkipPath(getActivity(), qualityHomeTypeBean.getAndroidLink(), false);
                                }
                            }
                        });
                    } else if (!homeTypeEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(getActivity(), homeTypeEntity.getMsg());
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                smart_communal_refresh.finishRefresh();
            }
        });
    }

    //第二栏
    private void getHomeIndexType() {
        NetLoadUtils.getNetInstance().loadNetDataGetCache(getActivity(), BASE_URL + Q_HOME_CLASS_TYPE, isUpdateCache, new NetCacheLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();
                Gson gson = new Gson();
                QualityHomeTypeEntity homeTypeEntity = gson.fromJson(result, QualityHomeTypeEntity.class);
                if (homeTypeEntity != null) {
                    if (homeTypeEntity.getCode().equals(SUCCESS_CODE)) {
                        typeBeanArrayList.clear();
                        for (int i = 0; i < (homeTypeEntity.getQualityHomeTypeList().size() < 3 ? homeTypeEntity.getQualityHomeTypeList().size() : 3); i++) {
                            QualityHomeTypeBean qualityHomeTypeBean = homeTypeEntity.getQualityHomeTypeList().get(i);
                            typeBeanArrayList.add(qualityHomeTypeBean);
                        }
                    } else if (!homeTypeEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(getActivity(), homeTypeEntity.getMsg());
                    }
                    qualityTypeAreaAdapter.setNewData(typeBeanArrayList);
                }
            }

            @Override
            public void onNotNetOrException() {
                smart_communal_refresh.finishRefresh();
            }
        });
    }

    class QualityTypeView {
        @BindView(R.id.communal_recycler_wrap)
        RecyclerView communal_recycler_wrap;
        @BindView(R.id.rv_ql_center_pro)
        RecyclerView rv_ql_center_pro;
        @BindView(R.id.rel_communal_banner)
        public RelativeLayout rel_communal_banner;
        @BindView(R.id.ad_communal_banner)
        public ConvenientBanner ad_communal_banner;

        public void initView() {
            qualityTypeAreaAdapter = new QualityTypeAreaNewAdapter(getActivity(), typeBeanArrayList);
            communal_recycler_wrap.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            communal_recycler_wrap.setNestedScrollingEnabled(false);
            rv_ql_center_pro.setNestedScrollingEnabled(false);
            communal_recycler_wrap.setAdapter(qualityTypeAreaAdapter);
            rv_ql_center_pro.addItemDecoration(new ItemDecoration.Builder()
                    // 设置分隔线资源ID
                    .setDividerId(R.drawable.item_divider_gray_f_two_px).create());
        }
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (START_AUTO_PAGE_TURN.equals(message.type)) {
            if (adBeanList.size() > 0 && qualityTypeView.ad_communal_banner != null && !qualityTypeView.ad_communal_banner.isTurning()) {
                qualityTypeView.ad_communal_banner.setCanScroll(true);
                qualityTypeView.ad_communal_banner.startTurning(getShowNumber(adBeanList.get(0).getShowTime()) * 1000);
                qualityTypeView.ad_communal_banner.setPointViewVisible(true);
            }
        } else if (STOP_AUTO_PAGE_TURN.equals(message.type)) {
            if (qualityTypeView.ad_communal_banner != null && qualityTypeView.ad_communal_banner.isTurning()) {
                qualityTypeView.ad_communal_banner.setCanScroll(false);
                qualityTypeView.ad_communal_banner.stopTurning();
                qualityTypeView.ad_communal_banner.setPointViewVisible(false);
            }
        }
    }
}
