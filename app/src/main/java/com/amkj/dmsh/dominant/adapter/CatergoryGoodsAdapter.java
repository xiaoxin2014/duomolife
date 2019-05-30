package com.amkj.dmsh.dominant.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.shopdetails.activity.ShopScrollDetailsActivity;
import com.amkj.dmsh.user.bean.UserLikedProductEntity.LikedProductBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.utils.ProductLabelCreateUtils.getLabelInstance;

/**
 * Created by xiaoxin on 2019/4/20 0020
 * Version:v4.0.0
 * ClassDescription :分类商品适配器
 */
public class CatergoryGoodsAdapter extends BaseQuickAdapter<LikedProductBean, BaseViewHolder> {

    private final Context context;

    public CatergoryGoodsAdapter(Context context, @Nullable List<LikedProductBean> data) {
        super(R.layout.item_home_catergory_goods, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, LikedProductBean likedProductBean) {
        if (likedProductBean == null) return;
        LinearLayout ll_quality_product = helper.getView(R.id.ll_quality_product);
        ImageView iv_quality_good_product_ad = helper.getView(R.id.iv_quality_good_product_ad);
        switch (getStrings(likedProductBean.getObjectType())) {
            //封面图片
            case "ad":
                ll_quality_product.setVisibility(View.GONE);
                iv_quality_good_product_ad.setVisibility(View.VISIBLE);
                GlideImageLoaderUtil.loadCenterCrop(context, iv_quality_good_product_ad, getStrings(likedProductBean.getPicUrl()));
                break;
            //普通商品（默认类型）
            default:
                String waterRemark = likedProductBean.getWaterRemark();
                String goodsUrl = likedProductBean.getPicUrl();
                if (!TextUtils.isEmpty(waterRemark)) {
                    goodsUrl = GlideImageLoaderUtil.getWaterMarkImgUrl(likedProductBean.getPicUrl(), likedProductBean.getWaterRemark());
                }
                GlideImageLoaderUtil.loadImage(context, helper.getView(R.id.iv_goods_pic), goodsUrl);
                helper.setGone(R.id.iv_com_pro_tag_out, likedProductBean.getQuantity() < 1)
                        .setText(R.id.tv_price, ConstantMethod.getRmbFormat(context, likedProductBean.getPrice()))
                        .setText(R.id.tv_name, getStrings(likedProductBean.getName()));
                FlexboxLayout fbl_label = helper.getView(R.id.fbl_label);
                if (!TextUtils.isEmpty(likedProductBean.getActivityTag()) || (likedProductBean.getMarketLabelList() != null
                        && likedProductBean.getMarketLabelList().size() > 0)) {
                    fbl_label.setVisibility(View.VISIBLE);
                    fbl_label.removeAllViews();
                    if (!TextUtils.isEmpty(likedProductBean.getActivityTag())) {
                        fbl_label.addView(getLabelInstance().createLabelText(context, likedProductBean.getActivityTag(), 1));
                    }
                    if (likedProductBean.getMarketLabelList() != null
                            && likedProductBean.getMarketLabelList().size() > 0) {
                        for (LikedProductBean.MarketLabelBean marketLabelBean : likedProductBean.getMarketLabelList()) {
                            if (!TextUtils.isEmpty(marketLabelBean.getTitle())) {
                                fbl_label.addView(getLabelInstance().createLabelText(context, marketLabelBean.getTitle(), 0));
                                if (fbl_label.getChildCount() >= 2) break;
                            }
                        }
                    }
                } else {
                    fbl_label.setVisibility(View.GONE);
                }
                break;

        }

        helper.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ShopScrollDetailsActivity.class);
            intent.putExtra("productId", String.valueOf(likedProductBean.getId()));
            context.startActivity(intent);

        });
        helper.itemView.setTag(likedProductBean);
    }
}
