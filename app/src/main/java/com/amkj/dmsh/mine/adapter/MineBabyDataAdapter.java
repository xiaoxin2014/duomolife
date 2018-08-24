package com.amkj.dmsh.mine.adapter;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.amkj.dmsh.mine.bean.MineBabyEntity.BabyBean;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

/**
 * Created by atd48 on 2016/8/18.
 */
public class MineBabyDataAdapter extends BaseQuickAdapter<BabyBean, BaseViewHolderHelper> {
    private final String[] SEX = new String[]{"小公主", "小王子"};

    public MineBabyDataAdapter(List<BabyBean> babyList) {
        super(R.layout.adapter_layout_baby_item, babyList);
    }

    @Override
    protected void convert(BaseViewHolderHelper helper, BabyBean babyBean) {
        String[] split = babyBean.getBirthday().split("-");
        if (split.length > 2) {
            int year = Integer.parseInt(split[0].trim());
            int month = Integer.parseInt(split[1].trim());
            int day = Integer.parseInt(split[2].trim());
            helper.setText(R.id.tv_mine_baby_sex, SEX[babyBean.getSex() == 0 ? 1 : 0])
                    .setText(R.id.tv_baby_data_time, year + "-" + (month >= 10 ? month : "0" + month) + "-" + (day >= 10 ? day : "0" + day));
        }
        helper.itemView.setTag(babyBean);
    }
}
