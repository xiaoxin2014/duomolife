package com.amkj.dmsh.qyservice;

import android.content.Context;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.amkj.dmsh.shopdetails.bean.InquiryOrderEntry.OrderInquiryDateEntry.OrderListBean;
import com.amkj.dmsh.shopdetails.bean.InquiryOrderEntry.OrderInquiryDateEntry.OrderListBean.GoodsBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/8/29
 * version 3.1.6
 * class description:客服订单弹窗
 */
public class ProductIndentAdapter extends BaseQuickAdapter<OrderListBean, BaseViewHolderHelper> {
    private final Context context;

    public ProductIndentAdapter(Context context, List<OrderListBean> orderListBeans) {
        super(R.layout.adapter_service_product_indent, orderListBeans);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolderHelper helper, OrderListBean orderListBean) {
        if (orderListBean.getGoods() != null && orderListBean.getGoods().size() > 0) {
            GoodsBean goodsBean = orderListBean.getGoods().get(0);
            GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_service_product_indent_image), getStrings(goodsBean.getPicUrl()));
        }
        helper.setText(R.id.tv_service_product_indent_no, "订 单 号：" + orderListBean.getNo())
                .setText(R.id.tv_service_product_indent_price, "订单金额：￥" + orderListBean.getAmount())
                .setText(R.id.tv_service_product_indent_time, "创建时间：" + orderListBean.getCreateTime())
                .itemView.setTag(orderListBean);
    }
}
