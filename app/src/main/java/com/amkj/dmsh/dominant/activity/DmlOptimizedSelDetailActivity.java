package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
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
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.constant.CommunalDetailBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
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
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
import com.amkj.dmsh.utils.webformatdata.ShareDataBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
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
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.skipProductUrl;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.Q_DML_OPTIMIZED_DETAIL;
import static com.amkj.dmsh.dao.AddClickDao.totalProNum;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/7/10
 * class description:多么优选-多么定制详情
 */
public class DmlOptimizedSelDetailActivity extends BaseActivity {
    @BindView(R.id.smart_communal_refresh)
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
        return R.layout.activity_ql_optimized_details;
    }

    @Override
    protected void initViews() {
        Intent intent = getIntent();
        optimizedId = intent.getStringExtra("optimizedId");
        //记录埋点参数sourceId(多么定制专题对应的id)
        ConstantMethod.saveSourceId(getClass().getSimpleName(), String.valueOf(optimizedId));
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
                .setDividerId(R.drawable.item_divider_gray_f_one_px).create());
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
                    totalProNum(getActivity(), productListBean.getId(), optimizedId);
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
                            + dmlOptimizedSelDetailBean.getId(), dmlOptimizedSelDetailBean.getId());

                }
                CommunalWebDetailUtils.getCommunalWebInstance()
                        .setWebDataClick(DmlOptimizedSelDetailActivity.this, shareDataBean, view, loadHud);
            }
        });
        //          关闭手势滑动
        dr_welfare_detail_pro.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.END);
        badge = getBadge(DmlOptimizedSelDetailActivity.this, fl_header_service);
    }

    @Override
    protected void loadData() {
        getOptDetailData();
    }

    @Override
    public View getLoadView() {
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

                        optimizedSelDetailEntity = GsonUtils.fromJson(result, DmlOptimizedSelDetailEntity.class);
                        if (optimizedSelDetailEntity != null) {
                            if (optimizedSelDetailEntity.getCode().equals(SUCCESS_CODE)) {
                                optDetailsList.clear();
                                dmlOptimizedSelDetailBean = optimizedSelDetailEntity.getDmlOptimizedSelDetailBean();
                                setOptData(dmlOptimizedSelDetailBean);
                            } else if (!optimizedSelDetailEntity.getCode().equals(EMPTY_CODE)) {
                                showToast(optimizedSelDetailEntity.getMsg());
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
                    public void onError(Throwable throwable) {
                        showToast( R.string.invalidData);
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
            new UMShareAction(DmlOptimizedSelDetailActivity.this
                    , dmlOptimizedSelDetailBean.getPicUrl()
                    , !TextUtils.isEmpty(dmlOptimizedSelDetailBean.getTitle()) ?
                    dmlOptimizedSelDetailBean.getTitle() : "多么定制"
                    , getStrings(dmlOptimizedSelDetailBean.getSubtitle())
                    , Url.BASE_SHARE_PAGE_TWO + "m/template/goods/optimize_detail.html?id="
                    + dmlOptimizedSelDetailBean.getId(),dmlOptimizedSelDetailBean.getId());
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
    protected void postEventResult(@NonNull EventMessage message) {
        if (message.type.equals(ConstantVariable.UPDATE_CAR_NUM)) {
            if (badge!=null){
                badge.setBadgeNumber((int) message.result);
            }
        }
    }

    public String getOptimizedId() {
        return optimizedId;
    }
}
