package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.BaseAddCarProInfoBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.adapter.NewUserCouponAdapter;
import com.amkj.dmsh.dominant.adapter.QualityNewUserShopAdapter;
import com.amkj.dmsh.dominant.bean.NewUserCouponEntity;
import com.amkj.dmsh.dominant.bean.NewUserCouponEntity.CouponGiftBean;
import com.amkj.dmsh.dominant.bean.QualityNewUserShopEntity;
import com.amkj.dmsh.dominant.bean.QualityNewUserShopEntity.QualityNewUserShopBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.DirectMyCouponActivity;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
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

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.insertNewTotalData;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_1;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_2;
import static com.amkj.dmsh.constant.Url.QUALITY_NEW_USER_COUPON_LIST;
import static com.amkj.dmsh.constant.Url.QUALITY_NEW_USER_COVER;
import static com.amkj.dmsh.constant.Url.QUALITY_NEW_USER_GET_COUPON;
import static com.amkj.dmsh.constant.Url.QUALITY_NEW_USER_LIST;

;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/6/29
 * class description:新人专享
 */
public class QualityNewUserActivity extends BaseActivity {
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
    private int scrollY;
    private float screenHeight;
    private List<QualityNewUserShopBean> qualityNewUserShopList = new ArrayList();
    //    用券专区
    private List<QualityNewUserShopBean> qualityNewUserCouponList = new ArrayList();
    private List<CouponGiftBean> couponGiftList = new ArrayList<>();
    private QualityNewUserShopAdapter qualityNewUserShopAdapter;
    private QNewUserCoverHelper qNewUserCoverHelper;
    private View qNewUserCoverView;
    private String newUserImgUrl;
    private NewUserCouponAdapter newUserCouponAdapter;
    private QualityNewUserShopEntity qualityNewUserShopEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_communal_ql_shop_car;
    }

    @Override
    protected void initViews() {
        tv_header_titleAll.setText("新人专享");
        iv_img_service.setVisibility(View.GONE);
        communal_recycler.setLayoutManager(new GridLayoutManager(QualityNewUserActivity.this, 2));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_gray_f)


                .create());

        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                qualityNewUserShopList.clear();
                qualityNewUserCouponList.clear();
                getQualityTypePro();
                getNewUserCouponProduct();
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
        qualityNewUserShopAdapter = new QualityNewUserShopAdapter(QualityNewUserActivity.this, qualityNewUserShopList);
        qualityNewUserShopAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return qualityNewUserShopList.get(position).getItemType() == TYPE_1 ? 2 : 1;
            }
        });
        qNewUserCoverView = LayoutInflater.from(QualityNewUserActivity.this).inflate(R.layout.layout_new_user_cover, communal_recycler, false);
        qNewUserCoverHelper = new QNewUserCoverHelper();
        ButterKnife.bind(qNewUserCoverHelper, qNewUserCoverView);
        qNewUserCoverHelper.initViews();
        qualityNewUserShopAdapter.addHeaderView(qNewUserCoverView);
        communal_recycler.setAdapter(qualityNewUserShopAdapter);
        qualityNewUserShopAdapter.setEnableLoadMore(false);
        qualityNewUserShopAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                QualityNewUserShopBean qualityNewUserShopBean = (QualityNewUserShopBean) view.getTag();
                if (qualityNewUserShopBean != null) {
                    Intent intent = new Intent(QualityNewUserActivity.this, ShopScrollDetailsActivity.class);
                    intent.putExtra("productId", String.valueOf(qualityNewUserShopBean.getId()));
                    startActivity(intent);
                }
            }
        });
        qualityNewUserShopAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                loadHud.show();
                QualityNewUserShopBean qualityNewUserShopBean = (QualityNewUserShopBean) view.getTag(R.id.iv_tag);
                if (qualityNewUserShopBean != null) {
                    if (userId > 0) {
                        switch (view.getId()) {
                            case R.id.iv_pro_add_car:
                                BaseAddCarProInfoBean baseAddCarProInfoBean = new BaseAddCarProInfoBean();
                                baseAddCarProInfoBean.setProductId(qualityNewUserShopBean.getId());
                                baseAddCarProInfoBean.setActivityCode(getStrings(qualityNewUserShopBean.getActivityCode()));
                                baseAddCarProInfoBean.setProName(getStrings(qualityNewUserShopBean.getName()));
                                baseAddCarProInfoBean.setProPic(getStrings(qualityNewUserShopBean.getPicUrl()));
                                ConstantMethod constantMethod = new ConstantMethod();
                                constantMethod.addShopCarGetSku(QualityNewUserActivity.this, baseAddCarProInfoBean, loadHud);
                                break;
                        }
                    } else {
                        loadHud.dismiss();
                        getLoginStatus(QualityNewUserActivity.this);
                    }
                }
            }
        });
        totalPersonalTrajectory = insertNewTotalData(QualityNewUserActivity.this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
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
        getQualityTypePro();
        getNewUserCouponProduct();
        getCoverImg();
    }

    private void getCoverImg() {
        NetLoadUtils.getNetInstance().loadNetDataPost(this, QUALITY_NEW_USER_COVER, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals(SUCCESS_CODE)) {
                        if (qualityNewUserShopAdapter.getHeaderLayoutCount() < 1) {
                            qualityNewUserShopAdapter.addHeaderView(qNewUserCoverView);
                        }
                        newUserImgUrl = requestStatus.getImgUrl();
                        GlideImageLoaderUtil.loadImgDynamicDrawable(QualityNewUserActivity.this, qNewUserCoverHelper.iv_new_user_cover,
                                getStrings(requestStatus.getImgUrl()));
                    } else {
                        qualityNewUserShopAdapter.removeAllHeaderView();
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                if (qualityNewUserShopAdapter.getHeaderLayoutCount() > 0) {
                    qualityNewUserShopAdapter.removeAllHeaderView();
                }
            }
        });
    }

    private void getQualityTypePro() {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", 1);
        params.put("showCount", TOTAL_COUNT_TWENTY);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(QualityNewUserActivity.this, QUALITY_NEW_USER_LIST
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        qualityNewUserShopAdapter.loadMoreComplete();
                        Gson gson = new Gson();
                        qualityNewUserShopEntity = gson.fromJson(result, QualityNewUserShopEntity.class);
                        if (qualityNewUserShopEntity != null) {
                            if (qualityNewUserShopEntity.getCode().equals(SUCCESS_CODE)) {
                                qualityNewUserShopList.clear();
                                qualityNewUserShopList.addAll(qualityNewUserShopEntity.getQualityNewUserShopList());
                            } else if (!qualityNewUserShopEntity.getCode().equals(EMPTY_CODE)) {
                                showToast(QualityNewUserActivity.this, qualityNewUserShopEntity.getMsg());
                            }
                            if (qualityNewUserCouponList.size() > 0) {
                                qualityNewUserShopList.addAll(qualityNewUserCouponList);
                                qualityNewUserShopAdapter.notifyDataSetChanged();
                            }
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, qualityNewUserShopList, qualityNewUserShopEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        qualityNewUserShopAdapter.loadMoreComplete();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, qualityNewUserShopList, qualityNewUserShopEntity);

                    }

                    @Override
                    public void netClose() {
                        showToast(QualityNewUserActivity.this, R.string.unConnectedNetwork);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showToast(QualityNewUserActivity.this, R.string.invalidData);
                    }
                });
    }

    /**
     * 新人专享
     */
    private void getNewUserCouponProduct() {
        NetLoadUtils.getNetInstance().loadNetDataPost(this, QUALITY_NEW_USER_COUPON_LIST, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                qualityNewUserCouponList.clear();
                Gson gson = new Gson();
                QualityNewUserShopEntity qualityNewUserShopEntity = gson.fromJson(result, QualityNewUserShopEntity.class);
                if (qualityNewUserShopEntity != null) {
                    if (qualityNewUserShopEntity.getCode().equals(SUCCESS_CODE)) {
                        QualityNewUserShopBean qualityNewUserShopBean = new QualityNewUserShopBean();
                        qualityNewUserShopBean.setcItemType(TYPE_1);
                        qualityNewUserShopBean.setcTitle("- 用券专区 -");
                        qualityNewUserCouponList.add(qualityNewUserShopBean);
                        for (QualityNewUserShopBean qualityNewUserProductBean : qualityNewUserShopEntity.getQualityNewUserShopList()) {
                            qualityNewUserProductBean.setcItemType(TYPE_2);
                            qualityNewUserCouponList.add(qualityNewUserProductBean);
                        }
                    }
                    if (qualityNewUserShopList.size() > 0) {
                        qualityNewUserShopList.addAll(qualityNewUserCouponList);
                        qualityNewUserShopAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                if (qualityNewUserShopList.size() > 0) {
                    qualityNewUserShopList.addAll(qualityNewUserCouponList);
                    qualityNewUserShopAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * 领取 新人优惠券礼包
     */
    private void getNewUserCouponGift() {
        loadHud.show();
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, QUALITY_NEW_USER_GET_COUPON, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                loadHud.dismiss();
                Gson gson = new Gson();
                NewUserCouponEntity newUserCouponEntity = gson.fromJson(result, NewUserCouponEntity.class);
                if (newUserCouponEntity != null) {
                    if (newUserCouponEntity.getCode().equals(SUCCESS_CODE)) {
                        couponGiftList.clear();
                        couponGiftList.addAll(newUserCouponEntity.getCouponGiftList());
                        setNewUserCouponShowWay();
                    } else if (!newUserCouponEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(QualityNewUserActivity.this, newUserCouponEntity.getMsg());
                        setNewUserGetCouponShowWay();
                    }
                    newUserCouponAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNotNetOrException() {
                loadHud.dismiss();
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(QualityNewUserActivity.this, "领取失败");
            }

            @Override
            public void netClose() {
                showToast(QualityNewUserActivity.this, R.string.unConnectedNetwork);
                setNewUserGetCouponShowWay();
            }
        });
    }

    private void setNewUserGetCouponShowWay() {
        qNewUserCoverHelper.rel_new_user_got.setVisibility(View.GONE);
        qNewUserCoverHelper.iv_new_user_cover.setVisibility(View.VISIBLE);
        qNewUserCoverHelper.tv_new_user_get_coupon.setVisibility(View.VISIBLE);
        qNewUserCoverHelper.tv_new_user_get_coupon.setText("立即领取");
    }

    private void setNewUserCouponShowWay() {
        qNewUserCoverHelper.rel_new_user_got.setVisibility(View.VISIBLE);
        qNewUserCoverHelper.iv_new_user_cover.setVisibility(View.GONE);
        qNewUserCoverHelper.tv_new_user_get_coupon.setVisibility(View.GONE);
    }

    class QNewUserCoverHelper {
        @BindView(R.id.iv_new_user_cover)
        ImageView iv_new_user_cover;
        @BindView(R.id.rel_new_user_got)
        RelativeLayout rel_new_user_got;
        @BindView(R.id.communal_recycler_wrap)
        RecyclerView communal_recycler_wrap;
        @BindView(R.id.tv_new_user_get_coupon)
        TextView tv_new_user_get_coupon;

        public void initViews() {
            communal_recycler_wrap.setLayoutManager(new GridLayoutManager(QualityNewUserActivity.this, 2));
            newUserCouponAdapter = new NewUserCouponAdapter(QualityNewUserActivity.this, couponGiftList);
            communal_recycler_wrap.setAdapter(newUserCouponAdapter);
            communal_recycler_wrap.addItemDecoration(new ItemDecoration.Builder()
                    // 设置分隔线资源ID
                    .setDividerId(R.drawable.item_divider_coupon_white).create());
        }

        @OnClick(R.id.tv_look_mine_coupon)
        void lookMineCoupon() {
            Intent intent = new Intent(QualityNewUserActivity.this, DirectMyCouponActivity.class);
            startActivity(intent);
        }

        @OnClick(R.id.tv_new_user_get_coupon)
        void getNewUserCoupon() {
            if (userId > 0) {
                getNewUserCouponGift();
            } else {
                getLoginStatus(QualityNewUserActivity.this);
            }
        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    //    页面分享
    @OnClick(R.id.iv_img_share)
    void sendShare() {
        if (!TextUtils.isEmpty(newUserImgUrl)) {
            new UMShareAction(QualityNewUserActivity.this
                    , newUserImgUrl
                    , "新人专享"
                    , "注册有礼"
                    , Url.BASE_SHARE_PAGE_TWO + "m/template/goods/new_exclusive.html"
                    , "pages/new_exclusive/new_exclusive",1);
        }
    }
}
