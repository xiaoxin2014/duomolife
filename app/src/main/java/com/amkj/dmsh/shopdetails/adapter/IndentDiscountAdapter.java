package com.amkj.dmsh.shopdetails.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.shopdetails.bean.PriceInfoBean;
import com.amkj.dmsh.views.alertdialog.AlertDialogTax;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;


/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/25
 * class description:优惠信息
 */

public class IndentDiscountAdapter extends BaseQuickAdapter<PriceInfoBean, BaseViewHolder> {

    private AlertDialogTax mAlertDialogTax;
    private Context context;

    public IndentDiscountAdapter(Context context, List<PriceInfoBean> priceInfoList) {
        super(R.layout.adapter_indent_discount, priceInfoList);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, PriceInfoBean priceInfoBean) {
        TextView tv_indent_discount_price = helper.getView(R.id.tv_indent_discount_price);
        helper.setText(R.id.tv_indent_discount_name, getStrings(priceInfoBean.getName()))
                .setText(R.id.tv_indent_discount_desc, getStrings(priceInfoBean.getDesc()))
                .setGone(R.id.tv_indent_discount_desc, !TextUtils.isEmpty(priceInfoBean.getDesc()))
                .setGone(R.id.iv_mark, priceInfoBean.isEcm());
        try {
            tv_indent_discount_price.setTextColor(Color.parseColor((!TextUtils.isEmpty(priceInfoBean.getColor()) ?
                    priceInfoBean.getColor() : "#333333")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        tv_indent_discount_price.setText(getStrings(priceInfoBean.getTotalPriceName()));
        helper.itemView.setOnClickListener(v -> {
            if (priceInfoBean.isEcm()) {
                if (mAlertDialogTax == null) {
                    mAlertDialogTax = new AlertDialogTax(context, null);
                }
                mAlertDialogTax.show(Gravity.BOTTOM);
            }
        });
    }
}
