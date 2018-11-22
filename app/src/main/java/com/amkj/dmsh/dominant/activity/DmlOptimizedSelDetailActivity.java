package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.baichuan.trade.biz.AlibcConstants;
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult;
import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.BaseAddCarProInfoBean;
import com.amkj.dmsh.constant.CommunalDetailBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.dominant.adapter.WelfareSlideProAdapter;
import com.amkj.dmsh.dominant.bean.DmlOptimizedSelDetailEntity;
import com.amkj.dmsh.dominant.bean.DmlOptimizedSelDetailEntity.DmlOptimizedSelDetailBean;
import com.amkj.dmsh.dominant.bean.DmlSearchDetailEntity.DmlSearchDetailBean.ProductListBean;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.mine.activity.ShopCarActivity;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.utils.Log;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
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
import static com.amkj.dmsh.constant.ConstantMethod.getDetailsDataList;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.insertNewTotalData;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.skipProductUrl;
import static com.amkj.dmsh.constant.ConstantMethod.totalProNum;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_COUPON;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_COUPON_PACKAGE;

;

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

    @BindView(tv_communal_pro_tag)
    TextView tv_wel_pro_tag;
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
                .setDividerId(R.drawable.item_divider_gray_f_two_px)






                .create());
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
        smart_refresh_ql_welfare_details.setOnRefreshListener((refreshLayout) -> {
                loadData();
        });
        optimizedDetailsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (loadHud != null) {
                    loadHud.show();
                }
                switch (view.getId()) {
                    case R.id.img_product_coupon_pic:
                        int couponId = (int) view.getTag(R.id.iv_avatar_tag);
                        int type = (int) view.getTag(R.id.iv_type_tag);
                        if (couponId > 0) {
                            if (userId != 0) {
                                if (type == TYPE_COUPON) {
                                    getDirectCoupon(couponId);
                                } else if (type == TYPE_COUPON_PACKAGE) {
                                    getDirectCouponPackage(couponId);
                                }
                            } else {
                                if (loadHud != null) {
                                    loadHud.dismiss();
                                }
                                getLoginStatus(DmlOptimizedSelDetailActivity.this);
                            }
                        }
                        break;
                    case R.id.iv_communal_cover_wrap:
                        CommunalDetailObjectBean detailObjectBean = (CommunalDetailObjectBean) view.getTag(R.id.iv_tag);
                        if (detailObjectBean != null) {
                            if (detailObjectBean.getItemType() == CommunalDetailObjectBean.TYPE_GOODS_IMG) {
                                Intent intent = new Intent(DmlOptimizedSelDetailActivity.this, ShopScrollDetailsActivity.class);
                                intent.putExtra("productId", String.valueOf(detailObjectBean.getId()));
                                startActivity(intent);
                            }
                        }
                        loadHud.dismiss();
                        break;

                    case R.id.ll_layout_tb_coupon:
                        CommunalDetailObjectBean couponBean = (CommunalDetailObjectBean) view.getTag();
                        if (couponBean != null) {
                            if (loadHud != null) {
                                loadHud.dismiss();
                            }
                            if (userId != 0) {
                                skipAliBCWebView(couponBean.getCouponUrl());
                            } else {
                                if (loadHud != null) {
                                    loadHud.dismiss();
                                }
                                getLoginStatus(DmlOptimizedSelDetailActivity.this);
                            }
                        }
                        break;
                    case R.id.iv_ql_bl_add_car:
                        CommunalDetailObjectBean qualityWelPro = (CommunalDetailObjectBean) view.getTag();
                        loadHud.show();
                        if (qualityWelPro != null) {
                            if (ConstantMethod.userId > 0) {
                                switch (view.getId()) {
                                    case R.id.iv_ql_bl_add_car:
                                        BaseAddCarProInfoBean baseAddCarProInfoBean = new BaseAddCarProInfoBean();
                                        baseAddCarProInfoBean.setProductId(qualityWelPro.getId());
                                        baseAddCarProInfoBean.setProName(getStrings(qualityWelPro.getName()));
                                        baseAddCarProInfoBean.setProPic(getStrings(qualityWelPro.getPicUrl()));
                                        ConstantMethod constantMethod = new ConstantMethod();
                                        constantMethod.addShopCarGetSku(DmlOptimizedSelDetailActivity.this, baseAddCarProInfoBean, loadHud);
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
                                getLoginStatus(DmlOptimizedSelDetailActivity.this);
                            }
                        }
                        break;
                    case R.id.tv_communal_tb_link:
                    case R.id.iv_communal_tb_cover:
                        if (loadHud != null) {
                            loadHud.show();
                        }
                        CommunalDetailObjectBean tbLink = (CommunalDetailObjectBean) view.getTag(R.id.iv_tag);
                        if(tbLink == null){
                            tbLink = (CommunalDetailObjectBean) view.getTag();
                        }
                        if(tbLink!=null){
                            skipAliBCWebView(tbLink.getUrl());
                        }
                        break;
                }
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
        totalPersonalTrajectory = insertNewTotalData(DmlOptimizedSelDetailActivity.this,optimizedId);
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
        String url = Url.BASE_URL + Url.Q_DML_OPTIMIZED_DETAIL;
        Map<String, Object> params = new HashMap<>();
        params.put("id", optimizedId);
        if(userId >0){
            params.put("uid",userId);
        }
        NetLoadUtils.getQyInstance().loadNetDataPost(DmlOptimizedSelDetailActivity.this, url
                , params, new NetLoadUtils.NetLoadListener() {
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
                        NetLoadUtils.getQyInstance().showLoadSir(loadService,optDetailsList, optimizedSelDetailEntity);
                    }

                    @Override
                    public void netClose() {
                        smart_refresh_ql_welfare_details.finishRefresh();
                        optimizedDetailsAdapter.loadMoreComplete();
                        showToast(DmlOptimizedSelDetailActivity.this, R.string.unConnectedNetwork);
                        NetLoadUtils.getQyInstance().showLoadSir(loadService,optDetailsList, optimizedSelDetailEntity);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        smart_refresh_ql_welfare_details.finishRefresh();
                        optimizedDetailsAdapter.loadMoreComplete();
                        showToast(DmlOptimizedSelDetailActivity.this, R.string.invalidData);
                        NetLoadUtils.getQyInstance().showLoadSir(loadService,optDetailsList, optimizedSelDetailEntity);
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
            optDetailsList.addAll(getDetailsDataList(descriptionList));
            optimizedDetailsAdapter.setNewData(optDetailsList);
        }
    }

    private void setPrepareData(DmlOptimizedSelDetailBean dmlOptimizedSelDetailBean) {
        tv_wel_pro_tag.setVisibility(View.VISIBLE);
        ll_communal_pro_list.setVisibility(View.VISIBLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        int radius = AutoSizeUtils.mm2px(mAppContext,50);
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

    private void getDirectCoupon(int id) {
        String url = Url.BASE_URL + Url.FIND_ARTICLE_COUPON;
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("couponId", id);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals("01")) {
                        showToast(DmlOptimizedSelDetailActivity.this, requestStatus.getResult() != null ? requestStatus.getResult().getMsg() : requestStatus.getMsg());
                    } else {
                        showToast(DmlOptimizedSelDetailActivity.this, requestStatus.getResult() != null ? requestStatus.getResult().getMsg() : requestStatus.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(DmlOptimizedSelDetailActivity.this, R.string.Get_Coupon_Fail);
                if (loadHud != null) {
                    loadHud.dismiss();
                }
            }
        });
    }

    private void getDirectCouponPackage(int couponId) {
        String url = Url.BASE_URL + Url.COUPON_PACKAGE;
        Map<String, Object> params = new HashMap<>();
        params.put("uId", userId);
        params.put("cpId", couponId);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null) {
                    if (requestStatus.getCode().equals("01")) {
                        showToast(DmlOptimizedSelDetailActivity.this, requestStatus.getResult() != null ? requestStatus.getResult().getMsg() : requestStatus.getMsg());
                    } else {
                        showToast(DmlOptimizedSelDetailActivity.this, requestStatus.getResult() != null ? requestStatus.getResult().getMsg() : requestStatus.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(DmlOptimizedSelDetailActivity.this, R.string.Get_Coupon_Fail);
                if (loadHud != null) {
                    loadHud.dismiss();
                }
            }
        });
    }

    public void skipAliBCWebView(final String url) {
        if (!TextUtils.isEmpty(url)) {
            if (userId != 0) {
                skipNewTaoBao(url);
            } else {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                getLoginStatus(DmlOptimizedSelDetailActivity.this);
            }
        } else {
            showToast(DmlOptimizedSelDetailActivity.this, "地址缺失");
            if (loadHud != null) {
                loadHud.dismiss();
            }
        }
    }

    private void skipNewTaoBao(final String url) {
        AlibcLogin alibcLogin = AlibcLogin.getInstance();
        alibcLogin.showLogin(new AlibcLoginCallback() {
            @Override
            public void onSuccess(int i) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                skipNewShopDetails(url);
            }

            @Override
            public void onFailure(int code, String msg) {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                showToast(DmlOptimizedSelDetailActivity.this, "登录失败 ");
            }
        });
    }

    private void skipNewShopDetails(String url) {
        //提供给三方传递配置参数
        Map<String, String> exParams = new HashMap<>();
        exParams.put(AlibcConstants.ISV_CODE, "appisvcode");
        //设置页面打开方式
        AlibcShowParams showParams = new AlibcShowParams(OpenType.Native, false);
        //实例化商品详情 itemID打开page
        AlibcBasePage ordersPage = new AlibcPage(url);
        AlibcTrade.show(DmlOptimizedSelDetailActivity.this, ordersPage, showParams, null, exParams, new AlibcTradeCallback() {
            @Override
            public void onTradeSuccess(AlibcTradeResult alibcTradeResult) {
                //打开电商组件，用户操作中成功信息回调。tradeResult：成功信息（结果类型：加购，支付；支付结果）
//                showToast(context, "获取详情成功");
                Log.d("商品详情", "onTradeSuccess: ");
            }

            @Override
            public void onFailure(int code, String msg) {
                Log.d("商品详情", "onFailure: " + code + msg);
                //打开电商组件，用户操作中错误信息回调。code：错误码；msg：错误信息
//                showToast(ShopTimeScrollDetailsActivity.this, msg);
            }
        });
    }

    private void getCarCount() {
        if (userId > 0) {
            //购物车数量展示
            String url = Url.BASE_URL + Url.Q_QUERY_CAR_COUNT;
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                    if (requestStatus != null) {
                        if (requestStatus.getCode().equals("01")) {
                            int cartNumber = requestStatus.getResult().getCartNumber();
                            badge.setBadgeNumber(cartNumber);
                        } else if (!requestStatus.getCode().equals("02")) {
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
        if(totalPersonalTrajectory!=null){
            Map<String,String> totalMap = new HashMap<>();
            totalMap.put("relate_id",optimizedId);
            totalPersonalTrajectory.stopTotal(totalMap);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(totalPersonalTrajectory!=null){
            Map<String,String> totalMap = new HashMap<>();
            totalMap.put("relate_id",optimizedId);
            totalPersonalTrajectory.stopTotal(totalMap);
        }
    }
}
