package com.amkj.dmsh.homepage.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.text.emoji.widget.EmojiTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.base.TinkerBaseApplicationLike;
import com.amkj.dmsh.bean.ImageBean;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.dominant.activity.DmlOptimizedSelDetailActivity;
import com.amkj.dmsh.dominant.activity.DoMoLifeWelfareDetailsActivity;
import com.amkj.dmsh.dominant.adapter.CatergoryGoodsAdapter;
import com.amkj.dmsh.dominant.adapter.GoodProductAdapter;
import com.amkj.dmsh.find.activity.ImagePagerActivity;
import com.amkj.dmsh.find.bean.InvitationImgDetailEntity.InvitationImgDetailBean.TagsBean;
import com.amkj.dmsh.homepage.bean.InvitationDetailEntity;
import com.amkj.dmsh.message.activity.OfficialNotifyDetailsActivity;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean;
import com.amkj.dmsh.user.adapter.InvitationProAdapter;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.ProductLabelCreateUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.amkj.dmsh.utils.itemdecoration.ItemDecoration;
import com.amkj.dmsh.views.JzVideo.CustomAudioPlayer;
import com.amkj.dmsh.views.JzVideo.JzVideoPlayerStatus;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.tencent.bugly.beta.tinker.TinkerManager;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.LinkHolder;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.RichTextConfig;
import com.zzhoujay.richtext.callback.DrawableGetter;
import com.zzhoujay.richtext.callback.LinkFixCallback;
import com.zzhoujay.richtext.callback.OnImageClickListener;
import com.zzhoujay.richtext.callback.OnUrlClickListener;
import com.zzhoujay.richtext.callback.SimpleImageFixCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jzvd.Jzvd;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;
import static com.amkj.dmsh.constant.ConstantMethod.setSkipPath;
import static com.amkj.dmsh.constant.ConstantMethod.showImageActivity;
import static com.amkj.dmsh.constant.ConstantVariable.IMG_REGEX_TAG;
import static com.amkj.dmsh.constant.ConstantVariable.PRODUCT;
import static com.amkj.dmsh.constant.ConstantVariable.REGEX_NUM;
import static com.amkj.dmsh.constant.ConstantVariable.REGEX_TEXT;
import static com.amkj.dmsh.constant.ConstantVariable.regexATextUrl;
import static com.amkj.dmsh.dao.AddClickDao.totalOfficialProNum;
import static com.amkj.dmsh.dao.AddClickDao.totalProNum;
import static com.amkj.dmsh.dao.AddClickDao.totalWelfareProNum;
import static com.amkj.dmsh.find.activity.ImagePagerActivity.IMAGE_DEF;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.NORTEXT;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_AUDIO;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_COUPON;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_COUPON_PACKAGE;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_EMPTY_OBJECT;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_GIF_IMG;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_GOODS_2X;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_GOODS_3X;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_GOODS_IMG;
import static com.amkj.dmsh.shopdetails.bean.CommunalDetailObjectBean.TYPE_GOODS_IMG_DIRECT_BUY;
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
import static com.amkj.dmsh.utils.glide.GlideImageLoaderUtil.getOpenglRenderLimitValue;
import static me.jessyan.autosize.utils.AutoSizeUtils.dp2px;


/**
 * Created by atd48 on 2016/8/30.
 */
public class CommunalDetailAdapter extends BaseMultiItemQuickAdapter<CommunalDetailObjectBean, CommunalDetailAdapter.CommunalHolderHelper> implements View.OnClickListener {
    private final Activity context;
    private final int screenWidth;
    private final KProgressHUD loadHud;
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
    //    透明色
    private String transparent = "transparent";
    //    末尾结束符
    private String endStyle = ";";
    private String endDash = "\"";
    private Map<String, String> urlMap;
    private int uid;
    //    是否是图片
    private boolean isImageTag;
    //图片集合
    List<String> imgList = new ArrayList<>();

