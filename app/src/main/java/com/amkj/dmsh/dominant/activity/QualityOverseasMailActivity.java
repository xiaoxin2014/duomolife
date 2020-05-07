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
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.DMLThemeEntity;
import com.amkj.dmsh.bean.DMLThemeEntity.DMLThemeBean;
import com.amkj.dmsh.bean.DMLThemeEntity.DMLThemeBean.DMLGoodsBean;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.dominant.adapter.GoodProductAdapter;
import com.amkj.dmsh.dominant.adapter.QualityOsMailHeaderAdapter;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.mine.activity.ShopCarActivity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.chad.library.adapter.base.BaseQuickAdapter;
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
import q.rorbin.badgeview.Badge;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.constant.ConstantMethod.getShowNumber;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.constant.Url.QUALITY_OVERSEAS_LIST;
import static com.amkj.dmsh.constant.Url.QUALITY_OVERSEAS_THEME;
import static com.amkj.dmsh.constant.Url.Q_QUALITY_TYPE_AD;
import static com.amkj.dmsh.dao.OrderDao.getCarCount;

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
    private GoodProductAdapter qualityTypeProductAdapter;
    private int themePage = 1;
    private int productPage = 1;
    private List<DMLThemeBean> themeList = new ArrayList();
    private List<CommunalADActivityBean> adBeanList = new ArrayList<>();

    //    头部
    private QualityOsMailHeaderAdapter qualityOsMailHeaderAdapter;
    private OverseasHeaderView overseasHeaderView;
    private Badge badge;
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
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp).create());

        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData();
            }
        });
        qualityTypeProductAdapter = new GoodProductAdapter(QualityOverseasMailActivity.this, typeDetails);
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
        overseasHeaderView.communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(QualityOverseasMailActivity.this));
        overseasHeaderView.communal_recycler_wrap.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_gray_f)


                .create());
        qualityOsMailHeaderAdapter = new QualityOsMailHeaderAdapter(QualityOverseasMailActivity.this, themeList, "overseas");
        overseasHeaderView.communal_recycler_wrap.setAdapter(qualityOsMailHeaderAdapter);
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
        Map<String, Object> params = new HashMap<>();
        params.put("showCount", TOTAL_COUNT_TWENTY);
        params.put("currentPage", productPage);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this, QUALITY_OVERSEAS_LIST, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                qualityTypeProductAdapter.loadMoreComplete();
                if (productPage == 1) {
                    //重新加载数据
                    typeDetails.clear();
                }

                UserLikedProductEntity likedProductEntity = GsonUtils.fromJson(result, UserLikedProductEntity.class);
                if (likedProductEntity != null) {
                    if (likedProductEntity.getCode().equals(SUCCESS_CODE)) {
                        typeDetails.addAll(likedProductEntity.getGoodsList());
                        qualityTypeProductAdapter.notifyDataSetChanged();
                    } else if (likedProductEntity.getCode().equals(EMPTY_CODE)) {
                        isLoadProData = false;
                        qualityTypeProductAdapter.loadMoreEnd();
                    } else {
                        isLoadProData = false;
                        qualityTypeProductAdapter.loadMoreEnd();
                        showToast( likedProductEntity.getMsg());
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                isLoadProData = false;
                qualityTypeProductAdapter.loadMoreEnd(true);
            }
        });
    }

    //    海外直邮主题商品列表
    private void getOverseasThemeData() {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", themePage);
        params.put("showCount", TOTAL_COUNT_TWENTY);
        params.put("goodsCurrentPage", 1);
        params.put("goodsShowCount", 8);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(QualityOverseasMailActivity.this, QUALITY_OVERSEAS_THEME
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        qualityTypeProductAdapter.loadMoreComplete();
                        if (themePage == 1) {
                            themeList.clear();
                        }

                        DMLThemeEntity dmlTheme = GsonUtils.fromJson(result, DMLThemeEntity.class);
                        NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
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
                                showToast( dmlTheme.getMsg());
                            }
                        }
                    }

                    @Override
                    public void onNotNetOrException() {
                        NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
                        smart_communal_refresh.finishRefresh();
                        qualityTypeProductAdapter.loadMoreEnd(true);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showToast( R.string.invalidData);
                    }
                });
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
            getCarCount(getActivity());
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
        String url = Q_QUALITY_TYPE_AD;
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
                                .setPointViewVisible(true).setPageIndicator(new int[]{R.drawable.unselected_radius, R.drawable.selected_radius})
                                .startTurning(getShowNumber(adBeanList.get(0).getShowTime()) * 1000);
                    } else {
                        if (adBeanList.size() < 1) {
                            overseasHeaderView.rel_communal_banner.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                smart_communal_refresh.finishRefresh();
            }
        });
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
}
