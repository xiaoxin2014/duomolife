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
public class VipCouponAdapter extends BaseQuickAdapter<CouponListBean, BaseViewHolder> {

    public VipCouponAdapter(@Nullable List<CouponListBean> data) {
        super(R.layout.item_vip_coupon, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CouponListBean item) {
        if (item == null) return;
        helper.setText(R.id.tv_amount, getStrings(item.getAmount()))
                .setText(R.id.tv_condition, getStrings(item.getCouponTitle()))
                .setText(R.id.tv_range, getStrings(item.getCouponDesc()));
    }
}
