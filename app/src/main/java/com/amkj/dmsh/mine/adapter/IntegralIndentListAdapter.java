package com.amkj.dmsh.mine.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.shopdetails.integration.bean.IntegralIndentOrderEntity.IntegralIndentOrderBean.OrderListBean;
import com.amkj.dmsh.shopdetails.integration.bean.IntegralIndentOrderEntity.IntegralIndentOrderBean.OrderListBean.GoodsBean;
import com.amkj.dmsh.utils.glide.GlideImageLoaderUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStringFilter;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantVariable.CANCEL_ORDER;
import static com.amkj.dmsh.constant.ConstantVariable.CANCEL_PAY_ORDER;
import static com.amkj.dmsh.constant.ConstantVariable.CHECK_LOG;
import static com.amkj.dmsh.constant.ConstantVariable.CONFIRM_ORDER;
import static com.amkj.dmsh.constant.ConstantVariable.INDENT_PRO_STATUS;
import static com.amkj.dmsh.constant.ConstantVariable.LITTER_CONSIGN;
import static com.amkj.dmsh.constant.ConstantVariable.PAY;
import static com.amkj.dmsh.constant.ConstantVariable.PRO_APPRAISE;
import static com.amkj.dmsh.constant.ConstantVariable.REFUND_APPLY;
import static com.amkj.dmsh.constant.ConstantVariable.REFUND_FEEDBACK;
import static com.amkj.dmsh.constant.ConstantVariable.REFUND_REPAIR;
import static com.amkj.dmsh.constant.ConstantVariable.VIRTUAL_COUPON;

/**
 * Created by atd48 on 2016/8/25.
 */
public class IntegralIndentListAdapter extends BaseQuickAdapter<OrderListBean, BaseViewHolder> {
    private final Context context;

