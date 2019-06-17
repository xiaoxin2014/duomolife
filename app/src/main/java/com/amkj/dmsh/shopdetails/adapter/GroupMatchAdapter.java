package com.amkj.dmsh.shopdetails.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.shopdetails.bean.GroupGoodsEntity.GroupGoodsBean.CombineCommonBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getRmbFormat;

/**
 * Created by xiaoxin on 2019/5/30
 * Version:v4.1.0
 * ClassDescription :组合搭配列表适配器
 */
public class GroupMatchAdapter extends BaseQuickAdapter<CombineCommonBean, BaseViewHolder> {

    private final Activity context;

    public GroupMatchAdapter(Activity activity, @Nullable List<CombineCommonBean> data) {
        super(R.layout.item_group_match, data);
        context = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, CombineCommonBean item) {
        if (item == null) return;
        GlideImageLoaderUtil.loadImage(context, helper.getView(R.id.iv_cover_right), item.getPicUrl());
        helper.setText(R.id.tv_save_price, item.getTag())
                .setGone(R.id.tv_save_price, !item.isMainProduct())
                .setText(R.id.tv_name, item.getName())
                .setText(R.id.tv_min_price, getRmbFormat(context, item.getMinPrice()))
                .setText(R.id.tv_max_price, "~" + "¥" + getRmbFormat(mContext, item.getMaxPrice(), false))
                .setGone(R.id.tv_max_price, (!TextUtils.isEmpty(item.getMaxPrice()) && !item.getMaxPrice().equals(item.getMinPrice())))
                .addOnClickListener(R.id.tv_select_sku).setTag(R.id.tv_select_sku, item)
                .addOnClickListener(R.id.tv_shop_car_sel).setTag(R.id.tv_shop_car_sel, item)
                .setEnabled(R.id.tv_shop_car_sel, !item.isMainProduct());
        TextView tvSelect = helper.getView(R.id.tv_shop_car_sel);
        tvSelect.setSelected(item.isSelected());
        helper.itemView.setTag(item);
    }
}
