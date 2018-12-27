package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.CommunalDetailBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.adapter.WelfareSlideProAdapter;
import com.amkj.dmsh.dominant.bean.DmlOptimizedSelDetailEntity;
import com.amkj.dmsh.dominant.bean.DmlOptimizedSelDetailEntity.DmlOptimizedSelDetailBean;
import com.amkj.dmsh.dominant.bean.DmlSearchDetailEntity.DmlSearchDetailBean.ProductListBean;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.mine.activity.ShopCarActivity;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
import com.amkj.dmsh.utils.webformatdata.ShareDataBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;
import q.rorbin.badgeview.Badge;

import static android.view.View.GONE;
import static com.amkj.dmsh.R.id.tv_communal_pro_tag;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getBadge;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.insertNewTotalData;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.skipProductUrl;
import static com.amkj.dmsh.constant.ConstantMethod.totalProNum;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.Q_DML_OPTIMIZED_DETAIL;
import static com.amkj.dmsh.constant.Url.Q_QUERY_CAR_COUNT;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/10
 * class description:多么优选-多么定制详情
 */
public class DmlOptimizedSelDetailActivity extends BaseActivity {
    @BindView(R.id.smart_refresh_ql_welfare_details)
    SmartRefreshLayout smart_refresh_ql_welfare_details;
    @BindView(R.id.communal_recycler)
    RecyclerView communal_recycler;
    @BindView(R.id.tv_header_title)
    TextView tv_header_titleAll;
    @BindView(R.id.iv_img_service)
    ImageView iv_img_service;
    @BindView(R.id.iv_img_share)
    ImageView iv_img_share;
    @BindView(R.id.fl_header_service)
    FrameLayout fl_header_service;
    //    滑动布局
    @BindView(R.id.dr_welfare_detail_pro)
    DrawerLayout dr_welfare_detail_pro;
    @BindView(R.id.ll_communal_pro_list)
    LinearLayout ll_communal_pro_list;
    @BindView(R.id.tv_communal_pro_title)
    TextView tv_communal_pro_title;
    @BindView(R.id.rv_communal_pro)
    RecyclerView rv_communal_pro;
    @BindView(R.id.tv_communal_pro_tag)
    TextView tv_wel_pro_tag;

