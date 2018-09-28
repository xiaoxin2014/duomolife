package com.amkj.dmsh.homepage.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.ImageBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.find.activity.FindTagDetailsActivity;
import com.amkj.dmsh.find.activity.ImagePagerActivity;
import com.amkj.dmsh.find.bean.InvitationImgDetailEntity.InvitationImgDetailBean.TagsBean;
import com.amkj.dmsh.homepage.activity.DoMoLifeCommunalActivity;
import com.amkj.dmsh.homepage.bean.InvitationDetailEntity;
import com.amkj.dmsh.mine.activity.MineLoginActivity;
import com.amkj.dmsh.mine.bean.SavePersonalInfoBean;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.user.adapter.InvitationProAdapter;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.views.JzVideo.CustomAudioPlayer;
import com.amkj.dmsh.views.JzVideo.JzVideoPlayerStatusDialog;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.RichTextConfig;
import com.zzhoujay.richtext.callback.DrawableGetter;
import com.zzhoujay.richtext.callback.OnImageClickListener;
import com.zzhoujay.richtext.callback.OnUrlClickListener;
import com.zzhoujay.richtext.callback.SimpleImageFixCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jzvd.JZVideoPlayer;
import emojicon.EmojiconTextView;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantMethod.showToast;
import static com.amkj.dmsh.constant.ConstantVariable.IMG_REGEX_TAG;
import static com.amkj.dmsh.constant.ConstantVariable.IS_LOGIN_CODE;
import static com.amkj.dmsh.constant.ConstantVariable.REGEX_NUM;
import static com.amkj.dmsh.constant.ConstantVariable.REGEX_TEXT;
import static com.amkj.dmsh.constant.ConstantVariable.regexATextUrl;
import static com.amkj.dmsh.find.activity.ImagePagerActivity.IMAGE_DEF;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.NORTEXT;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_AUDIO;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_COUPON;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_COUPON_PACKAGE;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_EMPTY_OBJECT;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_GIF_IMG;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_GOODS_IMG;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_GOODS_WEL;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_LINK_TAOBAO;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_LUCKY_MONEY;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_PRODUCT_MORE;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_PRODUCT_RECOMMEND;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_PRODUCT_TAG;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_PRODUCT_TITLE;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_PROMOTION_TITLE;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_RELEVANCE_PRODUCT;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_SHARE;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_VIDEO;

;

/**
 * Created by atd48 on 2016/8/30.
 */
public class CommunalDetailAdapter extends BaseMultiItemQuickAdapter<CommunalDetailObjectBean, CommunalDetailAdapter.CommunalHolderHelper> implements View.OnClickListener {
    private final Context context;
    private final int screenWidth;
    private final KProgressHUD loadHud;
    private final float density;
    //    换行
    private String br = "<br/>";
    //    换行字体样式
    private String lineStyle = "font-family:";

    private String lineFontEndStyle = "\\";

    private String blankStyle = "&nbsp;";

    private String brStyleReg = "<br[\\s\\S]*?>";

    private final String pTagStart = "<p";
    private final String rTagStart = "<span";
    private final String pTagEnd = "</p>";
    private final String rTagEnd = "</span>";
    private String TAOBAO_URL = "taobao.com";
    //    行间距
    private String line_height = "line-height";
    //    字体大小
    private String font_size = "font-size";
    //    背景颜色
    private String text_color = " color: rgb";
    //    显示位置
    private String alignGravity = "text-align:";
    private String alignRight = "right";
    private String alignCenter = "center";
    //    末尾结束符
    private String endStyle = ";";
    private String endDash = "\"";
    private Map<String, String> urlMap;
    private int uid;
    //    是否是图片
    private boolean isImageTag;

