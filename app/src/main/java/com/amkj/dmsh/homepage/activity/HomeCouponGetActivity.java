package com.amkj.dmsh.homepage.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.amkj.dmsh.base.BaseApplication;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.BaseAddCarProInfoBean;
import com.amkj.dmsh.constant.CommunalDetailBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.dominant.adapter.QualityProTitleAdapter;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.homepage.bean.CommunalDescriptionEntity;
import com.amkj.dmsh.homepage.bean.CommunalDescriptionEntity.CommunalDescriptionBean;
import com.amkj.dmsh.shopdetails.activity.DirectMyCouponActivity;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.Log;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.skipProductUrl;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TOTAL_COUNT_TWENTY;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_COUPON;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_COUPON_PACKAGE;

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

        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            loadData();
        });
        download_btn_communal.attachToRecyclerView(communal_recycler, null, new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrollY += dy;
                if (!recyclerView.canScrollVertically(-1)) {
                    scrollY = 0;
                }
                if (screenHeight == 0) {
                    BaseApplication app = (BaseApplication) getApplication();
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
        communal_recycler.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                // 设置分隔线资源ID
                .setDividerId(R.drawable.item_divider_five_dp)
                // 开启绘制分隔线，默认关闭
                .enableDivider(true)
                // 是否关闭标签点击事件，默认开启
                .disableHeaderClick(false)
                // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                .setHeaderClickListener(null)
                .create());
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
                if (page * TOTAL_COUNT_TWENTY <= likedProductBeanList.size()) {
                    page++;
                    getCouponData();
                } else {
                    couponProTitleAdapter.loadMoreEnd();
                    couponProTitleAdapter.setEnableLoadMore(false);
                }
            }
        }, communal_recycler);

        communalDetailAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
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
                                getLoginStatus(HomeCouponGetActivity.this);
                            }
                        }
                        break;
                    case R.id.iv_communal_cover_wrap:
                        CommunalDetailObjectBean detailObjectBean = (CommunalDetailObjectBean) view.getTag(R.id.iv_tag);
                        if (detailObjectBean != null) {
                            if (detailObjectBean.getItemType() == CommunalDetailObjectBean.TYPE_GOODS_IMG) {
                                Intent intent = new Intent(HomeCouponGetActivity.this, ShopScrollDetailsActivity.class);
                                intent.putExtra("productId", String.valueOf(detailObjectBean.getId()));
                                startActivity(intent);
                            }
                        }
                        loadHud.dismiss();
                        break;
                    case R.id.tv_click_get_lucky_money:
                    case R.id.ll_get_lucky_money:
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
                                getLoginStatus(HomeCouponGetActivity.this);
                            }
                        }
                        break;
                    case R.id.iv_ql_bl_add_car:
                        CommunalDetailObjectBean qualityWelPro = (CommunalDetailObjectBean) view.getTag();
                        if (qualityWelPro != null) {
                            if (userId > 0) {
                                BaseAddCarProInfoBean baseAddCarProInfoBean = new BaseAddCarProInfoBean();
                                baseAddCarProInfoBean.setProductId(qualityWelPro.getId());
                                baseAddCarProInfoBean.setProName(getStrings(qualityWelPro.getName()));
                                baseAddCarProInfoBean.setProPic(getStrings(qualityWelPro.getPicUrl()));
                                ConstantMethod constantMethod = new ConstantMethod();
                                constantMethod.addShopCarGetSku(HomeCouponGetActivity.this, baseAddCarProInfoBean, loadHud);
                            } else {
                                loadHud.dismiss();
                                getLoginStatus(HomeCouponGetActivity.this);
                            }
                        }
                        break;
                    default:
                        loadHud.dismiss();
                        break;
                }
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
    protected View getLoadView() {
        return smart_communal_refresh;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    private void getCouponData() {
        String url = Url.BASE_URL + Url.H_COUPON_CENTER_DATA;
        XUtil.Get(url, null, new MyCallBack<String>() {
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
                            couponDescriptionList.addAll(ConstantMethod.getDetailsDataList(descriptionBeanList));
                        }
                        getCouponProData(communalDescriptionBean.getId());
                    } else if (!shopDetailsEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(HomeCouponGetActivity.this, shopDetailsEntity.getMsg());
                    }
                    communalDetailAdapter.setNewData(couponDescriptionList);
                }
                NetLoadUtils.getQyInstance().showLoadSirSuccess(loadService);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                NetLoadUtils.getQyInstance().showLoadSirLoadFailed(loadService);
            }
        });
    }

    private void getCouponProData(int id) {
        String url = Url.BASE_URL + Url.Q_SP_DETAIL_DOMO_RECOM;
        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", page);
        params.put("showCount", DEFAULT_TOTAL_COUNT);
        params.put("reminder_id", id);
        NetLoadUtils.getQyInstance().loadNetDataPost(HomeCouponGetActivity.this, url, params, new NetLoadUtils.NetLoadListener() {
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
                        likedProductBeanList.addAll(likedProductEntity.getLikedProductBeanList());
                    }
                }
                if (likedProductBeanList.size() > 0) {
                    recommendGoodView.tv_pro_title.setVisibility(View.VISIBLE);
                }
                couponProTitleAdapter.notifyDataSetChanged();
            }

            @Override
            public void netClose() {
                smart_communal_refresh.finishRefresh();
                couponProTitleAdapter.loadMoreComplete();
                showToast(HomeCouponGetActivity.this,R.string.unConnectedNetwork);
            }

            @Override
            public void onError(Throwable throwable) {
                smart_communal_refresh.finishRefresh();
                couponProTitleAdapter.loadMoreComplete();
            }
        });
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
                        showToast(HomeCouponGetActivity.this, requestStatus.getResult() != null ? requestStatus.getResult().getMsg() : requestStatus.getMsg());
                    } else {
                        showToast(HomeCouponGetActivity.this, requestStatus.getResult() != null ? requestStatus.getResult().getMsg() : requestStatus.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(HomeCouponGetActivity.this, R.string.Get_Coupon_Fail);
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
                        showToast(HomeCouponGetActivity.this, requestStatus.getResult() != null ? requestStatus.getResult().getMsg() : requestStatus.getMsg());
                    } else {
                        showToast(HomeCouponGetActivity.this, requestStatus.getResult() != null ? requestStatus.getResult().getMsg() : requestStatus.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(HomeCouponGetActivity.this, R.string.Get_Coupon_Fail);
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
                getLoginStatus(HomeCouponGetActivity.this);
            }
        } else {
            showToast(HomeCouponGetActivity.this, "地址缺失");
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
                showToast(HomeCouponGetActivity.this, "登录失败 ");
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
        AlibcTrade.show(HomeCouponGetActivity.this, ordersPage, showParams, null, exParams, new AlibcTradeCallback() {
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
                    , Url.BASE_SHARE_PAGE_TWO + "m/template/home/couponCenter.html");
        }
    }

}
