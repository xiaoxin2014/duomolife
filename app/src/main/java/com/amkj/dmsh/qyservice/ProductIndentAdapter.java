package com.amkj.dmsh.qyservice;

import android.content.Context;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.OrderProductNewBean;
import com.amkj.dmsh.shopdetails.bean.MainOrderListEntity.MainOrderBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/8/29
 * version 3.1.6
 * class description:客服订单弹窗
 */
public class ProductIndentAdapter extends BaseQuickAdapter<MainOrderBean, BaseViewHolder> {
    private final Context context;

    public ProductIndentAdapter(Context context, List<MainOrderBean> orderListBeans) {
        super(R.layout.adapter_service_product_indent, orderListBeans);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, MainOrderBean mainOrderBean) {
        if (mainOrderBean == null) return;
        List<OrderProductNewBean> orderProductList = mainOrderBean.getOrderProductList();
        if (orderProductList != null && orderProductList.size() > 0) {
            OrderProductNewBean orderProductNewBean = orderProductList.get(0);
            GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_service_product_indent_image), orderProductNewBean.getPicUrl());
            helper.setText(R.id.tv_service_product_indent_name, orderProductNewBean.getProductName())
                    .setText(R.id.tv_service_product_indent_price, "订单金额 ¥" + mainOrderBean.getPayAmount())
                    .setText(R.id.tv_service_product_indent_status, mainOrderBean.getStatusText());
        }
        helper.itemView.setTag(mainOrderBean);
    }
}
