package com.amkj.dmsh.dominant.adapter;

import android.content.Context;
import android.widget.TextView;

import com.amkj.dmsh.R;
import com.amkj.dmsh.dominant.bean.NewUserCouponEntity.CouponGiftBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;

import java.util.List;
import java.util.regex.Pattern;

import me.jessyan.autosize.utils.AutoSizeUtils;

import static com.amkj.dmsh.base.TinkerBaseApplicationLike.mAppContext;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;
import static com.amkj.dmsh.constant.ConstantVariable.REGEX_NUM;
import static com.amkj.dmsh.utils.TimeUtils.getDateFormat;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/8/1
 * version 3.1.5
 * class description:新人专享优惠券
 */
public class NewUserCouponAdapter extends BaseQuickAdapter<CouponGiftBean, BaseViewHolder> {

    private final Context context;

    public NewUserCouponAdapter(Context context, List<CouponGiftBean> couponGiftBeans) {
        super(R.layout.adapter_new_user_coupon, couponGiftBeans);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CouponGiftBean couponGiftBean) {
        TextView tv_new_user_coupon_amount = helper.getView(R.id.tv_new_user_coupon_amount);
        tv_new_user_coupon_amount.setText(String.format(context.getString(R.string.money_price_chn), String.valueOf(couponGiftBean.getAmount())));
        Pattern p = Pattern.compile(REGEX_NUM);
        Link link = new Link(p)
                .setTextColor(0xff333333)
                .setUnderlined(false)
                .setHighlightAlpha(0f)
                .setTextSize(AutoSizeUtils.mm2px(mAppContext,50));
        LinkBuilder.on(tv_new_user_coupon_amount)
                .addLink(link).build();
        helper.setText(R.id.tv_new_user_start_fee, couponGiftBean.getStartFee() > 0
                ? String.format(context.getString(R.string.coupon_start_fee), couponGiftBean.getStartFee()) : "无门槛")
                .setText(R.id.tv_new_user_coupon_description, getStrings(couponGiftBean.getDesc()))
                .setText(R.id.tv_new_user_coupon_period, String.format(context.getString(R.string.coupon_period_date)
                        , getDateFormat(couponGiftBean.getStartTime(), "yyyy.MM.dd")
                        , getDateFormat(couponGiftBean.getEndTime(), "yyyy.MM.dd")));
    }
}
