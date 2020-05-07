package com.amkj.dmsh.dominant.fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.dominant.adapter.CatergoryGoodsAdapter;
import com.amkj.dmsh.dominant.bean.NewUserCouponEntity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
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

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.ERROR_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_FORTY;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.constant.Url.QUALITY_NEW_USER_COUPON_LIST;
import static com.amkj.dmsh.constant.Url.QUALITY_NEW_USER_GET_COUPON;
import static com.amkj.dmsh.constant.Url.QUALITY_NEW_USER_LIST;

/**
 * Created by xiaoxin on 2019/4/12 0012
 * Version:v4.0.0
 * ClassDescription :新人专享抽取Fragment
 */
public class QualityNewUserFragment extends BaseFragment {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    @BindView(R.id.tl_quality_bar)
    Toolbar mTlQualityBar;
    private int scrollY;
    private float screenHeight;
    private List<LikedProductBean> qualityNewUserShopList = new ArrayList<>();
    private CatergoryGoodsAdapter qualityNewUserShopAdapter;
    private QNewUserCoverHelper qNewUserCoverHelper;
    private View qNewUserCoverView;
    private UserLikedProductEntity qualityNewUserShopEntity;
    private int page = 1;

    @Override
    protected int getContentView() {
        return R.layout.activity_communal_ql_shop_car;
    }