    public CommunalDetailAdapter(Activity context, List<CommunalDetailObjectBean> descriptionBeanList) {
        super(descriptionBeanList);
//        正常文本
        addItemType(NORTEXT, R.layout.layout_article_details);
//        淘宝优惠券
        addItemType(TYPE_LUCKY_MONEY, R.layout.adapter_article_details_item_tb_coupon);
//        淘宝链接
        addItemType(TYPE_LINK_TAOBAO, R.layout.layout_communal_taobao_link);
//        自营商品优惠券
        addItemType(TYPE_COUPON, R.layout.adapter_article_details_item_direct_coupon);
//        自营商品优惠券礼包
        addItemType(TYPE_COUPON_PACKAGE, R.layout.adapter_article_details_item_direct_coupon);
//        带有添加购物车商品(插入一个商品)
        addItemType(TYPE_GOODS_WEL, R.layout.adapter_border_pro_wel);
//        图片商品(插入一个商品并上传图片)
        addItemType(TYPE_GOODS_IMG, R.layout.layout_communal_cover_wrap);
//        带有立即购买的图片商品（(插入一个商品并上传图片，小编精选专属）
        addItemType(TYPE_GOODS_IMG_DIRECT_BUY, R.layout.layout_communal_cover_direct_buy);
//        动态图片处理
        addItemType(TYPE_GIF_IMG, R.layout.layout_communal_img_gif);
//        视频播放
        addItemType(TYPE_VIDEO, R.layout.layout_article_video);
//        音频播放
        addItemType(TYPE_AUDIO, R.layout.layout_article_audio);
//        分享页面
        addItemType(TYPE_SHARE, R.layout.layout_communal_share_view);
//        详情头部
        addItemType(TYPE_PRODUCT_TITLE, R.layout.layout_communal_detail_title);
//        查看更多
        addItemType(TYPE_PRODUCT_MORE, R.layout.layout_communal_detail_more);
//        标签
        addItemType(TYPE_PRODUCT_TAG, R.layout.layout_communal_flexbox);
//        商品推荐
        addItemType(TYPE_PRODUCT_RECOMMEND, R.layout.layout_top_communal_wrap);
//        关联商品
        addItemType(TYPE_RELEVANCE_PRODUCT, R.layout.layout_top_communal_wrap);
//        占位
        addItemType(TYPE_EMPTY_OBJECT, R.layout.layout_empty_object);
//        限时特惠商品详情头部
        addItemType(TYPE_PROMOTION_TITLE, R.layout.layout_promotion_product_details_header_title);
//        并列商品2X(包含并列图片)
        addItemType(TYPE_GOODS_2X, R.layout.layout_communal_recycler_wrap);
//        并列商品3X(包含并列图片)
        addItemType(TYPE_GOODS_3X, R.layout.layout_communal_recycler_wrap);
        this.context = context;
        urlMap = new HashMap<>();
        TinkerBaseApplicationLike app = (TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike();
        screenWidth = app.getScreenWidth();
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
                        : detailObjectBean.getNewPirUrl(), -1);
                holder.setTag(R.id.img_product_coupon_pic, R.id.iv_avatar_tag, detailObjectBean.getId())
                        .setTag(R.id.img_product_coupon_pic, R.id.iv_type_tag, detailObjectBean.getItemType())
                        .addOnClickListener(R.id.img_product_coupon_pic);
                break;
            case TYPE_GOODS_WEL:
                GlideImageLoaderUtil.loadHeaderImg(context, (ImageView) holder.getView(R.id.iv_ql_bl_product), detailObjectBean.getPicUrl());
                holder.setText(R.id.tv_ql_bl_pro_name, getStrings(detailObjectBean.getName()))
                        .setText(R.id.tv_ql_bl_product_recommend, "")
                        .setText(R.id.tv_ql_bl_pro_price, "¥ " + detailObjectBean.getPrice())
                        .setGone(R.id.iv_ql_bl_add_car, detailObjectBean.getItemTypeId() == 1)
                        .setGone(R.id.iv_com_pro_tag_out, detailObjectBean.getQuantity() < 1)
                        .addOnClickListener(R.id.iv_ql_bl_add_car).setTag(R.id.iv_ql_bl_add_car, detailObjectBean);
                holder.itemView.setOnClickListener(v -> setGoodsClick(detailObjectBean.getItemTypeId(),
                        detailObjectBean.getId(), detailObjectBean.getAndroidLink(), detailObjectBean.isSelfGoods()));
                break;
            case TYPE_GOODS_IMG:
            case TYPE_GOODS_IMG_DIRECT_BUY:
                final ImageView iv_communal_cover_wrap = holder.getView(R.id.iv_communal_cover_wrap);
                GlideImageLoaderUtil.loadImgDynamicDrawable(context, iv_communal_cover_wrap, detailObjectBean.getPicUrl(), -1);
                if (!imgList.contains(detailObjectBean.getPicUrl()) && !TextUtils.isEmpty(detailObjectBean.getPicUrl())) {
                    imgList.add(detailObjectBean.getPicUrl());
                }
                holder.itemView.setOnClickListener(v -> setGoodsClick(detailObjectBean.getItemTypeId(),
                        detailObjectBean.getId(), detailObjectBean.getAndroidLink(), detailObjectBean.isSelfGoods()));
                break;
            case TYPE_GOODS_2X:
            case TYPE_GOODS_3X:
                holder.communal_recycler_wrap.setLayoutManager(new GridLayoutManager(context, holder.getItemViewType() == TYPE_GOODS_2X ? 2 : 3));
                holder.communal_recycler_wrap.setNestedScrollingEnabled(false);
                if (detailObjectBean.getGoodsList().size() > 0) {
                    int type = detailObjectBean.getGoodsList().get(0).getItemType();
                    if (type == PRODUCT) {
                        if (holder.communal_recycler_wrap.getItemDecorationCount() < 1) {
                            holder.communal_recycler_wrap.addItemDecoration(new ItemDecoration.Builder()
                                    // 设置分隔线资源ID
                                    .setDividerId(R.drawable.item_divider_five_gray_f).create());
                        }
                        if (holder.communal_recycler_wrap.getPaddingBottom() < 1) {
                            holder.communal_recycler_wrap.setBackgroundColor(context.getResources().getColor(R.color.light_gray_f));
                            holder.communal_recycler_wrap.setPadding(0, 0, 0, dp2px(context, 5));
                        }
                    } else {
                        //并列图片不需要分割线
                        if (holder.communal_recycler_wrap.getItemDecorationCount() > 0) {
                            holder.communal_recycler_wrap.removeItemDecorationAt(0);
                        }
                        if (holder.communal_recycler_wrap.getPaddingBottom() > 0) {
                            holder.communal_recycler_wrap.setPadding(0, 0, 0, 0);
                        }
                    }
                }

                BaseQuickAdapter communalGoodListAdapter = null;
                if (holder.getItemViewType() == TYPE_GOODS_2X) {
                    communalGoodListAdapter = new GoodProductAdapter(context, detailObjectBean.getGoodsList(), true);
                    holder.communal_recycler_wrap.setAdapter(communalGoodListAdapter);
                } else if (holder.getItemViewType() == TYPE_GOODS_3X) {
                    communalGoodListAdapter = new CatergoryGoodsAdapter((context), detailObjectBean.getGoodsList(), true);
                    holder.communal_recycler_wrap.setAdapter(communalGoodListAdapter);
                }

                communalGoodListAdapter.setOnItemClickListener((adapter, view, position) -> {
                    LikedProductBean likedProductBean = (LikedProductBean) (view instanceof ImageView ? view.getTag(R.id.iv_tag) : view.getTag());
                    setGoodsClick(likedProductBean.getType_id(),
                            likedProductBean.getId(), likedProductBean.getAndroidLink(), "selfGoods".equals(likedProductBean.getType()));
                });

                break;
            case TYPE_PRODUCT_RECOMMEND:
                GoodProductAdapter goodProductAdapter = new GoodProductAdapter(context, detailObjectBean.getProductList());
                holder.communal_recycler_wrap.setLayoutManager(new GridLayoutManager(context, 2));
                if (holder.communal_recycler_wrap.getItemDecorationCount() < 1) {
                    holder.communal_recycler_wrap.addItemDecoration(new ItemDecoration.Builder()
                            // 设置分隔线资源ID
                            .setDividerId(R.drawable.item_divider_five_dp).create());
                }
                holder.communal_recycler_wrap.setAdapter(goodProductAdapter);
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
//                            //记录埋点参数sourceId
//                            ConstantMethod.saveSourceId(getClass().getSimpleName(), String.valueOf(relevanceProBean.getProductId()));
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
                tv_tb_coupon_price.setText(getStringsChNPrice(context, detailObjectBean.getName()));
                Link link = new Link(Pattern.compile(REGEX_NUM));
                link.setTextColor(context.getResources().getColor(R.color.white))
                        .setUnderlined(false)
                        .setHighlightAlpha(0f)
                        .setTextSize(AutoSizeUtils.mm2px(mAppContext, 74))
                        .setOnClickListener(null);
                LinkBuilder.on(tv_tb_coupon_price)
                        .addLink(link)
                        .build();
                holder.setTag(R.id.ll_layout_tb_coupon, detailObjectBean)
                        .setGone(R.id.tv_last_tb_coupon_hint, detailObjectBean.isLastTbCoupon())
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
                flexboxLayout.setShowDivider(FlexboxLayout.SHOW_DIVIDER_MIDDLE);
                flexboxLayout.setDividerDrawable(context.getResources().getDrawable(R.drawable.item_divider_nine_dp_white));
                flexboxLayout.removeAllViews();
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) flexboxLayout.getLayoutParams();
                marginLayoutParams.setMargins((int) context.getResources().getDimension(R.dimen.gap_left), 0, 0, 0);
                flexboxLayout.setLayoutParams(marginLayoutParams);
                List<TagsBean> tagsBeans = detailObjectBean.getTagsBeans();
                for (int i = 0; i < tagsBeans.size(); i++) {
                    if (i == 0) {
                        flexboxLayout.addView(ProductLabelCreateUtils.createArticleIcon(context));
                    }
                    TagsBean tagsBean = tagsBeans.get(i);
                    if (!TextUtils.isEmpty(tagsBean.getTag_name())) {
                        flexboxLayout.addView(ProductLabelCreateUtils.createArticleClickTag(context, tagsBean));
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
                JzVideoPlayerStatus jvp_video_play = holder.getView(R.id.jvp_video_play);
                if (!TextUtils.isEmpty(detailObjectBean.getUrl())) {
                    jvp_video_play.setVisibility(View.VISIBLE);
                    jvp_video_play.setUp(getStrings(detailObjectBean.getUrl()), "", Jzvd.SCREEN_WINDOW_NORMAL);
                    GlideImageLoaderUtil.loadCenterCrop(context, jvp_video_play.thumbImageView, getStrings(detailObjectBean.getPicUrl()));
                } else {
                    jvp_video_play.setVisibility(View.GONE);
                }
                break;
            case NORTEXT:
                String content = detailObjectBean.getContent();
                final EmojiTextView tv_content_type = holder.getView(R.id.tv_content_type);
                tv_content_type.setPadding(0, 0, 0, 0);
                final RelativeLayout rel_communal_image = holder.getView(R.id.rel_communal_image);
                final ImageView iv_communal_image = holder.getView(R.id.iv_communal_image);
                if (!TextUtils.isEmpty(content)) {
                    //                正则匹配br样式
                    content = content.replaceAll(brStyleReg, br);
                    content = content.replaceAll(pTagStart, rTagStart);
                    content = content.replaceAll(pTagEnd, rTagEnd);
                    content = content.replaceAll(blankStyle, " ");
                    //richtext暂不支持transparent,使用透明色代替
                    content = content.replaceAll(transparent, "#00000000");

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

                        //帖子详情图片间距处理
                        if (detailObjectBean.isPost()) {
                            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv_communal_image.getLayoutParams();
                            layoutParams.setMargins(0, 0, 0, AutoSizeUtils.mm2px(mAppContext, 20));
                            iv_communal_image.setLayoutParams(layoutParams);
                        }
                        rel_communal_image.setVisibility(View.VISIBLE);
                        iv_communal_image.setVisibility(View.VISIBLE);
                        tv_content_type.setVisibility(View.GONE);
                        iv_communal_image.setImageDrawable(context.getResources().getDrawable(R.drawable.load_loading_image));
                        iv_communal_image.setTag(R.id.iv_tag, content);
                        iv_communal_image.setOnClickListener(this);
                        //判断content是不是正确的图片地址
                        boolean isPic = content.startsWith("http") && (content.contains("gif") || content.contains("jpg") || content.contains("jpeg") ||
                                content.contains("png") || content.contains("GIF") || content.contains("JPG") || content.contains("PNG") || content.contains("gif"));
                        String tag = (String) iv_communal_image.getTag(isPic ? R.id.iv_tag : R.id.iv_two_tag);
                        if (!imgList.contains(tag) && !TextUtils.isEmpty(tag)) imgList.add(tag);
                        GlideImageLoaderUtil.loadImgDynamicDrawable(context, iv_communal_image, tag, -1);
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
                                tv_content_type.setPadding(0, AutoSizeUtils.mm2px(mAppContext, 10), 0, 0);
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

                        //帖子详情内容间距，颜色以及超链接处理
                        if (detailObjectBean.isPost()) {
                            FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) tv_content_type.getLayoutParams();
                            layoutParams1.setMargins(0, 0, 0, 0);
                            tv_content_type.setTextColor(context.getResources().getColor(R.color.text_black_t));
                            tv_content_type.setLineSpacing(AutoSizeUtils.mm2px(mAppContext, 12), 1.0f);
                            tv_content_type.setLayoutParams(layoutParams1);
                        }

                        //小编精选字体颜色处理
                        if (detailObjectBean.isEditor()) {
                            tv_content_type.setTextColor(context.getResources().getColor(R.color.text_login_gray_s));
                            tv_content_type.setTextSize(14);
                            tv_content_type.setLineSpacing(AutoSizeUtils.mm2px(mAppContext, 12), 1.0f);
                        }
                        RichText.initCacheDir(context);
                        //硬件加速不能关闭，会对图片展示不兼容
                        tv_content_type.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
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
                            public void onSizeReady(ImageHolder holder, int imgWidth, int imgHeight, ImageHolder.SizeHolder sizeHolder) {
                                //图片下载完成（未加载到内存）
                                try {
                                    int screenWidth = ((TinkerBaseApplicationLike) TinkerManager.getTinkerApplicationLike()).getScreenWidth();
                                    if (imgWidth > 0 && imgHeight > 0) {
                                        //判断图片宽度是否大于屏幕宽度
                                        if (imgWidth > screenWidth) {
                                            int height = screenWidth * imgHeight / imgWidth;
                                            sizeHolder.setSize(screenWidth, height);
                                        }

                                        //判断缩放后的高度是否大于限制高度
                                        int limitHeight = getOpenglRenderLimitValue();
                                        if (imgHeight > limitHeight) {
                                            sizeHolder.setSize((int) (limitHeight * 1.0f / (imgHeight * 1.0f) * imgWidth), limitHeight);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onImageReady(ImageHolder holder, int width, int height) {
                                //图片已加载到内存
                            }

                            @Override
                            public void onFailure(ImageHolder holder, Exception e) {
                                holder.setSize(screenWidth, AutoSizeUtils.mm2px(mAppContext, 400));
                                super.onFailure(holder, e);
                            }
                        }).imageClick(new OnImageClickListener() {
                            @Override
                            public void imageClicked(List<String> imageUrls, int position) {
                                String key = imageUrls.get(position);
                                if (loadHud != null) {
                                    loadHud.show();
                                }
                                if (!TextUtils.isEmpty(urlMap.get(key))) {
                                    setSkipPath(context, urlMap.get(key), false);
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
                        }).urlClick(new OnUrlClickListener() {
                            @Override
                            public boolean urlClicked(String url) {
                                setSkipPath(context, url, false);
                                return true;
                            }
                        }).linkFix(new LinkFixCallback() {
                            @Override
                            public void fix(LinkHolder holder) {
                                holder.setColor(0xff0a88fa);
                                holder.setUnderLine(false);
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

    //富文本商品点击事件
    private void setGoodsClick(int itemTypeId, int id, String androidLink, boolean isSelfGoods) {
        ConstantMethod.skipProductUrl(context, itemTypeId, id, androidLink);
        if (TextUtils.isEmpty(androidLink) || isSelfGoods) {
            //统计商品点击
            if (context instanceof OfficialNotifyDetailsActivity) {
                totalOfficialProNum(context, id, ((OfficialNotifyDetailsActivity) context).getNotifyId());
            } else if (context instanceof DmlOptimizedSelDetailActivity) {
                totalProNum(context, id, ((DmlOptimizedSelDetailActivity) context).getOptimizedId());
            } else if (context instanceof DoMoLifeWelfareDetailsActivity) {
                totalWelfareProNum(context, id, ((DoMoLifeWelfareDetailsActivity) context).getWelfareId());
            }
        }
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
            if (imgList.contains(imageUrl)) {
                for (int i = 0; i < imgList.size(); i++) {
                    if (imageUrl.equals(imgList.get(i))) {
                        showImageActivity(context, IMAGE_DEF, i, imgList);
                        break;
                    }
                }
            }
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
