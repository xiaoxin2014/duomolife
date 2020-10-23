package com.amkj.dmsh.time.adapter;

import android.content.Context;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.dao.BaiChuanDao;
import com.amkj.dmsh.dominant.bean.GroupDetailEntity.CouponListBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * Created by xiaoxin on 2020/10/13
 * Version:v4.8.0
 * ClassDescription :团购商品详情-优惠券列表
 */
public class TimeCouponAdapter extends BaseQuickAdapter<CouponListBean, BaseViewHolder> {
    private Context mContext;

    public TimeCouponAdapter(Context context, @Nullable List<CouponListBean> data) {
        super(R.layout.item_time_coupon, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CouponListBean item) {
        if (item == null) return;
        helper.setText(R.id.tv_amount, ConstantMethod.getSpannableString(ConstantMethod.getStringsChNPrice(mContext, ConstantMethod.stripTrailingZeros(item.getPrice())), 0, 1, 0.4f, ""))
                .setText(R.id.tv_name, item.getName())
                .setText(R.id.tv_validity, item.getStartTime() + "~" + item.getEndTime());
        helper.itemView.setOnClickListener(v -> BaiChuanDao.skipAliBC(mContext, item.getUrl(), ""));
    }
}
