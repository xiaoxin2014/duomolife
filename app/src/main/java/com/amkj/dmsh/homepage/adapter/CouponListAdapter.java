package com.amkj.dmsh.homepage.adapter;

import android.support.annotation.Nullable;

import com.amkj.dmsh.R;
import com.amkj.dmsh.bean.CouponEntity.CouponListEntity.CouponBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * Created by xiaoxin on 2019/9/23
 * Version:v4.2.2
 * ClassDescription :优惠券列表
 */
public class CouponListAdapter extends BaseQuickAdapter<CouponBean, BaseViewHolder> {
    public CouponListAdapter(@Nullable List<CouponBean> data) {
        super(R.layout.adapter_coupon_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CouponBean item) {
        if (item == null) return;
        helper.setText(R.id.tv_mount, getStrings(item.getAmount()));
        helper.setText(R.id.tv_name, getStrings(item.getCouponTitle()));
        helper.setText(R.id.tv_condition, getStrings(item.getCouponDesc()));
    }
}
