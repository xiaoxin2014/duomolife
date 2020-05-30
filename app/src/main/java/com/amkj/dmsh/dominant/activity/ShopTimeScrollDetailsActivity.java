package com.amkj.dmsh.dominant.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ali.auth.third.ui.context.CallbackContext;
import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.base.BaseFragment;
import com.amkj.dmsh.base.EventMessage;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.RequestStatus;
import com.amkj.dmsh.bean.TabEntity;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.bean.PromotionProductDetailEntity;
import com.amkj.dmsh.dominant.bean.PromotionProductDetailEntity.PromotionProductDetailBean;
import com.amkj.dmsh.dominant.bean.PromotionProductDetailEntity.PromotionProductDetailBean.LuckyMoneyBean;
import com.amkj.dmsh.dominant.fragment.TopRecommendAtTimeEndGroupFragment;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.qyservice.QyProductIndentInfo;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.utils.CountDownTimer;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
import com.amkj.dmsh.utils.webformatdata.ShareDataBean;
import com.amkj.dmsh.views.CustomPopWindow;
import com.amkj.dmsh.views.flycoTablayout.CommonTabLayout;
import com.amkj.dmsh.views.flycoTablayout.listener.CustomTabEntity;
import com.amkj.dmsh.views.flycoTablayout.listener.OnTabSelectListener;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.melnykov.fab.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.tencent.stat.StatService;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.IdRes;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.iwgang.countdownview.CountdownView;
import cn.iwgang.countdownview.DynamicConfig;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getFloatNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;
import static com.amkj.dmsh.constant.ConstantMethod.showImageActivity;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.showToastRequestMsg;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.Url.ADD_MINE_WARM;
import static com.amkj.dmsh.constant.Url.CANCEL_MINE_WARM;
import static com.amkj.dmsh.constant.Url.H_TIME_GOODS_DETAILS;
import static com.amkj.dmsh.constant.Url.TIME_PRODUCT_CLICK_TOTAL;
import static com.amkj.dmsh.constant.Url.TIME_SHOW_PRO_WARM;
import static com.amkj.dmsh.constant.Url.TIME_WARM_PRO;
import static com.amkj.dmsh.dao.BaiChuanDao.skipAliBC;
import static com.amkj.dmsh.find.activity.ImagePagerActivity.IMAGE_DEF;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_EMPTY_OBJECT;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_PROMOTION_TITLE;
import static com.amkj.dmsh.utils.TimeUtils.getTimeDifference;
import static com.amkj.dmsh.utils.TimeUtils.isEndOrStartTime;
import static com.amkj.dmsh.utils.glide.GlideImageLoaderUtil.getWaterMarkImgUrl;


public class ShopTimeScrollDetailsActivity extends BaseActivity {
    @BindView(R.id.smart_time_product_details)
    SmartRefreshLayout smart_communal_refresh;
    @BindView(R.id.nsv_time_detail)
    NestedScrollView nsv_time_detail;
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
    @BindView(R.id.ll_time_details_bottom)
    LinearLayout ll_time_details_bottom;
    @BindView(R.id.download_btn_communal)
    FloatingActionButton download_btn_communal;
    //    轮播图片视频
    private List<CommunalADActivityBean> imagesVideoList = new ArrayList<>();
    private List<CommunalDetailObjectBean> itemBodyList = new ArrayList<>();
    private CommunalDetailAdapter contentOfficialAdapter;
    //产品Id
    private String productId;
    private String isTaobao;
    private String thirdUrl;
    private String thirdId;
    private CustomPopWindow mCustomPopWindow;
    private String sharePageUrl = Url.BASE_SHARE_PAGE_TWO + "m/template/common/taoBaoGoods.html?id=";
    private CBViewHolderCreator cbViewHolderCreator;
    private FragmentManager fragmentManager;
    private String[] promotionProduct = {"Top团品推荐", "即将截团"};
    //tab集合
    private ArrayList<CustomTabEntity> customTabEntities = new ArrayList<>();
    private FragmentTransaction transaction;
    private Fragment lastFragment;
    private PromotionProductDetailEntity productDetailEntity;
    private PromotionProductDetailBean productDetailBean;
    private int screenHeight;
    private CountDownTimer mCountDownTimer;

