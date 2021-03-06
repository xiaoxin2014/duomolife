package com.amkj.dmsh.dominant.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.user.bean.LikedProductBean;
import com.amkj.dmsh.user.bean.MarketLabelBean;
import com.amkj.dmsh.utils.ProductLabelCreateUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import androidx.annotation.Nullable;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;
import static com.amkj.dmsh.constant.ConstantMethod.isVip;
import static com.amkj.dmsh.constant.ConstantMethod.skipGroupDetail;
import static com.amkj.dmsh.constant.ConstantMethod.skipProductUrl;
import static com.amkj.dmsh.constant.ConstantVariable.PICTURE;
import static com.amkj.dmsh.constant.ConstantVariable.PRODUCT;
import static com.amkj.dmsh.constant.ConstantVariable.TITLE;

/**
 * Created by xiaoxin on 2019/4/20 0020
 * Version:v4.0.0
 * ClassDescription :一行三列商品通用适配器
 */
public class CatergoryGoodsAdapter extends BaseMultiItemQuickAdapter<LikedProductBean, BaseViewHolder> {

    private final Context context;
    private boolean isRichText;


    public CatergoryGoodsAdapter(Context context, @Nullable List<LikedProductBean> data) {
        this(context, data, false);
    }

    public CatergoryGoodsAdapter(Context context, @Nullable List<LikedProductBean> data, boolean isRichText) {
        super(data);
        addItemType(PRODUCT, R.layout.item_commual_goods_3x);//普通商品
        addItemType(TITLE, R.layout.adapter_new_user_header);//头部标题栏(新人专区)
        addItemType(PICTURE, R.layout.item_commual_cover_pic_3x);//封面图片或者图片商品
        this.context = context;
        this.isRichText = isRichText;
    }

    @Override
    protected void convert(BaseViewHolder helper, LikedProductBean likedProductBean) {
        if (likedProductBean == null) return;
        switch (helper.getItemViewType()) {
            case PRODUCT:
                GlideImageLoaderUtil.loadSquareImg(context, helper.getView(R.id.iv_goods_pic), likedProductBean.getPicUrl(), likedProductBean.getWaterRemark(), AutoSizeUtils.mm2px(mAppContext, 236));
                helper.setGone(R.id.iv_com_pro_tag_out, likedProductBean.getQuantity() < 1)
                        .setText(R.id.tv_name, getStrings(likedProductBean.getName()));

                //商品价格
                TextView tvPrice = helper.getView(R.id.tv_price);
                ImageView ivLogoFront = helper.getView(R.id.iv_vip_logo_front);
                String activityCode = likedProductBean.getActivityCode();
                if (!TextUtils.isEmpty(activityCode) && activityCode.contains("XSG")) {
                    tvPrice.setText(getStringsChNPrice(context, likedProductBean.getPrice()));
                    ivLogoFront.setVisibility(View.GONE);
                } else {
                    if (isVip() && !TextUtils.isEmpty(likedProductBean.getVipPrice())) {
                        tvPrice.setText(getStringsChNPrice(context, likedProductBean.getVipPrice()));
                        ivLogoFront.setVisibility(View.VISIBLE);
                    } else {
                        tvPrice.setText(getStringsChNPrice(context, likedProductBean.getPrice()));
                        ivLogoFront.setVisibility(View.GONE);
                    }
                }

                FlexboxLayout fbl_market_label = helper.getView(R.id.fbl_market_label);
                fbl_market_label.removeAllViews();

                //活动标签（仅有一个）
                if (!TextUtils.isEmpty(likedProductBean.getActivityTag())) {
                    fbl_market_label.addView(ProductLabelCreateUtils.createLabelText(context, likedProductBean.getActivityTag(), 1));
                }

                //会员标签
                if (!TextUtils.isEmpty(likedProductBean.getVipTag())) {
                    fbl_market_label.addView(ProductLabelCreateUtils.createLabelText(context, likedProductBean.getVipTag(), 0));
                }

                //营销标签(可以有多个)
                if (likedProductBean.getMarketLabelList() != null && likedProductBean.getMarketLabelList().size() > 0) {
                    for (MarketLabelBean marketLabelBean : likedProductBean.getMarketLabelList()) {
                        if (!TextUtils.isEmpty(marketLabelBean.getTitle())) {
                            fbl_market_label.addView(ProductLabelCreateUtils.createLabelText(context, marketLabelBean.getTitle(), 0));
                        }
                    }
                }
                fbl_market_label.setVisibility(fbl_market_label.getChildCount() > 0 ? View.VISIBLE : View.GONE);
                helper.itemView.setTag(likedProductBean);
                break;
            case TITLE:
                ImageView imageView = helper.getView(R.id.iv_type);
                imageView.setImageResource(likedProductBean.getTitleHead());
                break;
            case PICTURE:
                ImageView iv_quality_good_product_ad = helper.getView(R.id.iv_quality_good_product_ad);
                GlideImageLoaderUtil.loadCenterCrop(context, iv_quality_good_product_ad, getStrings(likedProductBean.getPicUrl()));
                helper.itemView.setTag(R.id.iv_tag, likedProductBean);
                break;
        }
        if (!isRichText) {
            helper.itemView.setOnClickListener(view -> {
                if (helper.getItemViewType() != ConstantVariable.TITLE) {
                    if (!TextUtils.isEmpty(likedProductBean.getGpInfoId())) {
                        skipGroupDetail(context, likedProductBean.getGpInfoId());
                    } else {
                        skipProductUrl(context, 1, likedProductBean.getId(), likedProductBean.getAndroidLink());
                    }
                }
            });
        }
    }
}
