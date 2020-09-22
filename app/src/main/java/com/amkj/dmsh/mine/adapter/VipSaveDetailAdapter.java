package com.amkj.dmsh.mine.adapter;

import com.amkj.dmsh.R;
import com.amkj.dmsh.mine.bean.CalculatorEntity.CalculatorBean.PriceDataBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;

import static com.amkj.dmsh.constant.ConstantMethod.getSpannableString;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * Created by xiaoxin on 2020/7/29
 * Version:v4.7.0
 * ClassDescription :会员-省钱账单
 */
public class VipSaveDetailAdapter extends BaseQuickAdapter<PriceDataBean, BaseViewHolder> {
    public VipSaveDetailAdapter(@Nullable List<PriceDataBean> data) {
        super(R.layout.item_save_detail, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PriceDataBean item) {
        if (item == null) return;
        String price = "已省" + getStrings(item.getPrice());
        helper.setText(R.id.tv_title, "•" + "\t" + getStrings(item.getText()))
                .setText(R.id.tv_amount, getSpannableString(price, 2, price.length(), -1, "", true));
    }
}