    @Override
    protected int getContentView() {
        return R.layout.activity_time_goods_details_new;
    }

    @Override
    protected void initViews() {
        tvHeaderTitle.setText("详情");
        Intent intent = getIntent();
        productId = intent.getStringExtra("productId");
        isTaobao = intent.getStringExtra("isTaobao");
        communalRecyclerWrap.setLayoutManager(new LinearLayoutManager(ShopTimeScrollDetailsActivity.this));
        communalRecyclerWrap.setNestedScrollingEnabled(false);
        contentOfficialAdapter = new CommunalDetailAdapter(ShopTimeScrollDetailsActivity.this, itemBodyList);
        communalRecyclerWrap.setAdapter(contentOfficialAdapter);

        smart_communal_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData();
                EventBus.getDefault().post(new EventMessage("refreshData", "timeProduct"));
            }
        });
        contentOfficialAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ShareDataBean shareDataBean = null;
                if (view.getId() == R.id.tv_communal_share && productDetailBean != null) {
                    shareDataBean = new ShareDataBean(productDetailBean.getPicUrl()
                            , "我在多么生活看中了" + productDetailBean.getName()
                            , getStrings(productDetailBean.getSubtitle())
                            , sharePageUrl + productDetailBean.getId());

                }
                CommunalWebDetailUtils.getCommunalWebInstance()
                        .setWebDataClick(ShopTimeScrollDetailsActivity.this, shareDataBean, view, loadHud);
            }
        });
        DynamicConfig.Builder dynamic = new DynamicConfig.Builder();
        dynamic.setSuffixTextSize(AutoSizeUtils.mm2px(mAppContext, 26));
        dynamic.setTimeTextSize(AutoSizeUtils.mm2px(mAppContext, 26));
        dynamic.setSuffixGravity(Gravity.CENTER);
        DynamicConfig.BackgroundInfo backgroundInfo = new DynamicConfig.BackgroundInfo();
        backgroundInfo.setColor(getResources().getColor(R.color.text_pink_red))
                .setBorderRadius((float) AutoSizeUtils.mm2px(mAppContext, 8))
                .setBorderColor(getResources().getColor(R.color.text_pink_red))
                .setShowTimeBgBorder(true);
        dynamic.setBackgroundInfo(backgroundInfo);
        ctPromotionProductTime.dynamicShow(dynamic.build());
        fragmentManager = getSupportFragmentManager();
        for (int i = 0; i < promotionProduct.length; i++) {
            customTabEntities.add(new TabEntity(promotionProduct[i], 0, 0));
        }
        ctbPromotionProduct.setTabData(customTabEntities);
        ctbPromotionProduct.setTextSize(AutoSizeUtils.mm2px(mAppContext, 28));
        ctbPromotionProduct.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                switch (position) {
                    case 0:
                        changeProductPage("topRecommend");
                        break;
                    case 1:
                        changeProductPage("atTimeGroup");
                        break;
                }
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        changeProductPage("topRecommend");
        TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        screenHeight = app.getScreenHeight();
        nsv_time_detail.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int i, int newY, int i2, int oldY) {
                if (newY > screenHeight * 1.5) {
                    if (download_btn_communal.getVisibility() == GONE) {
                        download_btn_communal.setVisibility(VISIBLE);
                        download_btn_communal.show(false);
                    }
                    if (!download_btn_communal.isVisible()) {
                        download_btn_communal.show(false);
                    }
                } else {
                    if (download_btn_communal.isVisible()) {
                        download_btn_communal.hide(false);
                    }
                }
            }
        });
        download_btn_communal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nsv_time_detail.fling(0);
                nsv_time_detail.scrollTo(0, 0);
                download_btn_communal.hide(false);
            }
        });
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
        getProductDetailsData();
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    //商品详情内容
    private void getProductDetailsData() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", productId);
        if (userId > 0) {
            params.put("userId", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(ShopTimeScrollDetailsActivity.this, H_TIME_GOODS_DETAILS
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {
                        smart_communal_refresh.finishRefresh();

                        productDetailEntity = GsonUtils.fromJson(result, PromotionProductDetailEntity.class);
                        if (productDetailEntity != null) {
                            if (productDetailEntity.getCode().equals(SUCCESS_CODE)) {
                                setTimeProductData(productDetailEntity);
                            } else if (productDetailEntity.getCode().equals(EMPTY_CODE)) {
                                showToast(R.string.shopOverdue);
                            } else {
                                showToast(productDetailEntity.getMsg());
                            }
                        }
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, productDetailEntity, productDetailEntity);
                    }

                    @Override
                    public void onNotNetOrException() {
                        smart_communal_refresh.finishRefresh();
                        NetLoadUtils.getNetInstance().showLoadSir(loadService, productDetailEntity, productDetailEntity);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showToast(R.string.connectedFaile);
                    }
                });
    }

    private void cancelWarm(int productId) {
        Map<String, Object> params = new HashMap<>();
        params.put("m_obj", productId);
        params.put("m_uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, CANCEL_MINE_WARM, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                loadHud.dismiss();

                RequestStatus status = GsonUtils.fromJson(result, RequestStatus.class);
                if (status != null) {
                    if (status.getCode().equals(SUCCESS_CODE)) {
                        tvTimeProductDetailsWarm.setText("设置提醒");
                        productDetailBean.setRemind(false);
                        showToast("已取消提醒");
                    } else {
                        showToastRequestMsg(status);
                    }
                    tvTimeProductDetailsWarm.setEnabled(true);
                }
            }

            @Override
            public void onNotNetOrException() {
                tvTimeProductDetailsWarm.setEnabled(true);
                loadHud.dismiss();
            }
        });
    }

    private void setWarm(int productId) {
        Map<String, Object> params = new HashMap<>();
        params.put("m_obj", productId);
        params.put("m_uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, ADD_MINE_WARM, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                loadHud.dismiss();

                RequestStatus status = GsonUtils.fromJson(result, RequestStatus.class);
                if (status != null) {
                    if (status.getCode().equals(SUCCESS_CODE)) {
                        tvTimeProductDetailsWarm.setText("取消提醒");
                        showToast("已设置提醒");
                        productDetailBean.setRemind(true);
                    } else {
                        showToastRequestMsg(status);
                    }
                    tvTimeProductDetailsWarm.setEnabled(true);
                }
            }

            @Override
            public void onNotNetOrException() {
                loadHud.dismiss();
                tvTimeProductDetailsWarm.setEnabled(true);
            }
        });
    }

    private void changeProductPage(String tag) {
        Fragment fragmentByTag = fragmentManager.findFragmentByTag(tag);
        transaction = fragmentManager.beginTransaction();
        if (fragmentByTag != null && fragmentByTag.isAdded()) {
            if (lastFragment != null) {
                transaction.hide(lastFragment).commit();
            }
            transaction.show(fragmentByTag);
            lastFragment = fragmentByTag;
            fragmentByTag.onResume();
        } else {
            Map<String, String> tagParams = new HashMap<>();
            tagParams.put("promotionProductType", tag);
            Fragment fragment = BaseFragment.newInstance(TopRecommendAtTimeEndGroupFragment.class, tagParams, null);
            if (lastFragment != null) {
                if (fragment.isAdded()) {
                    transaction.hide(lastFragment).show(fragment).commit(); // 隐藏当前的fragment，显示下一个
                } else {
                    transaction.hide(lastFragment).add(R.id.fl_promotion_product_details_contain, fragment, tag).commit();
                }
            } else {
                transaction.add(R.id.fl_promotion_product_details_contain, fragment, tag).commit();
            }
            lastFragment = fragment;
        }
    }

    public void setTimeProductData(final PromotionProductDetailEntity detailsEntity) {
        productDetailBean = detailsEntity.getPromotionProductDetailBean();
        Properties prop = new Properties();
        prop.setProperty("proName", getStrings(productDetailBean.getName()));
        prop.setProperty("proId", String.valueOf(productDetailBean.getId()));
        StatService.trackCustomKVEvent(this, "timeProLook", prop);
        imagesVideoList.clear();
        String[] images = productDetailBean.getImages().split(",");
        CommunalADActivityBean communalADActivityBean;
        List<String> imageList = new ArrayList<>();
        if (images.length != 0) {
            imageList.addAll(Arrays.asList(images));
            for (int i = 0; i < imageList.size(); i++) {
                communalADActivityBean = new CommunalADActivityBean();
                if (i == 0) {
                    communalADActivityBean.setPicUrl(getWaterMarkImgUrl(imageList.get(i), productDetailBean.getWaterRemark()));
                } else {
                    communalADActivityBean.setPicUrl(imageList.get(i));
                }
                imagesVideoList.add(communalADActivityBean);
            }
        } else {
            communalADActivityBean = new CommunalADActivityBean();
            communalADActivityBean.setPicUrl(getStrings(productDetailBean.getPicUrl()));
            imageList.add(getStrings(productDetailBean.getPicUrl()));
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
        bannerShopTimeDetails.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (imageList.size() > 0 && position < imageList.size()) {
                    showImageActivity(ShopTimeScrollDetailsActivity.this, IMAGE_DEF,
                            position,
                            imageList);
                }
            }
        });
