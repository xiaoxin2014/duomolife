package com.amkj.dmsh.shopdetails.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.amkj.dmsh.R;
import com.amkj.dmsh.shopdetails.bean.GroupGoodsEntity.GroupGoodsBean.CombineCommonBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getRmbFormat;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * Created by xiaoxin on 2019/5/24
 * Version:v4.1.0
 * ClassDescription :组合商品适配器
 */
public class GoodsGroupAdapter extends BaseQuickAdapter<CombineCommonBean, BaseViewHolder> {

    private final Activity mContext;

    public GoodsGroupAdapter(Activity context, @Nullable List<CombineCommonBean> data) {
        super(R.layout.item_scroll_detail_goods_group, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CombineCommonBean item) {
        if (item == null) return;
        GlideImageLoaderUtil.loadImage(mContext, helper.getView(R.id.iv_cover_right), item.getPicUrl());
        helper.setText(R.id.tv_min_price, getRmbFormat(mContext, item.getMinPrice()))
                .setGone(R.id.tv_min, (!TextUtils.isEmpty(item.getMaxPrice()) && !item.getMaxPrice().equals(item.getMinPrice())))
                .setText(R.id.tv_save_price, getStrings(item.getTag()));
        helper.itemView.setTag(item);
    }
}