    @BindView(R.id.tl_quality_bar)
    Toolbar tl_quality_bar;
    private List<CommunalDetailObjectBean> optDetailsList = new ArrayList();
    //侧滑商品列表
    private List<ProductListBean> welfareProductList = new ArrayList();
    private CommunalDetailAdapter optimizedDetailsAdapter;
    private Badge badge;
    private WelfareSlideProAdapter optimizedSlideProAdapter;
    private OptSelView optSelView;
    private String optimizedId;
    private DmlOptimizedSelDetailBean dmlOptimizedSelDetailBean;
    private DmlOptimizedSelDetailEntity optimizedSelDetailEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_ql_welfare_details;
    }

    @Override
    protected void initViews() {
        Intent intent = getIntent();
        optimizedId = intent.getStringExtra("optimizedId");
        iv_img_share.setVisibility(View.VISIBLE);
        iv_img_service.setImageResource(R.drawable.shop_car_gray_icon);
        tv_header_titleAll.setText("");
        communal_recycler.setNestedScrollingEnabled(false);
        communal_recycler.setLayoutManager(new LinearLayoutManager(DmlOptimizedSelDetailActivity.this));
        optimizedDetailsAdapter = new CommunalDetailAdapter(DmlOptimizedSelDetailActivity.this, optDetailsList);
        View optHeaderView = LayoutInflater.from(DmlOptimizedSelDetailActivity.this).inflate(R.layout.adapter_dml_optimized_sel, null, false);
        optSelView = new OptSelView();
        ButterKnife.bind(optSelView, optHeaderView);
        optSelView.initView();
        optimizedDetailsAdapter.addHeaderView(optHeaderView);
        communal_recycler.setAdapter(optimizedDetailsAdapter);
        rv_communal_pro.setLayoutManager(new LinearLayoutManager(DmlOptimizedSelDetailActivity.this));
        rv_communal_pro.addItemDecoration(new ItemDecoration.Builder()
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_gray_f_two_px).create());
        optimizedSlideProAdapter = new WelfareSlideProAdapter(DmlOptimizedSelDetailActivity.this, welfareProductList);
        optimizedSlideProAdapter.setEnableLoadMore(false);
        rv_communal_pro.setAdapter(optimizedSlideProAdapter);
        optimizedSlideProAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ProductListBean productListBean = (ProductListBean) view.getTag();
                if (productListBean != null) {
                    dr_welfare_detail_pro.closeDrawers();
                    skipProductUrl(DmlOptimizedSelDetailActivity.this, productListBean.getItemTypeId(), productListBean.getId());
                    //                    统计商品点击
                    totalProNum(productListBean.getId(), dmlOptimizedSelDetailBean.getId());
                }
            }
        });
        smart_refresh_ql_welfare_details.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData();
            }
        });
        optimizedDetailsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ShareDataBean shareDataBean = null;
                if (view.getId() == R.id.tv_communal_share && dmlOptimizedSelDetailBean != null) {
                    shareDataBean = new ShareDataBean(dmlOptimizedSelDetailBean.getPicUrl()
                            , !TextUtils.isEmpty(dmlOptimizedSelDetailBean.getTitle()) ?
                            dmlOptimizedSelDetailBean.getTitle() : "多么定制"
                            , getStrings(dmlOptimizedSelDetailBean.getSubtitle())
                            , Url.BASE_SHARE_PAGE_TWO + "m/template/goods/optimize_detail.html?id="
                            + dmlOptimizedSelDetailBean.getId(),dmlOptimizedSelDetailBean.getId());

                }
                CommunalWebDetailUtils.getCommunalWebInstance()
                        .setWebDataClick(DmlOptimizedSelDetailActivity.this, shareDataBean, view, loadHud);
            }
        });
        optimizedDetailsAdapter.setOnItemClickListener((adapter, view, position) -> {
            CommunalDetailObjectBean communalDetailBean = (CommunalDetailObjectBean) view.getTag();
            if (communalDetailBean != null) {
                skipProductUrl(DmlOptimizedSelDetailActivity.this, communalDetailBean.getItemTypeId(), communalDetailBean.getId());
                //                    统计商品点击
                totalProNum(communalDetailBean.getId(), dmlOptimizedSelDetailBean.getId());
            }
        });
        //          关闭手势滑动
        dr_welfare_detail_pro.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.END);
        badge = getBadge(DmlOptimizedSelDetailActivity.this, fl_header_service);
        totalPersonalTrajectory = insertNewTotalData(DmlOptimizedSelDetailActivity.this, optimizedId);
    }

    @Override
    protected void loadData() {
        getOptDetailData();
    }

    @Override
    protected View getLoadView() {
        return smart_refresh_ql_welfare_details;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getOptDetailData() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", optimizedId);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(DmlOptimizedSelDetailActivity.this, Q_DML_OPTIMIZED_DETAIL
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_refresh_ql_welfare_details.finishRefresh();
                        optimizedDetailsAdapter.loadMoreComplete();
                        Gson gson = new Gson();
                        optimizedSelDetailEntity = gson.fromJson(result, DmlOptimizedSelDetailEntity.class);
                        if (optimizedSelDetailEntity != null) {
                            if (optimizedSelDetailEntity.getCode().equals(SUCCESS_CODE)) {
                                optDetailsList.clear();
                                dmlOptimizedSelDetailBean = optimizedSelDetailEntity.getDmlOptimizedSelDetailBean();
                                setOptData(dmlOptimizedSelDetailBean);
                            } else if (!optimizedSelDetailEntity.getCode().equals(EMPTY_CODE)) {
                                showToast(DmlOptimizedSelDetailActivity.this, optimizedSelDetailEntity.getMsg());
                            }
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, optDetailsList, optimizedSelDetailEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_refresh_ql_welfare_details.finishRefresh();
                        optimizedDetailsAdapter.loadMoreComplete();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, optDetailsList, optimizedSelDetailEntity);
                    }

                    @Override
                    public void netClose() {
                        showToast(DmlOptimizedSelDetailActivity.this, R.string.unConnectedNetwork);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showToast(DmlOptimizedSelDetailActivity.this, R.string.invalidData);
                    }
                });
    }

    private void setOptData(DmlOptimizedSelDetailBean dmlOptimizedSelDetailBean) {
        tv_header_titleAll.setText(getStrings(dmlOptimizedSelDetailBean.getTitle()));
        GlideImageLoaderUtil.loadCenterCrop(DmlOptimizedSelDetailActivity.this, optSelView.iv_cover_detail_bg, dmlOptimizedSelDetailBean.getPicUrl());
        welfareProductList.clear();
        if (dmlOptimizedSelDetailBean.getProductList() != null
                && dmlOptimizedSelDetailBean.getProductList().size() > 0) {
            welfareProductList.addAll(dmlOptimizedSelDetailBean.getProductList());
            setPrepareData(dmlOptimizedSelDetailBean);
            optimizedSlideProAdapter.setNewData(welfareProductList);
        } else {
            tv_wel_pro_tag.setVisibility(View.GONE);
            ll_communal_pro_list.setVisibility(View.GONE);
        }
        List<CommunalDetailBean> descriptionList = dmlOptimizedSelDetailBean.getDescriptionList();
        if (descriptionList != null) {
            optDetailsList.clear();
            optDetailsList.addAll(CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(descriptionList));
            optimizedDetailsAdapter.setNewData(optDetailsList);
        }
    }

    private void setPrepareData(DmlOptimizedSelDetailBean dmlOptimizedSelDetailBean) {
        tv_wel_pro_tag.setVisibility(View.VISIBLE);
        ll_communal_pro_list.setVisibility(View.VISIBLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        int radius = AutoSizeUtils.mm2px(mAppContext, 50);
        drawable.setCornerRadii(new float[]{radius, radius, 0, 0, 0, 0, radius, radius});
        try {
            drawable.setColor(0xffffffff);
            drawable.setStroke(1, 0xffcccccc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tv_wel_pro_tag.setBackground(drawable);
        tv_wel_pro_tag.setText(welfareProductList.size() + "商品");
        tv_communal_pro_title.setText(getStrings(dmlOptimizedSelDetailBean.getTitle()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCarCount();
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
                            showToast(DmlOptimizedSelDetailActivity.this, requestStatus.getMsg());
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        try {
            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack() {
        finish();
    }

    //    页面分享
    @OnClick(R.id.iv_img_share)
    void sendShare() {
        if (dmlOptimizedSelDetailBean != null) {
            UMShareAction umShareAction = new UMShareAction(DmlOptimizedSelDetailActivity.this
                    , dmlOptimizedSelDetailBean.getPicUrl()
                    , !TextUtils.isEmpty(dmlOptimizedSelDetailBean.getTitle()) ?
                    dmlOptimizedSelDetailBean.getTitle() : "多么定制"
                    , getStrings(dmlOptimizedSelDetailBean.getSubtitle())
                    , Url.BASE_SHARE_PAGE_TWO + "m/template/goods/optimize_detail.html?id="
                    + dmlOptimizedSelDetailBean.getId());
            umShareAction.setOnShareSuccessListener(new UMShareAction.OnShareSuccessListener() {
                @Override
                public void onShareSuccess() {
                    ConstantMethod.addArticleShareCount(dmlOptimizedSelDetailBean.getId());
                }
            });
        }
    }

    @OnClick(R.id.iv_img_service)
    void skipShopCar() {
        Intent intent = new Intent(DmlOptimizedSelDetailActivity.this, ShopCarActivity.class);
        startActivity(intent);
    }

    class OptSelView {
        @BindView(R.id.iv_cover_detail_bg)
        ImageView iv_cover_detail_bg;
        @BindView(R.id.rel_dml_optimized)
        RelativeLayout rel_dml_optimized;

        public void initView() {
            rel_dml_optimized.setVisibility(GONE);
        }
    }

    @OnClick(tv_communal_pro_tag)
    void openSlideProList() {
//            商品列表
        if (dr_welfare_detail_pro.isDrawerOpen(ll_communal_pro_list)) {
            dr_welfare_detail_pro.closeDrawers();
        } else {
            dr_welfare_detail_pro.openDrawer(ll_communal_pro_list);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (totalPersonalTrajectory != null) {
            Map<String, String> totalMap = new HashMap<>();
            totalMap.put("relate_id", optimizedId);
            totalPersonalTrajectory.stopTotal(totalMap);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (totalPersonalTrajectory != null) {
            Map<String, String> totalMap = new HashMap<>();
            totalMap.put("relate_id", optimizedId);
            totalPersonalTrajectory.stopTotal(totalMap);
        }
    }
}
