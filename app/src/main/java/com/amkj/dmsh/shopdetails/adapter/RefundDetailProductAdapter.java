package com.amkj.dmsh.shopdetails.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;

import com.amkj.dmsh.R;
import com.amkj.dmsh.shopdetails.bean.RefundDetailProductBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getFloatNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeIntegers;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;

/**
 * Created by xiaoxin on 2019/7/20
 * Version:v4.1.0
 * ClassDescription :退款详情商品列表适配器
 */
public class RefundDetailProductAdapter extends BaseQuickAdapter<RefundDetailProductBean, BaseViewHolder> {
    Activity activity;

    public RefundDetailProductAdapter(Activity activity, @Nullable List<RefundDetailProductBean> data) {
        super(R.layout.item_refund_detail_product, data);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, RefundDetailProductBean item) {
        if (item == null) return;
        GlideImageLoaderUtil.loadCenterCrop(activity, helper.getView(R.id.iv_direct_indent_pro), item.getPicUrl());
        //价格显示
        String priceName;
        int integralPrice = getStringChangeIntegers(item.getIntegralPrice());
        if (integralPrice > 0) {
            float moneyPrice = getFloatNumber(item.getPrice());
            if (moneyPrice > 0) {
                priceName = String.format(activity.getResources().getString(R.string.integral_product_and_price)
                        , integralPrice, getStrings(item.getPrice()));
            } else {
                priceName = String.format(activity.getResources().getString(R.string.integral_indent_product_price)
                        , integralPrice);
            }
        } else {
            priceName = getStringsChNPrice(activity, item.getPrice());
        }

        helper.setText(R.id.tv_direct_indent_pro_name, getStrings(item.getProductName()))
                .setText(R.id.tv_direct_indent_pro_price, getStrings(priceName))
                .setText(R.id.tv_direct_indent_pro_sku, getStrings(item.getSaleSkuValue()))
                .setText(R.id.tv_direct_pro_count, ("x" + item.getCount()));
    }
}
