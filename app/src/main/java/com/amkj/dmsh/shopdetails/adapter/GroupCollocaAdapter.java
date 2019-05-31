package com.amkj.dmsh.shopdetails.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.shopdetails.bean.ShopRecommendHotTopicEntity.ShopRecommendHotTopicBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by xiaoxin on 2019/5/30
 * Version:v4.1.0
 * ClassDescription :组合搭配列表适配器
 */
public class GroupCollocaAdapter extends BaseQuickAdapter<ShopRecommendHotTopicBean, BaseViewHolder> {

    private final Activity context;

    public GroupCollocaAdapter(Activity activity, @Nullable List<ShopRecommendHotTopicBean> data) {
        super(R.layout.item_group_collocation, data);
        context = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, ShopRecommendHotTopicBean item) {
        if (item == null) return;
        GlideImageLoaderUtil.loadImage(context, helper.getView(R.id.iv_cover_right), item.getPicUrl());
        helper.setText(R.id.tv_save_price, item.getSave_num())
                .setText(R.id.tv_name, item.getTitle())
                .setText(R.id.tv_price, ConstantMethod.getRmbFormat(context, item.getPrice()))
                .addOnClickListener(R.id.tv_select_sku).setTag(R.id.tv_select_sku, item)
                .addOnClickListener(R.id.tv_shop_car_sel).setTag(R.id.tv_shop_car_sel, item);
        TextView tvSelect = helper.getView(R.id.tv_shop_car_sel);
        tvSelect.setSelected(item.isSelected());
        helper.itemView.setTag(item);
    }
}
