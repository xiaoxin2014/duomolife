package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.QualityTypeEntity;
import com.amkj.dmsh.bean.QualityTypeEntity.QualityTypeBean;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.constant.TabEntity;
import com.amkj.dmsh.dominant.adapter.ChildProductTypeAdapter;
import com.amkj.dmsh.dominant.adapter.GoodProductAdapter;
import com.amkj.dmsh.dominant.adapter.ProductTypeSortAdapter;
import com.amkj.dmsh.dominant.bean.SortTypeEntity;
import com.amkj.dmsh.dominant.initviews.BottomPopWindows;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.SearchCouponGoodsActivity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.RemoveExistUtils;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.views.flycoTablayout.CommonTabLayout;
import com.amkj.dmsh.views.flycoTablayout.listener.CustomTabEntity;
import com.amkj.dmsh.views.flycoTablayout.listener.OnTabSelectListener;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.bugly.beta.tinker.TinkerManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;
import razerdp.basepopup.BasePopupWindow;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getShowNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeIntegers;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_CHILD;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_ID;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_NAME;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.constant.Url.QUALITY_CATEGORY_TYPE;
import static com.amkj.dmsh.constant.Url.Q_COUPON_PRODUCT_TYPE_LIST;
import static com.amkj.dmsh.constant.Url.Q_PRODUCT_TYPE;
import static com.amkj.dmsh.constant.Url.Q_QUALITY_TYPE_AD;
import static com.amkj.dmsh.constant.Url.Q_SORT_TYPE;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/29
 * class description:良品 - 分类
 */
public class QualityTypeProductActivity extends BaseActivity {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    @BindView(R.id.tl_quality_type_bar)
    Toolbar tl_quality_type_bar;
    @BindView(R.id.ctb_qt_tab_product_type)
    CommonTabLayout ctb_qt_tab_product_type;
    @BindView(R.id.iv_search)
    ImageView iv_search;
    private int page = 1;
    private int scrollY;
    private float screenHeight;
    //    商品列表
    private List<LikedProductBean> typeDetails = new ArrayList<>();
    //    类别广告
    private List<CommunalADActivityBean> adBeanList = new ArrayList<>();
    //    父级分类
    private List<QualityTypeBean> productTypeList = new ArrayList<>();
    //    排序分类
    private List<QualityTypeBean> sortTypeList = new ArrayList<>();
    //    子分类
    private List<QualityTypeBean> childTypeList = new ArrayList<>();

