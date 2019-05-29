package com.amkj.dmsh.shopdetails.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;

import com.amkj.dmsh.R;
import com.amkj.dmsh.shopdetails.bean.ShopRecommendHotTopicEntity;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getRmbFormat;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * Created by xiaoxin on 2019/5/24
 * Version:v4.1.0
 * ClassDescription :自营商品详情页组团商品适配器
 */
public class GoodsGroupAdapter extends BaseQuickAdapter<ShopRecommendHotTopicEntity.ShopRecommendHotTopicBean, BaseViewHolder> {

    private final Activity mContext;

    public GoodsGroupAdapter(Activity context, @Nullable List<ShopRecommendHotTopicEntity.ShopRecommendHotTopicBean> data) {
        super(R.layout.item_scroll_detail_goods_group, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ShopRecommendHotTopicEntity.ShopRecommendHotTopicBean item) {
        if (item != null) {
            GlideImageLoaderUtil.loadImage(mContext, helper.getView(R.id.iv_cover_right), item.getPicUrl());
            helper.setText(R.id.tv_price_right, getRmbFormat(mContext, item.getPrice()))
                    .setText(R.id.tv_save_price, getStrings(item.getSave_num()));
            helper.itemView.setTag(item);
        }
    }
}
