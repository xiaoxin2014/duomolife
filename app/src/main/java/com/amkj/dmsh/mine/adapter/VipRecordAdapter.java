package com.amkj.dmsh.mine.adapter;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.ConstantMethod;
import com.amkj.dmsh.mine.bean.VipRecordEntity.VipRecordBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * Created by xiaoxin on 2020/8/26
 * Version:v4.7.0
 * ClassDescription :会员购买记录
 */
public class VipRecordAdapter extends BaseQuickAdapter<VipRecordBean, BaseViewHolder> {
    public VipRecordAdapter(@Nullable List<VipRecordBean> data) {
        super(R.layout.item_vip_record, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VipRecordBean item) {
        if (item == null) return;
        helper.setText(R.id.tv_vip_type, item.getTitle())
                .setText(R.id.tv_buy_time, "购买时间：" + item.getBuyTime())
                .setText(R.id.tv_end_time, "到期时间：" + item.getEndTime())
                .setText(R.id.tv_pay_way, "获取方式：" + item.getRoad())
                .setText(R.id.tv_price, "¥" + item.getPayAmount())
                .setGone(R.id.tv_price, ConstantMethod.getStringChangeDouble(item.getPayAmount()) > 0);
    }
}
