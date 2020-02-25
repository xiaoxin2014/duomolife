package com.amkj.dmsh.dominant.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.ProductLabelCreateUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getSpannableString;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;
import static com.amkj.dmsh.constant.ConstantMethod.skipGroupDetail;
import static com.amkj.dmsh.constant.ConstantMethod.skipProductUrl;
import static com.amkj.dmsh.constant.ConstantVariable.AD_COVER;
import static com.amkj.dmsh.constant.ConstantVariable.PRODUCT;
import static com.amkj.dmsh.constant.ConstantVariable.TITLE;

/**
 * Created by xiaoxin on 2019/4/20 0020
 * Version:v4.0.0
 * ClassDescription :一行三列商品通用适配器
 */
public class CatergoryGoodsAdapter extends BaseMultiItemQuickAdapter<LikedProductBean, BaseViewHolder> {

    private final Context context;
    private final boolean isRichText;

    public CatergoryGoodsAdapter(Context context, @Nullable List<LikedProductBean> data, boolean isRichText) {
        super(data);
        addItemType(PRODUCT, R.layout.item_commual_goods_3x);//普通商品
        addItemType(TITLE, R.layout.adapter_new_user_header);//头部标题栏(新人专区)
        addItemType(AD_COVER, R.layout.item_commual_cover_pic_3x);//封面图片或者图片商品
        this.context = context;
        this.isRichText = isRichText;
    }

    public CatergoryGoodsAdapter(Context context, @Nullable List<LikedProductBean> data) {
        this(context, data, false);
    }

    @Override
    protected void convert(BaseViewHolder helper, LikedProductBean likedProductBean) {
        if (likedProductBean == null) return;
        switch (helper.getItemViewType()) {
            case PRODUCT:
                String economizeNum = getStringsFormat(context, R.string.economize_money, getStrings(likedProductBean.getDecreasePrice()));
                GlideImageLoaderUtil.loadSquareImg(context, helper.getView(R.id.iv_goods_pic), likedProductBean.getPicUrl(), likedProductBean.getWaterRemark(), AutoSizeUtils.mm2px(mAppContext, 236));
                helper.setGone(R.id.iv_com_pro_tag_out, likedProductBean.getQuantity() < 1)
                        .setText(R.id.tv_price, ConstantMethod.getRmbFormat(context, likedProductBean.getPrice()))
                        .setText(R.id.tv_name, getStrings(likedProductBean.getName()))
                        .setGone(R.id.tv_economize_money, !TextUtils.isEmpty(likedProductBean.getDecreasePrice()))
                        .setText(R.id.tv_economize_money, getSpannableString(economizeNum, 1, economizeNum.length() - 1, 0, "#ff5e6b"));

                FlexboxLayout fbl_label = helper.getView(R.id.fbl_market_label);
                if (!TextUtils.isEmpty(likedProductBean.getActivityTag()) || (likedProductBean.getMarketLabelList() != null
                        && likedProductBean.getMarketLabelList().size() > 0)) {
                    fbl_label.setVisibility(View.VISIBLE);
                    fbl_label.removeAllViews();
                    if (!TextUtils.isEmpty(likedProductBean.getActivityTag())) {
                        fbl_label.addView(ProductLabelCreateUtils.createLabelText(context, likedProductBean.getActivityTag(), 1));
                    }
                    if (likedProductBean.getMarketLabelList() != null
                            && likedProductBean.getMarketLabelList().size() > 0) {
                        for (LikedProductBean.MarketLabelBean marketLabelBean : likedProductBean.getMarketLabelList()) {
                            if (!TextUtils.isEmpty(marketLabelBean.getTitle())) {
                                fbl_label.addView(ProductLabelCreateUtils.createLabelText(context, marketLabelBean.getTitle(), 0));
                            }
                        }
                    }
                } else {
                    fbl_label.setVisibility(View.GONE);
                }
                helper.itemView.setTag(likedProductBean);
                break;
            case TITLE:
                ImageView imageView = helper.getView(R.id.iv_type);
                imageView.setImageResource(likedProductBean.getTitleHead());
                break;
            case AD_COVER:
                ImageView iv_quality_good_product_ad = helper.getView(R.id.iv_quality_good_product_ad);
                GlideImageLoaderUtil.loadCenterCrop(context, iv_quality_good_product_ad, getStrings(likedProductBean.getPicUrl()));
                helper.itemView.setTag(R.id.iv_tag, likedProductBean);
                break;
        }
        if (!isRichText) {
            helper.itemView.setOnClickListener(view -> {
                if (helper.getItemViewType() != ConstantVariable.TITLE) {
                    if (!TextUtils.isEmpty(likedProductBean.getGpInfoId())) {//富文本不需要判断是否是拼团商品
                        skipGroupDetail(context, likedProductBean.getGpInfoId(), likedProductBean.getId());
                    } else {
                        skipProductUrl(context, 1, likedProductBean.getId(), likedProductBean.getAndroidLink());
                    }
                }
            });
        }
    }
}
