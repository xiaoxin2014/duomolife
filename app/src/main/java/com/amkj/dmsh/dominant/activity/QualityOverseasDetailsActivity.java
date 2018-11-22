package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.BaseAddCarProInfoBean;
import com.amkj.dmsh.constant.CommunalDetailBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.dominant.adapter.QualityTypeProductAdapter;
import com.amkj.dmsh.dominant.bean.QualityShopDescripEntity;
import com.amkj.dmsh.dominant.bean.QualityShopDescripEntity.QualityShopDescBean;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.mine.activity.ShopCarActivity;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.user.bean.UserLikedProductEntity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.Log;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
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
import static com.amkj.dmsh.constant.ConstantMethod.getDetailsDataList;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.insertNewTotalData;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.DEFAULT_TOTAL_COUNT;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_COUPON;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_COUPON_PACKAGE;

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

        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            loadData();
            qualityTypeProductAdapter.loadMoreEnd(true);
        });
        qualityTypeProductAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (page * DEFAULT_TOTAL_COUNT <= qualityTypeProductAdapter.getItemCount()) {
                    page++;
                    getRecommendData();
                } else {
                    qualityTypeProductAdapter.loadMoreEnd();
                }
            }
        }, communal_recycler);

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
        communalDescripAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
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
                                getLoginStatus(QualityOverseasDetailsActivity.this);
                            }
                        }
                        break;
                    case R.id.iv_communal_cover_wrap:
                        CommunalDetailObjectBean detailObjectBean = (CommunalDetailObjectBean) view.getTag(R.id.iv_tag);
                        if (detailObjectBean != null) {
                            if (detailObjectBean.getItemType() == CommunalDetailObjectBean.TYPE_GOODS_IMG) {
                                Intent intent = new Intent(QualityOverseasDetailsActivity.this, ShopScrollDetailsActivity.class);
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
                                getLoginStatus(QualityOverseasDetailsActivity.this);
                            }
                        }
                        break;
                    case R.id.iv_ql_bl_add_car:
                        CommunalDetailObjectBean qualityWelPro = (CommunalDetailObjectBean) view.getTag();
                        loadHud.show();
                        if (qualityWelPro != null) {
                            if (userId > 0) {
                                BaseAddCarProInfoBean baseAddCarProInfoBean = new BaseAddCarProInfoBean();
                                baseAddCarProInfoBean.setProductId(qualityWelPro.getId());
                                baseAddCarProInfoBean.setProName(getStrings(qualityWelPro.getName()));
                                baseAddCarProInfoBean.setProPic(getStrings(qualityWelPro.getPicUrl()));
                                ConstantMethod constantMethod = new ConstantMethod();
                                constantMethod.addShopCarGetSku(QualityOverseasDetailsActivity.this, baseAddCarProInfoBean, loadHud);
                                constantMethod.setAddOnCarListener(new ConstantMethod.OnAddCarListener() {
                                    @Override
                                    public void onAddCarSuccess() {
                                        getCarCount();
                                    }
                                });
                                break;
                            } else {
                                loadHud.dismiss();
                                getLoginStatus(QualityOverseasDetailsActivity.this);
                            }
                        }
                        break;
                }
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
        if (NetWorkUtils.checkNet(QualityOverseasDetailsActivity.this)) {
            Map<String, Object> params = new HashMap<>();
            params.put("id", overseasId);
            if (userId > 0) {
                params.put("uid", userId);
            }
            XUtil.Post(url, params, new MyCallBack<String>() {
                @Override
                public void onSuccess(String result) {
                    smart_communal_refresh.finishRefresh();
                    qualityTypeProductAdapter.loadMoreComplete();
                    Gson gson = new Gson();
                    qualityShopDescripEntity = gson.fromJson(result, QualityShopDescripEntity.class);
                    if (qualityShopDescripEntity != null) {
                        if (qualityShopDescripEntity.getCode().equals("01")) {
                            setData(qualityShopDescripEntity.getQualityShopDescBean());
                        } else if (qualityShopDescripEntity.getCode().equals("02")) {
                            if (page == 1) {
                                showToast(QualityOverseasDetailsActivity.this, R.string.invalidData);
                            }
                        } else {
                            showToast(QualityOverseasDetailsActivity.this, qualityShopDescripEntity.getMsg());
                        }
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    smart_communal_refresh.finishRefresh();
                    qualityTypeProductAdapter.loadMoreComplete();
                    showToast(QualityOverseasDetailsActivity.this, R.string.invalidData);
                    super.onError(ex, isOnCallback);
                }
            });
        } else {
            smart_communal_refresh.finishRefresh();
            qualityTypeProductAdapter.loadMoreComplete();
            showToast(this, R.string.unConnectedNetwork);
        }
    }

    private void getRecommendData() {
        String url = Url.BASE_URL + Url.QUALITY_OVERSEAS_DETAIL_LIST;
        Map<String, Object> params = new HashMap<>();
        params.put("showCount", DEFAULT_TOTAL_COUNT);
        params.put("currentPage", page);
        params.put("id", overseasId);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getQyInstance().loadNetDataPost(QualityOverseasDetailsActivity.this, url
                , params, new NetLoadUtils.NetLoadListener() {
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
                    } else if (!userLikedProductEntity.getCode().equals(EMPTY_CODE)) {
                        showToast(QualityOverseasDetailsActivity.this, userLikedProductEntity.getMsg());
                    }
                    qualityTypeProductAdapter.notifyDataSetChanged();
                }
                NetLoadUtils.getQyInstance().showLoadSir(loadService,proDetailList,userLikedProductEntity);
            }

            @Override
            public void netClose() {
                smart_communal_refresh.finishRefresh();
                qualityTypeProductAdapter.loadMoreComplete();
                showToast(QualityOverseasDetailsActivity.this, R.string.unConnectedNetwork);
                NetLoadUtils.getQyInstance().showLoadSir(loadService,proDetailList,userLikedProductEntity);
            }

            @Override
            public void onError(Throwable throwable) {
                smart_communal_refresh.finishRefresh();
                qualityTypeProductAdapter.loadMoreComplete();
                showToast(QualityOverseasDetailsActivity.this, R.string.invalidData);
                NetLoadUtils.getQyInstance().showLoadSir(loadService,proDetailList,userLikedProductEntity);
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
                        showToast(QualityOverseasDetailsActivity.this, requestStatus.getResult() != null ? requestStatus.getResult().getMsg() : requestStatus.getMsg());
                    } else {
                        showToast(QualityOverseasDetailsActivity.this, requestStatus.getResult() != null ? requestStatus.getResult().getMsg() : requestStatus.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(QualityOverseasDetailsActivity.this, R.string.Get_Coupon_Fail);
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
                        showToast(QualityOverseasDetailsActivity.this, requestStatus.getResult() != null ? requestStatus.getResult().getMsg() : requestStatus.getMsg());
                    } else {
                        showToast(QualityOverseasDetailsActivity.this, requestStatus.getResult() != null ? requestStatus.getResult().getMsg() : requestStatus.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(QualityOverseasDetailsActivity.this, R.string.Get_Coupon_Fail);
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
                getLoginStatus(QualityOverseasDetailsActivity.this);
            }
        } else {
            showToast(QualityOverseasDetailsActivity.this, "地址缺失");
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
                showToast(QualityOverseasDetailsActivity.this, "登录失败 ");
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
        AlibcTrade.show(QualityOverseasDetailsActivity.this, ordersPage, showParams, null, exParams, new AlibcTradeCallback() {
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
                            showToast(QualityOverseasDetailsActivity.this, requestStatus.getMsg());
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
            descripDetailList.addAll(getDetailsDataList(descripList));
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
                    , Url.BASE_SHARE_PAGE_TWO + "m/template/goods/Project_details.html?id=" + qualityShopDescripEntity.getQualityShopDescBean().getId());
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
