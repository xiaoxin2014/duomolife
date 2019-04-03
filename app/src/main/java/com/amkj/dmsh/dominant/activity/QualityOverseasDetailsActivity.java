package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.amkj.dmsh.dominant.adapter.QualityTypeProductAdapter;
import com.amkj.dmsh.dominant.bean.QualityShopDescripEntity;
import com.amkj.dmsh.dominant.bean.QualityShopDescripEntity.QualityShopDescBean;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.mine.activity.ShopCarActivity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
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
import static com.amkj.dmsh.constant.Url.QUALITY_OVERSEAS_DETAIL_LIST;
import static com.amkj.dmsh.constant.Url.Q_QUERY_CAR_COUNT;

;

public class QualityOverseasDetailsActivity extends BaseActivity {
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
    private List<LikedProductBean> proDetailList = new ArrayList();
    //    详细描述
    private List<CommunalDetailObjectBean> descripDetailList = new ArrayList<>();
    private String overseasId;
    private QualityTypeProductAdapter qualityTypeProductAdapter;
    private int page = 1;
    private int scrollY;
    private float screenHeight;
    private OverseasHeaderView overseasHeaderView;
    private QualityShopDescripEntity qualityShopDescripEntity;
    private CommunalDetailAdapter communalDescripAdapter;
    private Badge badge;
    private UserLikedProductEntity userLikedProductEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_communal_ql_shop_car;
    }

    @Override
    protected void initViews() {
        tv_header_titleAll.setText("");
        iv_img_service.setImageResource(R.drawable.shop_car_gray_icon);
        Intent intent = getIntent();
        overseasId = intent.getStringExtra("overseasId");
        View headerView = LayoutInflater.from(QualityOverseasDetailsActivity.this)
                .inflate(R.layout.layout_cover_title_descrip_header, (ViewGroup) communal_recycler.getParent(), false);
        overseasHeaderView = new OverseasHeaderView();
        ButterKnife.bind(overseasHeaderView, headerView);
        overseasHeaderView.initView();
        communal_recycler.setLayoutManager(new GridLayoutManager(QualityOverseasDetailsActivity.this, 2));
        qualityTypeProductAdapter = new QualityTypeProductAdapter(QualityOverseasDetailsActivity.this, proDetailList);
        qualityTypeProductAdapter.addHeaderView(headerView);
        communal_recycler.setAdapter(qualityTypeProductAdapter);
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp)


                .create());
        qualityTypeProductAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                loadHud.show();
                LikedProductBean likedProductBean = (LikedProductBean) view.getTag();
                if (likedProductBean != null) {
                    switch (view.getId()) {
                        case R.id.iv_pro_add_car:
                            if (userId > 0) {
                                BaseAddCarProInfoBean baseAddCarProInfoBean = new BaseAddCarProInfoBean();
                                baseAddCarProInfoBean.setProductId(likedProductBean.getId());
                                baseAddCarProInfoBean.setActivityCode(getStrings(likedProductBean.getActivityCode()));
                                baseAddCarProInfoBean.setProName(getStrings(likedProductBean.getName()));
                                baseAddCarProInfoBean.setProPic(getStrings(likedProductBean.getPicUrl()));
                                ConstantMethod constantMethod = new ConstantMethod();
                                constantMethod.addShopCarGetSku(QualityOverseasDetailsActivity.this, baseAddCarProInfoBean, loadHud);
                                constantMethod.setAddOnCarListener(new ConstantMethod.OnAddCarListener() {
                                    @Override
                                    public void onAddCarSuccess() {
                                        getCarCount();
                                    }
                                });
                            } else {
                                loadHud.dismiss();
                                getLoginStatus(QualityOverseasDetailsActivity.this);
                            }
                            break;
                    }
                }
            }
        });
        qualityTypeProductAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //跳转自营商品详情
                LikedProductBean likedProductBean = (LikedProductBean) view.getTag();
                if (likedProductBean != null) {
                    Intent intent = new Intent(QualityOverseasDetailsActivity.this, ShopScrollDetailsActivity.class);
                    intent.putExtra("productId", String.valueOf(likedProductBean.getId()));
                    startActivity(intent);
                }
            }
        });

        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData();
                qualityTypeProductAdapter.loadMoreEnd(true);
            }
        });
        qualityTypeProductAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getRecommendData();
            }
        }, communal_recycler);

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
        badge = ConstantMethod.getBadge(QualityOverseasDetailsActivity.this, fl_header_service);
        totalPersonalTrajectory = insertNewTotalData(QualityOverseasDetailsActivity.this, overseasId);
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
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
        page = 1;
        //头部信息
        getOverseasThemeDetailsData();
        //商品列表
        getRecommendData();