//        商品名字
        itemBodyList.clear();
        tvShopTimeProductName.setText(getStrings(productDetailBean.getName()));
//        商品价格区间
        if (!TextUtils.isEmpty(productDetailBean.getPrice()) && !TextUtils.isEmpty(productDetailBean.getMaxPrice())) {
            if (getFloatNumber(productDetailBean.getPrice()) < getFloatNumber(productDetailBean.getMaxPrice())) {
                tvShopTimeProductPrice.setText(getStringsChNPrice(this, productDetailBean.getPrice() + "-" + productDetailBean.getMaxPrice()));
            } else {
                tvShopTimeProductPrice.setText(getStringsChNPrice(this, productDetailBean.getPrice()));
            }
        } else {
            if (!TextUtils.isEmpty(productDetailBean.getPrice())) {
                tvShopTimeProductPrice.setText(getStringsChNPrice(this, productDetailBean.getPrice()));
            } else {
                tvShopTimeProductPrice.setVisibility(GONE);
            }
        }
//           市场价
        tvShopTimeProductMkPrice.setText(("非团购价：¥" + productDetailBean.getMarketPrice()));
        if (productDetailBean.getSkimEnable() == 1) {
            tvTimeProductDetailsBuyIt.setEnabled(true);
            tvTimeProductDetailsBuyIt.setText("提前预览");
        } else {
            tvTimeProductDetailsBuyIt.setEnabled(false);
            tvTimeProductDetailsBuyIt.setText("不可预览");
        }
        if (productDetailBean.isRemind()) {
            tvTimeProductDetailsWarm.setText("取消提醒");
        } else {
            tvTimeProductDetailsWarm.setText("开团提醒");
        }
        if (isEndOrStartTime(productDetailEntity.getCurrentTime(), productDetailBean.getStartTime())) {
//            参与人数
            if (!TextUtils.isEmpty(productDetailBean.getFlashBuyClickCount())) {
                tvProductDetailsJoinCount.setText(String.format(getResources().getString(R.string.time_join_group_count), getStrings(productDetailBean.getFlashBuyClickCount())));
                tvProductDetailsJoinCount.setVisibility(View.VISIBLE);
            }
//            底栏提示
            tvTimeProductDetailsWarm.setVisibility(View.GONE);
            if (isEndOrStartTime(productDetailEntity.getCurrentTime(), productDetailBean.getEndTime())) {
                tvTimeProductDetailsBuyIt.setEnabled(false);
                tvTimeProductDetailsBuyIt.setText("已结束");
            } else if (productDetailBean.getQuantity() == 0) {
                tvTimeProductDetailsBuyIt.setEnabled(false);
                tvTimeProductDetailsBuyIt.setText("已抢光");
            } else {
                tvTimeProductDetailsBuyIt.setEnabled(true);
                tvTimeProductDetailsBuyIt.setText("我要跟团");
            }
        } else {
//            参团数
            tvProductDetailsJoinCount.setVisibility(View.GONE);
//            底栏
            tvTimeProductDetailsWarm.setVisibility(View.VISIBLE);
            tvTimeProductDetailsWarm.setText(productDetailBean.isRemind() ? "取消提醒" : "设置提醒");
        }

        //淘你所爱商品只要上架了就可以参团，不用管开始时间和结束时间
        if ("1".equals(isTaobao)) {
            tvTimeProductDetailsBuyIt.setEnabled(true);
            tvTimeProductDetailsBuyIt.setText("我要跟团");
        }

        String currentTime = productDetailEntity.getCurrentTime();
        String startTime = productDetailBean.getStartTime();
        String endTime = productDetailBean.getEndTime();
        //活动未开始
        if (!isEndOrStartTime(currentTime, startTime)) {
            tvPromotionProductTimeStatus.setText("距开团");
            setCountTime(getTimeDifference(currentTime, startTime));
        } else if (!isEndOrStartTime(currentTime, endTime)) {
            //已开始，未结束
            tvPromotionProductTimeStatus.setText("距结束");
            setCountTime(getTimeDifference(currentTime, endTime));
        } else {
            //已结束
            relPromotionProductTime.setVisibility(GONE);
        }
