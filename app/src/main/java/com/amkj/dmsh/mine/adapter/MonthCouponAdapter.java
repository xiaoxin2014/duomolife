package com.amkj.dmsh.mine.adapter;

import com.amkj.dmsh.R;
import com.amkj.dmsh.mine.bean.VipCouponEntity.VipCouponBean.CouponListBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * Created by xiaoxin on 2020/7/28
 * Version:v4.7.0
 * ClassDescription :会员专享券横向列表适配器
 */
public class MonthCouponAdapter extends BaseQuickAdapter<CouponListBean, BaseViewHolder> {

    public MonthCouponAdapter(@Nullable List<CouponListBean> data) {
        super(R.layout.item_vip_coupon, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CouponListBean item) {
        if (item == null) return;
        int startFee = item.getStartFee();
        helper.setText(R.id.tv_amount, getStrings(item.getAmount()))
                .setText(R.id.tv_condition, startFee > 0 ? "满" + startFee : "无门槛")
                .setText(R.id.tv_discount, "减" + getStrings(item.getAmount()))
                .setGone(R.id.tv_discount, startFee > 0)
                .setText(R.id.tv_range, getStrings(item.getCouponDesc()));
    }
}
