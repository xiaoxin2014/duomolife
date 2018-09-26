package com.amkj.dmsh.qyservice;

import android.content.Context;

import com.amkj.dmsh.R;
import com.amkj.dmsh.shopdetails.bean.InquiryOrderEntry.OrderInquiryDateEntry.OrderListBean;
import com.amkj.dmsh.shopdetails.bean.InquiryOrderEntry.OrderInquiryDateEntry.OrderListBean.GoodsBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PRO_STATUS;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/8/29
 * version 3.1.6
 * class description:客服订单弹窗
 */
public class ProductIndentAdapter extends BaseQuickAdapter<OrderListBean, BaseViewHolder> {
    private final Context context;

    public ProductIndentAdapter(Context context, List<OrderListBean> orderListBeans) {
        super(R.layout.adapter_service_product_indent, orderListBeans);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderListBean orderListBean) {
        String name = "";
        if (orderListBean.getGoods() != null && orderListBean.getGoods().size() > 0) {
            GoodsBean goodsBean = orderListBean.getGoods().get(0);
            GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_service_product_indent_image), getStrings(goodsBean.getPicUrl()));
            name = goodsBean.getName();
        }
        helper.setText(R.id.tv_service_product_indent_name, name)
                .setText(R.id.tv_service_product_indent_price,"订单金额 ￥" + orderListBean.getAmount())
                .setText(R.id.tv_service_product_indent_status, INDENT_PRO_STATUS.get(String.valueOf(orderListBean.getStatus())))
                .itemView.setTag(orderListBean);
    }
}
