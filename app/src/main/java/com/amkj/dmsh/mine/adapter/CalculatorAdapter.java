package com.amkj.dmsh.mine.adapter;

import com.amkj.dmsh.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.amkj.dmsh.mine.bean.CalculatorEntity.CalculatorBean.PriceDataBean;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * Created by xiaoxin on 2020/7/29
 * Version:v4.7.0
 * ClassDescription :非会员-省钱计算器列表适配器
 */
public class CalculatorAdapter extends BaseQuickAdapter<PriceDataBean, BaseViewHolder> {
    public CalculatorAdapter(@Nullable List<PriceDataBean> data) {
        super(R.layout.item_calculator, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PriceDataBean item) {
        if (item == null) return;
        helper.setText(R.id.tv_title, getStrings(item.getText()))
                .setText(R.id.tv_amount, getStrings(item.getPrice()));
    }
}
