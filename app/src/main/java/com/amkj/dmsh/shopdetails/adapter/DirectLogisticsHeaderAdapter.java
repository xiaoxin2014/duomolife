package com.amkj.dmsh.shopdetails.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.amkj.dmsh.R;
import com.amkj.dmsh.shopdetails.bean.DirectLogisticsEntity.DirectLogisticsBean.LogisticsProductPacketBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.skipProductUrl;

/**
 * Created by atd48 on 2016/10/31.
 */
public class DirectLogisticsHeaderAdapter extends BaseQuickAdapter<LogisticsProductPacketBean, BaseViewHolder> {
    private final Context context;

    public DirectLogisticsHeaderAdapter(Context context, List<LogisticsProductPacketBean> logisticsBeanList) {
        super(R.layout.adapter_direct_logistics_goods, logisticsBeanList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, LogisticsProductPacketBean logisticsProductPacketBean) {
        if (logisticsProductPacketBean == null) return;
        GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_logistic_indent_pro), logisticsProductPacketBean.getPicUrl());
        helper.setText(R.id.tv_logistic_indent_pro_name, getStrings(logisticsProductPacketBean.getName()))
                .setGone(R.id.tv_logistic_indent_pro_name, !TextUtils.isEmpty(logisticsProductPacketBean.getName()))
                .setText(R.id.tv_logistics_indent_sku, getStrings(logisticsProductPacketBean.getSaleSkuValue()))
                .setGone(R.id.tv_logistics_indent_sku, !TextUtils.isEmpty(logisticsProductPacketBean.getSaleSkuValue()))
                .setText(R.id.tv_logistics_indent_price, getStrings("¥" + logisticsProductPacketBean.getPrice()))
                .setGone(R.id.tv_logistics_indent_price, !TextUtils.isEmpty(logisticsProductPacketBean.getPrice()))
                .setText(R.id.tv_logistics_indent_time, "下单时间：" + logisticsProductPacketBean.getDeliverTime())
                .setGone(R.id.tv_logistics_indent_time, !TextUtils.isEmpty(logisticsProductPacketBean.getDeliverTime()));

        helper.itemView.setOnClickListener(v -> {
            if (!logisticsProductPacketBean.isPresent()) {//赠品不可跳转商品详情
                skipProductUrl(context, 1, logisticsProductPacketBean.getId());
            }
        });
    }
}
