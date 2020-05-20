package com.amkj.dmsh.homepage.activity;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.CommunalDetailBean;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
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
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
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
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.skipProductUrl;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.constant.Url.H_COUPON_CENTER_DATA;
import static com.amkj.dmsh.constant.Url.Q_SP_DETAIL_DOMO_RECOM;

;
;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/31
 * class description:领券中心
 */
public class HomeCouponGetActivity extends BaseActivity {
    @BindView(R.id.smart_communal_refresh)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    //    滚动至顶部
    @BindView(R.id.download_btn_communal)
    public FloatingActionButton download_btn_communal;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.tv_header_shared)
    TextView tv_header_shared;
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
        tv_header_titleAll.setText("领券中心");
        communal_recycler.setLayoutManager(new GridLayoutManager(HomeCouponGetActivity.this, 2));

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
        couponProTitleAdapter = new QualityProTitleAdapter(HomeCouponGetActivity.this, likedProductBeanList);
        couponProTitleAdapter.setHeaderAndEmpty(true);
        View headerView = LayoutInflater.from(HomeCouponGetActivity.this).inflate(R.layout.layout_communal_detail_scroll_rec, (ViewGroup) communal_recycler.getParent(), false);
        View recommendView = LayoutInflater.from(HomeCouponGetActivity.this).inflate(R.layout.layout_home_coupon_pro, (ViewGroup) communal_recycler.getParent(), false);
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
                    Intent intent = new Intent(HomeCouponGetActivity.this, ShopScrollDetailsActivity.class);
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
    public View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getCouponData() {
        NetLoadUtils.getNetInstance().loadNetDataPost(this,H_COUPON_CENTER_DATA,new NetLoadListenerHelper(){
            @Override
            public void onSuccess(String result) {
                couponDescriptionList.clear();

                CommunalDescriptionEntity shopDetailsEntity = GsonUtils.fromJson(result, CommunalDescriptionEntity.class);
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
                        showToast( shopDetailsEntity.getMsg());
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
        NetLoadUtils.getNetInstance().loadNetDataPost(HomeCouponGetActivity.this, Q_SP_DETAIL_DOMO_RECOM,
                params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        couponProTitleAdapter.loadMoreComplete();
                        if (page == 1) {
                            likedProductBeanList.clear();
                        }

                        likedProductEntity = GsonUtils.fromJson(result, UserLikedProductEntity.class);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            loadData();
        }

    }

    class ShopRecommendView {
        @BindView(R.id.communal_recycler_wrap)
        RecyclerView communal_recycler_wrap;

        public void initView() {
            recommendGoodView.tv_pro_title.setVisibility(View.GONE);
            recommendGoodView.tv_pro_title.setText("商品推荐");
            communal_recycler_wrap.setNestedScrollingEnabled(false);
            communal_recycler_wrap.setLayoutManager(new LinearLayoutManager((HomeCouponGetActivity.this)));
            communalDetailAdapter = new CommunalDetailAdapter(HomeCouponGetActivity.this, couponDescriptionList);
            communal_recycler_wrap.setAdapter(communalDetailAdapter);
            communalDetailAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    if (view.getId() == R.id.tv_communal_share) {
                        return;
                    }
                    CommunalWebDetailUtils.getCommunalWebInstance()
                            .setWebDataClick(HomeCouponGetActivity.this, view, loadHud);
                }
            });
            communalDetailAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    CommunalDetailObjectBean communalDetailBean = (CommunalDetailObjectBean) view.getTag();
                    if (communalDetailBean != null) {
                        skipProductUrl(HomeCouponGetActivity.this, communalDetailBean.getItemTypeId(), communalDetailBean.getId());
                    }
                }
            });
        }
    }

    public class RecommendGoodView {
        @BindView(R.id.tv_pro_title)
        TextView tv_pro_title;

        @OnClick(R.id.tv_coupon_center_look)
        void lookCoupon() {
            Intent intent = new Intent(HomeCouponGetActivity.this, DirectMyCouponActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    @OnClick(R.id.tv_header_shared)
    void sharedContent(View view) {
        if (communalDescriptionBean != null) {
            new UMShareAction(HomeCouponGetActivity.this
                    , "http://image.domolife.cn/platform/wWyEfQwCaF1504945636936.png"
                    , "领券中心"
                    , "领券买买买"
                    , Url.BASE_SHARE_PAGE_TWO + "m/template/home/couponCenter.html",communalDescriptionBean.getId());
        }
    }

}
