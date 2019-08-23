package com.amkj.dmsh.shopdetails.adapter;

import android.app.Activity;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

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
 * ClassDescription :自营商品详情页推荐商品适配器
 */
public class GoodsRecommendAdapter extends BaseQuickAdapter<ShopRecommendHotTopicEntity.ShopRecommendHotTopicBean, BaseViewHolder> {

    private final Activity mContext;

    public GoodsRecommendAdapter(Activity context, @Nullable List<ShopRecommendHotTopicEntity.ShopRecommendHotTopicBean> data) {
        super(R.layout.item_scroll_detail_goods_recommend, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ShopRecommendHotTopicEntity.ShopRecommendHotTopicBean item) {
        if (item != null) {
            GlideImageLoaderUtil.loadImage(mContext, helper.getView(R.id.iv_cover_right), item.getPicUrl());
            helper.setText(R.id.tv_price_right, getRmbFormat(mContext, item.getPrice()))
                    .setGone(R.id.tv_market_price_right, !TextUtils.isEmpty(item.getMarketPrice()))
                    .setText(R.id.tv_market_price_right, "¥" + getStrings(item.getMarketPrice()));
            ((TextView) helper.getView(R.id.tv_market_price_right)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            ((TextView) helper.getView(R.id.tv_market_price_right)).getPaint().setAntiAlias(true);
            helper.itemView.setTag(item);
        }
    }
}