    @Override
    protected void initViews() {
        mTlQualityBar.setVisibility(GONE);
        communal_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        communal_recycler.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_gray_f)
                .create());
        smart_communal_refresh.setOnRefreshListener(refreshLayout -> {
            page = 1;
            getQualityTypePro();
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
        download_btn_communal.setOnClickListener(v -> {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) communal_recycler.getLayoutManager();
            int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
            int mVisibleCount = linearLayoutManager.findLastVisibleItemPosition()
                    - linearLayoutManager.findFirstVisibleItemPosition() + 1;
            if (firstVisibleItemPosition > mVisibleCount) {
                communal_recycler.scrollToPosition(mVisibleCount);
            }
            communal_recycler.smoothScrollToPosition(0);
        });
        qualityNewUserShopAdapter = new CatergoryGoodsAdapter(getActivity(), qualityNewUserShopList);
        qualityNewUserShopAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return qualityNewUserShopList.get(position).getItemType() == ConstantVariable.TITLE ? 3 : 1;
            }
        });
        qNewUserCoverView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_new_user_cover, communal_recycler, false);
        qNewUserCoverHelper = new QualityNewUserFragment.QNewUserCoverHelper();
        ButterKnife.bind(qNewUserCoverHelper, qNewUserCoverView);
        qualityNewUserShopAdapter.addHeaderView(qNewUserCoverView);
        communal_recycler.setAdapter(qualityNewUserShopAdapter);
        qualityNewUserShopAdapter.setOnLoadMoreListener(() -> {
            page++;
            getNewUserCouponProduct();
        }, communal_recycler);
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    @Override
    protected void loadData() {
        getQualityTypePro();
    }

    //新人专享
    private void getQualityTypePro() {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", 1);
        params.put("showCount", TOTAL_COUNT_FORTY);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), QUALITY_NEW_USER_LIST
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        if (page == 1) {
                            qualityNewUserShopList.clear();
                        }

                        qualityNewUserShopEntity = GsonUtils.fromJson(result, UserLikedProductEntity.class);
                        if (qualityNewUserShopEntity != null) {
                            List<LikedProductBean> goodsList = qualityNewUserShopEntity.getGoodsList();
                            if (goodsList != null && goodsList.size() > 0 && SUCCESS_CODE.equals(qualityNewUserShopEntity.getCode())) {
                                //添加新人专享头部
                                LikedProductBean qualityNewUserShopBean = new LikedProductBean();
                                qualityNewUserShopBean.setItemType(ConstantVariable.TITLE);
                                qualityNewUserShopBean.setTitleHead(R.drawable.newuser_exclusive);
                                qualityNewUserShopList.add(qualityNewUserShopBean);
                                //添加活动标签
                                for (int i = 0; i < goodsList.size(); i++) {
                                    LikedProductBean likedProductBean = goodsList.get(i);
                                    likedProductBean.setActivityTag("新人专享");
                                }
                                qualityNewUserShopList.addAll(goodsList);
                                qualityNewUserShopAdapter.notifyDataSetChanged();
                            } else if (EMPTY_CODE.equals(qualityNewUserShopEntity.getCode())) {
                                showToast( qualityNewUserShopEntity.getMsg());
                            }
                        }
                        getNewUserCouponProduct();
                    }

                    @Override
                    public void onNotNetOrException() {
                        if (page == 1) {
                            qualityNewUserShopList.clear();
                        }
                        getNewUserCouponProduct();
                    }
                });
    }

    //用券专区
    private void getNewUserCouponProduct() {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("showCount", TOTAL_COUNT_TWENTY);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), QUALITY_NEW_USER_COUPON_LIST, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                smart_communal_refresh.finishRefresh();

                UserLikedProductEntity qualityNewUserShopEntity = GsonUtils.fromJson(result, UserLikedProductEntity.class);
                if (qualityNewUserShopEntity != null) {
                    List<LikedProductBean> goodsList = qualityNewUserShopEntity.getGoodsList();
                    String code = qualityNewUserShopEntity.getCode();
                    if (goodsList != null && goodsList.size() > 0 && SUCCESS_CODE.equals(code)) {
                        //只会添加一次头部
                        if (page == 1) {
                            LikedProductBean qualityNewUserShopBean = new LikedProductBean();
                            qualityNewUserShopBean.setItemType(ConstantVariable.TITLE);
                            qualityNewUserShopBean.setTitleHead(R.drawable.coupon_zone);
                            qualityNewUserShopList.add(qualityNewUserShopBean);
                        }
                        qualityNewUserShopList.addAll(goodsList);
                        qualityNewUserShopAdapter.notifyDataSetChanged();
                        qualityNewUserShopAdapter.loadMoreComplete();
                    } else if (ERROR_CODE.equals(code)) {
                        showToast(qualityNewUserShopEntity.getMsg());
                        qualityNewUserShopAdapter.loadMoreFail();
                    } else {
                        qualityNewUserShopAdapter.loadMoreEnd();
                    }
                } else {
                    qualityNewUserShopAdapter.loadMoreEnd();
                }

                NetLoadUtils.getNetInstance().showLoadSir(loadService, qualityNewUserShopList, qualityNewUserShopEntity);
            }

            @Override
            public void onNotNetOrException() {
                smart_communal_refresh.finishRefresh();
                qualityNewUserShopAdapter.loadMoreFail();
                NetLoadUtils.getNetInstance().showLoadSir(loadService, qualityNewUserShopList, qualityNewUserShopEntity);
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
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), QUALITY_NEW_USER_GET_COUPON, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                loadHud.dismiss();

                NewUserCouponEntity newUserCouponEntity = GsonUtils.fromJson(result, NewUserCouponEntity.class);
                if (newUserCouponEntity != null) {
                    if (newUserCouponEntity.getCode().equals(SUCCESS_CODE)) {
                        qNewUserCoverHelper.tv_new_user_get_coupon.setText("已领取");
                    } else if (!newUserCouponEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(newUserCouponEntity.getMsg());
                        qNewUserCoverHelper.tv_new_user_get_coupon.setText("已领取");
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                loadHud.dismiss();
            }
        });
    }

    public class QNewUserCoverHelper {
        @BindView(R.id.iv_new_user_cover)
        ImageView iv_new_user_cover;
        @BindView(R.id.tv_new_user_get_coupon)
        TextView tv_new_user_get_coupon;

        @OnClick(R.id.tv_new_user_get_coupon)
        void getNewUserCoupon() {
            if (userId > 0) {
                getNewUserCouponGift();
            } else {
                getLoginStatus(getActivity());
            }
        }
    }

}
