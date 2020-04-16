package com.amkj.dmsh.shopdetails.adapter;

import android.content.Context;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.OrderProductNewBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * Created by xiaoxin on 2020/4/9
 * Version:v4.4.3
 * ClassDescription :售后列表适配器
 */
public class RefundProductListAdapter extends BaseQuickAdapter<OrderProductNewBean, BaseViewHolder> {
    private final Context context;

    public RefundProductListAdapter(Context context, List<OrderProductNewBean> list) {
        super(R.layout.item_refund_detail_product, list);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderProductNewBean orderProductNewBean) {
        if (orderProductNewBean == null) return;
        GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_direct_indent_pro), orderProductNewBean.getPicUrl());
        helper.setText(R.id.tv_direct_indent_pro_name, getStrings(orderProductNewBean.getProductName()))
                .setText(R.id.tv_direct_indent_pro_sku, getStrings(orderProductNewBean.getSaleSkuValue()))
                .setText(R.id.tv_direct_indent_pro_price, "¥" + orderProductNewBean.getPrice())
                .setText(R.id.tv_direct_pro_count, "x" + orderProductNewBean.getCount())
                .setGone(R.id.tv_dir_indent_pro_status, false)
                .setGone(R.id.tv_indent_pro_refund_price, true)
                .setText(R.id.tv_indent_pro_refund_price, "退款金额：¥" + orderProductNewBean.getRefundPrice());
    }
}
