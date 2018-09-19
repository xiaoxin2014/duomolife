package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ali.auth.third.ui.context.CallbackContext;
import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcDetailPage;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.baichuan.trade.biz.AlibcConstants;
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult;
import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.NetLoadUtils;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.constant.CommunalDetailBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.constant.XUtil;
import com.amkj.dmsh.homepage.activity.DoMoLifeCommunalActivity;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.qyservice.QyProductIndentInfo;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.shopdetails.bean.ShopDetailsEntity;
import com.amkj.dmsh.shopdetails.bean.ShopDetailsEntity.ShopPropertyBean;
import com.amkj.dmsh.shopdetails.bean.ShopDetailsEntity.ShopPropertyBean.LuckyMoneyBean;
import com.amkj.dmsh.utils.Log;
import com.amkj.dmsh.utils.NetWorkUtils;
import com.amkj.dmsh.utils.inteface.MyCallBack;
import com.amkj.dmsh.views.CustomPopWindow;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.tablayout.CommonTabLayout;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.stat.StatService;
import com.umeng.socialize.UMShareAPI;
import com.zhy.autolayout.utils.AutoUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.iwgang.countdownview.CountdownView;
import cn.iwgang.countdownview.DynamicConfig;

import static android.view.View.GONE;
import static com.amkj.dmsh.constant.ConstantMethod.getFloatNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;
import static com.amkj.dmsh.constant.ConstantMethod.isEndOrStartTime;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_EMPTY_OBJECT;
import static com.amkj.dmsh.utils.glide.GlideImageLoaderUtil.getWaterMarkImgUrl;

;

public class ShopTimeScrollDetailsActivity extends BaseActivity {
    @BindView(R.id.smart_time_product_details)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.banner_shop_time_details)
    ConvenientBanner bannerShopTimeDetails;
    @BindView(R.id.tv_promotion_product_time_status)
    TextView tvPromotionProductTimeStatus;
    @BindView(R.id.ct_promotion_product_time)
    CountdownView ctPromotionProductTime;
    @BindView(R.id.rel_promotion_product_time)
    RelativeLayout relPromotionProductTime;
    @BindView(R.id.tv_shop_time_product_name)
    TextView tvShopTimeProductName;
    @BindView(R.id.tv_shop_time_product_price)
    TextView tvShopTimeProductPrice;
    @BindView(R.id.tv_shop_time_product_mk_price)
    TextView tvShopTimeProductMkPrice;
    @BindView(R.id.tv_product_details_join_count)
    TextView tvProductDetailsJoinCount;
    @BindView(R.id.communal_recycler_wrap)
    RecyclerView communalRecyclerWrap;
    @BindView(R.id.ctb_promotion_product)
    CommonTabLayout ctbPromotionProduct;
    @BindView(R.id.fl_promotion_product_details_contain)
    FrameLayout flPromotionProductDetailsContain;
    @BindView(R.id.tv_time_product_details_service)
    TextView tvTimeProductDetailsService;
    @BindView(R.id.tv_time_product_details_warm)
    TextView tvTimeProductDetailsWarm;
    @BindView(R.id.tv_time_product_details_buy_it)
    TextView tvTimeProductDetailsBuyIt;
    //    轮播图片视频
    private List<CommunalADActivityBean> imagesVideoList = new ArrayList<>();
    private List<CommunalDetailObjectBean> itemBodyList = new ArrayList<>();
    private CommunalDetailAdapter contentOfficialAdapter;
    //产品Id
    private String productId;
    private int page = 1;
    private ShopDetailsEntity shopDetailsEntity;
    private String thirdUrl;
    private String thirdId;
    private int scrollY = 0;
    private CustomPopWindow mCustomPopWindow;
    private String sharePageUrl = Url.BASE_SHARE_PAGE_TWO + "m/template/common/taoBaoGoods.html?id=";
    private ShopPropertyBean shopProperty;
    private ConstantMethod constantMethod;
    private CBViewHolderCreator cbViewHolderCreator;

    @Override
    protected int getContentView() {
        return R.layout.activity_time_goods_details_new;
    }

    @Override
    protected void initViews() {
        tvHeaderTitle.setText("");
        Intent intent = getIntent();
        productId = intent.getStringExtra("productId");
        communalRecyclerWrap.setLayoutManager(new LinearLayoutManager(ShopTimeScrollDetailsActivity.this));
        communalRecyclerWrap.setNestedScrollingEnabled(false);
        contentOfficialAdapter = new CommunalDetailAdapter(ShopTimeScrollDetailsActivity.this, itemBodyList);
        communalRecyclerWrap.setAdapter(contentOfficialAdapter);

        smart_communal_refresh.setOnRefreshListener((refreshLayout) -> {
            loadData();
        });
        contentOfficialAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (loadHud != null) {
                    loadHud.show();
                }
                switch (view.getId()) {
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
                                getLoginStatus(ShopTimeScrollDetailsActivity.this);
                            }
                        }
                        break;
                }
            }
        });
        DynamicConfig.Builder dynamic = new DynamicConfig.Builder();
        dynamic.setSuffixTextSize(AutoUtils.getPercentWidthSize(26));
        dynamic.setTimeTextSize(AutoUtils.getPercentWidthSize(26));
        dynamic.setSuffixGravity(Gravity.CENTER);
        DynamicConfig.BackgroundInfo backgroundInfo = new DynamicConfig.BackgroundInfo();
        backgroundInfo.setColor(getResources().getColor(R.color.text_pink_red))
                .setBorderRadius((float) AutoUtils.getPercentWidthSize(8))
                .setBorderColor(getResources().getColor(R.color.text_pink_red))
                .setShowTimeBgBorder(true);
        dynamic.setBackgroundInfo(backgroundInfo);
        ctPromotionProductTime.dynamicShow(dynamic.build());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        try {
            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
            CallbackContext.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**
         * 销毁电商SDK相关资源引用，防止内存泄露
         */
        AlibcTradeSDK.destory();
    }

    @Override
    protected void loadData() {
        page = 1;
        getProductData();
    }

