package com.amkj.dmsh.mine.adapter;

import android.content.Context;
import android.widget.ProgressBar;

import com.amkj.dmsh.R;
import com.amkj.dmsh.mine.bean.VipCouponEntity.VipCouponBean.CouponListBean;
import com.amkj.dmsh.utils.DoubleUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantMethod.stripTrailingZeros;

/**
 * Created by xiaoxin on 2020/7/28
 * Version:v4.7.0
 * ClassDescription :会员专享券纵向列表适配器
 */
public class MonthCouponVerticalAdapter extends BaseQuickAdapter<CouponListBean, BaseViewHolder> {
    private Context context;

    public MonthCouponVerticalAdapter(Context context, @Nullable List<CouponListBean> data) {
        super(R.layout.item_vip_coupon_vertical, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CouponListBean item) {
        if (item == null) return;
        double progress = DoubleUtil.div(String.valueOf(item.getRobCount() * 100), String.valueOf(item.getTotalCount()), 2);
        helper.setText(R.id.tv_amount, getStrings(item.getAmount()))
                .setText(R.id.tv_condition, getStrings(item.getCouponTitle()))
                .setText(R.id.tv_range, getStrings(item.getCouponDesc()))
                .setText(R.id.tv_time, getStrings(item.getTimeText()))
                .setText(R.id.tv_get_coupon, item.isSoldOut() ? "已抢光" : (item.isDraw() ? "去使用" : "立即领取"))
                .setTextColor(R.id.tv_get_coupon, context.getResources().getColor(item.isSoldOut() || item.isDraw() ? R.color.text_black_t : R.color.white))
                .setEnabled(R.id.tv_get_coupon, !item.isSoldOut())
                .setBackgroundRes(R.id.tv_get_coupon, item.isSoldOut() ? R.drawable.sel_vip_coupon_soldout : (item.isDraw() ? R.drawable.sel_vip_coupon_isdraw : R.drawable.sel_vip_coupon_get))
                .setProgress(R.id.progress_coupon, item.isDraw() ? 100 : ((int) progress))
                .setText(R.id.tv_percent, item.isDraw() ? "已领取" : "已抢" + stripTrailingZeros(String.valueOf(Math.min(progress, 100.0f))) + "%");
        ((ProgressBar) helper.getView(R.id.progress_coupon)).setProgressDrawable(context.getResources().getDrawable(
                item.isSoldOut() ? R.drawable.shape_coupon_percent_progress_100 : R.drawable.shape_coupon_percent_progress));
        helper.itemView.setTag(item);
    }
}
