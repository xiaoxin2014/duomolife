package com.amkj.dmsh.dominant.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.ProductLabelCreateUtils;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getSpannableString;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsFormat;
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

    public CatergoryGoodsAdapter(Context context, @Nullable List<LikedProductBean> data) {
        super(data);
        addItemType(PRODUCT, R.layout.item_commual_goods_3x);//普通商品
        addItemType(TITLE, R.layout.adapter_new_user_header);//头部标题栏(新人专区)
        addItemType(AD_COVER, R.layout.item_commual_cover_pic_3x);//封面图片或者图片商品
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, LikedProductBean likedProductBean) {
        if (likedProductBean == null) return;
        switch (helper.getItemViewType()) {
            case PRODUCT:
                String waterRemark = likedProductBean.getWaterRemark();
                String goodsUrl = likedProductBean.getPicUrl();
                if (!TextUtils.isEmpty(waterRemark)) {
                    goodsUrl = GlideImageLoaderUtil.getWaterMarkImgUrl(likedProductBean.getPicUrl(), likedProductBean.getWaterRemark());
                }

                String economizeNum = getStringsFormat(context, R.string.economize_money, getStrings(likedProductBean.getDecreasePrice()));
                GlideImageLoaderUtil.loadImage(context, helper.getView(R.id.iv_goods_pic), goodsUrl);
                helper.setGone(R.id.iv_com_pro_tag_out, likedProductBean.getQuantity() < 1)
                        .setText(R.id.tv_price, ConstantMethod.getRmbFormat(context, likedProductBean.getPrice()))
                        .setText(R.id.tv_name, getStrings(likedProductBean.getName()))
                        .setGone(R.id.tv_economize_money, !TextUtils.isEmpty(likedProductBean.getDecreasePrice()))
                        .setText(R.id.tv_economize_money, getSpannableString(economizeNum, 1, economizeNum.length() - 1, 0, "#ff5e6b"));

                FlexboxLayout fbl_label = helper.getView(R.id.fbl_label);
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

                    //限制标签最多显示一行，超出屏幕外的自动移除
                    if (fbl_label.getChildCount()>0){
                        ViewTreeObserver observer = fbl_label.getViewTreeObserver();
                        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                int width = fbl_label.getMeasuredWidth();
                                int max = helper.itemView.getMeasuredWidth();
                                if (width >= max) {
                                    fbl_label.removeViewAt(fbl_label.getChildCount() - 1);
                                } else {
                                    fbl_label.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                }
                            }
                        });
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
                break;
        }


        helper.itemView.setOnClickListener(view -> {
            if (helper.getItemViewType() != ConstantVariable.TITLE) {
                Intent intent = new Intent(context, ShopScrollDetailsActivity.class);
                intent.putExtra("productId", String.valueOf(likedProductBean.getId()));
                context.startActivity(intent);
            }
        });
    }
}