//    @Override
//    protected View getLoadView() {
//        return smart_communal_refresh;
//    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    //商品详情内容
    private void getProductData() {
        String url = Url.BASE_URL + Url.H_TIME_GOODS_DETAILS;
        Map<String, Object> params = new HashMap<>();
        params.put("id", productId);
        if (userId > 0) {
            params.put("userId", userId);
        }
        NetLoadUtils.getQyInstance().loadNetDataPost(ShopTimeScrollDetailsActivity.this, url
                , params, new NetLoadUtils.NetLoadListener() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();
                        Gson gson = new Gson();
                        shopDetailsEntity = gson.fromJson(result, ShopDetailsEntity.class);
                        if (shopDetailsEntity != null) {
                            if (shopDetailsEntity.getCode().equals(SUCCESS_CODE)) {
                                setTimeProductData(shopDetailsEntity);
                            } else if (shopDetailsEntity.getCode().equals(EMPTY_CODE)) {
                                showToast(ShopTimeScrollDetailsActivity.this, R.string.shopOverdue);
                            } else {
                                showToast(ShopTimeScrollDetailsActivity.this, shopDetailsEntity.getMsg());
                            }
                        }
                        NetLoadUtils.getQyInstance().showLoadSir(loadService, shopProperty, shopDetailsEntity);
                    }

                    @Override
                    public void netClose() {
                        smart_communal_refresh.finishRefresh();
                        showToast(ShopTimeScrollDetailsActivity.this, R.string.unConnectedNetwork);
                        NetLoadUtils.getQyInstance().showLoadSir(loadService, shopProperty, shopDetailsEntity);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        smart_communal_refresh.finishRefresh();
                        showToast(ShopTimeScrollDetailsActivity.this, R.string.connectedFaile);
                        NetLoadUtils.getQyInstance().showLoadSir(loadService, shopProperty, shopDetailsEntity);
                    }
                });
    }

    private void cancelWarm(int productId) {
        String url = Url.BASE_URL + Url.CANCEL_MINE_WARM;
        Map<String, Object> params = new HashMap<>();
        params.put("m_obj", productId);
        params.put("m_uid", userId);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                loadHud.dismiss();
                Gson gson = new Gson();
                RequestStatus status = gson.fromJson(result, RequestStatus.class);
                if (status != null) {
                    if (status.getCode().equals(SUCCESS_CODE)) {
                        getProductData();
                    } else {
                        showToast(ShopTimeScrollDetailsActivity.this, status.getMsg());
                    }
                    tvTimeProductDetailsWarm.setEnabled(true);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                tvTimeProductDetailsWarm.setEnabled(true);
                loadHud.dismiss();
                super.onError(ex, isOnCallback);
            }
        });
    }

    private void setWarm(int productId) {
        String url = Url.BASE_URL + Url.ADD_MINE_WARM;
        Map<String, Object> params = new HashMap<>();
        params.put("m_obj", productId);
        params.put("m_uid", userId);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                loadHud.dismiss();
                Gson gson = new Gson();
                RequestStatus status = gson.fromJson(result, RequestStatus.class);
                if (status != null) {
                    if (status.getCode().equals(SUCCESS_CODE)) {
                        getProductData();
                    } else {
                        showToast(ShopTimeScrollDetailsActivity.this, status.getMsg());
                    }
                    tvTimeProductDetailsWarm.setEnabled(true);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadHud.dismiss();
                tvTimeProductDetailsWarm.setEnabled(true);
                super.onError(ex, isOnCallback);
            }
        });
    }

    public void setTimeProductData(final ShopDetailsEntity detailsEntity) {
        shopProperty = detailsEntity.getShopPropertyBean();
        Properties prop = new Properties();
        prop.setProperty("proName", getStrings(shopProperty.getName()));
        prop.setProperty("proId", String.valueOf(shopProperty.getId()));
        StatService.trackCustomKVEvent(this, "timeProLook", prop);
        imagesVideoList.clear();
        String[] images = shopProperty.getImages().split(",");
        CommunalADActivityBean communalADActivityBean;
        if (images.length != 0) {
            List<String> imageList = Arrays.asList(images);
            for (int i = 0; i < imageList.size(); i++) {
                communalADActivityBean = new CommunalADActivityBean();
                if (i == 0) {
                    communalADActivityBean.setPicUrl(getWaterMarkImgUrl(imageList.get(i), shopProperty.getWaterRemark()));
                } else {
                    communalADActivityBean.setPicUrl(imageList.get(i));
                }
                imagesVideoList.add(communalADActivityBean);
            }
        } else {
            communalADActivityBean = new CommunalADActivityBean();
            communalADActivityBean.setPicUrl(getStrings(shopProperty.getPicUrl()));
            imagesVideoList.add(communalADActivityBean);
        }
//         轮播图
        if (cbViewHolderCreator == null) {
            cbViewHolderCreator = new CBViewHolderCreator() {
                @Override
                public Holder createHolder(View itemView) {
                    return new CommunalAdHolderView(itemView, ShopTimeScrollDetailsActivity.this, false);
                }

                @Override
                public int getLayoutId() {
                    return R.layout.layout_ad_image_video;
                }
            };
        }
        bannerShopTimeDetails.setPages(ShopTimeScrollDetailsActivity.this, cbViewHolderCreator, imagesVideoList).setCanLoop(true)
                .setPageIndicator(new int[]{R.drawable.unselected_radius, R.drawable.selected_radius});
//        商品名字
        itemBodyList.clear();
        tvShopTimeProductName.setText(getStrings(shopProperty.getName()));
//        商品价格区间
        if (!TextUtils.isEmpty(shopProperty.getPrice()) && !TextUtils.isEmpty(shopProperty.getMaxPrice())) {
            if (getFloatNumber(shopProperty.getPrice()) < getFloatNumber(shopProperty.getMaxPrice())) {
                tvShopTimeProductPrice.setText(getStringsChNPrice(this, shopProperty.getPrice() + "-" + shopProperty.getMaxPrice()));
            } else {
                tvShopTimeProductPrice.setText(getStringsChNPrice(this, shopProperty.getPrice()));
            }
        } else {
            if (!TextUtils.isEmpty(shopProperty.getPrice())) {
                tvShopTimeProductPrice.setText(getStringsChNPrice(this, shopProperty.getPrice()));
            } else {
                tvShopTimeProductPrice.setVisibility(GONE);
            }
        }
//           市场价
        tvShopTimeProductMkPrice.setText(("非团购价：￥" + shopProperty.getMarketPrice()));
        if (shopProperty.getSkimEnable() == 1) {
            tvTimeProductDetailsBuyIt.setEnabled(true);
            tvTimeProductDetailsBuyIt.setText("提前预览");
        } else {
            tvTimeProductDetailsBuyIt.setEnabled(false);
            tvTimeProductDetailsBuyIt.setText("不可预览");
        }
        if (shopProperty.isRemind()) {
            tvTimeProductDetailsWarm.setText("取消提醒");
        } else {
            tvTimeProductDetailsWarm.setText("开团提醒");
        }
        if (isEndOrStartTime(shopDetailsEntity.getCurrentTime(), shopProperty.getStartTime())) {
//            参与人数
            if(!TextUtils.isEmpty(shopProperty.getFlashBuyClickCount())){
                tvProductDetailsJoinCount.setText(getStrings(shopProperty.getFlashBuyClickCount()));
                tvProductDetailsJoinCount.setVisibility(View.VISIBLE);
            }
//            底栏提示
            tvTimeProductDetailsWarm.setVisibility(View.GONE);
            if (isEndOrStartTime(shopDetailsEntity.getCurrentTime(),shopProperty.getEndTime())) {
                tvTimeProductDetailsBuyIt.setEnabled(false);
                tvTimeProductDetailsBuyIt.setText("已结束");
            } else if (shopProperty.getQuantity() == 0) {
                tvTimeProductDetailsBuyIt.setEnabled(false);
                tvTimeProductDetailsBuyIt.setText("已抢光");
            } else {
                tvTimeProductDetailsBuyIt.setEnabled(true);
                tvTimeProductDetailsBuyIt.setText("立即购买");
            }
        } else {
//            参团数
            tvProductDetailsJoinCount.setVisibility(View.GONE);
//            底栏
            tvTimeProductDetailsWarm.setVisibility(View.VISIBLE);
            tvTimeProductDetailsWarm.setText(shopProperty.isRemind()?"取消提醒":"设置提醒");
        }
        setCountTime();
        if (!isEndOrStartTime(shopDetailsEntity.getCurrentTime(), shopProperty.getEndTime())) {
            getConstant();
            constantMethod.createSchedule();
            constantMethod.setRefreshTimeListener(new ConstantMethod.RefreshTimeListener() {
                @Override
                public void refreshTime() {
                    shopProperty.setAddSecond(shopProperty.getAddSecond() + 1);
                    setCountTime();
                }
            });
        } else {
            if (constantMethod != null) {
                constantMethod.stopSchedule();
            }
        }
//            详情内容
        if (shopProperty.getLuckyMoney() != null || shopProperty.getItemBody() != null || shopProperty.getFlashsaleInfo() != null) {
            itemBodyList.clear();
            if (shopProperty.getLuckyMoney() != null && shopProperty.getLuckyMoney().size() > 0) {
                List<LuckyMoneyBean> luckyMoney = shopProperty.getLuckyMoney();
                CommunalDetailObjectBean communalDetailObjectBean;
                for (int i = 0; i < luckyMoney.size(); i++) {
                    LuckyMoneyBean luckyMoneyBean = luckyMoney.get(i);
                    communalDetailObjectBean = new CommunalDetailObjectBean();
                    communalDetailObjectBean.setName(luckyMoneyBean.getName());
                    communalDetailObjectBean.setCouponUrl(luckyMoneyBean.getUrl());
                    communalDetailObjectBean.setItemType(1);
                    itemBodyList.add(communalDetailObjectBean);
                }
            }
            if (shopProperty.getItemBody() != null && shopProperty.getItemBody().size() > 0) {
                List<CommunalDetailBean> itemBody = shopProperty.getItemBody();
                if (itemBody != null) {
                    itemBodyList.addAll(ConstantMethod.getDetailsDataList(itemBody));
                }
            }
            contentOfficialAdapter.setNewData(itemBodyList);
        }
        if (itemBodyList.size() < 1) {
            CommunalDetailObjectBean communalDetailObjectBean = new CommunalDetailObjectBean();
            communalDetailObjectBean.setItemType(TYPE_EMPTY_OBJECT);
            itemBodyList.add(communalDetailObjectBean);
            contentOfficialAdapter.setNewData(itemBodyList);
        }
        thirdUrl = shopProperty.getThirdUrl();
        thirdId = shopProperty.getThirdId();
    }

    // 提前预览 立即购买
    @OnClick({R.id.tv_time_product_details_buy_it})
    void aHeadWatch(View view) {
        if (userId != 0) {
            skipNewTaoBao();
            if (shopProperty != null) {
                Properties prop = new Properties();
                prop.setProperty("proName", getStrings(shopProperty.getName()));
                prop.setProperty("proId", String.valueOf(shopProperty.getId()));
                StatService.trackCustomKVEvent(this, "timeProAhead", prop);
            }
        } else {
            getLoginStatus(ShopTimeScrollDetailsActivity.this);
        }
    }

    //    设置提醒 取消提醒
    @OnClick(R.id.tv_time_product_details_warm)
    void buySetWarm() {
        if (shopDetailsEntity.getShopPropertyBean() != null) {
            if (userId != 0) {
                isFirstRemind(shopDetailsEntity);
            } else {
                getLoginStatus(ShopTimeScrollDetailsActivity.this);
            }
        }
    }

    @OnClick(R.id.tv_time_product_details_service)
    void foundService() {
        skipServiceDataInfo(shopDetailsEntity);
    }

    private void isFirstRemind(final ShopDetailsEntity shopDetailsEntity) {
        String url = Url.BASE_URL + Url.TIME_SHOW_PRO_WARM;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus foreShowBean = gson.fromJson(result, RequestStatus.class);
                if (foreShowBean != null) {
                    if (foreShowBean.getCode().equals("01")) {
                        if (foreShowBean.getResult().isHadRemind()) { //已设置过提醒
                            if (shopDetailsEntity != null) {
                                loadHud.show();
                                if (shopDetailsEntity.getShopPropertyBean().isRemind()) {
//            取消提醒
                                    cancelWarm(shopDetailsEntity.getShopPropertyBean().getId());
                                } else {
//            设置提醒
                                    setWarm(shopDetailsEntity.getShopPropertyBean().getId());
                                }
                            }
                        } else {
                            setDefaultWarm();
                        }
                    } else if (!foreShowBean.getCode().equals("02")) {
                        showToast(ShopTimeScrollDetailsActivity.this, foreShowBean.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(ShopTimeScrollDetailsActivity.this, R.string.unConnectedNetwork);
            }
        });
    }

    private void setDefaultWarm() {
//        设置提醒
        View indentPopWindow = LayoutInflater.from(ShopTimeScrollDetailsActivity.this).inflate(R.layout.layout_first_time_product_warm, smart_communal_refresh, false);
        AutoUtils.autoSize(indentPopWindow);
        PopupWindowView popupWindowView = new PopupWindowView();
        ButterKnife.bind(popupWindowView, indentPopWindow);
        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(ShopTimeScrollDetailsActivity.this)
                .setView(indentPopWindow)
                .enableBackgroundDark(true) //弹出popWindow时，背景是否变暗
                .setBgDarkAlpha(0.7f) // 控制亮度
                .create().showAtLocation(smart_communal_refresh, Gravity.CENTER, 0, 0);
        popupWindowView.rp_time_pro_warm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                String numberWarm = radioButton.getText().toString().trim();
                Message message = new Message();
                message.arg1 = 1;
                message.obj = numberWarm;
                handler.sendMessageDelayed(message, 618);
            }
        });
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.arg1 == 1) {
                String number = (String) msg.obj;
                mCustomPopWindow.dissmiss();
                setWarmTime(getNumber(number));
            }
            return false;
        }
    });

    private void setWarmTime(String number) {
        String url = Url.BASE_URL + Url.TIME_WARM_PRO;
        Map<String, Object> params = new HashMap<>();
        params.put("m_uid", userId);
        params.put("longtime", number);
        XUtil.Post(url, params, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                RequestStatus requestStatus = gson.fromJson(result, RequestStatus.class);
                if (requestStatus != null && requestStatus.getCode().equals("01")) {
                    showToast(ShopTimeScrollDetailsActivity.this, "已设置产品提醒时间，提前" + requestStatus.getLongtime() + "分钟");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }
        });
    }

    //    七鱼客服
    private void skipServiceDataInfo(ShopDetailsEntity shopDetailsEntity) {
        QyProductIndentInfo qyProductIndentInfo = null;
        String sourceTitle = "";
        String sourceUrl = "";
        if (shopDetailsEntity != null) {
            ShopPropertyBean shopPropertyBean = shopDetailsEntity.getShopPropertyBean();
            qyProductIndentInfo = new QyProductIndentInfo();
            sourceUrl = sharePageUrl + shopPropertyBean.getId();
            sourceTitle = "限时特惠详情：" + shopPropertyBean.getName();
            qyProductIndentInfo.setUrl(sourceUrl);
            qyProductIndentInfo.setTitle(getStrings(shopPropertyBean.getName()));
            qyProductIndentInfo.setPicUrl(getStrings(shopPropertyBean.getPicUrl()));
            qyProductIndentInfo.setNote("￥" + getStrings(shopPropertyBean.getPrice()));
        }
        QyServiceUtils.getQyInstance().openQyServiceChat(this, sourceTitle, sourceUrl, qyProductIndentInfo);
    }

    public void setCountTime() {
        //格式化结束时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date startTime;
        Date endTime;
        try {
            //格式化结束时间
            Date dateCurrent;
            if (!TextUtils.isEmpty(shopDetailsEntity.getCurrentTime())) {
                dateCurrent = formatter.parse(shopDetailsEntity.getCurrentTime());
            } else {
                dateCurrent = new Date();
            }
            startTime = formatter.parse(shopProperty.getStartTime());
            if (dateCurrent.getTime() < startTime.getTime()) {
                long timeMillis = startTime.getTime() - dateCurrent.getTime() - shopProperty.getAddSecond() * 1000;
                ctPromotionProductTime.updateShow(timeMillis);
                tvPromotionProductTimeStatus.setText("距开团");
            } else {
                //格式化开始时间
                endTime = formatter.parse(shopProperty.getEndTime());
                long timeMillis = endTime.getTime() - dateCurrent.getTime() - shopProperty.getAddSecond() * 1000;
                ctPromotionProductTime.updateShow(timeMillis);
                tvPromotionProductTimeStatus.setText("距结束");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (!isEndOrStartTime(shopDetailsEntity.getCurrentTime(), shopProperty.getEndTime())) {
            ctPromotionProductTime.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                @Override
                public void onEnd(CountdownView cv) {
                    cv.setOnCountdownEndListener(null);
                    loadData();
                }
            });
        } else {
            ctPromotionProductTime.setOnCountdownEndListener(null);
        }
    }

    private void skipNewTaoBao() {
        if (thirdId != null || thirdUrl != null) {
            setClickProductTotal();
            if (thirdId != null) {
                AlibcLogin alibcLogin = AlibcLogin.getInstance();
                alibcLogin.showLogin(new AlibcLoginCallback() {
                    @Override
                    public void onSuccess(int i) {
                        skipNewShopDetails();
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        showToast(ShopTimeScrollDetailsActivity.this, "登录失败 ");
                    }
                });
            } else {
                skipNewShopDetails();
            }
        } else {
            showToast(this, "地址缺失");
        }
    }

    /**
     * 设置限时特惠购买统计
     */
    private void setClickProductTotal() {
        if (NetWorkUtils.checkNet(ShopTimeScrollDetailsActivity.this)) {
            String url = Url.BASE_URL + Url.TIME_PRODUCT_CLICK_TOTAL;
            Map<String, Object> params = new HashMap<>();
            params.put("id", productId);
            XUtil.Post(url, params, new MyCallBack<String>() {
            });
        }
    }

    private void skipNewShopDetails() {
//                    跳转淘宝商品详情
        if (!TextUtils.isEmpty(thirdId)) {
            /**
             * 打开电商组件, 使用默认的webview打开
             *
             * @param activity             必填
             * @param tradePage            页面类型,必填，不可为null，详情见下面tradePage类型介绍
             * @param showParams           show参数
             * @param taokeParams          淘客参数
             * @param trackParam           yhhpass参数
             * @param tradeProcessCallback 交易流程的回调，必填，不允许为null；
             * @return 0标识跳转到手淘打开了, 1标识用h5打开,-1标识出错
             */
            //提供给三方传递配置参数
            Map<String, String> exParams = new HashMap<>();
            exParams.put(AlibcConstants.ISV_CODE, "appisvcode");
            //设置页面打开方式
            AlibcShowParams showParams = new AlibcShowParams(OpenType.Native, false);
            //实例化商品详情 itemID打开page
            AlibcBasePage ordersPage = null;
            if (thirdId != null) {
                ordersPage = new AlibcDetailPage(thirdId);
            } else {
                ordersPage = new AlibcPage(thirdUrl);
            }
//            AlibcTaokeParams taokeParams = new AlibcTaokeParams();
//            taokeParams.pid = "mm_113346569_43964046_400008826";

            AlibcTrade.show(ShopTimeScrollDetailsActivity.this, ordersPage, showParams, null, exParams, new AlibcTradeCallback() {
                @Override
                public void onTradeSuccess(AlibcTradeResult alibcTradeResult) {
                    //打开电商组件，用户操作中成功信息回调。tradeResult：成功信息（结果类型：加购，支付；支付结果）
                    showToast(ShopTimeScrollDetailsActivity.this, "获取详情成功");
                }

                @Override
                public void onFailure(int code, String msg) {
                    //打开电商组件，用户操作中错误信息回调。code：错误码；msg：错误信息
                    showToast(ShopTimeScrollDetailsActivity.this, msg);
                }
            });
        } else if (!TextUtils.isEmpty(thirdUrl)) {
//                     网页地址
            Intent intent = new Intent();
            intent.setClass(ShopTimeScrollDetailsActivity.this, DoMoLifeCommunalActivity.class);
            intent.putExtra("loadUrl", thirdUrl);
            startActivity(intent);
        }
    }

    public void skipAliBCWebView(final String url) {
        if (!TextUtils.isEmpty(url)) {
            if (userId != 0) {
                skipNewTaoBao(url);
            } else {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                getLoginStatus(ShopTimeScrollDetailsActivity.this);
            }
        } else {
            showToast(ShopTimeScrollDetailsActivity.this, "地址缺失");
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
                showToast(ShopTimeScrollDetailsActivity.this, "登录失败 ");
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
        AlibcTrade.show(ShopTimeScrollDetailsActivity.this, ordersPage, showParams, null, exParams, new AlibcTradeCallback() {
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


    class PopupWindowView {
        @BindView(R.id.rp_time_pro_warm)
        RadioGroup rp_time_pro_warm;
    }

    private String getNumber(String str) {
        String regex = "\\d*";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        while (m.find()) {
            if (!"".equals(m.group()))
                return m.group();
        }
        return "3";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (constantMethod != null) {
            constantMethod.stopSchedule();
            constantMethod.releaseHandlers();
        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    //    页面分享
    @OnClick(R.id.tv_header_shared)
    void sendShare(View view) {
        if (shopDetailsEntity != null) {
            new UMShareAction(ShopTimeScrollDetailsActivity.this
                    , shopDetailsEntity.getShopPropertyBean().getPicUrl()
                    , "我在多么生活看中了" + shopDetailsEntity.getShopPropertyBean().getName()
                    , getStrings(shopDetailsEntity.getShopPropertyBean().getSubtitle())
                    , sharePageUrl + shopDetailsEntity.getShopPropertyBean().getId());
        }
    }

    private void getConstant() {
        if (constantMethod == null) {
            constantMethod = new ConstantMethod();
        }
    }
}

