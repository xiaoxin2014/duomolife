package com.amkj.dmsh.mine.adapter;

import android.content.Context;

import com.amkj.dmsh.R;
import com.amkj.dmsh.mine.bean.VipCouponEntity.VipCouponBean.CouponListBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;

import static com.amkj.dmsh.constant.ConstantMethod.getIntegralFormat;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * Created by xiaoxin on 2020/7/28
 * Version:v4.7.0
 * ClassDescription :会员专享券纵向列表适配器
 */
public class VipCouponVerticalAdapter extends BaseQuickAdapter<CouponListBean, BaseViewHolder> {
    private Context context;

    public VipCouponVerticalAdapter(Context context, @Nullable List<CouponListBean> data) {
        super(R.layout.item_vip_coupon_vertical, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CouponListBean item) {
        if (item == null) return;
        int progress = (int) (item.getRobCount() * 1.0f / item.getTotalCount() * 100);

        helper.setText(R.id.tv_amount, getStrings(item.getAmount()))
                .setText(R.id.tv_condition, getStrings(item.getCouponTitle()))
                .setText(R.id.tv_range, getStrings(item.getCouponDesc()))
                .setText(R.id.tv_time, getStrings(item.getTimeText()))
                .setText(R.id.tv_get_coupon, getStatusText(item.getStatus()))
                .setProgress(R.id.progress_coupon, progress)
                .setText(R.id.tv_percent, item.getStatus() == 1 ? "已领取" : getIntegralFormat(context, R.string.vip_coupon_percent, progress) + "%");
        helper.itemView.setTag(item);
    }

    private String getStatusText(int status) {
        if (status == 1) {
            return "去使用";
        } else if (status == 2) {
            return "已抢光";
        } else {
            return "立即领取";
        }
    }
}
