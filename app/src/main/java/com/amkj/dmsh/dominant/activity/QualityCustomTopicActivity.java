package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.bean.CustomCoverDesEntity;
import com.amkj.dmsh.dominant.bean.CustomCoverDesEntity.CustomCoverDesBean;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.homepage.adapter.QualityCustomTopicAdapter;
import com.amkj.dmsh.mine.activity.ShopCarActivity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
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

import static com.amkj.dmsh.constant.ConstantMethod.getBadge;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.insertNewTotalData;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.DOUBLE_INTEGRAL_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_0;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_1;
import static com.amkj.dmsh.constant.Url.Q_CUSTOM_PRO_COVER;
import static com.amkj.dmsh.constant.Url.Q_CUSTOM_PRO_LIST;
import static com.amkj.dmsh.constant.Url.Q_QUERY_CAR_COUNT;

;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/12/02
 * class description:自定义专区
 */
public class QualityCustomTopicActivity extends BaseActivity {
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
    @BindView(R.id.iv_img_share)
    ImageView iv_img_share;
    @BindView(R.id.tl_quality_bar)
    Toolbar tl_quality_bar;
    private int page = 1;
    private int scrollY;
    private float screenHeight;
    private QualityCustomTopicAdapter qualityCustomTopicAdapter;
    private List<LikedProductBean> customProList = new ArrayList();
    private List<CommunalDetailObjectBean> descriptionList = new ArrayList();
    private QNewProView qNewProView;
    private Badge badge;
    private String productType;
    private String showType;
    private View headerView;
    private CommunalDetailAdapter communalDetailAdapter;
    private UserLikedProductEntity userLikedProductEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_communal_ql_shop_car;
    }

    @Override
    protected void initViews() {
        Intent intent = getIntent();
        productType = intent.getStringExtra("productType");
        showType = intent.getStringExtra("showType");
        if (TextUtils.isEmpty(productType)) {
            showToast(this, R.string.invalidData);
            finish();
        }
        tv_header_titleAll.setText("");
        tl_quality_bar.setSelected(true);
        iv_img_service.setImageResource(R.drawable.shop_car_gray_icon);
        communal_recycler.setLayoutManager(new GridLayoutManager(QualityCustomTopicActivity.this, 2));

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
        qualityCustomTopicAdapter = new QualityCustomTopicAdapter(QualityCustomTopicActivity.this, customProList);
        headerView = LayoutInflater.from(QualityCustomTopicActivity.this).inflate(R.layout.layout_communal_detail_scroll_rec_cover_wrap, null, false);
        qNewProView = new QNewProView();
        ButterKnife.bind(qNewProView, headerView);
        qNewProView.initViews();
        communal_recycler.setAdapter(qualityCustomTopicAdapter);
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp)


                .create());
        qualityCustomTopicAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                getQualityCustomPro();
            }
        }, communal_recycler);
        qualityCustomTopicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LikedProductBean likedProductBean = (LikedProductBean) view.getTag();
                if (likedProductBean != null) {
                    Intent intent = new Intent(QualityCustomTopicActivity.this, ShopScrollDetailsActivity.class);
                    intent.putExtra("productId", String.valueOf(likedProductBean.getId()));
                    startActivity(intent);
                }
            }
        });
        qualityCustomTopicAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
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
                                constantMethod.addShopCarGetSku(QualityCustomTopicActivity.this, baseAddCarProInfoBean, loadHud);
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
                        getLoginStatus(QualityCustomTopicActivity.this);
                    }
                }
            }
        });
        badge = getBadge(QualityCustomTopicActivity.this, fl_header_service);
        totalPersonalTrajectory = insertNewTotalData(QualityCustomTopicActivity.this, productType);
    }

    @Override
    protected void loadData() {
        page = 1;
        getData();
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
    protected void getData() {
        getCustomCoverDescription();
        getQualityCustomPro();
        getCarCount();
    }

    /**
     * 获取自定义专区封面描述
     */
    private void getCustomCoverDescription() {
        if (TextUtils.isEmpty(productType)) {
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("productType", productType);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_CUSTOM_PRO_COVER, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                if (descriptionList.size() > 0) {
                    descriptionList.clear();
                    communalDetailAdapter.notifyDataSetChanged();
                }
                Gson gson = new Gson();
                CustomCoverDesEntity customCoverDesEntity = gson.fromJson(result, CustomCoverDesEntity.class);
                if (customCoverDesEntity != null) {
                    if (customCoverDesEntity.getCode().equals(SUCCESS_CODE)
                            && customCoverDesEntity.getCoverDesList() != null
                            && customCoverDesEntity.getCoverDesList().size() > 0) {
                        CustomCoverDesBean customCoverDesBean = customCoverDesEntity.getCoverDesList().get(0);
                        if (!TextUtils.isEmpty(customCoverDesBean.getPicUrl())) {
                            qNewProView.iv_communal_cover_wrap.setVisibility(View.VISIBLE);
                            GlideImageLoaderUtil.loadImgDynamicDrawable(QualityCustomTopicActivity.this, qNewProView.iv_communal_cover_wrap,
                                    getStrings(customCoverDesBean.getPicUrl()));
                        } else {
                            qNewProView.iv_communal_cover_wrap.setVisibility(View.GONE);
                        }
                        if (customCoverDesBean.getDescriptionList() != null
                                && customCoverDesBean.getDescriptionList().size() > 0) {
                            qNewProView.communal_recycler_wrap.setVisibility(View.VISIBLE);
                            qNewProView.v_line_bottom.setVisibility(View.VISIBLE);
                            List<CommunalDetailObjectBean> detailsDataList = ConstantMethod.getDetailsDataList(customCoverDesBean.getDescriptionList());
                            if (detailsDataList != null) {
                                descriptionList.addAll(detailsDataList);
                                communalDetailAdapter.notifyDataSetChanged();
                            }
                        } else {
                            qNewProView.v_line_bottom.setVisibility(View.GONE);
                            qNewProView.communal_recycler_wrap.setVisibility(View.GONE);
                        }
                        if (qualityCustomTopicAdapter.getHeaderLayoutCount() < 1) {
                            qualityCustomTopicAdapter.addHeaderView(headerView);
                            qualityCustomTopicAdapter.notifyDataSetChanged();
                        }
                    } else {
                        qualityCustomTopicAdapter.removeAllHeaderView();
                        qualityCustomTopicAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                if (qualityCustomTopicAdapter.getHeaderLayoutCount() > 0) {
                    qualityCustomTopicAdapter.removeAllHeaderView();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCarCount();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            getCarCount();
        }
    }

    private void getQualityCustomPro() {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("productType", productType);
        params.put("showCount", TOTAL_COUNT_TWENTY);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(QualityCustomTopicActivity.this, Q_CUSTOM_PRO_LIST
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        qualityCustomTopicAdapter.loadMoreComplete();
                        if (page == 1) {
                            customProList.clear();
                        }
                        Gson gson = new Gson();
                        userLikedProductEntity = gson.fromJson(result, UserLikedProductEntity.class);
                        if (userLikedProductEntity != null) {
                            if (userLikedProductEntity.getCode().equals(SUCCESS_CODE)) {
                                for (LikedProductBean likedProductBean : userLikedProductEntity.getLikedProductBeanList()) {
                                    likedProductBean.setItemType(DOUBLE_INTEGRAL_TYPE.equals(showType) ? TYPE_1 : TYPE_0);
                                    customProList.add(likedProductBean);
                                }
                                tv_header_titleAll.setText(getStrings(userLikedProductEntity.getZoneName()));
                            } else if (userLikedProductEntity.getCode().equals(EMPTY_CODE)) {
                                qualityCustomTopicAdapter.loadMoreEnd();
                            } else {
                                showToast(QualityCustomTopicActivity.this, userLikedProductEntity.getMsg());
                            }
                            qualityCustomTopicAdapter.notifyDataSetChanged();
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, customProList, userLikedProductEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        qualityCustomTopicAdapter.loadMoreEnd(true);
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, customProList, userLikedProductEntity);
                    }

                    @Override
                    public void netClose() {
                        showToast(QualityCustomTopicActivity.this, R.string.unConnectedNetwork);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showToast(QualityCustomTopicActivity.this, R.string.invalidData);
                    }
                });
    }

    private void getCarCount() {
        if (userId > 0) {
            //购物车数量展示
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            NetLoadUtils.getNetInstance().loadNetDataPost(this, Q_QUERY_CAR_COUNT, params, new NetLoadListenerHelper() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                    if (requestStatus != null) {
                        if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                            int cartNumber = requestStatus.getResult().getCartNumber();
                            badge.setBadgeNumber(cartNumber);
                        } else if (!requestStatus.getCode().equals(EMPTY_CODE)) {
                            showToast(QualityCustomTopicActivity.this, requestStatus.getMsg());
                        }
                    }
                }
            });
        }
    }

    class QNewProView {
        @BindView(R.id.iv_communal_cover_wrap)
        ImageView iv_communal_cover_wrap;
        @BindView(R.id.communal_recycler_wrap)
        RecyclerView communal_recycler_wrap;
        @BindView(R.id.v_line_bottom)
        View v_line_bottom;

        public void initViews() {
            communal_recycler_wrap.setNestedScrollingEnabled(false);
            communal_recycler_wrap.setLayoutManager(new LinearLayoutManager((QualityCustomTopicActivity.this)));
            communalDetailAdapter = new CommunalDetailAdapter(QualityCustomTopicActivity.this, descriptionList);
            communal_recycler_wrap.setAdapter(communalDetailAdapter);
        }
    }

    @OnClick(R.id.iv_img_service)
    void skipShopCar(View view) {
        Intent intent = new Intent(QualityCustomTopicActivity.this, ShopCarActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    @OnClick(R.id.iv_img_share)
    void toShare(View view) {
        if (customProList.size() > 0) {
            LikedProductBean likedProductBean = customProList.get(0);
            new UMShareAction(QualityCustomTopicActivity.this
                    , likedProductBean.getPicUrl()
                    , "推荐你看看这个"
                    , "我在多么生活发现这几样好物，性价比不错，还包邮"
                    , Url.BASE_SHARE_PAGE_TWO + "m/template/goods/CustomZone.html?id=" + productType
                    , "pages/handpick/handpick?id=" + productType);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (totalPersonalTrajectory != null) {
            Map<String, String> totalMap = new HashMap<>();
            totalMap.put("relate_id", productType);
            totalPersonalTrajectory.stopTotal(totalMap);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (totalPersonalTrajectory != null) {
            Map<String, String> totalMap = new HashMap<>();
            totalMap.put("relate_id", productType);
            totalPersonalTrajectory.stopTotal(totalMap);
        }
    }
}
