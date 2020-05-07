package com.amkj.dmsh.shopdetails.integration;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.text.emoji.widget.EmojiTextView;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.BaseActivity;
import com.amkj.dmsh.bean.ImageBean;
import com.amkj.dmsh.constant.CommunalAdHolderView;
import com.amkj.dmsh.constant.CommunalDetailBean;
import com.amkj.dmsh.constant.TabEntity;
import com.amkj.dmsh.constant.UMShareAction;
import com.amkj.dmsh.constant.Url;
import com.amkj.dmsh.dominant.activity.DirectProductEvaluationActivity;
import com.amkj.dmsh.find.activity.ImagePagerActivity;
import com.amkj.dmsh.find.adapter.FindImageListAdapter;
import com.amkj.dmsh.homepage.adapter.CommunalDetailAdapter;
import com.amkj.dmsh.homepage.bean.CommunalADActivityEntity.CommunalADActivityBean;
import com.amkj.dmsh.homepage.bean.CommunalRuleEntity;
import com.amkj.dmsh.homepage.bean.InvitationDetailEntity.InvitationDetailBean.PictureBean;
import com.amkj.dmsh.network.NetLoadListenerHelper;
import com.amkj.dmsh.network.NetLoadUtils;
import com.amkj.dmsh.qyservice.QyProductIndentInfo;
import com.amkj.dmsh.qyservice.QyServiceUtils;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.shopdetails.bean.EditGoodsSkuEntity.EditGoodsSkuBean;
import com.amkj.dmsh.shopdetails.bean.GoodsCommentEntity;
import com.amkj.dmsh.shopdetails.bean.GoodsCommentEntity.GoodsCommentBean;
import com.amkj.dmsh.shopdetails.bean.ShopCarGoodsSku;
import com.amkj.dmsh.shopdetails.bean.ShopDetailsEntity.ShopPropertyBean.TagsBean;
import com.amkj.dmsh.shopdetails.bean.SkuSaleBean;
import com.amkj.dmsh.shopdetails.integration.bean.IntegralProductInfoEntity;
import com.amkj.dmsh.shopdetails.integration.bean.IntegralProductInfoEntity.IntegralProductInfoBean;
import com.amkj.dmsh.shopdetails.integration.initview.IntegralPopWindows;
import com.amkj.dmsh.user.activity.UserPagerActivity;
import com.amkj.dmsh.user.bean.UserPagerInfoEntity;
import com.amkj.dmsh.utils.ProductLabelCreateUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.gson.GsonUtils;
import com.amkj.dmsh.utils.webformatdata.CommunalWebDetailUtils;
import com.amkj.dmsh.views.RectAddAndSubViewCommunal;
import com.amkj.dmsh.views.bottomdialog.SimpleSkuDialog;
import com.amkj.dmsh.views.flycoTablayout.CommonTabLayout;
import com.amkj.dmsh.views.flycoTablayout.listener.CustomTabEntity;
import com.amkj.dmsh.views.flycoTablayout.listener.OnTabSelectListener;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.FlexboxLayout;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import me.jessyan.autosize.utils.AutoSizeUtils;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getFloatNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getLoginStatus;
import static com.amkj.dmsh.constant.ConstantMethod.getNumCount;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantMethod.userId;
import static com.amkj.dmsh.constant.ConstantVariable.EMPTY_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.REGEX_NUM;
import static com.amkj.dmsh.constant.ConstantVariable.SUCCESS_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.TYPE_3;
import static com.amkj.dmsh.find.activity.ImagePagerActivity.IMAGE_DEF;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_PRODUCT_TITLE;
import static com.amkj.dmsh.utils.glide.GlideImageLoaderUtil.getWaterMarkImgUrl;