    public CommunalDetailAdapter(Context context, List<CommunalDetailObjectBean> descriptionBeanList) {
        super(descriptionBeanList);
//        正常文本
        addItemType(NORTEXT, R.layout.layout_article_details);
//        淘宝优惠券
        addItemType(TYPE_LUCKY_MONEY, R.layout.adapter_article_details_item_tb_coupon);
//        自营商品优惠券
        addItemType(TYPE_COUPON, R.layout.adapter_article_details_item_direct_coupon);
//        优惠券礼包
        addItemType(TYPE_COUPON_PACKAGE, R.layout.adapter_article_details_item_direct_coupon);
//        插入福利社商品
        addItemType(TYPE_GOODS_WEL, R.layout.adapter_border_pro_wel);
//        插入图片商品
        addItemType(TYPE_GOODS_IMG, R.layout.layout_communal_cover_wrap);
//        动态图片处理
        addItemType(TYPE_GIF_IMG, R.layout.layout_communal_img_gif);
//        视频播放
        addItemType(TYPE_VIDEO, R.layout.layout_article_video);
//        音频播放
        addItemType(TYPE_AUDIO, R.layout.layout_article_audio);
//        分享页面
        addItemType(TYPE_SHARE, R.layout.layout_communal_share_view);
//         详情头部
        addItemType(TYPE_PRODUCT_TITLE, R.layout.layout_communal_detail_title);
//        查看更多
        addItemType(TYPE_PRODUCT_MORE, R.layout.layout_communal_detail_more);
//        标签
        addItemType(TYPE_PRODUCT_TAG, R.layout.layout_communal_flexbox);
//        淘宝链接
        addItemType(TYPE_LINK_TAOBAO, R.layout.layout_communal_taobao_link);
//        商品推荐
        addItemType(TYPE_PRODUCT_RECOMMEND, R.layout.layout_top_communal_wrap);
//        关联商品
        addItemType(TYPE_RELEVANCE_PRODUCT, R.layout.layout_top_communal_wrap);
//        占位
        addItemType(TYPE_EMPTY_OBJECT, R.layout.layout_empty_object);
//        限时特惠商品详情头部
        addItemType(TYPE_PROMOTION_TITLE, R.layout.layout_promotion_product_details_header_title);
        this.context = context;
        urlMap = new HashMap<>();
        TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        screenWidth = app.getScreenWidth();
        density = app.getDensity();
        loadHud = KProgressHUD.create(context)
                .setCancellable(false)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
    }