    public IntegralIndentListAdapter(Context context, List<OrderListBean> orderListBeanList) {
        super(R.layout.adapter_integral_exchange_indent, orderListBeanList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderListBean orderListBean) {
        List<OrderListBean.GoodsBean> goods = orderListBean.getGoods();
        if (goods != null && goods.size() > 0) {
            String price;//商品价格
            String amout;//实付金额
            GoodsBean goodsBean = goods.get(0);
            helper.setText(R.id.tv_integral_indent_product_name, getStringFilter(goodsBean.getName()))
                    .setText(R.id.tv_integral_indent_sku_value, getStringFilter(goodsBean.getSaleSkuValue()))
                    .setText(R.id.tv_integral_indent_count, String.format(context.getResources().getString(R.string.integral_lottery_award_count), goodsBean.getCount()))
                    .setText(R.id.tv_integral_indent_status, INDENT_PRO_STATUS.get(String.valueOf(goodsBean.getStatus())));
//            纯积分
            if (goodsBean.getIntegralType() == 0) {
                price = String.format(context.getResources().getString(R.string.integral_indent_product_price), goodsBean.getIntegralPrice());
                amout = String.format(context.getResources().getString(R.string.integral_indent_product_price), goodsBean.getIntegralPrice() * goodsBean.getCount());
            } else { // 积分+金钱
                price = String.format(context.getResources().getString(R.string.integral_indent_product_price_and_money), goodsBean.getIntegralPrice(), String.valueOf(Double.parseDouble(goodsBean.getPrice())));
                amout = String.format(context.getResources().getString(R.string.integral_indent_product_price_and_money), goodsBean.getIntegralPrice() * goodsBean.getCount(), String.valueOf(Double.parseDouble(goodsBean.getPrice()) * goodsBean.getCount()));
            }
            helper.setText(R.id.tv_integral_indent_product_price, price)
                    .setText(R.id.tv_intent_count_price, (String.format(context.getResources().getString(R.string.integral_indent_product_count), goodsBean.getCount())) + amout);
            setIntegralIntentStatus(helper, orderListBean, goodsBean);
            GlideImageLoaderUtil.loadCenterCrop(context, helper.getView(R.id.iv_integral_indent_product_image), goodsBean.getPicUrl());
        }
        helper.setText(R.id.tv_integral_indent_create_time, getStrings(orderListBean.getCreateTime()))
                .itemView.setTag(orderListBean);
    }

    private void setIntegralIntentStatus(BaseViewHolder helper, OrderListBean orderListBean, GoodsBean goodBean) {
        int statusCode = goodBean.getStatus();
        LinearLayout ll_indent_bottom = helper.getView(R.id.ll_indent_bottom);
        TextView tv_indent_border_first_gray = helper.getView(R.id.tv_indent_border_first_gray);
        TextView tv_indent_border_second_blue = helper.getView(R.id.tv_indent_border_second_blue);
        helper.addOnClickListener(R.id.tv_indent_border_first_gray)
                .addOnClickListener(R.id.tv_indent_border_second_blue);
        ll_indent_bottom.setVisibility(View.VISIBLE);
        if (goodBean.getIntegralProductType() == 0) {
            if (0 <= statusCode && statusCode < 10) {
//          底栏 件数
                tv_indent_border_first_gray.setVisibility(View.VISIBLE);
                tv_indent_border_second_blue.setVisibility(View.VISIBLE);
                tv_indent_border_second_blue.setText("立即付款");
                tv_indent_border_first_gray.setText("取消订单");
                tv_indent_border_first_gray.setTag(R.id.tag_first, CANCEL_ORDER);
                tv_indent_border_first_gray.setTag(R.id.tag_second, orderListBean);
                tv_indent_border_second_blue.setTag(R.id.tag_first, PAY);
                tv_indent_border_second_blue.setTag(R.id.tag_second, orderListBean);
            } else if (12 == statusCode) {
                ll_indent_bottom.setVisibility(View.VISIBLE);
                tv_indent_border_second_blue.setVisibility(View.GONE);
                tv_indent_border_first_gray.setVisibility(View.VISIBLE);
                tv_indent_border_first_gray.setText("查看物流");
                tv_indent_border_first_gray.setTag(R.id.tag_first, LITTER_CONSIGN);
                tv_indent_border_first_gray.setTag(R.id.tag_second, orderListBean);
            } else if (10 <= statusCode && statusCode < 20) {
                if (10 == statusCode) {
                    boolean isRefund = true;
                    for (int i = 0; i < orderListBean.getGoods().size(); i++) {
                        GoodsBean goodsBean = orderListBean.getGoods().get(i);
                        if (goodsBean.getStatus() == 10) {
                            continue;
                        } else {
                            isRefund = false;
                            break;
                        }
                    }
                    if (isRefund) {
//            取消订单
                        tv_indent_border_second_blue.setVisibility(View.GONE);
                        tv_indent_border_first_gray.setVisibility(View.VISIBLE);
                        tv_indent_border_first_gray.setText("取消订单");
                        tv_indent_border_first_gray.setTag(R.id.tag_first, CANCEL_PAY_ORDER);
                        tv_indent_border_first_gray.setTag(R.id.tag_second, orderListBean);
                    } else {
                        //            不可取消订单
                        ll_indent_bottom.setVisibility(View.GONE);
                    }
                } else {
//            不可取消订单
                    ll_indent_bottom.setVisibility(View.GONE);
                }
            } else if (20 <= statusCode && statusCode < 30) {
                tv_indent_border_first_gray.setVisibility(View.VISIBLE);
                tv_indent_border_second_blue.setVisibility(View.VISIBLE);
                tv_indent_border_first_gray.setText("查看物流");
                tv_indent_border_second_blue.setText("确认收货");
                tv_indent_border_first_gray.setTag(R.id.tag_first, CHECK_LOG);
                tv_indent_border_first_gray.setTag(R.id.tag_second, orderListBean);
                tv_indent_border_second_blue.setTag(R.id.tag_first, CONFIRM_ORDER);
                tv_indent_border_second_blue.setTag(R.id.tag_second, orderListBean);
//          确认订单
                tv_indent_border_second_blue.setTag(R.id.tag_first, CONFIRM_ORDER);
                tv_indent_border_second_blue.setTag(R.id.tag_second, orderListBean);
            }  else if (30 <= statusCode && statusCode <= 40) {
//                if(statusCode==40){
                    tv_indent_border_first_gray.setVisibility(View.GONE);
//                }else{
//                    tv_indent_border_first_gray.setVisibility(View.VISIBLE);
//                    tv_indent_border_first_gray.setText("评价");
//                    tv_indent_border_first_gray.setTag(R.id.tag_first, PRO_APPRAISE);
//                    tv_indent_border_first_gray.setTag(R.id.tag_second, orderListBean);
//                }
                tv_indent_border_second_blue.setVisibility(View.VISIBLE);
                tv_indent_border_second_blue.setText("申请售后");
                tv_indent_border_second_blue.setTag(R.id.tag_first, REFUND_APPLY);
                tv_indent_border_second_blue.setTag(R.id.tag_second, orderListBean);
            } else if (statusCode == -40 ||
                    (statusCode <= -30 && -32 <= statusCode)
                    || statusCode == -35) {
                tv_indent_border_first_gray.setVisibility(View.GONE);
                tv_indent_border_second_blue.setVisibility(View.VISIBLE);
                tv_indent_border_second_blue.setText(getStrings(INDENT_PRO_STATUS.get(String.valueOf(statusCode))));
                tv_indent_border_second_blue.setTag(R.id.tag_first, REFUND_FEEDBACK);
                tv_indent_border_second_blue.setTag(R.id.tag_second, orderListBean);
            } else if (50 <= statusCode && statusCode <= 58) {
                tv_indent_border_first_gray.setVisibility(View.GONE);
                tv_indent_border_second_blue.setVisibility(View.VISIBLE);
                tv_indent_border_second_blue.setText(getStrings(INDENT_PRO_STATUS.get(String.valueOf(statusCode))));
                tv_indent_border_second_blue.setTag(R.id.tag_first, REFUND_REPAIR);
                tv_indent_border_second_blue.setTag(R.id.tag_second, orderListBean);
            } else {
                ll_indent_bottom.setVisibility(View.GONE);
            }
        } else {
            ll_indent_bottom.setVisibility(View.VISIBLE);
            tv_indent_border_first_gray.setVisibility(View.GONE);
            tv_indent_border_second_blue.setVisibility(View.VISIBLE);
            tv_indent_border_second_blue.setText("查看优惠券");
            tv_indent_border_second_blue.setTag(R.id.tag_first, VIRTUAL_COUPON);
            tv_indent_border_second_blue.setTag(R.id.tag_second, orderListBean);
            tv_indent_border_first_gray.setVisibility(View.GONE);
        }
    }
}
