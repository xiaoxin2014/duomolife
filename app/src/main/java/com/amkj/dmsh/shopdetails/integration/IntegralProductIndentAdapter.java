package com.amkj.dmsh.shopdetails.integration;

import android.content.Context;

import com.amkj.dmsh.R;
import com.amkj.dmsh.shopdetails.integration.bean.IntegralIndentOrderEntity.IntegralIndentOrderBean.OrderListBean.GoodsBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStringFilter;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/4/12
 * class description:积分产品
 */

public class IntegralProductIndentAdapter extends BaseQuickAdapter<GoodsBean, BaseViewHolder> {
    private final Context context;

    public IntegralProductIndentAdapter(Context context, List<GoodsBean> orderBeanList) {
        super(R.layout.layout_integral_direct_product, orderBeanList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsBean goodsBean) {
        // 纯积分
        String price;
        if (goodsBean.getIntegralType() == 0) {
            price = String.format(context.getResources().getString(R.string.integral_indent_product_price), goodsBean.getIntegralPrice());
        } else { // 积分+金钱
            price = String.format(context.getResources().getString(R.string.integral_indent_product_price_and_money), goodsBean.getIntegralPrice(), goodsBean.getPrice());
        }
        helper.setText(R.id.tv_integral_indent_product_name, getStringFilter(goodsBean.getName()))
                .setText(R.id.tv_integral_indent_count, String.format(context.getResources().getString(R.string.integral_lottery_award_count), goodsBean.getCount()))
                .setText(R.id.tv_integral_indent_sku_value, getStringFilter(goodsBean.getSaleSkuValue()))
                .setText(R.id.tv_integral_indent_product_price, price);
//        商品图片
        GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_integral_indent_product_image), goodsBean.getPicUrl());
        helper.itemView.setTag(goodsBean);
    }
}
