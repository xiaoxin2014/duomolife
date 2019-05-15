package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.BaseAddCarProInfoBean;
import com.amkj.dmsh.constant.CommunalDetailBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.adapter.QualityBuyListAdapter;
import com.amkj.dmsh.dominant.bean.QualityBuyListEntity;
import com.amkj.dmsh.dominant.bean.QualityBuyListEntity.QualityBuyListBean;
import com.amkj.dmsh.dominant.bean.ShopBuyDetailEntity;
import com.amkj.dmsh.dominant.bean.ShopBuyDetailEntity.ShopBuyDetailBean;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.mine.activity.ShopCarActivity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
import com.amkj.dmsh.utils.webformatdata.ShareDataBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.umeng.socialize.UMShareAPI;

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
import static com.amkj.dmsh.constant.ConstantMethod.getBadge;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.insertNewTotalData;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.constant.Url.QUALITY_WEEK_OPTIMIZED_DETAIL;
import static com.amkj.dmsh.constant.Url.QUALITY_WEEK_OPTIMIZED_PRO;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/9/15
 * class description:每周优选
 */
public class QualityWeekOptimizedActivity extends BaseActivity {
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
    @BindView(R.id.fl_header_service)
    FrameLayout fl_header_service;
    private int page = 1;
    private int scrollY;
    private float screenHeight;
    private List<QualityBuyListBean> qualityBuyListBeanList = new ArrayList();
    private List<CommunalDetailObjectBean> itemDescriptionList = new ArrayList();
    private QualityBuyListAdapter qualityBuyListAdapter;
    private CommunalDetailAdapter communalDetailAdapter;
    private ShopBuyListView shopBuyListView;
    private Badge badge;
    private ShopBuyDetailBean shopBuyDetailBean;
    private QualityBuyListEntity qualityBuyListEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_communal_ql_shop_car;
    }

    @Override
    protected void initViews() {
        tv_header_titleAll.setText("每周优选");
        iv_img_service.setImageResource(R.drawable.shop_car_gray_icon);
        communal_recycler.setLayoutManager(new LinearLayoutManager(QualityWeekOptimizedActivity.this));

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
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_two_px)
                .create());
        qualityBuyListAdapter = new QualityBuyListAdapter(QualityWeekOptimizedActivity.this, qualityBuyListBeanList);
        qualityBuyListAdapter.setHeaderAndEmpty(true);
        View headerView = LayoutInflater.from(QualityWeekOptimizedActivity.this).inflate(R.layout.layout_communal_detail_scroll_rec_cover_wrap, null);
        shopBuyListView = new ShopBuyListView();
        ButterKnife.bind(shopBuyListView, headerView);
        shopBuyListView.initView();
        qualityBuyListAdapter.addHeaderView(headerView);
        communal_recycler.setAdapter(qualityBuyListAdapter);
        qualityBuyListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getBuyListRecommend();
            }
        }, communal_recycler);
        qualityBuyListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                QualityBuyListBean qualityBuyListBean = (QualityBuyListBean) view.getTag();
                if (qualityBuyListBean != null) {
                    Intent intent = new Intent(QualityWeekOptimizedActivity.this, ShopScrollDetailsActivity.class);
                    intent.putExtra("productId", String.valueOf(qualityBuyListBean.getId()));
                    startActivity(intent);
                }
            }
        });
        qualityBuyListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                loadHud.show();
                QualityBuyListBean qualityBuyListBean = (QualityBuyListBean) view.getTag();
                if (qualityBuyListBean != null) {
                    if (userId > 0) {
                        switch (view.getId()) {
                            case R.id.iv_ql_bl_add_car:
                                BaseAddCarProInfoBean baseAddCarProInfoBean = new BaseAddCarProInfoBean();
                                baseAddCarProInfoBean.setProductId(qualityBuyListBean.getId());
                                baseAddCarProInfoBean.setActivityCode(getStrings(qualityBuyListBean.getActivityCode()));
                                baseAddCarProInfoBean.setProName(getStrings(qualityBuyListBean.getName()));
                                baseAddCarProInfoBean.setProPic(getStrings(qualityBuyListBean.getPicUrl()));
                                ConstantMethod constantMethod = new ConstantMethod();
                                constantMethod.addShopCarGetSku(QualityWeekOptimizedActivity.this, baseAddCarProInfoBean, loadHud);
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
                        getLoginStatus(QualityWeekOptimizedActivity.this);
                    }
                }
            }
        });
        badge = getBadge(QualityWeekOptimizedActivity.this, fl_header_service);
        totalPersonalTrajectory = insertNewTotalData(QualityWeekOptimizedActivity.this);
    }

    private void getCarCount() {
        if (userId > 0) {
            //购物车数量展示
            String url = Url.BASE_URL + Url.Q_QUERY_CAR_COUNT;
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                    if (requestStatus != null) {
                        if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                            int cartNumber = requestStatus.getResult().getCartNumber();
                            badge.setBadgeNumber(cartNumber);
                        } else if (!requestStatus.getCode().equals(EMPTY_CODE)) {
                            showToastRequestMsg(QualityWeekOptimizedActivity.this, requestStatus);
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCarCount();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            loadData();
        }
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void loadData() {
        page = 1;
        getCarCount();
        getBuyListDetailData();
    }

    @Override
    protected View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getBuyListDetailData() {
        NetLoadUtils.getNetInstance().loadNetDataPost(this, QUALITY_WEEK_OPTIMIZED_DETAIL, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                itemDescriptionList.clear();
                Gson gson = new Gson();
                ShopBuyDetailEntity shopDetailsEntity = gson.fromJson(result, ShopBuyDetailEntity.class);
                if (shopDetailsEntity != null) {
                    if (shopDetailsEntity.getCode().equals(SUCCESS_CODE)) {
                        shopBuyDetailBean = shopDetailsEntity.getShopBuyDetailBean();
                        //记录埋点参数sourceId(每周优选id)
                        ConstantMethod.saveSourceId(getSimpleName(), String.valueOf(shopBuyDetailBean.getId()));
//                        //配置封面图
                        GlideImageLoaderUtil.loadImgDynamicDrawable(QualityWeekOptimizedActivity.this, shopBuyListView.iv_communal_cover_wrap
                                , shopBuyDetailBean.getCoverImgUrl());
                        List<CommunalDetailBean> descriptionBeanList = shopBuyDetailBean.getDescriptionBeanList();
                        if (descriptionBeanList != null) {
//                            //筛选空行
//                            Iterator<CommunalDetailBean> iterator = descriptionBeanList.iterator();
//                            while (iterator.hasNext()) {
//                                CommunalDetailBean bean = iterator.next();
//                                if ("text".equals(bean.getType()) && bean.getContent() instanceof String && "<p><br/></p>".equals(bean.getContent())) {
//                                    iterator.remove();
//                                }
//                            }

                            itemDescriptionList.addAll(CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(descriptionBeanList));
                        }
                    } else if (!shopDetailsEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(QualityWeekOptimizedActivity.this, shopDetailsEntity.getMsg());
                    }
                    communalDetailAdapter.setNewData(itemDescriptionList);
                    getBuyListRecommend();
                }
            }

            @Override
            public void onNotNetOrException() {
                getBuyListRecommend();
            }
        });
    }

    private void getBuyListRecommend() {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("showCount", TOTAL_COUNT_TWENTY);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(QualityWeekOptimizedActivity.this, QUALITY_WEEK_OPTIMIZED_PRO
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        qualityBuyListAdapter.loadMoreComplete();
                        smart_communal_refresh.finishRefresh();
                        if (page == 1) {
                            qualityBuyListBeanList.clear();
                        }
                        Gson gson = new Gson();
                        qualityBuyListEntity = gson.fromJson(result, QualityBuyListEntity.class);
                        if (qualityBuyListEntity != null) {
                            if (qualityBuyListEntity.getCode().equals(SUCCESS_CODE)) {
                                qualityBuyListBeanList.addAll(qualityBuyListEntity.getQualityBuyListBeanList());
                            } else if (qualityBuyListEntity.getCode().equals(EMPTY_CODE)) {
                                qualityBuyListAdapter.loadMoreEnd();
                            } else {
                                showToast(QualityWeekOptimizedActivity.this, qualityBuyListEntity.getMsg());
                            }
                            qualityBuyListAdapter.notifyDataSetChanged();
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, qualityBuyListBeanList, qualityBuyListEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        qualityBuyListAdapter.loadMoreEnd(true);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, qualityBuyListBeanList, qualityBuyListEntity);
                    }
                });
    }

    class ShopBuyListView {
        @BindView(R.id.communal_recycler_wrap)
        RecyclerView communal_recycler_wrap;
        @BindView(R.id.iv_communal_cover_wrap)
        ImageView iv_communal_cover_wrap;

        public void initView() {
            iv_communal_cover_wrap.setVisibility(View.VISIBLE);
            communal_recycler_wrap.setNestedScrollingEnabled(false);
            communal_recycler_wrap.setLayoutManager(new LinearLayoutManager((QualityWeekOptimizedActivity.this)));
            communalDetailAdapter = new CommunalDetailAdapter(QualityWeekOptimizedActivity.this, itemDescriptionList);
            communal_recycler_wrap.setAdapter(communalDetailAdapter);
            communalDetailAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    ShareDataBean shareDataBean = null;
                    if (view.getId() == R.id.tv_communal_share && shopBuyDetailBean != null) {
                        shareDataBean = new ShareDataBean(shopBuyDetailBean.getCoverImgUrl()
                                , "每周优选"
                                , "摸透你的心，为你精选最应季最实用最优质的热门精品"
                                , Url.BASE_SHARE_PAGE_TWO + "m/template/home/weekly_optimization.html");

                    }
                    CommunalWebDetailUtils.getCommunalWebInstance()
                            .setWebDataClick(QualityWeekOptimizedActivity.this, shareDataBean, view, loadHud);
                }
            });
            communalDetailAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    CommunalDetailObjectBean communalDetailBean = (CommunalDetailObjectBean) view.getTag();
                    if (communalDetailBean != null) {
                        ConstantMethod.skipProductUrl(QualityWeekOptimizedActivity.this, communalDetailBean.getItemTypeId(), communalDetailBean.getId());
                    }
                }
            });
        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    @OnClick(R.id.iv_img_service)
    void skipService(View view) {
        Intent intent = new Intent(QualityWeekOptimizedActivity.this, ShopCarActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.iv_img_share)
    void setShare(View view) {
        if (shopBuyDetailBean != null) {
            new UMShareAction(QualityWeekOptimizedActivity.this
                    , shopBuyDetailBean.getCoverImgUrl()
                    , "每周优选"
                    , "摸透你的心，为你精选最应季最实用最优质的热门精品"
                    , Url.BASE_SHARE_PAGE_TWO + "m/template/home/weekly_optimization.html"
                    , "pages/weekly_optimization/weekly_optimization", shopBuyDetailBean.getId());
        }
    }
}
