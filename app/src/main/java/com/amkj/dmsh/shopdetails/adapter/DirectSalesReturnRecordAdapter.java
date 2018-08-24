package com.amkj.dmsh.shopdetails.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.amkj.dmsh.constant.ConstantVariable;
import com.amkj.dmsh.shopdetails.bean.DirectReturnRecordEntity.DirectReturnRecordBean.OrderListBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getFloatNumber;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

;

/**
 * Created by atd48 on 2016/11/1.
 */
public class DirectSalesReturnRecordAdapter extends BaseQuickAdapter<OrderListBean, BaseViewHolderHelper> {
    private final Context context;

    public DirectSalesReturnRecordAdapter(Context context, List<OrderListBean> orderListBeanList) {
        super(R.layout.adapter_direct_sales_return_record_item, orderListBeanList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolderHelper helper, OrderListBean orderListBean) {
        TextView tv_tag_record_state = helper.getView(R.id.tv_tag_record_state);
        tv_tag_record_state.setText(ConstantVariable.INDENT_PRO_STATUS.get(String.valueOf(orderListBean.getStatus())));
        GlideImageLoaderUtil.loadCenterCrop(context, (ImageView) helper.getView(R.id.iv_direct_indent_pro), orderListBean.getPicUrl());
        helper.setText(R.id.tv_direct_indent_pro_name, getStrings(orderListBean.getName()))
                .setText(R.id.tv_direct_indent_pro_sku, getStrings(orderListBean.getSaleSkuValue()))
                .setText(R.id.tv_direct_indent_pro_price, "￥" + orderListBean.getPrice())
                .setGone(R.id.tv_direct_indent_pro_price,getFloatNumber(orderListBean.getPrice())>0)
                .setText(R.id.tv_direct_pro_count, "x" + orderListBean.getCount())
                .setGone(R.id.tv_dir_indent_pro_status,false)
                .setGone(R.id.tv_indent_pro_refund_price, getFloatNumber(orderListBean.getRefundPrice())>0)
                .setText(R.id.tv_indent_pro_refund_price, "退款金额：￥" + orderListBean.getRefundPrice())
                .setText(R.id.tv_goods_indent_code, "申请时间 " + getStrings(orderListBean.getRefundTime()))
                .addOnClickListener(R.id.tv_indent_reply_border_first_gray).setTag(R.id.tv_indent_reply_border_first_gray, orderListBean);
        helper.itemView.setTag(orderListBean);
    }
}
