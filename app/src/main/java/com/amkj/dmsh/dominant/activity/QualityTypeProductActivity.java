package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.QualityTypeEntity;
import com.amkj.dmsh.bean.QualityTypeEntity.QualityTypeBean;
import com.amkj.dmsh.bean.QualityTypeEntity.QualityTypeBean.ChildCategoryListBean;
import com.amkj.dmsh.constant.BaseAddCarProInfoBean;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.TabEntity;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.dominant.adapter.ChildProductTypeAdapter;
import com.amkj.dmsh.dominant.adapter.ProductTypeSortAdapter;
import com.amkj.dmsh.dominant.adapter.QualityTypeProductAdapter;
import com.amkj.dmsh.dominant.bean.SortTypeEntity;
import com.amkj.dmsh.dominant.initviews.BottomPopWindows;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.RemoveExistUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.amkj.dmsh.utils.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
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

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getIntegers;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getShowNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.insertNewTotalData;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_CHILD;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_ID;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_NAME;
import static com.amkj.dmsh.constant.ConstantVariable.CATEGORY_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.START_AUTO_PAGE_TURN;
import static com.amkj.dmsh.constant.ConstantVariable.STOP_AUTO_PAGE_TURN;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;

;


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
    private int page = 1;
    private int scrollY;
    private float screenHeight;
    //    商品列表
    private List<LikedProductBean> typeDetails = new ArrayList();
    //    类别广告
    private List<CommunalADActivityBean> adBeanList = new ArrayList<>();
    //    父级分类
    private List<QualityTypeBean> productTypeList = new ArrayList<>();
    //    排序分类
    private List<QualityTypeBean> sortTypeList = new ArrayList<>();
    //    子分类
    private List<QualityTypeBean> childTypeList = new ArrayList<>();

    private QualityTypeProductAdapter qualityTypeProductAdapter;
    private QTypeView qTypeView;
    //tab集合
    private ArrayList<CustomTabEntity> tabs = new ArrayList<>();
    private QualityTypeBean qualityTypeBeanChange;
    private BottomPopWindows bottomPopWindows;
    private List<QualityTypeBean> qualityTypeBeans = new ArrayList<>();
    private View typeSortView;
    private View headerAdView;
    private View childTypeHeaderView;
    private ProductTypeSortAdapter productTypeSortAdapter;
    private ChildProductTypeAdapter childProductTypeAdapter;
    private CBViewHolderCreator cbViewHolderCreator;
    private UserLikedProductEntity likedProductEntity;
    private RemoveExistUtils removeExistUtils;

    @Override
    protected int getContentView() {
        return R.layout.activity_communal_ql_product_type;
    }

    @Override
    protected void initViews() {
        tl_quality_type_bar.setSelected(true);
        ctb_qt_tab_product_type.setVisibility(View.GONE);
        ctb_qt_tab_product_type.setTabPadding(AutoSizeUtils.mm2px(mAppContext,44));
        ctb_qt_tab_product_type.setTextSize(AutoSizeUtils.mm2px(mAppContext,32));
        Intent intent = getIntent();
        if (intent != null) {
            qualityTypeBeanChange = new QualityTypeBean();
            qualityTypeBeanChange.setChildCategory(getStrings(intent.getStringExtra(CATEGORY_CHILD)));
            qualityTypeBeanChange.setId(getIntegers(intent.getStringExtra(CATEGORY_ID)));
            qualityTypeBeanChange.setType(getIntegers(intent.getStringExtra(CATEGORY_TYPE)));
            qualityTypeBeanChange.setName(getStrings(intent.getStringExtra(CATEGORY_NAME)));
        }
        if (qualityTypeBeanChange != null) {
            ctb_qt_tab_product_type.setIndicatorWidth((int) (AutoSizeUtils.mm2px(mAppContext,32) * 4.5));
        }

        communal_recycler.setLayoutManager(new GridLayoutManager(QualityTypeProductActivity.this, 2));
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
                    TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
                    screenHeight = app.getScreenHeight();
                }
                if (scrollY >= screenHeight) {
                    download_btn_communal.setVisibility(View.VISIBLE);
                } else {
                    download_btn_communal.setVisibility(View.GONE);
                }
            }
        });
        download_btn_communal.setOnClickListener(v -> scrollHeader());
        qualityTypeProductAdapter = new QualityTypeProductAdapter(QualityTypeProductActivity.this, typeDetails);
        headerAdView = LayoutInflater.from(QualityTypeProductActivity.this).inflate(R.layout.layout_al_new_sp_banner, null, false);
        childTypeHeaderView = LayoutInflater.from(QualityTypeProductActivity.this).inflate(R.layout.layout_communal_recycler_wrap, null, false);
        qTypeView = new QTypeView();
        ButterKnife.bind(qTypeView, headerAdView);
        ButterKnife.bind(qTypeView, childTypeHeaderView);
        qTypeView.initViews();
        communal_recycler.setAdapter(qualityTypeProductAdapter);
        communal_recycler.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        qualityTypeProductAdapter.setOnLoadMoreListener(() -> {
            page++;
            getQualityTypePro();
        }, communal_recycler);
        qualityTypeProductAdapter.setOnItemClickListener((adapter, view, position) -> {
            LikedProductBean likedProductBean = (LikedProductBean) view.getTag();
            if (likedProductBean != null) {
                Intent intent1 = new Intent(QualityTypeProductActivity.this, ShopScrollDetailsActivity.class);
                intent1.putExtra("productId", String.valueOf(likedProductBean.getId()));
                startActivity(intent1);
            }
        });
        qualityTypeProductAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            loadHud.show();
            LikedProductBean likedProductBean = (LikedProductBean) view.getTag();
            if (likedProductBean != null) {
                if (userId > 0) {
                    switch (view.getId()) {
                        case R.id.iv_pro_add_car:
                            BaseAddCarProInfoBean baseAddCarProInfoBean = new BaseAddCarProInfoBean();
                            baseAddCarProInfoBean.setProductId(likedProductBean.getId());
                            baseAddCarProInfoBean.setActivityCode(getStrings(likedProductBean.getActivityCode()));
                            baseAddCarProInfoBean.setProName(getStrings(likedProductBean.getName()));
                            baseAddCarProInfoBean.setProPic(getStrings(likedProductBean.getPicUrl()));
                            ConstantMethod constantMethod = new ConstantMethod();
                            constantMethod.addShopCarGetSku(QualityTypeProductActivity.this, baseAddCarProInfoBean, loadHud);
                            break;
                    }
                } else {
                    loadHud.dismiss();
                    getLoginStatus(QualityTypeProductActivity.this);
                }
            }
        });
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
        totalPersonalTrajectory = insertNewTotalData(this, String.valueOf(qualityTypeBeanChange.getId()));
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
            communal_recycler_wrap.setAdapter(productTypeSortAdapter);
            setProductSortShow(tabPosition, qualityTypeBeanChange);
            productTypeSortAdapter.setOnItemClickListener((adapter, view, position) -> {
                QualityTypeBean qualityTypeBean = (QualityTypeBean) view.getTag();
                if (qualityTypeBean != null && !qualityTypeBean.isSelect()) {
//                        修改商品类别
                    setProductSort(ctb_qt_tab_product_type.getCurrentTab(), qualityTypeBean);
                    page = 1;
                    getQualityTypePro();
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
                setProductSortShow(tabPosition, qualityTypeBeanChange);
                bottomPopWindows.showPopupWindow(tl_quality_type_bar);
            }
        }
    }

    private void setTabIcon(boolean isShow) {
        int currentTab = ctb_qt_tab_product_type.getCurrentTab();
        tabs.clear();
        if (isShow) {//展示popWindows
            tabs.add(new TabEntity(getStrings(qualityTypeBeanChange.getName()), R.drawable.solid_top, R.drawable.solid_bottom));
            tabs.add(new TabEntity(getStrings(qualityTypeBeanChange.getSortName()), R.drawable.solid_top, R.drawable.solid_bottom));
        } else { //不展示popWindows
            if (currentTab == 1) {
                tabs.add(new TabEntity(getStrings(qualityTypeBeanChange.getName()), R.drawable.solid_top, R.drawable.solid_bottom));
                tabs.add(new TabEntity(getStrings(qualityTypeBeanChange.getSortName()), R.drawable.solid_bottom, R.drawable.solid_top));
            } else {
                tabs.add(new TabEntity(getStrings(qualityTypeBeanChange.getName()), R.drawable.solid_bottom, R.drawable.solid_top));
                tabs.add(new TabEntity(getStrings(qualityTypeBeanChange.getSortName()), R.drawable.solid_top, R.drawable.solid_bottom));
            }
        }
        ctb_qt_tab_product_type.setTabData(tabs);
    }

    /**
     * 设置商品类型 排序展示
     *
     * @param position
     * @param qualityTypeBean
     */
    private void setProductSort(int position, QualityTypeBean qualityTypeBean) {
        if (position == 0) {
            for (int i = 0; i < productTypeList.size(); i++) {
                QualityTypeBean qualityType = productTypeList.get(i);
                if (qualityType.getId() == qualityTypeBean.getId()) {
                    qualityType.setSelect(true);
                } else {
                    qualityType.setSelect(false);
                }
            }
            productTypeSortAdapter.removeAllHeaderView();
            productTypeSortAdapter.notifyItemRangeRemoved(0, qualityTypeBeans.size());
            qualityTypeBeans.clear();
            qualityTypeBeans.addAll(productTypeList);
            productTypeSortAdapter.addHeaderView(typeSortView);
            productTypeSortAdapter.notifyDataSetChanged();
            qualityTypeBean.setSortType(qualityTypeBeanChange.getSortType());
            qualityTypeBean.setSortName(qualityTypeBeanChange.getSortName());
            if (qualityTypeBean.getChildCategoryList() != null && qualityTypeBean.getChildCategoryList().size() > 0) {
                qualityTypeBean.setChildCategory(String.valueOf(qualityTypeBean.getChildCategoryList().get(0).getId()));
            }
            qualityTypeBeanChange = qualityTypeBean;
            addTotalData();
            setChildProductTypeList(qualityTypeBean);
        } else { // 修改排序
            for (int i = 0; i < sortTypeList.size(); i++) {
                QualityTypeBean qualityType = sortTypeList.get(i);
                if (qualityType.getSortType().equals(qualityTypeBean.getSortType())) {
                    qualityType.setSelect(true);
                } else {
                    qualityType.setSelect(false);
                }
            }
            productTypeSortAdapter.removeAllHeaderView();
            productTypeSortAdapter.notifyItemRangeRemoved(0, qualityTypeBeans.size());
            qualityTypeBeans.clear();
            qualityTypeBeans.addAll(sortTypeList);
            productTypeSortAdapter.notifyItemRangeChanged(0, qualityTypeBeans.size());
            qualityTypeBeanChange.setSortType(qualityTypeBean.getSortType());
            qualityTypeBeanChange.setSortName(qualityTypeBean.getSortName());
        }
    }

    /**
     * 设置子分类
     *
     * @param qualityTypeBean
     */
    private void setChildProductTypeList(QualityTypeBean qualityTypeBean) {
        childTypeList.clear();
        if (qualityTypeBean.getChildCategoryList() != null && qualityTypeBean.getChildCategoryList().size() > 0) {
            QualityTypeBean qualityType;
            for (ChildCategoryListBean childCategoryListBean : qualityTypeBean.getChildCategoryList()) {
                qualityType = new QualityTypeBean();
                qualityType.setChildCategory(String.valueOf(childCategoryListBean.getId()));
                qualityType.setChildName(getStrings(childCategoryListBean.getName()));
                if (qualityTypeBeanChange.getChildCategory().equals(qualityType.getChildCategory())) {
                    qualityType.setSelect(true);
                    qualityTypeBeanChange.setChildName(getStrings(qualityType.getChildName()));
                }
                childTypeList.add(qualityType);
            }
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
        childProductTypeAdapter.notifyDataSetChanged();
    }

    private void setProductSortShow(int position, QualityTypeBean qualityTypeBean) {
        if (position == 0) {
            for (int i = 0; i < productTypeList.size(); i++) {
                QualityTypeBean qualityType = productTypeList.get(i);
                if (qualityType.getId() == qualityTypeBean.getId()) {
                    qualityType.setSelect(true);
                } else {
                    qualityType.setSelect(false);
                }
            }
            productTypeSortAdapter.removeAllHeaderView();
            productTypeSortAdapter.notifyItemRangeRemoved(0, qualityTypeBeans.size());
            qualityTypeBeans.clear();
            qualityTypeBeans.addAll(productTypeList);
            productTypeSortAdapter.addHeaderView(typeSortView);
            productTypeSortAdapter.notifyDataSetChanged();
        } else { // 修改排序
            for (int i = 0; i < sortTypeList.size(); i++) {
                QualityTypeBean qualityType = sortTypeList.get(i);
                if (qualityType.getSortType().equals(qualityTypeBean.getSortType())) {
                    qualityType.setSelect(true);
                } else {
                    qualityType.setSelect(false);
                }
            }
            productTypeSortAdapter.removeAllHeaderView();
            productTypeSortAdapter.notifyItemRangeRemoved(0, qualityTypeBeans.size());
            qualityTypeBeans.clear();
            qualityTypeBeans.addAll(sortTypeList);
            productTypeSortAdapter.notifyItemRangeChanged(0, qualityTypeBeans.size());
        }
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

    @Override
    protected void loadData() {
        if (qualityTypeBeanChange != null) {
            page = 1;
            getSortType();
            getRefreshTypeProData();
        }
    }
    @Override
    protected View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }
    private void getRefreshTypeProData() {
        getProductType();
        getAdTypeData();
        getChildrenType();
    }

    private void getAdTypeData() {
        String url = Url.BASE_URL + Url.Q_QUALITY_TYPE_AD;
        if (NetWorkUtils.checkNet(QualityTypeProductActivity.this)) {
            Map<String, Object> params = new HashMap<>();
            params.put("categoryType", qualityTypeBeanChange.getType());
            params.put("categoryId", qualityTypeBeanChange.getId());
            params.put("vidoShow", "1");
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    smart_communal_refresh.finishRefresh();
                    Gson gson = new Gson();
                    adBeanList.clear();
                    CommunalADActivityEntity qualityAdLoop = gson.fromJson(result, CommunalADActivityEntity.class);
                    if (qualityAdLoop != null) {
                        if (qualityAdLoop.getCode().equals("01")) {
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
                                    .setPointViewVisible(true).setCanScroll(true).setPageIndicator(new int[]{R.drawable.unselected_radius, R.drawable.selected_radius})
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
                public void onError(Throwable ex, boolean isOnCallback) {
                    smart_communal_refresh.finishRefresh();
                    if (adBeanList.size() > 0) {
                        if (headerAdView.getParent() == null) {
                            qualityTypeProductAdapter.addHeaderView(headerAdView);
                        }
                    } else {
                        qualityTypeProductAdapter.removeHeaderView(headerAdView);
                    }
                    super.onError(ex, isOnCallback);
                }
            });
        }
    }

    private void getProductType() {
        if (NetWorkUtils.checkNet(QualityTypeProductActivity.this)) {
            String url = Url.BASE_URL + Url.QUALITY_SHOP_TYPE;
            XUtil.Post(url, null, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    productTypeList.clear();
                    Gson gson = new Gson();
                    QualityTypeEntity qualityTypeEntity = gson.fromJson(result, QualityTypeEntity.class);
                    if (qualityTypeEntity != null) {
                        if (qualityTypeEntity.getCode().equals("01")) {
                            if (qualityTypeEntity.getQualityTypeBeanList() != null
                                    && qualityTypeEntity.getQualityTypeBeanList().size() > 0) {
                                productTypeList.addAll(qualityTypeEntity.getQualityTypeBeanList());
                            }
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    super.onError(ex, isOnCallback);
                }
            });
        }
    }

    private void getSortType() {
        if (NetWorkUtils.checkNet(QualityTypeProductActivity.this)) {
            String url = Url.BASE_URL + Url.Q_SORT_TYPE;
            XUtil.Get(url, null, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    sortTypeList.clear();
                    Gson gson = new Gson();
                    SortTypeEntity sortTypeEntity = gson.fromJson(result, SortTypeEntity.class);
                    if (sortTypeEntity != null) {
                        if (sortTypeEntity.getCode().equals("01")) {
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
                                    qualityTypeBeanChange.setSortType(sortTypeList.get(0).getSortType());
                                    qualityTypeBeanChange.setSortName(sortTypeList.get(0).getSortName());
                                    getQualityTypePro();
                                }
                            }
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    super.onError(ex, isOnCallback);
                }
            });
        }
    }

    private void getQualityTypePro() {
        String url = Url.BASE_URL + Url.Q_PRODUCT_TYPE_LIST;
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("showCount", TOTAL_COUNT_TWENTY);
        if (!TextUtils.isEmpty(qualityTypeBeanChange.getChildCategory())) {
            params.put("id", qualityTypeBeanChange.getChildCategory());
            params.put("pid", qualityTypeBeanChange.getId());
        } else {
            params.put("id", qualityTypeBeanChange.getId());
            params.put("pid", 0);
        }
        params.put("orderTypeId", qualityTypeBeanChange.getSortType());
        NetLoadUtils.getQyInstance().loadNetDataPost(QualityTypeProductActivity.this, url
                , params, new NetLoadUtils.NetLoadListener() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                smart_communal_refresh.finishRefresh();
                qualityTypeProductAdapter.loadMoreComplete();
                if (page == 1) {
                    typeDetails.clear();
                    removeExistUtils.clearData();
                }
                Gson gson = new Gson();
                likedProductEntity = gson.fromJson(result, UserLikedProductEntity.class);
                if (likedProductEntity != null) {
                    if (likedProductEntity.getCode().equals(SUCCESS_CODE)) {
                        typeDetails.addAll(removeExistUtils.removeExistList(likedProductEntity.getLikedProductBeanList()));
                    } else {
                        qualityTypeProductAdapter.loadMoreEnd();
                        showToast(QualityTypeProductActivity.this, likedProductEntity.getMsg());
                    }
                    if (page == 1) {
                        ctb_qt_tab_product_type.setVisibility(View.VISIBLE);
                        setTabIcon(false);
                        qualityTypeProductAdapter.setNewData(typeDetails);
                    } else {
                        qualityTypeProductAdapter.notifyDataSetChanged();
                    }
                }
                NetLoadUtils.getQyInstance().showLoadSir(loadService,typeDetails,likedProductEntity);
            }

            @Override
            public void netClose() {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                smart_communal_refresh.finishRefresh();
                qualityTypeProductAdapter.loadMoreComplete();
                showToast(QualityTypeProductActivity.this, R.string.unConnectedNetwork);
                NetLoadUtils.getQyInstance().showLoadSir(loadService,typeDetails,likedProductEntity);
            }

            @Override
            public void onError(Throwable throwable) {
                smart_communal_refresh.finishRefresh();
                qualityTypeProductAdapter.loadMoreComplete();
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                showToast(QualityTypeProductActivity.this, R.string.invalidData);
                NetLoadUtils.getQyInstance().showLoadSir(loadService,typeDetails,likedProductEntity);
            }
        });
    }

    /**
     * 获取子类类型
     */
    private void getChildrenType() {
        if (NetWorkUtils.checkNet(QualityTypeProductActivity.this)) {
            String url = Url.BASE_URL + Url.Q_PRODUCT_TYPE;
            Map<String, Object> params = new HashMap<>();
            params.put("pid", qualityTypeBeanChange.getId());
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    childTypeList.clear();
                    Gson gson = new Gson();
                    QualityTypeEntity qualityTypeEntity = gson.fromJson(result, QualityTypeEntity.class);
                    if (qualityTypeEntity != null) {
                        if (qualityTypeEntity.getCode().equals("01")) {
                            if (qualityTypeEntity.getQualityTypeBeanList() != null) {
                                for (QualityTypeBean qualityTypeBean : qualityTypeEntity.getQualityTypeBeanList()) {
                                    qualityTypeBean.setChildCategory(String.valueOf(qualityTypeBean.getId()));
                                    qualityTypeBean.setChildName(getStrings(qualityTypeBean.getName()));
                                }
                                childTypeList.addAll(qualityTypeEntity.getQualityTypeBeanList());
                                if (childTypeList.size() > 0 && !TextUtils.isEmpty(qualityTypeBeanChange.getChildCategory())) {
                                    for (QualityTypeBean qualityTypeBean : childTypeList) {
                                        if (qualityTypeBeanChange.getChildCategory().equals(qualityTypeBean.getChildCategory())) {
                                            qualityTypeBean.setSelect(true);
                                            qualityTypeBeanChange.setChildName(getStrings(qualityTypeBean.getChildName()));
                                            break;
                                        }
                                    }
                                }
                            }
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
                        childProductTypeAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    super.onError(ex, isOnCallback);
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
                }
            });
        }
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
                communal_recycler_wrap.setPadding(AutoSizeUtils.mm2px(mAppContext,20), 0, 0, AutoSizeUtils.mm2px(mAppContext,20));
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
        if (qualityTypeBeanChange != null) {
            qualityTypeBeanChange.setChildCategory(getStrings(qualityTypeBean.getChildCategory()));
            qualityTypeBeanChange.setChildName(qualityTypeBean.getChildName());
        }
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

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (START_AUTO_PAGE_TURN.equals(message.type)) {
            if (adBeanList.size() > 0 && qTypeView.ad_communal_banner != null && !qTypeView.ad_communal_banner.isTurning()) {
                qTypeView.ad_communal_banner.setCanScroll(true);
                qTypeView.ad_communal_banner.startTurning(getShowNumber(adBeanList.get(0).getShowTime()) * 1000);
                qTypeView.ad_communal_banner.setPointViewVisible(true);
            }
        } else if (STOP_AUTO_PAGE_TURN.equals(message.type)) {
            if (qTypeView.ad_communal_banner != null && !qTypeView.ad_communal_banner.isTurning()) {
                qTypeView.ad_communal_banner.setCanScroll(false);
                qTypeView.ad_communal_banner.stopTurning();
                qTypeView.ad_communal_banner.setPointViewVisible(false);
            }
        }
    }

    @OnClick(R.id.tv_ql_type_back)
    void goBack(View view) {
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        addTotalData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        addTotalData();
    }

    private void addTotalData() {
        if (totalPersonalTrajectory != null) {
            Map<String, String> totalMap = new HashMap<>();
            totalMap.put("categoryId", String.valueOf(qualityTypeBeanChange.getId()));
            totalPersonalTrajectory.stopTotal(totalMap);
        }
    }
}
