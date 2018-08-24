package com.amkj.dmsh.shopdetails.adapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.amkj.dmsh.shopdetails.bean.DirectCouponEntity.DirectCouponBean;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.zhy.autolayout.utils.AutoUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.amkj.dmsh.constant.ConstantMethod.getCurrentTime;
import static com.amkj.dmsh.constant.ConstantMethod.getStringFilter;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

;

/**
 * Created by atd48 on 2016/8/23.
 */
public class DirectMyCouponAdapter extends BaseMultiItemQuickAdapter<DirectCouponBean, DirectMyCouponAdapter.DirectMyCouponHolder> {

    private final String couponStatus;
    private String BLUE_BG = "#E9F5FC";

    public DirectMyCouponAdapter(List<DirectCouponBean> couponList, String couponStatus) {
        super(couponList);
        this.couponStatus = couponStatus;
        addItemType(0, R.layout.adapter_shop_details_coupon);
        addItemType(1, R.layout.adapter_not_apply_coupon);
    }

    @Override
    protected void convert(final DirectMyCouponHolder helper, DirectCouponBean directCouponBean) {
        if (helper.getItemViewType() == 0) {
//            背景颜色
            try {
                helper.ll_layout_coupon_bg.setBackgroundColor(Color.parseColor((!TextUtils.isEmpty(directCouponBean.getBgColor()) ?
                        directCouponBean.getBgColor() : BLUE_BG)));
            } catch (Exception e) {
                e.printStackTrace();
            }
//            优惠券类型背景颜色
            TextView tv_coupon_type = helper.getView(R.id.tv_coupon_type);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            int radius = (int) (AutoUtils.getPercentWidth1px() * 8);
            drawable.setCornerRadius(radius);
            try {
                drawable.setColor(Color.parseColor((!TextUtils.isEmpty(directCouponBean.getModeBgColor()) ?
                        directCouponBean.getModeBgColor() : BLUE_BG)));
                tv_coupon_type.setBackground(drawable);
            } catch (Exception e) {
                e.printStackTrace();
            }
//        优惠券是否过期
//        s是否过期
            if (!TextUtils.isEmpty(directCouponBean.getBeOverdue())
                    && directCouponBean.getBeOverdue().equals("0") ||
                    TextUtils.isEmpty(directCouponBean.getBeOverdue())) {
                helper.setGone(R.id.rImg_coupon_tag, false);
            } else {
                helper.setGone(R.id.rImg_coupon_tag, true)
                        .setImageResource(R.id.rImg_coupon_tag, R.drawable.coupon_icon_due_soon);
            }
            helper.setText(R.id.tv_coupon_money, String.valueOf(directCouponBean.getAmount()))
                    .setGone(R.id.view_coupon, helper.getLayoutPosition() - getHeaderLayoutCount() == 0)
                    .setGone(R.id.iv_due_soon_expire, directCouponBean.isSoonExpire())
                    .setText(R.id.tv_coupon_start_fee, directCouponBean.getStartFee() > 0 ? "满" + directCouponBean.getStartFee() + "可使用" : "无门槛")
                    .setText(R.id.tv_coupon_timer_limitation, "有效期:"
                            + getDateFormat(directCouponBean.getStartTime())
                            + " - " + getDateFormat(directCouponBean.getEndTime()))
                    .setText(R.id.tv_coupon_title, (!TextUtils.isEmpty(directCouponBean.getTitle()) ?
                            directCouponBean.getTitle() : directCouponBean.getType()))
                    .setText(R.id.tv_coupon_descrip, getStrings(directCouponBean.getDesc()))
                    .setGone(R.id.rel_nonuse_coupon_reason, !TextUtils.isEmpty(directCouponBean.getUsableCause()))
                    .setText(R.id.tv_nonuse_coupon_reason, getStringFilter(directCouponBean.getUsableCause()))
                    .setGone(R.id.tv_nonuse_coupon_reason, directCouponBean.isCheckStatus())
                    .getView(R.id.tv_nonuse_coupon_text).setSelected(directCouponBean.isCheckStatus());
            if (directCouponBean.getMode() == 1) {
                helper.setText(R.id.tv_coupon_type, directCouponBean.getStartFee() > 0 ? "满减券" : "无门槛")
                        .setGone(R.id.tv_money_symbol, true)
                        .setText(R.id.tv_money_symbol, "￥")
                        .setGone(R.id.tv_coupon_discount, false);
            } else if (directCouponBean.getMode() == 2) {
                helper.setText(R.id.tv_coupon_type, "折扣券")
                        .setGone(R.id.tv_money_symbol, false)
                        .setText(R.id.tv_coupon_discount, "折")
                        .setGone(R.id.tv_coupon_discount, true);
            }
//            是否使用
            TextView tv_coupon_status = helper.getView(R.id.tv_coupon_status);
            helper.tv_coupon_used.setVisibility(View.GONE);
            if (directCouponBean.getStatus() == 1) {
                tv_coupon_status.setVisibility(View.VISIBLE);
                tv_coupon_status.setText("已使用");
                helper.setGone(R.id.rImg_coupon_tag, false);
            } else {
                if (TextUtils.isEmpty(directCouponBean.getBeOverdue()) ||
                        (!TextUtils.isEmpty(directCouponBean.getBeOverdue())
                                && directCouponBean.getBeOverdue().equals("0"))) {
                    helper.setGone(R.id.rImg_coupon_tag, false);
                    tv_coupon_status.setVisibility(View.GONE);
                    helper.tv_coupon_used.setVisibility("checkCoupon".equals(couponStatus) ? View.VISIBLE : View.GONE);
                } else {
                    helper.setGone(R.id.rImg_coupon_tag, true)
                            .setImageResource(R.id.rImg_coupon_tag, R.drawable.coupon_icon_due_soon);
                    tv_coupon_status.setVisibility(View.VISIBLE);
                    tv_coupon_status.setText("已过期");
                }
            }
        }else{
            try {
                helper.getView(R.id.tv_nonuse_coupon)
                        .setBackgroundColor(Color.parseColor((!TextUtils.isEmpty(directCouponBean.getBgColor()) ?
                        directCouponBean.getBgColor() : BLUE_BG)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        helper.itemView.setTag(directCouponBean);
    }

    private String getDateFormat(String dateString) {
        SimpleDateFormat s1 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        SimpleDateFormat s2 = new SimpleDateFormat("yyyy.MM.dd", Locale.CHINA);
        Date tempDate;
        String outTime = "";
        try {
            if (TextUtils.isEmpty(dateString)) {
                dateString = getCurrentTime();
            }
            tempDate = s1.parse(dateString);
            outTime = s2.format(tempDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outTime;
    }

    public class DirectMyCouponHolder extends BaseViewHolderHelper {
        LinearLayout ll_layout_coupon_bg;
        TextView tv_coupon_used;

        public DirectMyCouponHolder(View view) {
            super(view);
            ll_layout_coupon_bg = (LinearLayout) view.findViewById(R.id.ll_layout_coupon_bg);
            tv_coupon_used = (TextView) view.findViewById(R.id.tv_coupon_used);
            if (tv_coupon_used != null) {
                GradientDrawable drawable = new GradientDrawable();
                drawable.setShape(GradientDrawable.RECTANGLE);
                int radius = (int) (AutoUtils.getPercentWidth1px() * 21);
                drawable.setCornerRadius(radius);
                drawable.setColor(mContext.getResources().getColor(R.color.text_normal_red));
                tv_coupon_used.setBackground(drawable);
            }
        }
    }
}
