package com.amkj.dmsh.dominant.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

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

    private final Context mContext;

    public CatergoryGoodsAdapter(Context context, @Nullable List<LikedProductBean> data) {
        super(R.layout.item_home_catergory_goods, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, LikedProductBean goodsBean) {
        if (goodsBean == null) return;
        String waterRemark = goodsBean.getWaterRemark();
        String goodsUrl = goodsBean.getPicUrl();
        if (!TextUtils.isEmpty(waterRemark)) {
            goodsUrl = GlideImageLoaderUtil.getWaterMarkImgUrl(goodsBean.getPicUrl(), goodsBean.getWaterRemark());
        }
        GlideImageLoaderUtil.loadImage(mContext, helper.getView(R.id.iv_goods_pic), goodsUrl);
        helper.setText(R.id.tv_price, ConstantMethod.getRmbFormat(mContext, goodsBean.getPrice()))
                .setText(R.id.tv_name, getStrings(goodsBean.getName()));
        FlexboxLayout fbl_label = helper.getView(R.id.fbl_label);
        if (!TextUtils.isEmpty(goodsBean.getActivityTag()) || (goodsBean.getMarketLabelList() != null
                && goodsBean.getMarketLabelList().size() > 0)) {
            fbl_label.setVisibility(View.VISIBLE);
            fbl_label.removeAllViews();
            if (!TextUtils.isEmpty(goodsBean.getActivityTag())) {
                fbl_label.addView(getLabelInstance().createLabelText(mContext, goodsBean.getActivityTag(), 1));
            }
            if (goodsBean.getMarketLabelList() != null
                    && goodsBean.getMarketLabelList().size() > 0) {
                for (LikedProductBean.MarketLabelBean marketLabelBean : goodsBean.getMarketLabelList()) {
                    if (!TextUtils.isEmpty(marketLabelBean.getTitle())) {
                        fbl_label.addView(getLabelInstance().createLabelText(mContext, marketLabelBean.getTitle(), 0));
                    }
                }
            }
        } else {
            fbl_label.setVisibility(View.GONE);
        }

        helper.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, ShopScrollDetailsActivity.class);
            intent.putExtra("productId", String.valueOf(goodsBean.getId()));
            mContext.startActivity(intent);

        });
        helper.itemView.setTag(goodsBean);
    }
}
