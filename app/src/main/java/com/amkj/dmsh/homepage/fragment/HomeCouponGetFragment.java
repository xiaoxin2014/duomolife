package com.amkj.dmsh.homepage.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.constant.CommunalDetailBean;
import com.amkj.dmsh.dominant.adapter.QualityProTitleAdapter;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.homepage.bean.CommunalDescriptionEntity;
import com.amkj.dmsh.homepage.bean.CommunalDescriptionEntity.CommunalDescriptionBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.activity.DirectMyCouponActivity;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
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

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.constant.Url.H_COUPON_CENTER_DATA;
import static com.amkj.dmsh.constant.Url.Q_SP_DETAIL_DOMO_RECOM;

/**
 * Created by xiaoxin on 2019/4/12 0012
 * Version:v4.0.0
 * ClassDescription :领券中心抽取Fragment
 */

public class HomeCouponGetFragment extends BaseFragment {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    @BindView(R.id.tl_normal_bar)
    Toolbar mTlNormalBar;
    private int page = 1;
    private int scrollY;
    private float screenHeight;
    //    domo推荐
    private List<LikedProductBean> likedProductBeanList = new ArrayList<>();
    private QualityProTitleAdapter couponProTitleAdapter;
    private ShopRecommendView shopRecommendView;
    private CommunalDetailAdapter communalDetailAdapter;
    private List<CommunalDetailObjectBean> couponDescriptionList = new ArrayList();
    private CommunalDescriptionBean communalDescriptionBean;
    private RecommendGoodView recommendGoodView;
    private UserLikedProductEntity likedProductEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_home_coupon;
    }

    @Override
    protected void initViews() {
        mTlNormalBar.setVisibility(GONE);
        communal_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        smart_communal_refresh.setOnRefreshListener(refreshLayout -> loadData());
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
                .setDividerId(R.drawable.item_divider_five_dp).create());
        couponProTitleAdapter = new QualityProTitleAdapter(getActivity(), likedProductBeanList);
        couponProTitleAdapter.setHeaderAndEmpty(true);
        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_communal_detail_scroll_rec, (ViewGroup) communal_recycler.getParent(), false);
        View recommendView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_home_coupon_pro, (ViewGroup) communal_recycler.getParent(), false);
        shopRecommendView = new ShopRecommendView();
        ButterKnife.bind(shopRecommendView, headerView);
        recommendGoodView = new RecommendGoodView();
        ButterKnife.bind(recommendGoodView, recommendView);
        shopRecommendView.initView();
        couponProTitleAdapter.addHeaderView(headerView);
        couponProTitleAdapter.addHeaderView(recommendView);
        communal_recycler.setAdapter(couponProTitleAdapter);
        couponProTitleAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (communalDescriptionBean != null) {
                    page++;
                    getCouponProData(communalDescriptionBean.getId());
                }
            }
        }, communal_recycler);
        couponProTitleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LikedProductBean likedProductBean = (LikedProductBean) view.getTag();
                if (likedProductBean != null) {
                    Intent intent = new Intent(getActivity(), ShopScrollDetailsActivity.class);
                    intent.putExtra("productId", String.valueOf(likedProductBean.getId()));
                    if (likedProductEntity != null && !TextUtils.isEmpty(likedProductEntity.getRecommendFlag())) {
                        intent.putExtra("recommendFlag", likedProductEntity.getRecommendFlag());
                    }
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void loadData() {
        page = 1;
        getCouponData();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getCouponData() {
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), H_COUPON_CENTER_DATA, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                couponDescriptionList.clear();
                Gson gson = new Gson();
                CommunalDescriptionEntity shopDetailsEntity = gson.fromJson(result, CommunalDescriptionEntity.class);
                if (shopDetailsEntity != null) {
                    if (shopDetailsEntity.getCode().equals(SUCCESS_CODE)) {
                        communalDescriptionBean = shopDetailsEntity.getCommunalDescriptionBean();
                        List<CommunalDetailBean> descriptionBeanList = communalDescriptionBean.getDescriptionList();
                        if (descriptionBeanList != null) {
                            couponDescriptionList.clear();
                            couponDescriptionList.addAll(CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(descriptionBeanList));
                        }
                        getCouponProData(communalDescriptionBean.getId());
                    } else if (!shopDetailsEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(shopDetailsEntity.getMsg());
                    }
                    communalDetailAdapter.setNewData(couponDescriptionList);
                }
                NetLoadUtils.getNetInstance().showLoadSirSuccess(loadService);
            }

            @Override
            public void onNotNetOrException() {
                NetLoadUtils.getNetInstance().showLoadSirLoadFailed(loadService);
            }
        });
    }

    private void getCouponProData(int id) {
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("showCount", TOTAL_COUNT_TWENTY);
        params.put("reminder_id", id);
        NetLoadUtils.getNetInstance().loadNetDataPost(getActivity(), Q_SP_DETAIL_DOMO_RECOM,
                params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        couponProTitleAdapter.loadMoreComplete();
                        if (page == 1) {
                            likedProductBeanList.clear();
                        }
                        Gson gson = new Gson();
                        likedProductEntity = gson.fromJson(result, UserLikedProductEntity.class);
                        if (likedProductEntity != null) {
                            if (likedProductEntity.getCode().equals(SUCCESS_CODE)) {
                                likedProductBeanList.addAll(likedProductEntity.getGoodsList());
                            } else if (EMPTY_CODE.equals(likedProductEntity.getCode())) {
                                couponProTitleAdapter.loadMoreEnd();
                            }
                        }
                        if (likedProductBeanList.size() > 0) {
                            recommendGoodView.tv_pro_title.setVisibility(View.VISIBLE);
                        }
                        couponProTitleAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        couponProTitleAdapter.loadMoreEnd(true);
                    }
                });
    }

    class ShopRecommendView {
        @BindView(R.id.communal_recycler_wrap)
        RecyclerView communal_recycler_wrap;

        public void initView() {
            recommendGoodView.tv_pro_title.setVisibility(View.GONE);
            recommendGoodView.tv_pro_title.setText("商品推荐");
            communal_recycler_wrap.setNestedScrollingEnabled(false);
            communal_recycler_wrap.setLayoutManager(new LinearLayoutManager((getActivity())));
            communalDetailAdapter = new CommunalDetailAdapter(getActivity(), couponDescriptionList);
            communal_recycler_wrap.setAdapter(communalDetailAdapter);
            communalDetailAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    if (view.getId() == R.id.tv_communal_share) {
                        return;
                    }
                    CommunalWebDetailUtils.getCommunalWebInstance()
                            .setWebDataClick(getActivity(), view, loadHud);
                }
            });
        }
    }

    public class RecommendGoodView {
        @BindView(R.id.tv_pro_title)
        TextView tv_pro_title;

        @OnClick(R.id.tv_coupon_center_look)
        void lookCoupon() {
            Intent intent = new Intent(getActivity(), DirectMyCouponActivity.class);
            startActivity(intent);
        }

    }
}
