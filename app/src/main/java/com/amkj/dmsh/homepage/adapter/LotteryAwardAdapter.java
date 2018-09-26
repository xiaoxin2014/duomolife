package com.amkj.dmsh.homepage.adapter;

import com.amkj.dmsh.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2018/7/31
 * version 3.1.5
 * class description:我的夺宝码
 */
public class LotteryAwardAdapter extends BaseQuickAdapter<String,BaseViewHolder>{
    public LotteryAwardAdapter(List<String> lotteryCodeList) {
        super(R.layout.adapter_mine_lottery_code,lotteryCodeList);
    }

    @Override
    protected void convert(BaseViewHolder helper, String lotteryCode) {
        helper.setText(R.id.tv_mint_lottery_code,getStrings(lotteryCode));
    }
}
