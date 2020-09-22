package com.amkj.dmsh.shopdetails.adapter;

import android.app.Activity;
import android.text.TextUtils;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.OrderProductNewBean;
import com.amkj.dmsh.shopdetails.bean.PresentProductOrder;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStringChangeFloat;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getStringsChNPrice;
import static com.amkj.dmsh.constant.ConstantVariable.APPLY_REFUND_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_DETAILS_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.REFUND_DETAIL_TYPE;
import static com.amkj.dmsh.constant.ConstantVariable.SELECT_REFUND_GOODS;
import static com.amkj.dmsh.constant.ConstantVariable.SELECT_REFUND_TYPE;


/**
 * Created by atd48 on 2016/8/17.
 */
public class DirectProductListAdapter extends BaseQuickAdapter<OrderProductNewBean, BaseViewHolder> {
    private final String type;
    private final Activity context;

    public DirectProductListAdapter(Activity context, List<OrderProductNewBean> list, String type) {
        super(R.layout.layout_direct_indent_product_new_item, list);
        this.context = context;
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderProductNewBean orderProductBean) {
        if (orderProductBean == null) return;
        //主商品
        if (!orderProductBean.isPresent()) {
            GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_direct_indent_pro), orderProductBean.getPicUrl());
            helper.setText(R.id.tv_direct_indent_pro_name, getStrings(orderProductBean.getProductName()))
                    .setText(R.id.tv_direct_indent_pro_sku, getStrings(orderProductBean.getSaleSkuValue()))
                    .setText(R.id.tv_product_status, getStrings(orderProductBean.getStatusText()))
                    .setGone(R.id.tv_product_status, !TextUtils.isEmpty((orderProductBean.getStatusText())))
                    .setText(R.id.tv_predict_time, getStrings(orderProductBean.getRemindText()))
                    .setGone(R.id.tv_predict_time, !TextUtils.isEmpty(orderProductBean.getRemindText()))
                    .setText(R.id.tv_direct_pro_count, "x" + orderProductBean.getCount())
                    .setText(R.id.tv_direct_indent_pro_price, getStringsChNPrice(context, orderProductBean.getPrice()))
                    .addOnClickListener(R.id.ll_product).setTag(R.id.ll_product, orderProductBean)
                    .setGone(R.id.ll_new_product, true);

            switch (type) {
                //订单列表
                case INDENT_TYPE:
                    helper.addOnClickListener(R.id.ll_present);
                    showPresentInfo(helper, orderProductBean);
                    break;
                //普通商品订单详情
                case INDENT_DETAILS_TYPE:
                    //申请退款和售后详情按钮
                    helper.addOnClickListener(R.id.tv_refund).setTag(R.id.tv_refund, orderProductBean)
                            .addOnClickListener(R.id.tv_service).setTag(R.id.tv_service, orderProductBean)
                            .setGone(R.id.tv_refund, orderProductBean.isShowRefundApply())
                            .setGone(R.id.tv_service, !TextUtils.isEmpty(orderProductBean.getRefundNo()));
                    String activityTag = orderProductBean.getActivityTag();
                    helper.setGone(R.id.ll_communal_activity_topic_tag, !TextUtils.isEmpty(activityTag))
                            .setGone(R.id.tv_communal_activity_tag, !TextUtils.isEmpty(activityTag))
                            .setText(R.id.tv_communal_activity_tag, activityTag)
                            //最后一个活动组不需要展示分割线
                            .setGone(R.id.tv_details_gray, helper.getPosition() != getData().size() - 1 && orderProductBean.getShowLine())
                            .setGone(R.id.ll_communal_activity_tag_rule, false);

                    showPresentInfo(helper, orderProductBean);
                    break;
                //选择退款商品
                case SELECT_REFUND_GOODS:
                    helper.setGone(R.id.checkbox_refund, true)
                            .setChecked(R.id.checkbox_refund, orderProductBean.isChecked())
                            .addOnClickListener(R.id.checkbox_refund).setTag(R.id.checkbox_refund, orderProductBean);
                    break;
                case SELECT_REFUND_TYPE://选择退款类型
                case APPLY_REFUND_TYPE://申请退款
                case REFUND_DETAIL_TYPE://退款详情
                    //如果是积分商品
                    if (orderProductBean.getIntegralPrice() > 0) {
                        String priceName;
                        float moneyPrice = getStringChangeFloat(orderProductBean.getPrice());
                        if (moneyPrice > 0) {
                            priceName = String.format(context.getResources().getString(R.string.integral_product_and_price)
                                    , orderProductBean.getIntegralPrice(), getStrings(orderProductBean.getPrice()));
                        } else {
                            priceName = String.format(context.getResources().getString(R.string.integral_indent_product_price)
                                    , orderProductBean.getIntegralPrice());
                        }
                        helper.setText(R.id.tv_direct_indent_pro_price, priceName);
                    }
                    break;
            }
        } else {
            //赠品
            helper.setGone(R.id.ll_present, true)
                    .setGone(R.id.ll_communal_activity_topic_tag, false)
                    .setGone(R.id.ll_new_product, false)
                    .setGone(R.id.tv_details_gray, false);
            if (INDENT_TYPE.equals(type)) {
                helper.addOnClickListener(R.id.ll_present);
            }
            helper.setText(R.id.tv_name, orderProductBean.getProductName())
                    .setText(R.id.tv_count, "×" + orderProductBean.getCount());
            GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_cover), orderProductBean.getPicUrl());
        }


        helper.itemView.setTag(orderProductBean);
    }

    //显示赠品信息
    private void showPresentInfo(BaseViewHolder helper, OrderProductNewBean orderProductBean) {
        PresentProductOrder presentProductOrder = orderProductBean.getPresentProductOrder();
        helper.setGone(R.id.ll_present, presentProductOrder != null);
        if (presentProductOrder != null) {
            helper.setText(R.id.tv_name, presentProductOrder.getPresentName())
                    .setText(R.id.tv_count, "x" + presentProductOrder.getPresentCount());
            GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_cover), presentProductOrder.getPresentPicUrl());
        }
    }
}