public class IntegralScrollDetailsActivity extends BaseActivity {
    @BindView(R.id.smart_integral_details)
    SmartRefreshLayout smart_integral_details;
    @BindView(R.id.ll_integral_detail_product)
    LinearLayout ll_integral_detail_product;
    @BindView(R.id.scroll_integral_product_header)
    NestedScrollView scroll_integral_product_header;
    //    轮播图
    @BindView(R.id.banner_integration_details)
    ConvenientBanner banner_integration_details;
    //积分产品名字
    @BindView(R.id.tv_integral_introduce_pro_n)
    TextView tv_integration_details_introduce_productName;
    //    积分产品价格
    @BindView(R.id.tv_integral_introduce_price)
    TextView tv_integration_details_price;
    //    市场参考价
    @BindView(R.id.tv_integral_introduce_mk)
    TextView tv_integral_introduce_mk;
    //    我要兑换
    @BindView(R.id.tv_integral_product_exchange)
    TextView tv_integral_product_exchange;
    /**
     * 商品评论
     */
    @BindView(R.id.ll_product_comment)
    LinearLayout ll_product_comment;
    //    评论头部
    @BindView(R.id.tv_shop_comment_count)
    TextView tv_shop_comment_count;
    @BindView(R.id.img_direct_avatar)
    CircleImageView imgDirectAvatar;
    @BindView(R.id.tv_eva_user_name)
    EmojiTextView tvEvaUserName;
    @BindView(R.id.ratingBar_direct_count)
    MaterialRatingBar ratingBarDirectCount;
    @BindView(R.id.tv_eva_count)
    TextView tvEvaCount;
    @BindView(R.id.tv_direct_evaluation)
    EmojiTextView tvDirectEvaluation;
    @BindView(R.id.rv_pro_eva)
    RecyclerView rvProEva;
    @BindView(R.id.emo_direct_eva_reply)
    EmojiTextView emoDirectEvaReply;
    @BindView(R.id.ll_eva_comment_reply)
    LinearLayout llEvaCommentReply;
    @BindView(R.id.flex_communal_tag)
    FlexboxLayout flex_communal_tag;


    @BindView(R.id.communal_recycler_wrap)
    RecyclerView communal_recycler_wrap;

    //    头部
    @BindView(R.id.tv_life_back)
    TextView tvLifeBack;
    @BindView(R.id.ctb_qt_pro_details)
    CommonTabLayout ctbQtProDetails;
    @BindView(R.id.iv_img_service)
    ImageView ivImgService;
    @BindView(R.id.fl_header_service)
    FrameLayout flHeaderService;
    @BindView(R.id.iv_img_share)
    ImageView ivImgShare;
    @BindView(R.id.fl_integral_product)
    FrameLayout fl_integral_product;

    private String productId;
    private List<CommunalADActivityBean> imagesVideoList = new ArrayList<>();

    private List<CommunalDetailObjectBean> itemBodyList = new ArrayList<>();
    private int personalScore;
    private CommunalDetailAdapter communalDetailAdapter;
    private CBViewHolderCreator cbViewHolderCreator;
    private String[] detailTabData = {"商品", "详情"};
    private ArrayList<CustomTabEntity> tabData = new ArrayList<>();
    private int measuredHeight;
    private IntegralProductInfoBean productInfoBean;
    private GoodsCommentBean goodsCommentBean;
    private IntegralPopWindows integralPopWindows;
    private SimpleSkuDialog simpleSkuDialog;
    private IntegralProductInfoEntity productInfoEntity;

    @Override
    protected int getContentView() {
        return R.layout.activity_integration_details_new;
    }