//            详情内容
        if (productDetailBean.getLuckyMoneyList() != null || productDetailBean.getItemBodyList() != null
                || productDetailBean.getFlashsaleInfoList() != null) {
            itemBodyList.clear();
            if (productDetailBean.getItemBodyList() != null
                    && productDetailBean.getItemBodyList().size() > 0) {
                CommunalDetailObjectBean communalDetailObjectBean = new CommunalDetailObjectBean();
                communalDetailObjectBean.setItemType(TYPE_PROMOTION_TITLE);
                communalDetailObjectBean.setName("团长推荐");
                itemBodyList.add(communalDetailObjectBean);
                itemBodyList.addAll(CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(productDetailBean.getItemBodyList()));
            }
            if (productDetailBean.getLuckyMoneyList() != null
                    && productDetailBean.getLuckyMoneyList().size() > 0) {
                List<LuckyMoneyBean> luckyMoney = productDetailBean.getLuckyMoneyList();
                CommunalDetailObjectBean communalDetailObjectBean = new CommunalDetailObjectBean();
                communalDetailObjectBean.setItemType(TYPE_PROMOTION_TITLE);
                communalDetailObjectBean.setName("领取优惠券");
                itemBodyList.add(communalDetailObjectBean);
                for (int i = 0; i < luckyMoney.size(); i++) {
                    LuckyMoneyBean luckyMoneyBean = luckyMoney.get(i);
                    communalDetailObjectBean = new CommunalDetailObjectBean();
                    communalDetailObjectBean.setName(luckyMoneyBean.getName());
                    communalDetailObjectBean.setCouponUrl(luckyMoneyBean.getUrl());
                    communalDetailObjectBean.setItemType(1);
                    if (i == luckyMoney.size() - 1) {
                        communalDetailObjectBean.setLastTbCoupon(true);
                    }
                    itemBodyList.add(communalDetailObjectBean);
                }
            }

            if (productDetailBean.getFlashsaleInfoList() != null
                    && productDetailBean.getFlashsaleInfoList().size() > 0) {
                CommunalDetailObjectBean communalDetailObjectBean = new CommunalDetailObjectBean();
                communalDetailObjectBean.setItemType(TYPE_PROMOTION_TITLE);
                communalDetailObjectBean.setName("参团须知");
                itemBodyList.add(communalDetailObjectBean);
                itemBodyList.addAll(CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(productDetailBean.getFlashsaleInfoList()));
            }
            contentOfficialAdapter.notifyDataSetChanged();
        }
        if (itemBodyList.size() < 1) {
            CommunalDetailObjectBean communalDetailObjectBean = new CommunalDetailObjectBean();
            communalDetailObjectBean.setItemType(TYPE_EMPTY_OBJECT);
            itemBodyList.add(communalDetailObjectBean);
            contentOfficialAdapter.setNewData(itemBodyList);
        }
        thirdUrl = productDetailBean.getThirdUrl();
        thirdId = productDetailBean.getThirdId();
    }

    // 提前预览 立即购买
    @OnClick({R.id.tv_time_product_details_buy_it})
    void aHeadWatch(View view) {
        if (userId != 0) {
            setClickProductTotal();
            if (productDetailBean != null) {
                skipAliBC(this, thirdUrl, thirdId, productDetailBean.getTaoBao() == 1);
                Properties prop = new Properties();
                prop.setProperty("proName", getStrings(productDetailBean.getName()));
                prop.setProperty("proId", String.valueOf(productDetailBean.getId()));
                StatService.trackCustomKVEvent(this, "timeProAhead", prop);
            }
        } else {
            getLoginStatus(ShopTimeScrollDetailsActivity.this);
        }
    }

    //    设置提醒 取消提醒
    @OnClick(R.id.tv_time_product_details_warm)
    void buySetWarm() {
        if (productDetailBean != null) {
            if (userId != 0) {
                isFirstRemind(productDetailBean);
            } else {
                getLoginStatus(ShopTimeScrollDetailsActivity.this);
            }
        }
    }

    @OnClick(R.id.tv_time_product_details_service)
    void foundService() {
        skipServiceDataInfo(productDetailBean);
    }

    private void isFirstRemind(PromotionProductDetailBean productDetailBean) {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, TIME_SHOW_PRO_WARM, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                RequestStatus foreShowBean = GsonUtils.fromJson(result, RequestStatus.class);
                if (foreShowBean != null) {
                    if (foreShowBean.getCode().equals(SUCCESS_CODE)) {
                        if (foreShowBean.getResult().isHadRemind()) { //已设置过提醒
                            if (productDetailBean != null) {
                                loadHud.show();
                                if (productDetailBean.isRemind()) {
//            取消提醒
                                    cancelWarm(productDetailBean.getId());
                                } else {
//            设置提醒
                                    setWarm(productDetailBean.getId());
                                }
                            }
                        } else {
                            setDefaultWarm();
                        }
                    } else if (!foreShowBean.getCode().equals(EMPTY_CODE)) {
                        showToastRequestMsg(foreShowBean);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(R.string.do_failed);
            }
        });
    }

    private void setDefaultWarm() {
//        设置提醒
        View indentPopWindow = LayoutInflater.from(ShopTimeScrollDetailsActivity.this).inflate(R.layout.layout_first_time_product_warm, smart_communal_refresh, false);
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
        Map<String, Object> params = new HashMap<>();
        params.put("m_uid", userId);
        params.put("longtime", number);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, TIME_WARM_PRO, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                RequestStatus requestStatus = GsonUtils.fromJson(result, RequestStatus.class);
                if (requestStatus != null && requestStatus.getCode().equals(SUCCESS_CODE)) {
                    showToast("已设置产品提醒时间，提前" + requestStatus.getLongtime() + "分钟");
                }
            }
        });
    }

    //    七鱼客服
    private void skipServiceDataInfo(PromotionProductDetailBean productDetailBean) {
        QyProductIndentInfo qyProductIndentInfo = null;
        String sourceTitle = "";
        String sourceUrl = "";
        if (productDetailBean != null) {
            qyProductIndentInfo = new QyProductIndentInfo();
            sourceUrl = sharePageUrl + productDetailBean.getId();
            sourceTitle = "限时特惠详情：" + productDetailBean.getName();
            qyProductIndentInfo.setUrl(sourceUrl);
            qyProductIndentInfo.setTitle(getStrings(productDetailBean.getName()));
            qyProductIndentInfo.setPicUrl(getStrings(productDetailBean.getPicUrl()));
            qyProductIndentInfo.setNote("¥" + getStrings(productDetailBean.getPrice()));
        }
        QyServiceUtils.getQyInstance().openQyServiceChat(this, sourceTitle, sourceUrl, qyProductIndentInfo);
    }

    public void setCountTime(long millisInFuture) {
        if (mCountDownTimer == null) {
            mCountDownTimer = new CountDownTimer(getActivity()) {
                @Override
                public void onTick(long millisUntilFinished) {
                    ctPromotionProductTime.updateShow(millisUntilFinished);
                }

                @Override
                public void onFinish() {
                    loadData();
                }
            };
        }
        mCountDownTimer.setMillisInFuture(millisInFuture);
        mCountDownTimer.start();
    }

    /**
     * 设置限时特惠购买统计
     */
    private void setClickProductTotal() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", productId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, TIME_PRODUCT_CLICK_TOTAL, params, null);
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

    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    //    页面分享
    @OnClick(R.id.tv_header_shared)
    void sendShare(View view) {
        if (productDetailBean != null) {
            new UMShareAction(ShopTimeScrollDetailsActivity.this
                    , productDetailBean.getPicUrl()
                    , getStringsFormat(this, R.string.group_price, productDetailBean.getPrice()) + productDetailBean.getName()
                    , getStrings(productDetailBean.getSubtitle())
                    , sharePageUrl + productDetailBean.getId(), productDetailBean.getId());
        }
    }
}

