package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.DMLThemeEntity;
import com.amkj.dmsh.bean.DMLThemeEntity.DMLThemeBean;
import com.amkj.dmsh.bean.DMLThemeEntity.DMLThemeBean.DMLGoodsBean;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.BaseAddCarProInfoBean;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.dominant.adapter.QualityOsMailHeaderAdapter;
import com.amkj.dmsh.dominant.adapter.QualityTypeProductAdapter;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.mine.activity.ShopCarActivity;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.chad.library.adapter.base.BaseQuickAdapter;
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
import q.rorbin.badgeview.Badge;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getShowNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.START_AUTO_PAGE_TURN;
import static com.amkj.dmsh.constant.ConstantVariable.STOP_AUTO_PAGE_TURN;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;

;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/29
 * class description:海外直邮
 */

public class QualityOverseasMailActivity extends BaseActivity {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.iv_img_service)
    ImageView iv_img_service;
    @BindView(R.id.iv_img_share)
    ImageView iv_img_share;
    @BindView(R.id.fl_header_service)
    FrameLayout fl_header_service;
    private int scrollY;
    private float screenHeight;
    private List<LikedProductBean> typeDetails = new ArrayList();
    private QualityTypeProductAdapter qualityTypeProductAdapter;
    private int themePage = 1;
    private int productPage = 1;
    private List<DMLThemeBean> themeList = new ArrayList();
    private List<CommunalADActivityBean> adBeanList = new ArrayList<>();

    //    头部
    private QualityOsMailHeaderAdapter qualityOsMailHeaderAdapter;
    private OverseasHeaderView overseasHeaderView;
    private Badge badge;
    private String type = "overseas";
    private String categoryType;
    private String categoryId;
    private String categoryName;
    private boolean isLoadThemeData = true;
    private boolean isLoadProData = true;
    private CBViewHolderCreator cbViewHolderCreator;

    @Override
    protected int getContentView() {
        return R.layout.activity_communal_ql_product_type;
    }

    @Override
    protected void initViews() {
        iv_img_share.setVisibility(View.GONE);
        Intent intent = getIntent();
        categoryType = intent.getStringExtra("categoryType");
        categoryId = intent.getStringExtra("categoryId");
        categoryName = intent.getStringExtra("categoryName");
        tv_header_titleAll.setText("");
        if (!TextUtils.isEmpty(categoryName)) {
            tv_header_titleAll.setText(categoryName);
        }
        iv_img_service.setImageResource(R.drawable.shop_car_gray_icon);
        communal_recycler.setLayoutManager(new GridLayoutManager(QualityOverseasMailActivity.this, 2));
        View headerView = LayoutInflater.from(QualityOverseasMailActivity.this)
                .inflate(R.layout.layout_overseas_mail_header, (ViewGroup) communal_recycler.getParent(), false);
        overseasHeaderView = new OverseasHeaderView();
        ButterKnife.bind(overseasHeaderView, headerView);
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

        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            loadData();
        });
        qualityTypeProductAdapter = new QualityTypeProductAdapter(QualityOverseasMailActivity.this, typeDetails);
        qualityTypeProductAdapter.addHeaderView(headerView);
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
        qualityTypeProductAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
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
                                constantMethod.addShopCarGetSku(QualityOverseasMailActivity.this, baseAddCarProInfoBean, loadHud);
                                constantMethod.setAddOnCarListener(new ConstantMethod.OnAddCarListener() {
                                    @Override
                                    public void onAddCarSuccess() {
                                        getCarCount();
                                    }
                                });
                                break;
                        }
                    } else {
                        loadHud.dismiss();
                        getLoginStatus(QualityOverseasMailActivity.this);
                    }
                }
            }
        });
        qualityTypeProductAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LikedProductBean likedProductBean = (LikedProductBean) view.getTag();
                if (likedProductBean != null) {
                    Intent intent = new Intent(QualityOverseasMailActivity.this, ShopScrollDetailsActivity.class);
                    intent.putExtra("productId", String.valueOf(likedProductBean.getId()));
                    startActivity(intent);
                }
            }
        });
        overseasHeaderView.communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(QualityOverseasMailActivity.this));
        overseasHeaderView.communal_recycler_wrap.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_product)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
        qualityOsMailHeaderAdapter = new QualityOsMailHeaderAdapter(QualityOverseasMailActivity.this, themeList, type);
        overseasHeaderView.communal_recycler_wrap.setAdapter(qualityOsMailHeaderAdapter);
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
        download_btn_communal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) communal_recycler.getLayoutManager();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                int mVisibleCount = linearLayoutManager.findLastVisibleItemPosition()
                        - linearLayoutManager.findFirstVisibleItemPosition() + 1;
                if (firstVisibleItemPosition > mVisibleCount) {
                    communal_recycler.scrollToPosition(mVisibleCount);
                }
                communal_recycler.smoothScrollToPosition(0);
            }
        });
        badge = ConstantMethod.getBadge(QualityOverseasMailActivity.this, fl_header_service);
    }

    //    海外直邮商品列表
    private void getOverseasProData() {
        String url = Url.BASE_URL + Url.QUALITY_OVERSEAS_LIST;
        if (NetWorkUtils.checkNet(QualityOverseasMailActivity.this)) {
            Map<String, Object> params = new HashMap<>();
            params.put("showCount", DEFAULT_TOTAL_COUNT);
            params.put("currentPage", productPage);
            if (userId > 0) {
                params.put("uid", userId);
            }
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    qualityTypeProductAdapter.loadMoreComplete();
                    if (productPage == 1) {
                        //重新加载数据
                        typeDetails.clear();
                    }
                    Gson gson = new Gson();
                    UserLikedProductEntity likedProductEntity = gson.fromJson(result, UserLikedProductEntity.class);
                    if (likedProductEntity != null) {
                        if (likedProductEntity.getCode().equals(SUCCESS_CODE)) {
                            typeDetails.addAll(likedProductEntity.getLikedProductBeanList());
                            qualityTypeProductAdapter.notifyDataSetChanged();
                        } else if (likedProductEntity.getCode().equals(EMPTY_CODE)) {
                            isLoadProData = false;
                            qualityTypeProductAdapter.loadMoreEnd();
                        } else {
                            isLoadProData = false;
                            qualityTypeProductAdapter.loadMoreEnd();
                            showToast(QualityOverseasMailActivity.this, likedProductEntity.getMsg());
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    isLoadProData = false;
                    qualityTypeProductAdapter.loadMoreComplete();
                    super.onError(ex, isOnCallback);
                }
            });
        }
    }

    //    海外直邮主题商品列表
    private void getOverseasThemeData() {
        String url = Url.BASE_URL + Url.QUALITY_OVERSEAS_THEME;
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", themePage);
        params.put("showCount", DEFAULT_TOTAL_COUNT);
        params.put("goodsCurrentPage", 1);
        params.put("goodsShowCount", 8);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getQyInstance().loadNetDataPost(QualityOverseasMailActivity.this, url
                , params, new NetLoadUtils.NetLoadListener() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        qualityTypeProductAdapter.loadMoreComplete();
                        if (themePage == 1) {
                            themeList.clear();
                        }
                        Gson gson = new Gson();
                        DMLThemeEntity dmlTheme = gson.fromJson(result, DMLThemeEntity.class);
                        NetLoadUtils.getQyInstance().showLoadSirSuccess(loadService);
                        if (dmlTheme != null) {
                            if (dmlTheme.getCode().equals(SUCCESS_CODE)) {
                                for (int i = 0; i < dmlTheme.getThemeList().size(); i++) {
                                    DMLThemeBean dmlThemeBean = dmlTheme.getThemeList().get(i);
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
                            } else if (dmlTheme.getCode().equals(EMPTY_CODE)) {
                                isLoadThemeData = false;
                                if (themePage == 1) {
                                    overseasHeaderView.communal_recycler_wrap.setVisibility(View.GONE);
                                } else {
                                    overseasHeaderView.communal_recycler_wrap.setVisibility(View.VISIBLE);
                                }
                            } else {
                                showToast(QualityOverseasMailActivity.this, dmlTheme.getMsg());
                            }
                        }
                    }

                    @Override
                    public void netClose() {
                        NetLoadUtils.getQyInstance().showLoadSirSuccess(loadService);
                        smart_communal_refresh.finishRefresh();
                        qualityTypeProductAdapter.loadMoreComplete();
                        showToast(QualityOverseasMailActivity.this, R.string.unConnectedNetwork);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        NetLoadUtils.getQyInstance().showLoadSirSuccess(loadService);
                        smart_communal_refresh.finishRefresh();
                        qualityTypeProductAdapter.loadMoreComplete();
                        showToast(QualityOverseasMailActivity.this, R.string.invalidData);
                    }
                });
    }

    @Override
    protected void postEventResult(@NonNull EventMessage message) {
        if (START_AUTO_PAGE_TURN.equals(message.type)) {
            if (adBeanList.size() > 0 && overseasHeaderView.ad_communal_banner != null && !overseasHeaderView.ad_communal_banner.isTurning()) {
                overseasHeaderView.ad_communal_banner.setCanScroll(true);
                overseasHeaderView.ad_communal_banner.startTurning(getShowNumber(adBeanList.get(0).getShowTime()) * 1000);
                overseasHeaderView.ad_communal_banner.setPointViewVisible(true);
            }
        } else if (STOP_AUTO_PAGE_TURN.equals(message.type)) {
            if (overseasHeaderView.ad_communal_banner != null && overseasHeaderView.ad_communal_banner.isTurning()) {
                overseasHeaderView.ad_communal_banner.setCanScroll(false);
                overseasHeaderView.ad_communal_banner.stopTurning();
                overseasHeaderView.ad_communal_banner.setPointViewVisible(false);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCarCount();
    }

    private void getCarCount() {
        if (userId > 0) {
            //购物车数量展示
            String url = Url.BASE_URL + Url.Q_QUERY_CAR_COUNT;
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                    if (requestStatus != null) {
                        if (requestStatus.getCode().equals("01")) {
                            int cartNumber = requestStatus.getResult().getCartNumber();
                            badge.setBadgeNumber(cartNumber);
                        } else if (!requestStatus.getCode().equals("02")) {
                            showToast(QualityOverseasMailActivity.this, requestStatus.getMsg());
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            productPage = 1;
            getOverseasProData();
            getCarCount();
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

    @Override
    protected void loadData() {
        themePage = 1;
        productPage = 1;
        getOverseasThemeData();
        getOverseasProData();
        if (!TextUtils.isEmpty(categoryName) && !TextUtils.isEmpty(categoryId)) {
            getAdTypeData();
        }
    }

    private void getAdTypeData() {
        String url = Url.BASE_URL + Url.Q_QUALITY_TYPE_AD;
        if (NetWorkUtils.checkNet(QualityOverseasMailActivity.this)) {
            Map<String, Object> params = new HashMap<>();
            params.put("categoryType", categoryType);
            params.put("categoryId", categoryId);
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
                            overseasHeaderView.rel_communal_banner.setVisibility(View.VISIBLE);
                            if (cbViewHolderCreator == null) {
                                cbViewHolderCreator = new CBViewHolderCreator() {
                                    @Override
                                    public Holder createHolder(View itemView) {
                                        return new CommunalAdHolderView(itemView, QualityOverseasMailActivity.this, true);
                                    }

                                    @Override
                                    public int getLayoutId() {
                                        return R.layout.layout_ad_image_video;
                                    }
                                };
                            }
                            overseasHeaderView.ad_communal_banner.setPages(QualityOverseasMailActivity.this, cbViewHolderCreator, adBeanList).setCanLoop(true)
                                    .setPointViewVisible(true).setCanScroll(true).setPageIndicator(new int[]{R.drawable.unselected_radius, R.drawable.selected_radius})
                                    .startTurning(getShowNumber(adBeanList.get(0).getShowTime()) * 1000);
                        } else {
                            if (adBeanList.size() < 1) {
                                overseasHeaderView.rel_communal_banner.setVisibility(View.GONE);
                            }
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    smart_communal_refresh.finishRefresh();
                    super.onError(ex, isOnCallback);
                }
            });
        }
    }

    @OnClick(R.id.iv_img_service)
    void skipShopCar(View view) {
        Intent intent = new Intent(QualityOverseasMailActivity.this, ShopCarActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    class OverseasHeaderView {
        @BindView(R.id.communal_recycler_wrap)
        RecyclerView communal_recycler_wrap;
        @BindView(R.id.rel_communal_banner)
        public RelativeLayout rel_communal_banner;
        @BindView(R.id.ad_communal_banner)
        public ConvenientBanner ad_communal_banner;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
