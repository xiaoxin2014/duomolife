package com.amkj.dmsh.shopdetails.adapter;

import android.app.Activity;

import com.amkj.dmsh.R;
import com.amkj.dmsh.shopdetails.bean.RefundApplyEntity.RefundApplyBean.ProductsBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeFloat;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;


/**
 * Created by atd48 on 2016/8/17.
 */
public class IntegralRefundAdapter extends BaseQuickAdapter<ProductsBean, BaseViewHolder> {
    private final Activity context;

    public IntegralRefundAdapter(Activity context, List<ProductsBean> list) {
        super(R.layout.layout_direct_indent_product_item, list);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductsBean productsBean) {
        if (productsBean == null) return;
        GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_direct_indent_pro), productsBean.getPicUrl());
        helper.setText(R.id.tv_direct_indent_pro_name, getStrings(productsBean.getProductName()))
                .setText(R.id.tv_direct_indent_pro_sku, getStrings(productsBean.getProductSkuValue()))
                .setText(R.id.tv_direct_pro_count, "x" + productsBean.getCount());
        String priceName;
        if (productsBean.getIntegralPrice() > 0) {
            float moneyPrice = getStringChangeFloat(productsBean.getPrice());
            if (moneyPrice > 0) {
                priceName = String.format(context.getResources().getString(R.string.integral_product_and_price)
                        , productsBean.getIntegralPrice(), getStrings(productsBean.getPrice()));
            } else {
                priceName = String.format(context.getResources().getString(R.string.integral_indent_product_price)
                        , productsBean.getIntegralPrice());
            }
        } else {
            priceName = getStringsChNPrice(context, productsBean.getPrice());
        }
        helper.setText(R.id.tv_direct_indent_pro_price, priceName);
        helper.itemView.setTag(productsBean);
    }
}
