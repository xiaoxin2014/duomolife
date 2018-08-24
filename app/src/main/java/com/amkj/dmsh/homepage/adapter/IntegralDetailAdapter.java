package com.amkj.dmsh.homepage.adapter;

import com.amkj.dmsh.R;
import com.amkj.dmsh.constant.BaseViewHolderHelper;
import com.amkj.dmsh.message.bean.IntegrationDetailsEntity.IntegrationDetailsBean;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import static com.amkj.dmsh.constant.ConstantMethod.formatStrings;
import static com.amkj.dmsh.constant.ConstantMethod.getDateFormat;
import static com.amkj.dmsh.constant.ConstantMethod.getStrings;

;

/**
 * @author LGuiPeng
 * @email liuguipeng163@163.com
 * created on 2017/8/22
 * class description:积分明细
 */

public class IntegralDetailAdapter extends BaseQuickAdapter<IntegrationDetailsBean, BaseViewHolderHelper> {

    public IntegralDetailAdapter(List<IntegrationDetailsBean> integrationDetailsList) {
        super(R.layout.adapter_integral_detail, integrationDetailsList);
    }

    @Override
    protected void convert(BaseViewHolderHelper helper, IntegrationDetailsBean integrationDetailsBean) {
        helper.setText(R.id.tv_integral_detail_time,
                getDateFormat(getStrings(integrationDetailsBean.getCtime()), "yyyy.MM.dd"))
                .setText(R.id.tv_integral_detail_title, getStrings(integrationDetailsBean.getTitle()))
                .setText(R.id.tv_integral_detail_score, integrationDetailsBean.getScore() > 0 ? formatStrings("+%1$d", integrationDetailsBean.getScore())
                        : String.valueOf(integrationDetailsBean.getScore()));
    }
}
