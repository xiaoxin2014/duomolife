package com.amkj.dmsh.shopdetails.adapter;

import android.content.Context;

import com.amkj.dmsh.R;
import com.amkj.dmsh.shopdetails.bean.DirectLogisticsEntity.DirectLogisticsBean.LogisticsProductPacketBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

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
        GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_logistic_indent_pro), logisticsProductPacketBean.getPicUrl());
        helper.setText(R.id.tv_logistic_indent_pro_name, getStrings(logisticsProductPacketBean.getName()))
                .setText(R.id.tv_logistics_indent_time, "下单时间：" + logisticsProductPacketBean.getDeliverTime());
    }
}