    private GoodProductAdapter qualityTypeProductAdapter;
    private QTypeView qTypeView;
    //tab集合
    private ArrayList<CustomTabEntity> tabs = new ArrayList<>();
    private BottomPopWindows bottomPopWindows;
    private List<QualityTypeBean> qualityTypeBeans = new ArrayList<>();
    private View typeSortView;
    private View headerAdView;
    private View childTypeHeaderView;
    //    分类 排序
    private ProductTypeSortAdapter productTypeSortAdapter;
    //    子类
    private ChildProductTypeAdapter childProductTypeAdapter;
    private CBViewHolderCreator cbViewHolderCreator;
    private UserLikedProductEntity likedProductEntity;
    private RemoveExistUtils removeExistUtils;
    private String couponId;
    private int categoryType;
    private int categoryId;
    private String categoryName = "";
    private int categoryChildId;
    private int sortType;
    private String sortTypeName = "";
    private Map<String, Object> mParams = new HashMap<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_communal_ql_product_type;
    }

    @Override
    protected void initViews() {
        tl_quality_type_bar.setSelected(true);
        ctb_qt_tab_product_type.setVisibility(View.GONE);
        ctb_qt_tab_product_type.setTabPadding(AutoSizeUtils.mm2px(mAppContext, 44));
        ctb_qt_tab_product_type.setTextSize(AutoSizeUtils.mm2px(mAppContext, 32));
        Intent intent = getIntent();
        if (intent != null) {
            categoryChildId = getStringChangeIntegers(intent.getStringExtra(CATEGORY_CHILD));
            categoryId = getStringChangeIntegers(intent.getStringExtra(CATEGORY_ID));
            categoryType = getStringChangeIntegers(intent.getStringExtra(CATEGORY_TYPE));
            categoryName = getStrings(intent.getStringExtra(CATEGORY_NAME));
            couponId = intent.getStringExtra("couponId");
        }
        if (categoryId < 1) {
            showToast(R.string.miss_parameters_hint);
            return;
        }
        ctb_qt_tab_product_type.setIndicatorWidth((int) (AutoSizeUtils.mm2px(mAppContext, 32) * 4.5));
        communal_recycler.setLayoutManager(new GridLayoutManager(QualityTypeProductActivity.this, 2));
        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData();
            }
        });
        TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        screenHeight = app.getScreenHeight();
        communal_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                scrollY += dy;
                if (!recyclerView.canScrollVertically(-1)) {
                    scrollY = 0;
                }
                if (scrollY > screenHeight * 1.5 && dy < 0) {
                    if (download_btn_communal.getVisibility() == GONE) {
                        download_btn_communal.setVisibility(VISIBLE);
                        download_btn_communal.hide(false);
                    }
                    if (!download_btn_communal.isVisible()) {
                        download_btn_communal.show();
                    }
                } else {
                    if (download_btn_communal.isVisible()) {
                        download_btn_communal.hide();
                    }
                }
            }
        });
        download_btn_communal.setOnClickListener(v -> scrollHeader());
        qualityTypeProductAdapter = new GoodProductAdapter(QualityTypeProductActivity.this, typeDetails);
        headerAdView = LayoutInflater.from(QualityTypeProductActivity.this).inflate(R.layout.layout_al_new_sp_banner, null, false);
        childTypeHeaderView = LayoutInflater.from(QualityTypeProductActivity.this).inflate(R.layout.layout_communal_recycler_wrap, null, false);
        qTypeView = new QTypeView();
        ButterKnife.bind(qTypeView, headerAdView);
        ButterKnife.bind(qTypeView, childTypeHeaderView);
        qTypeView.initViews();
        communal_recycler.setAdapter(qualityTypeProductAdapter);
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp).create());
        qualityTypeProductAdapter.setOnLoadMoreListener(() -> {
            page++;
            getQualityTypePro();
        }, communal_recycler);
        ctb_qt_tab_product_type.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                openCloseTypeSelector(position, false);
            }

            @Override
            public void onTabReselect(int position) {
                openCloseTypeSelector(position, true);
            }
        });
        typeSortView = LayoutInflater.from(QualityTypeProductActivity.this).inflate(R.layout.layout_type_sort_text_header, null, false);
        removeExistUtils = new RemoveExistUtils();
    }

    private void scrollHeader() {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) communal_recycler.getLayoutManager();
        int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
        int mVisibleCount = linearLayoutManager.findLastVisibleItemPosition()
                - linearLayoutManager.findFirstVisibleItemPosition() + 1;
        if (firstVisibleItemPosition > mVisibleCount) {
            communal_recycler.scrollToPosition(mVisibleCount);
        }
        communal_recycler.smoothScrollToPosition(0);
    }

    private void openCloseTypeSelector(final int tabPosition, final boolean isReselect) {
        if (bottomPopWindows == null) {
            bottomPopWindows = new BottomPopWindows(QualityTypeProductActivity.this);
            bottomPopWindows.setOnDismissListener(new BasePopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    setTabIcon(false);
                }
            });
            bottomPopWindows.setOnBeforeShowCallback((popupRootView, anchorView, hasShowAnima) -> {
                setTabIcon(true);
                return true;
            });
            final View popupWindowView = bottomPopWindows.getContentView();
            RecyclerView communal_recycler_wrap = (RecyclerView) popupWindowView.findViewById(R.id.communal_recycler_wrap);
            communal_recycler_wrap.setLayoutManager(new GridLayoutManager(QualityTypeProductActivity.this, 4));
            productTypeSortAdapter = new ProductTypeSortAdapter(qualityTypeBeans);
            productTypeSortAdapter.addHeaderView(typeSortView);
            communal_recycler_wrap.setAdapter(productTypeSortAdapter);
            setProductSortShow(tabPosition);
            productTypeSortAdapter.setOnItemClickListener((adapter, view, position) -> {
                QualityTypeBean qualityTypeBean = (QualityTypeBean) view.getTag();
                if (qualityTypeBean != null && !qualityTypeBean.isSelect() || typeDetails.size() < 1) {
//                        修改商品类别
                    setProductSort(ctb_qt_tab_product_type.getCurrentTab(), qualityTypeBean);
                }
                if (bottomPopWindows != null && bottomPopWindows.isShowing()) {
                    bottomPopWindows.dismiss();
                }
            });
            bottomPopWindows.showPopupWindow(tl_quality_type_bar);
        } else {
            if (isReselect && bottomPopWindows.isShowing()) {
                closePopWindows();
            } else {
                setProductSortShow(tabPosition);
                bottomPopWindows.showPopupWindow(tl_quality_type_bar);
            }
        }
    }

    private void setTabIcon(boolean isShow) {
        int currentTab = ctb_qt_tab_product_type.getCurrentTab();
        tabs.clear();
        if (isShow) {//展示popWindows
            tabs.add(new TabEntity(getStrings(categoryName), R.drawable.solid_top, R.drawable.solid_bottom));
            tabs.add(new TabEntity(getStrings(sortTypeName), R.drawable.solid_top, R.drawable.solid_bottom));
        } else { //不展示popWindows
            if (currentTab == 1) {
                tabs.add(new TabEntity(getStrings(categoryName), R.drawable.solid_top, R.drawable.solid_bottom));
                tabs.add(new TabEntity(getStrings(sortTypeName), R.drawable.solid_bottom, R.drawable.solid_top));
            } else {
                tabs.add(new TabEntity(getStrings(categoryName), R.drawable.solid_bottom, R.drawable.solid_top));
                tabs.add(new TabEntity(getStrings(sortTypeName), R.drawable.solid_top, R.drawable.solid_bottom));
            }
        }
        ctb_qt_tab_product_type.setTabData(tabs);
    }

    /**
     * 设置商品类型&排序展示
     *
     * @param position
     * @param qualityTypeBean
     */
    private void setProductSort(int position, QualityTypeBean qualityTypeBean) {
        if (loadHud != null) {
            loadHud.show();
        }
        if (position == 0) {
            for (int i = 0; i < productTypeList.size(); i++) {
                QualityTypeBean qualityType = productTypeList.get(i);
                if (qualityType.getId() == qualityTypeBean.getId()) {
                    qualityType.setSelect(true);
                } else {
                    qualityType.setSelect(false);
                }
            }
            categoryName = getStrings(qualityTypeBean.getName());
            categoryId = qualityTypeBean.getId();
            qualityTypeBeans.clear();
            qualityTypeBeans.addAll(productTypeList);
//            重新设置child数据
            page = 1;
            categoryChildId = -1;
            getChildrenType();
        } else { // 修改排序
            for (int i = 0; i < sortTypeList.size(); i++) {
                QualityTypeBean qualityType = sortTypeList.get(i);
                if (qualityType.getSortType().equals(qualityTypeBean.getSortType())) {
                    qualityType.setSelect(true);
                } else {
                    qualityType.setSelect(false);
                }
            }
            qualityTypeBeans.clear();
            qualityTypeBeans.addAll(sortTypeList);
            sortType = Integer.parseInt(qualityTypeBean.getSortType());
            sortTypeName = getStrings(qualityTypeBean.getSortName());
            page = 1;
            getQualityTypePro();
        }
        productTypeSortAdapter.notifyDataSetChanged();
    }

    /**
     * 设置弹窗 品类
     *
     * @param position
     */
    private void setProductSortShow(int position) {
        if (position == 0) {
            for (int i = 0; i < productTypeList.size(); i++) {
                QualityTypeBean qualityType = productTypeList.get(i);
                if (qualityType.getId() == categoryId) {
                    qualityType.setSelect(true);
                } else {
                    qualityType.setSelect(false);
                }
            }
            qualityTypeBeans.clear();
            qualityTypeBeans.addAll(productTypeList);
            typeSortView.setVisibility(View.VISIBLE);
        } else { // 修改排序
            for (int i = 0; i < sortTypeList.size(); i++) {
                QualityTypeBean qualityType = sortTypeList.get(i);
                if (qualityType.getSortType().equals(String.valueOf(sortType))) {
                    qualityType.setSelect(true);
                } else {
                    qualityType.setSelect(false);
                }
            }
            qualityTypeBeans.clear();
            qualityTypeBeans.addAll(sortTypeList);
            typeSortView.setVisibility(View.GONE);
        }
        productTypeSortAdapter.notifyDataSetChanged();
    }

    private void closePopWindows() {
        if (bottomPopWindows != null && bottomPopWindows.isShowing()) {
            bottomPopWindows.dismiss();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            loadData();
        }
    }

    /**
     * 获取排序-> 获取子类 -> 根据排序跟子类请求 商品数据
     */
    @Override
    protected void loadData() {
        if (categoryId > 0) {
            page = 1;
            sortTypeList.clear();
            getCategoryType();
            getSortType();
            getAdTypeData();
        }
    }


    @Override
    public View getLoadView() {
        return smart_communal_refresh;
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
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_QUALITY_TYPE_AD, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();

                adBeanList.clear();
                CommunalADActivityEntity qualityAdLoop = GsonUtils.fromJson(result, CommunalADActivityEntity.class);
                if (qualityAdLoop != null) {
                    if (qualityAdLoop.getCode().equals(SUCCESS_CODE)) {
                        adBeanList.addAll(qualityAdLoop.getCommunalADActivityBeanList());
                        if (cbViewHolderCreator == null) {
                            cbViewHolderCreator = new CBViewHolderCreator() {
                                @Override
                                public Holder createHolder(View itemView) {
                                    return new CommunalAdHolderView(itemView, QualityTypeProductActivity.this, true);
                                }

                                @Override
                                public int getLayoutId() {
                                    return R.layout.layout_ad_image_video;
                                }
                            };
                        }
                        qTypeView.ad_communal_banner.setPages(QualityTypeProductActivity.this, cbViewHolderCreator, adBeanList).setCanLoop(true)
                                .setPointViewVisible(true).setPageIndicator(new int[]{R.drawable.unselected_radius, R.drawable.selected_radius})
                                .startTurning(getShowNumber(adBeanList.get(0).getShowTime()) * 1000);
                    }
                    if (adBeanList.size() > 0) {
                        if (headerAdView.getParent() == null) {
                            qualityTypeProductAdapter.addHeaderView(headerAdView);
                        }
                    } else {
                        qualityTypeProductAdapter.removeHeaderView(headerAdView);
                    }

                }
            }

            @Override
            public void onNotNetOrException() {
                smart_communal_refresh.finishRefresh();
                if (adBeanList.size() > 0) {
                    if (headerAdView.getParent() == null) {
                        qualityTypeProductAdapter.addHeaderView(headerAdView);
                    }
                } else {
                    qualityTypeProductAdapter.removeHeaderView(headerAdView);
                }
            }
        });
    }

    /**
     * 获取分类排序
     */
    private void getSortType() {
        if (sortTypeList.size() < 1) {
            Map<String, String> map = new HashMap<>();
            map.put("version", "1");
            NetLoadUtils.getNetInstance().loadNetDataPost(QualityTypeProductActivity.this, Q_SORT_TYPE, map,
                    new NetLoadListenerHelper() {
                        @Override
                        public void onSuccess(String result) {
                            sortTypeList.clear();

                            SortTypeEntity sortTypeEntity = GsonUtils.fromJson(result, SortTypeEntity.class);
                            if (sortTypeEntity != null) {
                                if (sortTypeEntity.getCode().equals(SUCCESS_CODE)) {
                                    if (sortTypeEntity.getCategoryOrderType() != null) {
                                        Map<String, String> categoryOrderType = sortTypeEntity.getCategoryOrderType();
                                        QualityTypeBean qualityTypeBean;
                                        for (Map.Entry<String, String> entry : categoryOrderType.entrySet()) {
                                            qualityTypeBean = new QualityTypeBean();
                                            qualityTypeBean.setSortType(entry.getKey());
                                            qualityTypeBean.setSortName(entry.getValue());
                                            qualityTypeBean.setDataType(qualityTypeBean.SORT_TYPE);
                                            sortTypeList.add(qualityTypeBean);
                                        }
                                        if (sortTypeList.size() > 0) {
                                            QualityTypeBean qualityType = sortTypeList.get(0);
                                            qualityType.setSelect(true);
                                            sortTypeList.set(0, qualityType);
                                            sortType = getStringChangeIntegers(sortTypeList.get(0).getSortType());
                                            sortTypeName = getStrings(sortTypeList.get(0).getSortName());
                                            //获取子类
                                            getChildrenType();
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onNotNetOrException() {
                            setEmptyErrorException();
                        }


                        @Override
                        public void onError(Throwable throwable) {
                            showToast(R.string.request_json_data_error);
                        }
                    });
        } else {
//          获取子类
            getChildrenType();
        }
    }

    /**
     * 获取一级分类
     */
    private void getCategoryType() {
        NetLoadUtils.getNetInstance().loadNetDataPost(this, QUALITY_CATEGORY_TYPE, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                productTypeList.clear();

                if (!TextUtils.isEmpty(result)) {
                    QualityTypeEntity qualityTypeEntity = GsonUtils.fromJson(result, QualityTypeEntity.class);
                    if (qualityTypeEntity != null) {
                        if (SUCCESS_CODE.equals(qualityTypeEntity.getCode())) {
                            productTypeList.addAll(qualityTypeEntity.getQualityTypeBeanList());
                            for (QualityTypeBean qualityTypeBean : qualityTypeEntity.getQualityTypeBeanList()) {
                                if (categoryId == qualityTypeBean.getId()) {
                                    qualityTypeBean.setSelect(true);
                                }
                            }
                        } else {
                            setEmptyErrorException();
                        }
                        if (productTypeSortAdapter != null) {
                            productTypeSortAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                setEmptyErrorException();
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(R.string.request_json_data_error);
            }
        });
    }

    private void getQualityTypePro() {
        iv_search.setVisibility(VISIBLE);
        mParams = new HashMap<>();
        mParams.put("currentPage", page);
        mParams.put("showCount", TOTAL_COUNT_TWENTY);
        if (categoryChildId > -1) {
            mParams.put("id", categoryChildId);
            mParams.put("pid", categoryId);
        } else {
            mParams.put("id", categoryId);
            mParams.put("pid", 0);
        }
        mParams.put("orderTypeId", sortType);
        if (!TextUtils.isEmpty(couponId)) {
            mParams.put("couponId", couponId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(QualityTypeProductActivity.this, Q_COUPON_PRODUCT_TYPE_LIST
                , mParams, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        if (loadHud != null) {
                            loadHud.dismiss();
                        }
                        if (page == 1) {
                            typeDetails.clear();
                            removeExistUtils.clearData();
                        }
                        qualityTypeProductAdapter.loadMoreComplete();

                        likedProductEntity = GsonUtils.fromJson(result, UserLikedProductEntity.class);
                        if (likedProductEntity != null) {
                            if (likedProductEntity.getCode().equals(SUCCESS_CODE)) {
                                typeDetails.addAll(removeExistUtils.removeExistList(likedProductEntity.getGoodsList()));
                            } else {
                                qualityTypeProductAdapter.loadMoreEnd();
                                if (!EMPTY_CODE.equals(likedProductEntity.getCode())) {
                                    showToast(likedProductEntity.getMsg());
                                }
                            }
                            if (page == 1) {
                                ctb_qt_tab_product_type.setVisibility(View.VISIBLE);
                                setTabIcon(false);
                                qualityTypeProductAdapter.setNewData(typeDetails);
                            } else {
                                qualityTypeProductAdapter.notifyDataSetChanged();
                            }
                        } else {
                            qualityTypeProductAdapter.loadMoreComplete();
                        }
                        setEmptyErrorException();
                    }

                    @Override
                    public void onNotNetOrException() {
                        if (loadHud != null) {
                            loadHud.dismiss();
                        }
                        qualityTypeProductAdapter.loadMoreEnd(true);
                        setEmptyErrorException();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showToast(R.string.invalidData);
                    }
                });
    }

    private void setEmptyErrorException() {
        if (loadHud != null) {
            loadHud.dismiss();
        }
        smart_communal_refresh.finishRefresh();
        if (childTypeList.size() < 1) {
            NetLoadUtils.getNetInstance().showLoadSir(loadService, typeDetails, likedProductEntity);
        } else {
            NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
        }
    }

    /**
     * 获取子类类型
     */
    private void getChildrenType() {
        childTypeList.clear();
        Map<String, Object> params = new HashMap<>();
        params.put("pid", categoryId);
        NetLoadUtils.getNetInstance().loadNetDataPost(QualityTypeProductActivity.this, Q_PRODUCT_TYPE,
                params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        childTypeList.clear();

                        QualityTypeEntity qualityTypeEntity = GsonUtils.fromJson(result, QualityTypeEntity.class);
                        if (qualityTypeEntity != null) {
                            if (qualityTypeEntity.getCode().equals(SUCCESS_CODE)) {
                                if (qualityTypeEntity.getQualityTypeBeanList() != null) {
                                    for (QualityTypeBean qualityTypeBean : qualityTypeEntity.getQualityTypeBeanList()) {
                                        qualityTypeBean.setChildCategory(String.valueOf(qualityTypeBean.getId()));
                                        qualityTypeBean.setChildName(getStrings(qualityTypeBean.getName()));
                                    }
                                    childTypeList.addAll(qualityTypeEntity.getQualityTypeBeanList());
                                    if (childTypeList.size() > 0) {
                                        if (categoryChildId > -1) {
                                            for (int i = 0; i < childTypeList.size(); i++) {
                                                QualityTypeBean qualityTypeBean = childTypeList.get(i);
                                                int id = qualityTypeBean.getId();
                                                if (categoryChildId == id) {
                                                    qualityTypeBean.setSelect(true);
                                                    break;
                                                }
                                                if (i == childTypeList.size() - 1) {
                                                    childTypeList.get(0).setSelect(true);
                                                }
                                            }
                                        } else {
                                            QualityTypeBean qualityTypeBean = childTypeList.get(0);
                                            qualityTypeBean.setSelect(true);
                                            categoryChildId = Integer.parseInt(qualityTypeBean.getChildCategory());
                                        }
                                    } else {
                                        categoryChildId = -1;
                                    }
                                    if (childProductTypeAdapter != null) {
                                        childProductTypeAdapter.notifyDataSetChanged();
                                    }
                                }
                            } else {
                                categoryChildId = -1;
                            }
                            getQualityTypePro();
                            if (childTypeList.size() > 0) {
                                if (childTypeHeaderView != null) {
                                    if (childTypeHeaderView.getParent() == null) {
                                        qualityTypeProductAdapter.addHeaderView(childTypeHeaderView);
                                        scrollHeader();
                                    }
                                }
                            } else {
                                if (childTypeHeaderView != null) {
                                    qualityTypeProductAdapter.removeHeaderView(childTypeHeaderView);
                                }
                            }
                            childProductTypeAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onNotNetOrException() {
                        if (loadHud != null) {
                            loadHud.dismiss();
                        }
                        if (childTypeList.size() > 0) {
                            if (childTypeHeaderView != null) {
                                if (childTypeHeaderView.getParent() == null) {
                                    qualityTypeProductAdapter.addHeaderView(childTypeHeaderView);
                                    scrollHeader();
                                }
                            }
                        } else {
                            if (childTypeHeaderView != null) {
                                qualityTypeProductAdapter.removeHeaderView(childTypeHeaderView);
                            }
                        }
                        setEmptyErrorException();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showToast(R.string.request_json_data_error);
                    }
                });
    }

    class QTypeView {
        @Nullable
        @BindView(R.id.ad_communal_banner)
        ConvenientBanner ad_communal_banner;
        @Nullable
        @BindView(R.id.communal_recycler_wrap)
        RecyclerView communal_recycler_wrap;

        public void initViews() {
            if (communal_recycler_wrap != null) {
                communal_recycler_wrap.setBackgroundColor(getResources().getColor(R.color.gray_bg));
                communal_recycler_wrap.setPadding(AutoSizeUtils.mm2px(mAppContext, 20), 0, 0, AutoSizeUtils.mm2px(mAppContext, 20));
                // 这一步必须要做,否则不会显示.
                communal_recycler_wrap.setLayoutManager(new GridLayoutManager(QualityTypeProductActivity.this, 4));
                childProductTypeAdapter = new ChildProductTypeAdapter(childTypeList);
                communal_recycler_wrap.setAdapter(childProductTypeAdapter);
                childProductTypeAdapter.setOnItemClickListener((adapter, view, position) -> {
                    QualityTypeBean qualityTypeBean = (QualityTypeBean) view.getTag();
                    if (qualityTypeBean != null && !qualityTypeBean.isSelect()) {
//                        修改商品子分类
                        setChildProductType(qualityTypeBean);
                        if (loadHud != null) {
                            loadHud.show();
                        }
                        page = 1;
                        getQualityTypePro();
                    }
                });
            }
        }
    }

    /**
     * 设置子分类数据
     *
     * @param qualityTypeBean
     */
    private void setChildProductType(QualityTypeBean qualityTypeBean) {
        categoryChildId = Integer.parseInt(qualityTypeBean.getChildCategory());
        if (childTypeList.size() > 0) {
            for (QualityTypeBean qualityType : childTypeList) {
                if (qualityTypeBean.getChildCategory().equals(qualityType.getChildCategory())) {
                    qualityType.setSelect(true);
                } else {
                    qualityType.setSelect(false);
                }
            }
        }
        childProductTypeAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.tv_ql_type_back)
    void goBack(View view) {
        finish();
    }

    @OnClick(R.id.iv_search)
    void search(View view) {
        Intent intent = new Intent(this, SearchCouponGoodsActivity.class);
        intent.putExtra("params", JSON.toJSONString(mParams));
        startActivity(intent);
    }
}