    @Override
    protected void convert(final CommunalHolderHelper holder, final CommunalDetailObjectBean detailObjectBean) {
        switch (holder.getItemViewType()) {
            case TYPE_COUPON:
            case TYPE_COUPON_PACKAGE:
                final ImageView img_product_coupon_pic = holder.getView(R.id.img_product_coupon_pic);
                GlideImageLoaderUtil.loadImgDynamicDrawable(context, img_product_coupon_pic, TextUtils.isEmpty(detailObjectBean.getNewPirUrl()) ?
                        (TextUtils.isEmpty(detailObjectBean.getPicUrl()) ? "" : detailObjectBean.getPicUrl())
                        : detailObjectBean.getNewPirUrl());
                holder.setTag(R.id.img_product_coupon_pic, R.id.iv_avatar_tag, detailObjectBean.getId())
                        .setTag(R.id.img_product_coupon_pic, R.id.iv_type_tag, detailObjectBean.getItemType())
                        .addOnClickListener(R.id.img_product_coupon_pic);
                break;
            case TYPE_GOODS_WEL:
                GlideImageLoaderUtil.loadHeaderImg(context, (ImageView) holder.getView(R.id.iv_ql_bl_product), detailObjectBean.getPicUrl());
                holder.setText(R.id.tv_ql_bl_pro_name, getStrings(detailObjectBean.getName()))
                        .setText(R.id.tv_ql_bl_pro_rec, "")
                        .setText(R.id.tv_ql_bl_pro_price, "￥ " + detailObjectBean.getPrice())
                        .setGone(R.id.iv_ql_bl_add_car, detailObjectBean.getItemTypeId() == 1)
                        .addOnClickListener(R.id.iv_ql_bl_add_car).setTag(R.id.iv_ql_bl_add_car, detailObjectBean);
                holder.itemView.setTag(detailObjectBean);
                break;
            case TYPE_GOODS_IMG:
                final ImageView iv_communal_cover_wrap = holder.getView(R.id.iv_communal_cover_wrap);
                GlideImageLoaderUtil.loadImgDynamicDrawable(context, iv_communal_cover_wrap, detailObjectBean.getNewPirUrl());
                holder.addOnClickListener(R.id.iv_communal_cover_wrap).setTag(R.id.iv_communal_cover_wrap, R.id.iv_tag, detailObjectBean);
                holder.itemView.setTag(detailObjectBean);
                break;
            case TYPE_PRODUCT_RECOMMEND:
                ProNoShopCarAdapter qualityTypeProductAdapter = new ProNoShopCarAdapter(context, detailObjectBean.getProductList());
                holder.communal_recycler_wrap.setLayoutManager(new GridLayoutManager(context, 2));
                if (holder.communal_recycler_wrap.getItemDecorationCount() < 1) {
                    holder.communal_recycler_wrap.addItemDecoration(new PinnedHeaderItemDecoration.Builder(-1)
                            // 设置分隔线资源ID
                            .setDividerId(R.drawable.item_divider_five_dp)
                            // 开启绘制分隔线，默认关闭
                            .enableDivider(true)
                            // 是否关闭标签点击事件，默认开启
                            .disableHeaderClick(false)
                            // 设置标签和其内部的子控件的监听，若设置点击监听不为null，但是disableHeaderClick(true)的话，还是不会响应点击事件
                            .setHeaderClickListener(null)
                            .create());
                }
                holder.communal_recycler_wrap.setAdapter(qualityTypeProductAdapter);
                qualityTypeProductAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        LikedProductBean likedProductBean = (LikedProductBean) view.getTag();
                        if (likedProductBean != null) {
                            Intent intent = new Intent(context, ShopScrollDetailsActivity.class);
                            intent.putExtra("productId", String.valueOf(likedProductBean.getId()));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    }
                });
                break;
            case TYPE_RELEVANCE_PRODUCT:
                holder.communal_recycler_wrap.setLayoutManager(new LinearLayoutManager(context));
                InvitationProAdapter relevanceProAdapter = new InvitationProAdapter(context, detailObjectBean.getRelevanceProBeanList());
                holder.communal_recycler_wrap.setAdapter(relevanceProAdapter);
                relevanceProAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        InvitationDetailEntity.InvitationDetailBean.RelevanceProBean relevanceProBean = (InvitationDetailEntity.InvitationDetailBean.RelevanceProBean) view.getTag();
                        if (relevanceProBean != null) {
                            Intent intent = new Intent(context, ShopScrollDetailsActivity.class);
                            intent.putExtra("productId", String.valueOf(relevanceProBean.getProductId()));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    }
                });
                break;
            case TYPE_GIF_IMG:
                final ImageView iv_gif_load_image = holder.getView(R.id.iv_gif_load_image);
                GlideImageLoaderUtil.loadGif(context, iv_gif_load_image, detailObjectBean.getPicUrl());
                break;
            case TYPE_PROMOTION_TITLE:
                holder.setText(R.id.tv_communal_red_column_title, getStrings(detailObjectBean.getName()));
                break;
            case TYPE_LUCKY_MONEY:
                TextView tv_tb_coupon_price = holder.getView(R.id.tv_tb_coupon_price);
                tv_tb_coupon_price.setText(getStringsChNPrice(context,detailObjectBean.getName()));
                Link link = new Link(Pattern.compile(REGEX_NUM));
                link.setTextColor(context.getResources().getColor(R.color.white))
                        .setUnderlined(false)
                        .setHighlightAlpha(0f)
                        .setTextSize(AutoSizeUtils.mm2px(mAppContext,74))
                        .setOnClickListener(null);
                LinkBuilder.on(tv_tb_coupon_price)
                        .addLink(link)
                        .build();
                holder.setTag(R.id.ll_layout_tb_coupon, detailObjectBean)
                        .setGone(R.id.tv_last_tb_coupon_hint,detailObjectBean.isLastTbCoupon())
                        .addOnClickListener(R.id.ll_layout_tb_coupon);
                break;
            case TYPE_PRODUCT_TITLE:
                holder.setText(R.id.tv_communal_detail_title, getStrings(detailObjectBean.getContent()));
                break;
            case TYPE_LINK_TAOBAO:
                if (!TextUtils.isEmpty(detailObjectBean.getPicUrl())) {
                    GlideImageLoaderUtil.loadFitCenter(context, (ImageView) holder.getView(R.id.iv_communal_tb_cover), getStrings(detailObjectBean.getPicUrl()));
                }
                holder.setText(R.id.tv_communal_tb_link, getStrings(detailObjectBean.getContent()))
                        .setGone(R.id.iv_communal_tb_cover, !TextUtils.isEmpty(detailObjectBean.getPicUrl()))
                        .setGone(R.id.tv_communal_tb_link, TextUtils.isEmpty(detailObjectBean.getPicUrl()))
                        .addOnClickListener(R.id.tv_communal_tb_link).setTag(R.id.tv_communal_tb_link, detailObjectBean)
                        .addOnClickListener(R.id.iv_communal_tb_cover).setTag(R.id.iv_communal_tb_cover, R.id.iv_tag, detailObjectBean);
                break;
            case TYPE_PRODUCT_MORE:
                holder.itemView.setTag(detailObjectBean);
                break;
            case TYPE_PRODUCT_TAG:
                FlexboxLayout flexboxLayout = holder.getView(R.id.flex_communal_tag);
                flexboxLayout.removeAllViews();
                ImageView iv_label = new ImageView(context);
                iv_label.setImageResource(R.drawable.tag_label_icon);
                iv_label.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                flexboxLayout.addView(iv_label);
                List<TagsBean> tagsBeans = detailObjectBean.getTagsBeans();
                for (TagsBean tagsBean : tagsBeans) {
                    if (!TextUtils.isEmpty(tagsBean.getTag_name())) {
                        flexboxLayout.addView(createTagView(tagsBean));
                    }
                }
                break;
            case TYPE_AUDIO:
                CustomAudioPlayer jvp_audio_play = holder.getView(R.id.jvp_audio_play);
                if (!TextUtils.isEmpty(detailObjectBean.getUrl())) {
                    jvp_audio_play.setVisibility(View.VISIBLE);
                    jvp_audio_play.setAudioData(getStrings(detailObjectBean.getUrl())
                            , detailObjectBean.getName(), detailObjectBean.getFrom());
                } else {
                    jvp_audio_play.setVisibility(View.GONE);
                }
                break;
            case TYPE_VIDEO:
                JzVideoPlayerStatusDialog jvp_video_play = holder.getView(R.id.jvp_video_play);
                if (!TextUtils.isEmpty(detailObjectBean.getUrl())) {
                    jvp_video_play.setVisibility(View.VISIBLE);
                    jvp_video_play.setUp(getStrings(detailObjectBean.getUrl()), JZVideoPlayer.SCREEN_WINDOW_NORMAL, "");
                    GlideImageLoaderUtil.loadCenterCrop(context, jvp_video_play.thumbImageView, getStrings(detailObjectBean.getPicUrl()));
                } else {
                    jvp_video_play.setVisibility(View.GONE);
                }
                break;
            case NORTEXT:
                String content = detailObjectBean.getContent();
                final EmojiconTextView tv_content_type = holder.getView(R.id.tv_content_type);
                tv_content_type.setPadding(0, 0, 0, 0);
                final RelativeLayout rel_communal_image = holder.getView(R.id.rel_communal_image);
                final ImageView iv_communal_image = holder.getView(R.id.iv_communal_image);
                if (!TextUtils.isEmpty(content)) {
                    //                正则匹配br样式
                    content = content.replaceAll(brStyleReg, br);
                    content = content.replaceAll(pTagStart, rTagStart);
                    content = content.replaceAll(pTagEnd, rTagEnd);
                    content = content.replaceAll(blankStyle, " ");

//                匹配图片地址
                    Matcher imgIsFind = Pattern.compile(IMG_REGEX_TAG).matcher(content);
                    isImageTag = imgIsFind.find();
                    if (isImageTag) {
                        //                    匹配网址
                        Matcher aMatcher = Pattern.compile(regexATextUrl).matcher(content);
                        boolean isImageUrl = aMatcher.find();
                        if (isImageUrl) {
                            //                    匹配网址
                            String tbUrlImgValue = "";
                            Matcher matcher = Pattern.compile(REGEX_TEXT).matcher(content);
                            while (matcher.find()) {
                                tbUrlImgValue = matcher.group();
                                if (matcher.find()) {
                                    content = matcher.group();
                                }
                            }
                            iv_communal_image.setTag(R.id.iv_two_tag, tbUrlImgValue);
                        } else {
                            String stringContent = imgIsFind.group();
                            Matcher matcher = Pattern.compile(REGEX_TEXT).matcher(stringContent);
                            while (matcher.find()) {
                                content = matcher.group();
                            }
                        }
                        rel_communal_image.setVisibility(View.VISIBLE);
                        iv_communal_image.setVisibility(View.VISIBLE);
                        tv_content_type.setVisibility(View.GONE);
                        iv_communal_image.setImageDrawable(context.getResources().getDrawable(R.drawable.load_loading_image));
                        iv_communal_image.setTag(R.id.iv_tag, content);
                        iv_communal_image.setOnClickListener(this);
                        GlideImageLoaderUtil.loadImgDynamicDrawable(context, iv_communal_image, content);
                    } else {
                        rel_communal_image.setVisibility(View.GONE);
                        tv_content_type.setVisibility(View.VISIBLE);
                        //                字体样式
                        int lineStyleIndex = 0;
                        int fontStyleTag = content.indexOf(lineStyle);
                        if (fontStyleTag != -1) {
                            try {
                                lineStyleIndex = content.indexOf(";", fontStyleTag) > 0 ?
                                        content.indexOf(";", fontStyleTag) :
                                        content.indexOf(lineFontEndStyle, fontStyleTag) < 0 ? fontStyleTag + 4
                                                : content.indexOf(lineFontEndStyle, fontStyleTag);
                                content = content.replaceAll(content.substring(fontStyleTag, lineStyleIndex), "");
                            } catch (Exception e) {
                                lineStyleIndex = -1;
                                e.printStackTrace();
                            }
                        }
                        tv_content_type.setGravity(Gravity.LEFT);

                        tv_content_type.setTextSize(TypedValue.COMPLEX_UNIT_PX,AutoSizeUtils.mm2px(context,30));
                        tv_content_type.setTextColor(context.getResources().getColor(R.color.home_text_color));
                        ViewGroup.LayoutParams layoutParams = tv_content_type.getLayoutParams();
                        tv_content_type.setText("");
                        if (content.contains(br)
                                && ((lineStyleIndex > 0 && !Pattern.compile("[\u4e00-\u9fa5]").matcher(content).find()))) {
                            layoutParams.height = AutoSizeUtils.mm2px(mAppContext, 12);
                            tv_content_type.setLayoutParams(layoutParams);
                        } else {
                            if (layoutParams.height != -2) {
                                layoutParams.height = -2;
                                tv_content_type.setLayoutParams(layoutParams);
                            }

                            //                匹配间距
                            if (detailObjectBean.getFirstLinePadding()) {
                                tv_content_type.setPadding(0, AutoSizeUtils.mm2px(mAppContext,10), 0, 0);
                            }
//                    匹配字体颜色
                            List<String> fontColorValue = getStyleValue(content, content.indexOf(text_color));
                            if (fontColorValue != null && fontColorValue.size() > 2) {
                                if (Integer.parseInt(fontColorValue.get(0)) == 255
                                        && Integer.parseInt(fontColorValue.get(1)) == 255
                                        && Integer.parseInt(fontColorValue.get(2)) == 255) {
                                } else {
                                    tv_content_type.setTextColor(Color.argb(255
                                            , Integer.parseInt(fontColorValue.get(0))
                                            , Integer.parseInt(fontColorValue.get(1))
                                            , Integer.parseInt(fontColorValue.get(2))));
                                }
                            }
//                    匹配显示位置
                            int alignGravityIndex = content.indexOf(alignGravity);
                            if (alignGravityIndex != -1 && !isImageTag) {
                                if (content.indexOf(endStyle, alignGravityIndex) != -1) {
                                    String locationGravity = content.substring(alignGravityIndex + alignGravity.length(), content.indexOf(endStyle, alignGravityIndex)).trim();
                                    if (locationGravity.equals(alignCenter)) {
                                        tv_content_type.setGravity(Gravity.CENTER);
                                    } else if (locationGravity.equals(alignRight)) {
                                        tv_content_type.setGravity(Gravity.RIGHT);
                                    } else {
                                        tv_content_type.setGravity(Gravity.LEFT);
                                    }
                                } else if (content.indexOf(endDash, alignGravityIndex) != -1) {
                                    String locationGravity = content.substring(alignGravityIndex + alignGravity.length(), content.indexOf(endDash, alignGravityIndex)).trim();
                                    if (locationGravity.equals(alignCenter)) {
                                        tv_content_type.setGravity(Gravity.CENTER);
                                    } else if (locationGravity.equals(alignRight)) {
                                        tv_content_type.setGravity(Gravity.RIGHT);
                                    } else {
                                        tv_content_type.setGravity(Gravity.LEFT);
                                    }
                                }
                            }
                        }
                        RichText.initCacheDir(context);
//                        硬件加速不能关闭，会对图片展示不兼容
//                        tv_content_type.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                        DrawableGetter drawableGetter = new DrawableGetter() {
                            @Override
                            public Drawable getDrawable(ImageHolder holder, RichTextConfig config, TextView textView) {
                                return context.getResources().getDrawable(R.drawable.load_loading_image);
                            }
                        };
                        RichText.from(content).autoFix(true).autoPlay(false).scaleType(ImageHolder.ScaleType.fit_center)
                                .placeHolder(drawableGetter)
                                .errorImage(drawableGetter).fix(new SimpleImageFixCallback() {
                            @Override
                            public void onInit(ImageHolder holder) {
                                super.onInit(holder);
                            }

                            @Override
                            public void onSizeReady(ImageHolder holder, int imageWidth, int imageHeight, ImageHolder.SizeHolder sizeHolder) {
                                int height = (int) (imageHeight * ((screenWidth * 1f) / imageWidth + 1));
                                holder.setWidth(screenWidth);
                                holder.setHeight(height);
                            }

                            @Override
                            public void onImageReady(ImageHolder holder, int width, int height) {
                                if (width >= 50) {
                                    int reallyHeight = (int) (height * ((screenWidth) * 1f / width) + 1);
                                    if (reallyHeight > 3072) {
                                        holder.setWidth((int) (width * (3072 * 1f / height) + 1));
                                        holder.setHeight(3072);
                                    } else {
                                        if (screenWidth != 0) {
                                            holder.setWidth(screenWidth);
                                            holder.setHeight(reallyHeight);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(ImageHolder holder, Exception e) {
                                holder.setSize(screenWidth, AutoSizeUtils.mm2px(mAppContext,400));
                                super.onFailure(holder, e);
                            }
                        })
                                .imageClick(new OnImageClickListener() {
                                    @Override
                                    public void imageClicked(List<String> imageUrls, int position) {
                                        String key = imageUrls.get(position);
                                        if (loadHud != null) {
                                            loadHud.show();
                                        }
                                        if (!TextUtils.isEmpty(urlMap.get(key))) {
                                            skipAliBCWebView(urlMap.get(key));
                                        } else {
                                            if (loadHud != null) {
                                                loadHud.dismiss();
                                            }
                                            ImageBean imageBean = null;
                                            List<ImageBean> imageBeanList = new ArrayList<>();
                                            for (String picUrl : imageUrls) {
                                                imageBean = new ImageBean();
                                                imageBean.setPicUrl(picUrl);
                                                imageBeanList.add(imageBean);
                                            }
                                            ImagePagerActivity.startImagePagerActivity(context, IMAGE_DEF, imageBeanList, position, null);
                                        }
                                    }
                                })
                                .urlClick(new OnUrlClickListener() {
                                    @Override
                                    public boolean urlClicked(String url) {
                                        setSkipPath(context, url, false);
                                        return true;
                                    }
                                }).into(tv_content_type);
                    }
                }
                break;
            case TYPE_SHARE:
                holder.addOnClickListener(R.id.tv_communal_share).setTag(R.id.tv_communal_share, detailObjectBean);
                break;
            default:
                break;
        }
    }

    private TextView createTagView(TagsBean tagsBean) {
        TextView textView = new TextView(context);
        textView.setPadding(AutoSizeUtils.mm2px(mAppContext,10), AutoSizeUtils.mm2px(mAppContext,10), AutoSizeUtils.mm2px(mAppContext,10), AutoSizeUtils.mm2px(mAppContext,10));
        textView.setBackground(context.getResources().getDrawable(R.drawable.border_cir_1dp5_s_ff));
        textView.setTextColor(context.getResources().getColor(R.color.text_login_gray_s));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoSizeUtils.mm2px(mAppContext,24));
        textView.setText(getStrings(tagsBean.getTag_name()));
        textView.setTag(tagsBean);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setOnClickListener((v) -> {
            TagsBean tag = (TagsBean) v.getTag();
            if (tag != null) {
                Intent intent = new Intent(context, FindTagDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("tagId", String.valueOf(tagsBean.getTag_id()));
                context.startActivity(intent);
            }
        });
        return textView;
    }


    public int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.top) + 2;
    }

    /**
     * 匹配颜色样式
     *
     * @param content
     * @param lineHeightIndex
     * @return
     */
    private List<String> getStyleValue(String content, int lineHeightIndex) {
        if (lineHeightIndex != -1 && !isImageTag) {
            if (content.indexOf(endStyle, lineHeightIndex) == -1) {
                if (content.indexOf(endDash, lineHeightIndex) != -1) {
                    return getNumber(content.substring(lineHeightIndex, content.indexOf(endDash, lineHeightIndex)));
                } else {
                    return null;
                }
            } else {
                return getNumber(content.substring(lineHeightIndex, content.indexOf(endDash, lineHeightIndex)));
            }
        }
        return null;
    }

    public void skipAliBCWebView(final String url) {
        if (!TextUtils.isEmpty(url)) {
            if (uid != 0) {
                skipNewTaoBao(url);
//                skipOldTaoBao(url);
            } else {
                if (loadHud != null) {
                    loadHud.dismiss();
                }
                getLoginStatus();
            }
        } else {
            showToast(context, "地址缺失");
            if (loadHud != null) {
                loadHud.dismiss();
            }
        }
    }

    private void skipNewTaoBao(final String url) {
        if (url.contains(TAOBAO_URL)) {
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
                    showToast(context, "登录失败 ");
                }
            });
        } else {
            if (loadHud != null) {
                loadHud.dismiss();
            }
//                     网页地址
            Intent intent = new Intent();
            intent.setClass(context, DoMoLifeCommunalActivity.class);
            intent.putExtra("loadUrl", url);
            context.startActivity(intent);
        }
    }

    private void skipNewShopDetails(String url) {
        //提供给三方传递配置参数
        Map<String, String> exParams = new HashMap<>();
        exParams.put(AlibcConstants.ISV_CODE, "appisvcode");
        //设置页面打开方式
        AlibcShowParams showParams = new AlibcShowParams(OpenType.Native, false);
//        AlibcTradeSDK.setTaokeParams(AlibcTaokeParams taokeParams)
//        String pidPrefix = AlibcTaokeParams.PID_PREFIX;
//        mm_113346569_13808180_75190827
//        AlibcTaokeParams taokeParams = new AlibcTaokeParams("mm_113346569_13808180_75190827", null, null);
        //实例化商品详情 itemID打开page
        AlibcBasePage ordersPage = new AlibcPage(url);
        AlibcTrade.show((Activity) context, ordersPage, showParams, null, exParams, new AlibcTradeCallback() {
            @Override
            public void onTradeSuccess(AlibcTradeResult alibcTradeResult) {
                //打开电商组件，用户操作中成功信息回调。tradeResult：成功信息（结果类型：加购，支付；支付结果）
            }

            @Override
            public void onFailure(int code, String msg) {
                //打开电商组件，用户操作中错误信息回调。code：错误码；msg：错误信息
            }
        });
    }

    private void getLoginStatus() {
        SavePersonalInfoBean personalInfo = ConstantMethod.getPersonalInfo(context);
        if (personalInfo.isLogin()) {
            uid = personalInfo.getUid();
        } else {
            Intent intent = new Intent(context, MineLoginActivity.class);
            ((Activity) context).startActivityForResult(intent, IS_LOGIN_CODE);
        }
    }

    private List<String> getNumber(String str) {
        List<String> numberList = new ArrayList<>();
        String regex = "[0-9]\\d*\\.?\\d*";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        boolean isAgain = m.find();
        while (isAgain) {
            if (!TextUtils.isEmpty(m.group())) {
                numberList.add(m.group());
            }
            isAgain = m.find();
        }
        return numberList;
    }

    @Override
    public void onClick(View v) {
        String imageUrl = (String) v.getTag(R.id.iv_tag);
        String imageUrlUrl = (String) v.getTag(R.id.iv_two_tag);
        if (!TextUtils.isEmpty(imageUrlUrl)) {
            setSkipPath(context, imageUrlUrl, false);
        } else if (!TextUtils.isEmpty(imageUrl)) {
            List<ImageBean> imageBeanList = new ArrayList<>();
            ImageBean imageBean = new ImageBean();
            imageBean.setPicUrl(imageUrl);
            imageBeanList.add(imageBean);
            ImagePagerActivity.startImagePagerActivity(context, IMAGE_DEF, imageBeanList, 0, null);
        }
    }

    public class CommunalHolderHelper extends BaseViewHolder {
        RecyclerView communal_recycler_wrap;

        public CommunalHolderHelper(View view) {
            super(view);
            communal_recycler_wrap = view.findViewById(R.id.communal_recycler_wrap);
            if (communal_recycler_wrap != null) {
                communal_recycler_wrap.setNestedScrollingEnabled(false);
            }
        }
    }
}