//        购物车数量
        getCarCount();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCarCount();
    }

    private void getOverseasThemeDetailsData() {
        String url = Url.BASE_URL + Url.QUALITY_OVERSEAS_THEME_DETAIL;
        Map<String, Object> params = new HashMap<>();
        params.put("id", overseasId);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this,url, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    smart_communal_refresh.finishRefresh();
                    qualityTypeProductAdapter.loadMoreComplete();
                    Gson gson = new Gson();
                    qualityShopDescripEntity = gson.fromJson(result, QualityShopDescripEntity.class);
                    if (qualityShopDescripEntity != null) {
                        if (qualityShopDescripEntity.getCode().equals(SUCCESS_CODE)) {
                            setData(qualityShopDescripEntity.getQualityShopDescBean());
                        } else if (qualityShopDescripEntity.getCode().equals(EMPTY_CODE)) {
                            if (page == 1) {
                                showToast(QualityOverseasDetailsActivity.this, R.string.invalidData);
                            }
                        } else {
                            showToast(QualityOverseasDetailsActivity.this, qualityShopDescripEntity.getMsg());
                        }
                    }
                }

            @Override
            public void onNotNetOrException() {
                smart_communal_refresh.finishRefresh();
                qualityTypeProductAdapter.loadMoreComplete();
            }

            @Override
                public void onError(Throwable ex) {
                    smart_communal_refresh.finishRefresh();
                    qualityTypeProductAdapter.loadMoreComplete();
                    showToast(QualityOverseasDetailsActivity.this, R.string.invalidData);
                }

            @Override
            public void netClose() {
                showToast(QualityOverseasDetailsActivity.this, R.string.unConnectedNetwork);
            }
        });
    }

    private void getRecommendData() {
        Map<String, Object> params = new HashMap<>();
        params.put("showCount", TOTAL_COUNT_TWENTY);
        params.put("currentPage", page);
        params.put("id", overseasId);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(QualityOverseasDetailsActivity.this, QUALITY_OVERSEAS_DETAIL_LIST
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        qualityTypeProductAdapter.loadMoreComplete();
                        Gson gson = new Gson();
                        if (page == 1) {
                            //重新加载数据
                            proDetailList.clear();
                        }
                        userLikedProductEntity = gson.fromJson(result, UserLikedProductEntity.class);
                        if (userLikedProductEntity != null) {
                            if (userLikedProductEntity.getCode().equals(SUCCESS_CODE)) {
                                proDetailList.addAll(userLikedProductEntity.getLikedProductBeanList());
                            } else if (userLikedProductEntity.getCode().equals(EMPTY_CODE)) {
                                qualityTypeProductAdapter.loadMoreEnd();
                            } else {
                                showToast(QualityOverseasDetailsActivity.this, userLikedProductEntity.getMsg());
                            }
                            qualityTypeProductAdapter.notifyDataSetChanged();
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, proDetailList, userLikedProductEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        qualityTypeProductAdapter.loadMoreEnd(true);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, proDetailList, userLikedProductEntity);
                    }

                    @Override
                    public void netClose() {
                        showToast(QualityOverseasDetailsActivity.this, R.string.unConnectedNetwork);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showToast(QualityOverseasDetailsActivity.this, R.string.invalidData);
                    }
                });
    }

    private void getCarCount() {
        if (userId > 0) {
            //购物车数量展示
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            NetLoadUtils.getNetInstance().loadNetDataPost(this,Q_QUERY_CAR_COUNT,params,new NetLoadListenerHelper(){
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                    if (requestStatus != null) {
                        if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                            int cartNumber = requestStatus.getResult().getCartNumber();
                            badge.setBadgeNumber(cartNumber);
                        } else if (!requestStatus.getCode().equals(EMPTY_CODE)) {
                            showToastRequestMsg(QualityOverseasDetailsActivity.this, requestStatus);
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
            page = 1;
            getRecommendData();
        }
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private void setData(QualityShopDescBean qualityShopDescBean) {
        GlideImageLoaderUtil.loadCenterCrop(QualityOverseasDetailsActivity.this, overseasHeaderView.iv_cover_detail_bg, qualityShopDescBean.getPicUrl());
        tv_header_titleAll.setText(getStrings(qualityShopDescBean.getTitle()));
//        if (!TextUtils.isEmpty(qualityShopDescBean.getSubtitle())) {
//            overseasHeaderView.tv_descrip_title.setVisibility(View.VISIBLE);
//            overseasHeaderView.tv_descrip_title.setText(qualityShopDescBean.getSubtitle());
//        } else {
        overseasHeaderView.tv_descrip_title.setVisibility(GONE);
//        }
        List<CommunalDetailBean> descripList = qualityShopDescBean.getDescripList();
        if (descripList != null) {
            descripDetailList.clear();
            descripDetailList.addAll(CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(descripList));
            communalDescripAdapter.setNewData(descripDetailList);
        }
    }

    //    页面分享
    @OnClick(R.id.iv_img_share)
    void sendShare(View view) {
        if (qualityShopDescripEntity != null && qualityShopDescripEntity.getQualityShopDescBean() != null) {
            new UMShareAction(QualityOverseasDetailsActivity.this
                    , qualityShopDescripEntity.getQualityShopDescBean().getPicUrl()
                    , qualityShopDescripEntity.getQualityShopDescBean().getTitle()
                    , getStrings(qualityShopDescripEntity.getQualityShopDescBean().getSubtitle())
                    , Url.BASE_SHARE_PAGE_TWO + "m/template/goods/Project_details.html?id=" + qualityShopDescripEntity.getQualityShopDescBean().getId(),qualityShopDescripEntity.getQualityShopDescBean().getId());
        }
    }

    @OnClick(R.id.iv_img_service)
    void skipService(View view) {
        Intent intent = new Intent(QualityOverseasDetailsActivity.this, ShopCarActivity.class);
        startActivity(intent);
    }

    class OverseasHeaderView {
        //标题
        @BindView(R.id.tv_descrip_title)
        TextView tv_descrip_title;
        @BindView(R.id.communal_recycler_wrap)
        RecyclerView communal_recycler_wrap;
        //头部背景
        @BindView(R.id.iv_cover_detail_bg)
        ImageView iv_cover_detail_bg;

        public void initView() {
            communal_recycler_wrap.setNestedScrollingEnabled(false);
            communalDescripAdapter = new CommunalDetailAdapter(QualityOverseasDetailsActivity.this, descripDetailList);
            communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(QualityOverseasDetailsActivity.this));
            communal_recycler_wrap.setAdapter(communalDescripAdapter);
            communalDescripAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    ShareDataBean shareDataBean = null;
                    if (view.getId() == R.id.tv_communal_share
                            && qualityShopDescripEntity != null && qualityShopDescripEntity.getQualityShopDescBean()!=null) {
                        QualityShopDescBean qualityShopDescBean = qualityShopDescripEntity.getQualityShopDescBean();
                        shareDataBean = new ShareDataBean(qualityShopDescBean.getPicUrl()
                                , qualityShopDescBean.getTitle()
                                , getStrings(qualityShopDescBean.getSubtitle())
                                , Url.BASE_SHARE_PAGE_TWO + "m/template/goods/Project_details.html?id=" + qualityShopDescBean.getId());

                    }
                    CommunalWebDetailUtils.getCommunalWebInstance()
                            .setWebDataClick(QualityOverseasDetailsActivity.this, shareDataBean, view, loadHud);
                }
            });
            communalDescripAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    CommunalDetailObjectBean communalDetailBean = (CommunalDetailObjectBean) view.getTag();
                    if (communalDetailBean != null) {
                        ConstantMethod.skipProductUrl(QualityOverseasDetailsActivity.this, communalDetailBean.getItemTypeId(), communalDetailBean.getId());
                    }
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (totalPersonalTrajectory != null) {
            Map<String, String> totalMap = new HashMap<>();
            totalMap.put("relate_id", overseasId);
            totalPersonalTrajectory.stopTotal(totalMap);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (totalPersonalTrajectory != null) {
            Map<String, String> totalMap = new HashMap<>();
            totalMap.put("relate_id", overseasId);
            totalPersonalTrajectory.stopTotal(totalMap);
        }
    }
}