    @Override
    protected void initViews() {
        communal_recycler_wrap.setNestedScrollingEnabled(false);
        Intent intent = getIntent();
        productId = intent.getStringExtra("productId");
        communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(IntegralScrollDetailsActivity.this));
        communal_recycler_wrap.setNestedScrollingEnabled(false);
        communalDetailAdapter = new CommunalDetailAdapter(IntegralScrollDetailsActivity.this, itemBodyList);
        communalDetailAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_communal_share) {
                    return;
                }
                CommunalWebDetailUtils.getCommunalWebInstance()
                        .setWebDataClick(IntegralScrollDetailsActivity.this, view, loadHud);
            }
        });
        communal_recycler_wrap.setAdapter(communalDetailAdapter);
        smart_integral_details.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                loadData();
            }
        });
        for (int i = 0; i < detailTabData.length; i++) {
            tabData.add(new TabEntity(detailTabData[i], 0, 0));
        }
        ctbQtProDetails.setTextSize(AutoSizeUtils.mm2px(mAppContext, 30));
        ctbQtProDetails.setIndicatorWidth(AutoSizeUtils.mm2px(mAppContext, 28 * 2));
        ctbQtProDetails.setTabPadding(AutoSizeUtils.mm2px(mAppContext, 20));
        ctbQtProDetails.setTabData(tabData);
        ctbQtProDetails.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                scrollLocation(position);
            }

            @Override
            public void onTabReselect(int position) {
                scrollLocation(position);
            }
        });
        ctbQtProDetails.setCurrentTab(0);
        scroll_integral_product_header.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (measuredHeight < 1) {
                    measuredHeight = ll_integral_detail_product.getMeasuredHeight();
                }
                int currentTab = ctbQtProDetails.getCurrentTab();
                if (scrollY >= 0 && scrollY < measuredHeight && currentTab != 0) {
                    ctbQtProDetails.setCurrentTab(0);
                } else if (scrollY >= measuredHeight && currentTab != 1) {
                    ctbQtProDetails.setCurrentTab(1);
                }
            }
        });
    }

    private void scrollLocation(int position) {
        measuredHeight = ll_integral_detail_product.getMeasuredHeight();
        if (position == 0) {
            scroll_integral_product_header.scrollTo(0, 0);
        } else if (position == 1) {
            if (ll_integral_detail_product.getMeasuredHeight() > 0) {
                scroll_integral_product_header.scrollTo(0, measuredHeight);
            }
        }
    }

    @Override
    protected void loadData() {
        getIntegrationData();
        getIntegralComment();
    }

    @Override
    public View getLoadView() {
        return fl_integral_product;
    }

    @Override
    protected boolean isAddLoad() {
        return true;
    }

    /**
     * 商品详情内容
     */
    private void getIntegrationData() {
        String url = Url.H_INTEGRAL_DETAILS_GOODS;
        Map<String, Object> params = new HashMap<>();
        params.put("id", productId);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                smart_integral_details.finishRefresh();

                productInfoEntity = GsonUtils.fromJson(result, IntegralProductInfoEntity.class);
                if (productInfoEntity != null) {
                    if (productInfoEntity.getCode().equals(SUCCESS_CODE)) {
                        productInfoBean = productInfoEntity.getIntegralProductInfoBean();
                        setIntegralProductData(productInfoBean);
                    } else if (productInfoEntity.getCode().equals(SUCCESS_CODE)) {
                        showToast(R.string.shopOverdue);
                    } else {
                        showToast(productInfoEntity.getMsg());
                    }
                }
                NetLoadUtils.getNetInstance().showLoadSir(loadService, productInfoEntity);
            }

            @Override
            public void onNotNetOrException() {
                smart_integral_details.finishRefresh();
                showToast(R.string.connectedFaile);
                NetLoadUtils.getNetInstance().showLoadSir(loadService, productInfoEntity);
            }
        });
    }

    private void getIntegralComment() {
        String url = Url.Q_SHOP_DETAILS_COMMENT;
        Map<String, Object> params = new HashMap<>();
        params.put("showCount", 1);
        params.put("currentPage", 1);
        params.put("id", productId);
        if (userId > 0) {
            params.put("uid", userId);
        }
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {
                ll_product_comment.setVisibility(View.GONE);

                GoodsCommentEntity goodsCommentEntity = GsonUtils.fromJson(result, GoodsCommentEntity.class);
                if (goodsCommentEntity != null) {
                    if (goodsCommentEntity.getCode().equals(SUCCESS_CODE) && goodsCommentEntity.getGoodsComments() != null
                            && goodsCommentEntity.getGoodsComments().size() > 0) {
                        ll_product_comment.setVisibility(View.VISIBLE);
                        List<GoodsCommentBean> goodsComments = goodsCommentEntity.getGoodsComments();
                        tv_shop_comment_count.setText(String.format(getResources().getString(R.string.product_comment_count), goodsCommentEntity.getEvaluateCount()));
                        goodsCommentBean = goodsComments.get(0);
                        setCommentData();
                    }
                }
            }

            @Override
            public void onNotNetOrException() {
                ll_product_comment.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 设置评论数据
     */
    private void setCommentData() {
        if (!TextUtils.isEmpty(goodsCommentBean.getImages())) {
            rvProEva.setVisibility(View.VISIBLE);
            setEvaImages(rvProEva, goodsCommentBean.getImages());
        } else {
            rvProEva.setVisibility(View.GONE);
        }
        GlideImageLoaderUtil.loadHeaderImg(IntegralScrollDetailsActivity.this, imgDirectAvatar, goodsCommentBean.getAvatar());
        ratingBarDirectCount.setVisibility(goodsCommentBean.getStar() < 1 ? View.GONE : View.VISIBLE);
        ratingBarDirectCount.setRating(goodsCommentBean.getStar());
        ratingBarDirectCount.setMax(goodsCommentBean.getStar());
        tvEvaCount.setText(goodsCommentBean.getLikeNum() > 0
                ? goodsCommentBean.getLikeNum() + "" : "赞");
        tvEvaCount.setSelected(goodsCommentBean.isFavor());
        tvEvaCount.setTag(goodsCommentBean);
        tvEvaUserName.setText(getStrings(goodsCommentBean.getNickname()));
        if (!TextUtils.isEmpty(goodsCommentBean.getContent())) {
            tvDirectEvaluation.setVisibility(View.VISIBLE);
            tvDirectEvaluation.setText(goodsCommentBean.getContent());
        } else {
            tvDirectEvaluation.setVisibility(View.GONE);
        }
        if (goodsCommentBean.getIs_reply() != 0) {
            llEvaCommentReply.setVisibility(View.VISIBLE);
            String reply_content = goodsCommentBean.getReply_content();
            String replyName = getStrings("多么生活：");
            reply_content = getStrings(replyName + (TextUtils.isEmpty(reply_content) ?
                    getResources().getString(R.string.direct_eva_hint) : reply_content));
            Link replyNameLink = new Link(replyName);
//                    回复颜色
            replyNameLink.setTextColor(Color.parseColor("#8e8e8e"));
            replyNameLink.setUnderlined(false);
            replyNameLink.setHighlightAlpha(0f);
            emoDirectEvaReply.setText(getStrings(reply_content));
            LinkBuilder.on(emoDirectEvaReply)
                    .setText(reply_content)
                    .addLink(replyNameLink)
                    .build();
        } else {
            llEvaCommentReply.setVisibility(View.GONE);
        }
    }


    @OnClick(R.id.tv_life_back)
    void goBack(View view) {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IS_LOGIN_CODE) {
            getIntegration();
        }
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private void setIntegralProductData(final IntegralProductInfoBean productInfoBean) {
        imagesVideoList.clear();
        itemBodyList.clear();
        String[] images = productInfoBean.getImages().split(",");
        CommunalADActivityBean communalADActivityBean;
        if (images.length != 0) {
            List<String> imageList = Arrays.asList(images);
            for (int i = 0; i < imageList.size(); i++) {
                communalADActivityBean = new CommunalADActivityBean();
                if (i == 0) {
                    communalADActivityBean.setPicUrl(getWaterMarkImgUrl(imageList.get(i), productInfoBean.getWaterRemark()));
                } else {
                    communalADActivityBean.setPicUrl(imageList.get(i));
                }
                imagesVideoList.add(communalADActivityBean);
            }
        } else {
            communalADActivityBean = new CommunalADActivityBean();
            communalADActivityBean.setPicUrl(getStrings(productInfoBean.getPicUrl()));
            imagesVideoList.add(communalADActivityBean);
        }
//         轮播图
        if (cbViewHolderCreator == null) {
            cbViewHolderCreator = new CBViewHolderCreator() {
                @Override
                public Holder createHolder(View itemView) {
                    return new CommunalAdHolderView(itemView, IntegralScrollDetailsActivity.this, false);
                }

                @Override
                public int getLayoutId() {
                    return R.layout.layout_ad_image_video;
                }
            };
        }
        banner_integration_details.setPages(this, cbViewHolderCreator, imagesVideoList).setCanLoop(true)
                .setPageIndicator(new int[]{R.drawable.unselected_radius, R.drawable.selected_radius});

//        商品名字
        tv_integration_details_introduce_productName.setText(getStrings(productInfoBean.getName()));
        String priceName;
        if (productInfoBean.getIntegralType() == 0) {
            priceName = String.format(getResources().getString(R.string.integral_indent_product_price), productInfoBean.getIntegralPrice());
        } else {
            priceName = String.format(getResources().getString(R.string.integral_product_and_price)
                    , productInfoBean.getIntegralPrice(), getStrings(productInfoBean.getMoneyPrice()));
        }
//        积分 34
        tv_integration_details_price.setText(priceName);
        Pattern p = Pattern.compile(REGEX_NUM);
        Link redNum = new Link(p);
        //        @用户昵称
        redNum.setTextColor(getResources().getColor(R.color.text_normal_red));
        redNum.setUnderlined(false);
        redNum.setTextSize(AutoSizeUtils.mm2px(mAppContext, 34));
        redNum.setHighlightAlpha(0f);
        LinkBuilder.on(tv_integration_details_price)
                .addLink(redNum)
                .build();
//           市场价
        tv_integral_introduce_mk.setText(("市场价¥" + productInfoBean.getMarketPrice()));
//
        if (productInfoBean.getProductDetails() != null && productInfoBean.getProductDetails().size() > 0) {
            itemBodyList.clear();
            CommunalDetailObjectBean communalDetailObjectBean = new CommunalDetailObjectBean("商品详情");
            communalDetailObjectBean.setItemType(CommunalDetailObjectBean.TYPE_PRODUCT_TITLE);
            itemBodyList.add(communalDetailObjectBean);
            List<CommunalDetailBean> productDetails = productInfoBean.getProductDetails();
            if (productDetails != null) {
                itemBodyList.addAll(CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(productDetails));
            }
            getIntegralProductRule();
            communalDetailAdapter.notifyDataSetChanged();
        }
//        积分商品标签
        if (productInfoBean.getTags() != null && !TextUtils.isEmpty(productInfoBean.getTagIds())) {
            final Map<Integer, String> tagMap = new HashMap<>();
            for (TagsBean tagsBean : productInfoBean.getTags()) {
                tagMap.put(tagsBean.getId(), getStrings(tagsBean.getName()));
            }
            final String[] tagSelected = productInfoBean.getTagIds().split(",");
            if (tagSelected.length > 0) {
                flex_communal_tag.setVisibility(View.VISIBLE);
                flex_communal_tag.removeAllViews();
                for (String tagName : tagSelected) {
                    flex_communal_tag.addView(ProductLabelCreateUtils.createProductTag(IntegralScrollDetailsActivity.this, tagMap.get(Integer.parseInt(tagName))));
                }
            } else {
                flex_communal_tag.setVisibility(View.GONE);
            }
        } else {
            flex_communal_tag.setVisibility(View.GONE);
        }
        if (productInfoBean.getSkuSale().size() > 1) {
            EditGoodsSkuBean editGoodsSkuBean = new EditGoodsSkuBean();
            editGoodsSkuBean.setQuantity(productInfoBean.getQuantity());
            editGoodsSkuBean.setId(productInfoBean.getId());
            editGoodsSkuBean.setProps(productInfoBean.getProps());
            editGoodsSkuBean.setPropvalues(productInfoBean.getPropvalues());
            editGoodsSkuBean.setSkuSale(productInfoBean.getSkuSale());
            editGoodsSkuBean.setOldCount(1);
            editGoodsSkuBean.setUserScore(productInfoBean.getUserScore());
            simpleSkuDialog = new SimpleSkuDialog(IntegralScrollDetailsActivity.this);
            simpleSkuDialog.refreshView(editGoodsSkuBean);
        } else {
            if (integralPopWindows == null) {
                SkuSaleBean skuSaleBean = productInfoBean.getSkuSale().get(0);
                integralPopWindows = new IntegralPopWindows(IntegralScrollDetailsActivity.this);
                integralPopWindows.setPopupWindowFullScreen(true);
                View popupWindowView = integralPopWindows.getContentView();
                RectAddAndSubViewCommunal rectAddAndSubViewCommunal = popupWindowView.findViewById(R.id.rect_integral_product_number);
                TextView tv_integral_product_exchange = popupWindowView.findViewById(R.id.tv_integral_product_exchange);
                rectAddAndSubViewCommunal.setMaxNum(skuSaleBean.getQuantity());
                rectAddAndSubViewCommunal.setNum(1);
                rectAddAndSubViewCommunal.setOnNumChangeListener(new RectAddAndSubViewCommunal.OnNumChangeListener() {
                    @Override
                    public void onNumChange(View view, int type, int newNum, int oldNum) {
                        if (newNum > 0) {
                            float currentIntegralPrice = newNum * getFloatNumber(skuSaleBean.getPrice());
                            if (productInfoBean.getUserScore() >= currentIntegralPrice) {
                                tv_integral_product_exchange.setEnabled(true);
                                tv_integral_product_exchange.setText("立即兑换");
                            } else {
                                tv_integral_product_exchange.setEnabled(false);
                                tv_integral_product_exchange.setText("积分不足");
                            }
                        }
                    }
                });
                ShopCarGoodsSku shopCarGoodsSku = new ShopCarGoodsSku();
                shopCarGoodsSku.setSaleSkuId(skuSaleBean.getId());
                shopCarGoodsSku.setPrice(Double.parseDouble(skuSaleBean.getPrice()));
                shopCarGoodsSku.setValuesName(!TextUtils.isEmpty(productInfoBean.getPropvalues().get(0).getPropValueName())
                        ? productInfoBean.getPropvalues().get(0).getPropValueName() : "默认");
                shopCarGoodsSku.setPicUrl(getStrings(productInfoBean.getPicUrl()));
                shopCarGoodsSku.setMoneyPrice(skuSaleBean.getMoneyPrice());
                shopCarGoodsSku.setProductId(productInfoBean.getId());
                shopCarGoodsSku.setIntegralName(productInfoBean.getName());
                tv_integral_product_exchange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        integralPopWindows.dismiss();
                        shopCarGoodsSku.setIntegralProductType(productInfoBean.getIntegralProductType());
                        shopCarGoodsSku.setIntegralType(productInfoBean.getIntegralType());
                        shopCarGoodsSku.setCount(rectAddAndSubViewCommunal.getNum());
                        skipExchangeIntegral(shopCarGoodsSku);
                    }
                });
            }
        }
        if (productInfoBean.getQuantity() < 1) {
            tv_integral_product_exchange.setEnabled(false);
            tv_integral_product_exchange.setText(R.string.buy_go_null);
        } else {
            if (userId > 0 && productInfoBean.getUserScore() < productInfoBean.getIntegralPrice()) {
                tv_integral_product_exchange.setEnabled(false);
                tv_integral_product_exchange.setText("积分不足");
            } else {
                tv_integral_product_exchange.setEnabled(true);
                tv_integral_product_exchange.setText("立即兑换");
            }
        }
    }

    /**
     * 积分商品服务承诺
     */
    private void getIntegralProductRule() {
        String url = Url.INTEGRAL_PRODUCT_SERVICE;
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, new NetLoadListenerHelper() {
            @Override
            public void onSuccess(String result) {

                CommunalRuleEntity communalRuleEntity = GsonUtils.fromJson(result, CommunalRuleEntity.class);
                if (communalRuleEntity != null && SUCCESS_CODE.equals(communalRuleEntity.getCode())) {
                    if (communalRuleEntity.getCommunalRuleList() != null
                            && communalRuleEntity.getCommunalRuleList().size() > 0) {
                        CommunalDetailObjectBean communalDetailObjectBean = new CommunalDetailObjectBean();
                        communalDetailObjectBean.setItemType(TYPE_PRODUCT_TITLE);
                        communalDetailObjectBean.setContent("服务承诺");
                        itemBodyList.add(communalDetailObjectBean);
                        List<CommunalDetailObjectBean> detailsDataList = CommunalWebDetailUtils.getCommunalWebInstance().getWebDetailsFormatDataList(communalRuleEntity.getCommunalRuleList());
                        if (detailsDataList != null && detailsDataList.size() > 0) {
                            itemBodyList.addAll(detailsDataList);
                        }
                        communalDetailAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void getIntegration() {
        String url = Url.MINE_PAGE;
        Map<String, Object> params = new HashMap<>();
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url
                , params, new NetLoadListenerHelper() {
                    @Override
                    public void onSuccess(String result) {

                        UserPagerInfoEntity pagerInfoBean = GsonUtils.fromJson(result, UserPagerInfoEntity.class);
                        if (pagerInfoBean != null) {
                            if (pagerInfoBean.getCode().equals(SUCCESS_CODE)) {
                                personalScore = pagerInfoBean.getUserInfo().getScore();
                                getExchangeScore(personalScore);
                            } else if (!pagerInfoBean.getCode().equals(EMPTY_CODE)) {
                                showToast(pagerInfoBean.getMsg());
                            }
                        }
                    }
                });
    }

    private void getExchangeScore(int personalScore) {
        if (productInfoBean != null) {
            if (productInfoBean.getIntegralPrice() <= personalScore) {
                tv_integral_product_exchange.setEnabled(true);
                tv_integral_product_exchange.setText("立即兑换");
            } else {
                tv_integral_product_exchange.setEnabled(false);
                tv_integral_product_exchange.setText("积分不足");
            }
        }
    }

    @OnClick(R.id.tv_integral_product_exchange)
    void exchangeScore(View view) {
        if (productInfoBean != null) {
            if (userId > 0) {
                if (integralPopWindows != null) {
                    integralPopWindows.showPopupWindow();
                } else {
                    if (simpleSkuDialog != null) {
                        simpleSkuDialog.show();
                        simpleSkuDialog.getGoodsSKu(new SimpleSkuDialog.DataListener() {
                            @Override
                            public void getSkuProperty(ShopCarGoodsSku shopCarGoodsSku) {
                                if (shopCarGoodsSku != null) {
                                    shopCarGoodsSku.setIntegralName(productInfoBean.getName());
                                    shopCarGoodsSku.setProductId(productInfoBean.getId());
                                    shopCarGoodsSku.setIntegralProductType(productInfoBean.getIntegralProductType());
                                    shopCarGoodsSku.setIntegralType(productInfoBean.getIntegralType());
                                    if (TextUtils.isEmpty(shopCarGoodsSku.getPicUrl())) {
                                        shopCarGoodsSku.setPicUrl(productInfoBean.getPicUrl());
                                    }
                                    skipExchangeIntegral(shopCarGoodsSku);
                                }
                            }
                        });
                    }
                }
            }
        } else {
            getLoginStatus(this);
        }
    }

    private void skipExchangeIntegral(@NonNull ShopCarGoodsSku shopCarGoodsSku) {
        Intent intent = new Intent(IntegralScrollDetailsActivity.this, IntegrationIndentWriteActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("integralProduct", shopCarGoodsSku);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void setEvaImages(RecyclerView rvProductEva, String images) {
        if (!TextUtils.isEmpty(images)) {
            final List<PictureBean> pictureBeanList = new ArrayList<>();
            String[] evaImages = images.split(",");
            PictureBean pictureBean;
            final List<String> originalPhotos = new ArrayList<>(Arrays.asList(evaImages));
            for (int i = 0; i < evaImages.length; i++) {
                pictureBean = new PictureBean();
                pictureBean.setItemType(TYPE_3);
                pictureBean.setIndex(i);
                pictureBean.setPath(evaImages[i]);
                pictureBean.setOriginalList(originalPhotos);
                pictureBeanList.add(pictureBean);
            }
            FindImageListAdapter findImageListAdapter = new FindImageListAdapter(IntegralScrollDetailsActivity.this, pictureBeanList);
            rvProductEva.setAdapter(findImageListAdapter);
            findImageListAdapter.setNewData(pictureBeanList);
            findImageListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    PictureBean pictureBean = (PictureBean) view.getTag();
                    if (pictureBean != null) {
                        ImageBean imageBean;
                        List<ImageBean> imageBeanList = new ArrayList<>();
                        for (String picUrl : pictureBean.getOriginalList()) {
                            imageBean = new ImageBean();
                            imageBean.setPicUrl(picUrl);
                            imageBeanList.add(imageBean);
                        }
                        ImagePagerActivity.startImagePagerActivity(IntegralScrollDetailsActivity.this, IMAGE_DEF, imageBeanList
                                , pictureBean.getIndex() < pictureBean.getOriginalList().size() ? pictureBean.getIndex() : 0, null);
                    }
                }
            });
        } else {
            rvProductEva.setVisibility(View.GONE);
        }
    }

    /**
     * 跳转更多评论
     */
    @OnClick(R.id.tv_shop_comment_more)
    void moreComment() {
        Intent intent = new Intent(this, DirectProductEvaluationActivity.class);
        intent.putExtra("productId", productId);
        startActivity(intent);
    }

    /**
     * 头像设置
     */
    @OnClick(R.id.img_direct_avatar)
    void checkUserInfo() {
        if (goodsCommentBean != null) {
            Intent intent = new Intent(IntegralScrollDetailsActivity.this, UserPagerActivity.class);
            intent.putExtra("userId", String.valueOf(goodsCommentBean.getUserId()));
            startActivity(intent);
        }
    }

    @OnClick(R.id.tv_life_back)
    void goBack() {
        finish();
    }

    @OnClick(R.id.iv_img_service)
    void foundService() {
        skipServiceDataInfo(productInfoBean);

    }

    //    七鱼客服
    private void skipServiceDataInfo(IntegralProductInfoBean integralProductInfoBean) {
        QyProductIndentInfo qyProductIndentInfo = null;
        String sourceTitle = "";
        String sourceUrl = "";
        if (integralProductInfoBean != null) {
            qyProductIndentInfo = new QyProductIndentInfo();
            sourceUrl = Url.BASE_SHARE_PAGE_TWO + "m/template/common/integralGoods.html?id=" + productInfoBean.getId();
            ;
            sourceTitle = "积分商品详情：" + productInfoBean.getName();
            qyProductIndentInfo.setUrl(sourceUrl);
            qyProductIndentInfo.setTitle(getStrings(integralProductInfoBean.getName()));
            qyProductIndentInfo.setPicUrl(getStrings(integralProductInfoBean.getPicUrl()));
            String priceName;
            if (productInfoBean.getIntegralType() == 0) {
                priceName = String.format(getResources().getString(R.string.integral_indent_product_price), productInfoBean.getIntegralPrice());
            } else {
                priceName = String.format(getResources().getString(R.string.integral_product_and_price)
                        , productInfoBean.getIntegralPrice(), getStrings(productInfoBean.getMoneyPrice()));
            }
            qyProductIndentInfo.setNote(priceName);
        }
        QyServiceUtils.getQyInstance().openQyServiceChat(this, sourceTitle, sourceUrl, qyProductIndentInfo);
    }

    @OnClick(R.id.iv_img_share)
    void setShareData() {
        if (productInfoBean != null) {
            new UMShareAction(IntegralScrollDetailsActivity.this
                    , productInfoBean.getPicUrl()
                    , "我在多么生活看中了" + productInfoBean.getName()
                    , "积分商品"
                    , Url.BASE_SHARE_PAGE_TWO + "m/template/common/integralGoods.html?id=" + productInfoBean.getId(), productInfoBean.getId());
        }
    }

    @OnClick(R.id.tv_eva_count)
    void evaFavorCount(View view) {
        GoodsCommentBean goodsCommentBean = (GoodsCommentBean) view.getTag();
        if (goodsCommentBean != null && !goodsCommentBean.isFavor()) {
            if (userId > 0) {
                setProductEvaLike(view);
            } else {
                getLoginStatus(this);
            }
        }
    }

    private void setProductEvaLike(View view) {
        GoodsCommentBean goodsCommentBean = (GoodsCommentBean) view.getTag();
        TextView tv_eva_like = (TextView) view;
        String url = Url.SHOP_EVA_LIKE;
        Map<String, Object> params = new HashMap<>();
        params.put("id", goodsCommentBean.getId());
        params.put("uid", userId);
        NetLoadUtils.getNetInstance().loadNetDataPost(this, url, params, null);
        goodsCommentBean.setFavor(!goodsCommentBean.isFavor());
        tv_eva_like.setSelected(!tv_eva_like.isSelected());
        tv_eva_like.setText(getNumCount(tv_eva_like.isSelected(), goodsCommentBean.isFavor(), goodsCommentBean.getLikeNum(), "赞"));
    }
}
